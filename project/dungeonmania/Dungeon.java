package dungeonmania;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dungeonmania.BuildableFactories.*;
import dungeonmania.Entities.*;
import dungeonmania.Entities.MovingEntity.*;
import dungeonmania.Entities.MovingEntity.Boss.*;
import dungeonmania.Entities.CollectableEntities.*;
import dungeonmania.Entities.StaticEntities.*;
import dungeonmania.GameModes.GameModeStrategy;
import dungeonmania.GameModes.HardMode;
import dungeonmania.Goals.Goal;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Dungeon {
    private Map<Position, List<Entity>> map = new HashMap<>();
    private Map<String, Entity> entities = new HashMap<>();
    private String id = UUID.randomUUID().toString();
    private String name;
    private Player player;
    private Goal goal;
    private GameModeStrategy gameMode;
    static private Map<String, BuildableFactory> buildableFactories = Map.of("bow", new BowFactory(), "shield",
            new ShieldFactory(), "sceptre", new SceptreFactory(), "midnight_armour", new MidnightArmourFactory());
    private int height, width, mercSpawnTickCounter, hydraSpawnTickCounter;
    private Position entryPosition;
    private Random random;

    public Dungeon(JSONObject dungeonJson, GameModeStrategy gameMode) {
        this.gameMode = gameMode;
        this.mercSpawnTickCounter = 0;
        this.hydraSpawnTickCounter = 0;
        random = new Random();

        // have to create player first
        JSONArray JSONEntities = dungeonJson.getJSONArray("entities");
        int j = 0;
        for (; j < JSONEntities.length(); j++) {
            JSONObject jsonObj = JSONEntities.getJSONObject(j);
            if (jsonObj.getString("type").equals("player")) {
                this.createEntity(jsonObj);
                break;
            }
        }

        // add all other entities
        for (int i = 0; i < JSONEntities.length(); i++) {
            if (i == j) {
                continue;
            }
            JSONObject JSONEntity = JSONEntities.getJSONObject(i);
            this.createEntity(JSONEntity);
        }

        try {
            this.goal = Goal.parse(dungeonJson.getJSONObject("goal-condition"));
        } catch (JSONException e) {
            this.goal = Goal.parse(null);
        }

        // infer height and width of dungeon from given entities extremities
        List<Entity> entityList = new ArrayList<Entity>(this.entities.values());
        Stream<Position> entityPositions = entityList.stream().map(entity -> entity.getPosition());
        this.width = entityPositions.map(pos -> pos.getX()).max(Comparator.naturalOrder()).get() + 1;

        entityPositions = entityList.stream().map(entity -> entity.getPosition());
        this.height = entityPositions.map(pos -> pos.getY()).max(Comparator.naturalOrder()).get() + 1;
    }

    public DungeonResponse getDungeonResponse() {

        List<EntityResponse> entityResponses = entities.values().stream().map(entity -> entity.getEntityResponse())
                .collect(Collectors.toList());

        List<String> buildablesStrings = buildableFactories.entrySet().stream()
                .filter(entry -> entry.getValue().isBuildable(player.getInventory(), entities.values()))
                .map(entry -> entry.getKey()).collect(Collectors.toList());

        return new DungeonResponse(id, name, entityResponses, player.getInventory().getItemResponses(),
                buildablesStrings, goal.getUnsatisfiedGoals(map, entities, player));
    }

    public DungeonResponse tick(String itemUsed, Direction movementDirection)
            throws IllegalArgumentException, InvalidActionException {
        if (player.isDead()) {
            return getDungeonResponse();
        }

        int MAX_NUM_SPIDER = 4;

        // player moves first
        player.setItemUsed(itemUsed);
        player.setDirection(movementDirection);
        tickWithoutSpawn();

        // spawn spider
        long numOfSpider = entities.values().stream().filter(entity -> entity instanceof Spider).count();
        if (numOfSpider < MAX_NUM_SPIDER) {
            spawnSpider();
        }

        // spawn hydra
        if (getGameMode() instanceof HardMode) {
            spawnHydra();
        }

        // spawn mercenaries
        spawnMercenary();

        player.resetHasBattle();
        return getDungeonResponse();
    }

    public void spawnHydra() {
        this.hydraSpawnTickCounter += 1;
        if (this.hydraSpawnTickCounter >= 19) {
            Position pos;
            do {
                pos = new Position(random.nextInt(width), random.nextInt(height));
            } while (!canSpawnHydra(pos));
            new Hydra(pos, this);
            this.hydraSpawnTickCounter = 0;
        }
    }

    private boolean canSpawnHydra(Position position) {
        List<Entity> samePosEntities = map.get(position);
        if (samePosEntities == null) {
            return true;
        }
        return !samePosEntities.stream().anyMatch(entity -> (entity instanceof Boulder) || (entity instanceof Door)
                || (entity instanceof Wall) || (entity instanceof ZombieSpawner));
    }

    /**
     * create mercenary at the entry point there is a chance that mercenary will
     * spawn as assassin instead
     */
    public void spawnMercenary() {
        // if there is at least one enemy
        // spawn a merc every 20 ticks
        Stream<Entity> enemies = entities.values().stream().filter(entity -> List
                .of("spider", "mercenary", "zombie_toast", "assassin", "hydra").contains(entity.getType()));

        boolean anyMercsHostile = enemies.filter(e -> e.getType().equals("mercenary"))
                .anyMatch(merc -> ((Mercenary) merc).isBribed() == false);

        boolean anyNonMercsHostile = entities.values().stream()
                .filter(entity -> List.of("spider", "zombie_toast", "hydra").contains(entity.getType())).count() > 0;

        if (anyNonMercsHostile || anyMercsHostile) {
            this.mercSpawnTickCounter += 1;
            if (this.mercSpawnTickCounter >= 19) {
                // 20% chance for assassin instead of mercenary
                if (random.nextInt(5) == 0) {
                    new Assassin(this.entryPosition, this);
                } else {
                    new Mercenary(this.entryPosition, this);
                }
                this.mercSpawnTickCounter = 0;
            }
        }
    }

    public DungeonResponse tickWithoutSpawn(String itemUsed, Direction movementDirection) {
        // player moves first
        player.setItemUsed(itemUsed);
        player.setDirection(movementDirection);
        tickWithoutSpawn();
        return getDungeonResponse();
    }

    /**
     * every entities will be ticked
     */
    public void tickWithoutSpawn() {
        // player moves first
        player.tick();

        // move all entities before spawning spider
        List<Entity> entityList = new ArrayList<>(entities.values());
        Collections.sort(entityList, new Comparator<Entity>() {
            @Override
            public int compare(Entity arg0, Entity arg1) {
                if (arg0 instanceof Bomb) {
                    return 1;
                } else if (arg1 instanceof Bomb) {
                    return -1;
                }
                return 0;
            }
        });

        for (Entity entity : entityList) {
            if (entity instanceof Player) {
                continue;
            }
            if (!entity.isRemoved()) {
                entity.tick();
            }
        }
    }

    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        Entity interactableEntity = this.entities.get(entityId);
        if (interactableEntity == null) {
            throw new IllegalArgumentException("the entity does not exist");
        }

        if (!interactableEntity.isInteractable()) {
            throw new IllegalArgumentException("the entity is not interactable");
        }

        if (player.isDead()) {
            throw new InvalidActionException("player is not alive");
        }

        if (interactableEntity instanceof Bribeable) {
            Bribeable entity = (Bribeable) interactableEntity;
            entity.interact(this.player);
        } else if (interactableEntity instanceof ZombieSpawner) {
            ZombieSpawner spawner = (ZombieSpawner) interactableEntity;
            spawner.interact(this.player);
        }

        return getDungeonResponse();
    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        if (player.isDead()) {
            throw new InvalidActionException("the player is dead");
        }

        BuildableFactory factory = buildableFactories.get(buildable);
        if (factory == null) {
            throw new IllegalArgumentException("not buildable");
        }
        factory.build(player.getInventory(), entities.values());
        return getDungeonResponse();
    }

    public Inventory getInventory() {
        return player.getInventory();
    }

    public Map<Position, List<Entity>> getMap() {
        return map;
    }

    public List<Entity> getEntitiesAtPos(Position pos) {
        return map.get(pos);
    }

    public Player getPlayer() {
        return player;
    }

    public Map<String, Entity> getEntities() {
        return entities;
    }

    public void removeEntity(Entity entity, Position position) {
        entities.remove(entity.getId());
        map.get(position).remove(entity);
    }

    public void addEntity(Entity entity, Position position) {
        entities.put(entity.getId(), entity);
        if (map.get(position) != null) {
            map.get(position).add(entity);
        } else {
            map.put(position, new ArrayList<>(Arrays.asList(entity)));
        }
    }

    public void updateEntityPosition(Entity entity, Position oldPosition, Position newPosition) {
        List<Entity> entitiesAtPos = map.get(oldPosition);
        if (entitiesAtPos != null) {
            entitiesAtPos.remove(entity);
        }
        if (!entities.containsKey(entity.getId())) {
            entities.put(entity.getId(), entity);
        }
        if (map.get(newPosition) != null) {
            map.get(newPosition).add(entity);
        } else {
            map.put(newPosition, new ArrayList<>(Arrays.asList(entity)));
        }
    }

    public GameModeStrategy getGameMode() {
        return gameMode;
    }

    public void spawnSpider() {
        if (random.nextInt(8) == 0) {
            Position pos;
            do {
                pos = new Position(random.nextInt(width), random.nextInt(height));
            } while (!canSpawnSpider(pos));

            new Spider(pos, this);
        }
    }

    private boolean canSpawnSpider(Position position) {
        List<Entity> samePosEntities = map.get(position);
        if (samePosEntities == null) {
            return true;
        }
        return !samePosEntities.stream().anyMatch(entity -> entity instanceof Boulder);
    }

    public void setEntryPosition(Position pos) {
        this.entryPosition = pos;
    }

    public void createEntity(JSONObject jsonObj) {
        Position position = new Position(jsonObj.getInt("x"), jsonObj.getInt("y"));
        String type = jsonObj.getString("type");

        // cases added in order of spec
        switch (type) {
        case "player":
            this.player = new Player(position, this);
            break;
        // Static entities
        case "wall":
            new Wall(position, this);
            break;
        case "exit":
            new Exit(position, this);
            break;
        case "boulder":
            new Boulder(position, this);
            break;
        case "switch":
            new FloorSwitch(position, this);
            break;
        case "door":
            int keyNum1 = jsonObj.getInt("key");
            new Door(position, this, keyNum1);
            break;
        case "portal":
            String colour = jsonObj.getString("colour");
            new Portal(position, this, colour);
            break;
        case "zombie_toast_spawner":
            new ZombieSpawner(position, this);
            break;

        // Moving Entities
        case "spider":
            new Spider(position, this);
            break;
        case "zombie_toast":
            new ZombieToast(position, this);
            break;
        case "mercenary":
            new Mercenary(position, this);
            break;
        // boss
        case "assassin":
            new Assassin(position, this);
            break;
        case "hydra":
            new Hydra(position, this);
            break;

        // Collectable Entities
        case "treasure":
            new Treasure(position, this);
            break;
        case "key":
            int keyNum2 = jsonObj.getInt("key");
            new Key(position, this, keyNum2);
            break;
        case "health_potion":
            new HealthPotion(position, this);
            break;
        case "invincibility_potion":
            new InvincibilityPotion(position, this);
            break;
        case "invisibility_potion":
            new InvisibilityPotion(position, this);
            break;
        case "wood":
            new Wood(position, this);
            break;
        case "arrow":
            new Arrow(position, this);
            break;
        case "bomb":
            new Bomb(position, this);
            break;
        case "sword":
            new Sword(position, this);
            break;
        case "armour":
            new Armour(position, this);
            break;

        // rare collectable entities
        case "one_ring":
            new OneRing(position, this);
            break;
        }
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

}
