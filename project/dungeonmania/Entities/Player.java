package dungeonmania.Entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import dungeonmania.AttackWeapon;
import dungeonmania.Battleable;
import dungeonmania.CharacterObserver;
import dungeonmania.DefendWeapon;
import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.Entities.MovingEntity.*;
import dungeonmania.States.InvincibilityPotionState;
import dungeonmania.States.InvisibilityPotionState;
import dungeonmania.States.PlayerState;
import dungeonmania.Storable;
import dungeonmania.Usable;
import dungeonmania.Entities.CollectableEntities.Armour;
import dungeonmania.Entities.CollectableEntities.OneRing;
import dungeonmania.Entities.CollectableEntities.SunStone;
import dungeonmania.Entities.CollectableEntities.Treasure;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Entity implements Battleable {
    
    static private double fullHealth = 100; // a default value for health
    private double health = fullHealth;
    static private double defaultAttackDamage = 20;
    private Direction direction = Direction.NONE;
    private Inventory inventory = new Inventory(this);
    // private PlayerState potionState = new NormalState();
    private List<PlayerState> playerStates = new ArrayList<>();
    private List<CharacterObserver> observers = new ArrayList<>();
    private List<BribeableMovingEntity> allies = new ArrayList<>();
    private int numOfAttack = 1;
    private Usable itemUsed = null;
    private boolean hasBattle = false;
    private Random random;

    public Player(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
        dungeon.setEntryPosition(pos);
        random = new Random();
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void addObserver(CharacterObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CharacterObserver observer) {
        observers.remove(observer);
    }

    public Usable getItemUsed() {
        return itemUsed;
    }

    public void setItemUsed(Usable itemUsed) {
        this.itemUsed = itemUsed;
    }

    /**
     * set the item used if the item is usable and is in the inventory
     * @param itemUsedId
     * @throws IllegalArgumentException when it is not an usable item
     * @throws InvalidActionException when it is not in the inventory
     */
    public void setItemUsed(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        // set the item as null if the string is null
        if (itemUsedId == null) {
            setItemUsed((Usable)null);
            return;
        }

        Storable item = inventory.getItem(itemUsedId);
        if (item == null) {
            throw new InvalidActionException("no item in inventory has the given id");
        }
        if (item instanceof Usable) {
            setItemUsed((Usable)item);
        } else {
            throw new IllegalArgumentException("not an usable item");
        }
    }

    @Override
    public String getType() {
        return "player";
    }

    public List<PlayerState> getPlayerStates() {
        return new ArrayList<>(playerStates);
    }

    public void addPlayerState(PlayerState state) {
        playerStates.add(state);
    }

    public void removePlayerState(PlayerState state) {
        playerStates.remove(state);
    }

    public boolean hasState(Class<? extends PlayerState> stateType) {
        return playerStates.stream()
            .anyMatch(state -> stateType.isAssignableFrom(state.getClass()));
    }

    public Iterator<PlayerState> playerStateIterator() {
        return playerStates.iterator();
    }

    private void updateStates() {
        for (PlayerState state: new ArrayList<>(playerStates)) {
            state.used(this);
        }
    }

    public List<BribeableMovingEntity> getAllies() {
        return allies;
    }

    public void addAllies(BribeableMovingEntity ally) {
        allies.add(ally);
    }

    public void removeAllies(BribeableMovingEntity ally) {
        allies.remove(ally);
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void resetHealth() {
        health = fullHealth;
    }

    public int getNumOfAttack() {
        return numOfAttack;
    }

    public void setNumOfAttack(int numOfAttack) {
        this.numOfAttack = numOfAttack;
    }

    @Override
    public void setPositionAndEncounter(Position position, Direction direction) {
        resetHasBattle();
        // use the item first
        if (itemUsed != null) {
            itemUsed.use(this);
        }
        super.setPositionAndEncounter(position, direction);
    }

    @Override
    public void setPosition(Position position) {
        super.setPosition(position);
        updateObservers();
    }

    /**
     * update all teh observers
     */
    public void updateObservers() {
        // update observers after the position is updaed
        for (CharacterObserver observer: new ArrayList<>(observers)) {
            observer.update(this);
        }
    }

    @Override
    public void tick() {
        Position nextPosition = getPosition().translateBy(direction);
        if (checkPositionAllowMove(nextPosition, direction)) {
            setPositionAndEncounter(nextPosition, direction);
        } else {
            // still have to use the item
            setPositionAndEncounter(this.getPosition(), Direction.NONE);
        }

        // tell the states that we have used it
        updateStates();
        // set back the direction and itemUsed
        setDirection(Direction.NONE);
        setItemUsed((Usable)null);
    }

    public boolean checkPositionAllowMove(Position position, Direction direction) {
        List<Entity> otherEntities = getMap().get(position);
        if (otherEntities == null) {
            return true;
        }

        return otherEntities.stream()
            .filter(entity -> entity != this)
            .allMatch(entity -> entity.allowMove(this, direction));
    }

    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        if (entity instanceof MovingEntity) {
            if (entity instanceof BribeableMovingEntity && ((BribeableMovingEntity)entity).isBribed()) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void encounter(Entity entity, Direction direction) {
        if (entity instanceof MovingEntity && 
            ! (hasState(InvisibilityPotionState.class))) {
            // don't battle if the player is invisible
            this.battle((MovingEntity)entity);
        }
    }

    // will be called when the enemies are encountered
    // this function will call the attack and defends method of this and enemies
    // to simulate a battle
    public void battle(Battleable enemy) {
        if (hasState(InvincibilityPotionState.class)) {
            ((MovingEntity)enemy).removeFromDungeon();
            return;
        }

        if (hasBattle == false) {
            hasBattle = true;
            updateObservers();
        }

        while (true) {

            // enemy attacks me
            double enemyDamage = enemy.attack(enemy);
            if (! this.defend(enemyDamage)) {
                return;
            }

            // the allies will attack first
            for (BribeableMovingEntity ally: allies) {
                double allyDamage = ally.allyAttack();

                if (! enemy.defend(allyDamage)) {
                    // create rare collectable entities
                    randomSpawnOneRing();

                    // stop attacking if the enemy is dead
                    return;
                }
            }

            // then the player will attack at most numOfAttack times
            int i = 0;
            while (i < numOfAttack) {
                double playerDamage = this.attack(enemy);

                if (! enemy.defend(playerDamage)) {
                    // create rare collectable entities
                    randomSpawnOneRing();

                    // create armour 
                    if (enemy instanceof ZombieToast || enemy instanceof Mercenary) {
                        if (((EntityDrop) enemy).getDropArmour()) {
                            randomSpawnArmour();
                        }
                    }

                    // stop attacking if the enemy is dead
                    return;
                }
                i++;
            }

        }

    }

    @Override
    public double attack(Battleable enemy) {

        double attackDamage = new ArrayList<>(inventory.getCollections()).stream()
            .filter(item -> (item instanceof AttackWeapon))
            .map(item-> (AttackWeapon)item)
            .reduce(defaultAttackDamage, 
                (oldAttackDamage, item)-> item.attack(oldAttackDamage, enemy), 
                Double::sum);
        // NOTE: we need Double::sum just for its type, it won't be invoked as 
        // this is a sequential stream (https://stackoverflow.com/a/33971436)

        return (health * attackDamage) / 5;
    }

    @Override
    public boolean defend(double damage) {

        double newDamage = inventory.getCollections().stream()
        .filter(item -> (item instanceof DefendWeapon))
        .map(item-> (DefendWeapon)item)
        .reduce(damage, (oldAttackDamage, item)->
            item.defend(oldAttackDamage), Double::sum);
        // NOTE: we need Double::sum just for its type, it won't be invoked as 
        // this is a sequential stream
        

        health -= newDamage;
        boolean isAlive = health > 0;
        if (!isAlive) {
            OneRing oneRing = getOneRing();
            if (oneRing == null) {
                removeFromDungeon();
            } else {
                // respawn and remove the ring
                setHealth(fullHealth);
                inventory.remove(oneRing);
                // continue battle
                return true;
            }
        }
        return isAlive;
    }

    /**
     * get a one ring from the inventory
     * @return a one ring if it is in the inventory, otherwise return null
     */
    private OneRing getOneRing() {
        return (OneRing) (inventory.getCollections().stream()
            .filter(item -> item instanceof OneRing)
            .findFirst()
            .orElse(null));
    }

    public boolean getHasBattle() {
        return hasBattle;
    }

    /**
     * reset the hasBattle attribute as false when a tick is finish
     * @return
     */
    public void resetHasBattle() {
        hasBattle = false;
    }

    /**
     * spawn a one ring with a probability of 1/100
     */
    public void randomSpawnOneRing() {
        if (random.nextInt(100) == 0) {
            new OneRing(inventory, getDungeon());
        }
    }

        /**
     * spawn a armour with a probability of 1/20
     */
    public void randomSpawnArmour() {
        if (random.nextInt(20) == 0) {
            new Armour(inventory, getDungeon());
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    public long getNumTreasureEquivalents() {
        Inventory inventory = this.getInventory();
        return inventory.getNumOfTypes(Treasure.class) + inventory.getNumOfTypes(SunStone.class);
    }
}
