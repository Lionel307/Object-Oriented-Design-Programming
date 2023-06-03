package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.GameModes.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.Entities.CollectableEntities.Armour;
import dungeonmania.Entities.CollectableEntities.Sword;
import dungeonmania.Entities.CollectableEntities.Wood;
import dungeonmania.Entities.StaticEntities.ZombieSpawner;
import dungeonmania.util.Position;

public class ZombieSpawnerTest {
    @Test
    public void createZombieSqawnerTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());

        ZombieSpawner spawner = new ZombieSpawner(new Position(0, 1), dungeon);

        assertEquals(new Position(0, 1), spawner.getPosition());
    }


    @Test
    public void spawnZombieTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());
        ZombieSpawner zombieSpawner = new ZombieSpawner(new Position(0, 1), dungeon);

        // tick 20 times
        for (int i = 0; i < 20; i++) {
            zombieSpawner.tick();
        }

        // check list of entities that the zombie has spawned
        List<EntityResponse> entityResponses = dungeon.getDungeonResponse().getEntities();
        Boolean testPassed = false;
        for (EntityResponse entityResponse: entityResponses) {
            System.out.println(entityResponse.getType());
            if (entityResponse.getType().equals("zombie_toast")) {
                testPassed = true;
                break;
            }
        }
        assertTrue(testPassed);
    }

    @Test
    public void breakSpawnerTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());


        ZombieSpawner spawner = new ZombieSpawner(new Position(0, 1), dungeon);
        dungeon.getPlayer().getInventory().add(new Sword(new Position(0, 4), dungeon));

        dungeon.interact(spawner.getId());

        Boolean testPassed = true;
        // check list of entities that the spawner is removed
        List<EntityResponse> entityResponses = dungeon.getDungeonResponse().getEntities();
        for (EntityResponse entityResponse: entityResponses) {
            if (entityResponse.getType().equals("zombie_toast_spawner")) {
                testPassed = false;
            }
        }
        assertTrue(testPassed);
    }

    @Test
    public void breakSpawnerTestDefendWepon() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());


        ZombieSpawner spawner = new ZombieSpawner(new Position(0, 1), dungeon);
        dungeon.getPlayer().getInventory().add(new Armour(new Position(0, 4), dungeon));

        dungeon.interact(spawner.getId());

        Boolean testPassed = true;
        // check list of entities that the spawner is removed
        List<EntityResponse> entityResponses = dungeon.getDungeonResponse().getEntities();
        for (EntityResponse entityResponse: entityResponses) {
            if (entityResponse.getType().equals("zombie_toast_spawner")) {
                testPassed = false;
            }
        }
        assertTrue(testPassed);
    }

    @Test
    public void failBreakSpawnerTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());


        ZombieSpawner spawner = new ZombieSpawner(new Position(0, 1), dungeon);
        dungeon.getPlayer().getInventory().add(new Wood(new Position(0, 4), dungeon));

        assertThrows(InvalidActionException.class, ()->{
            dungeon.interact(spawner.getId());
        });
    }


    @Test 
    public void spawnZombieHardModeTest() {
        // check zombie spawns every 15 ticks

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new HardMode());
        
        ZombieSpawner spawner = new ZombieSpawner(new Position(0, 1), dungeon);

        // tick 15 times
        for (int i = 0; i < 15; i++) {
            spawner.tick();
        }
        
        // check list of entities that the zombie has spawned
        List<EntityResponse> entityResponses = dungeon.getDungeonResponse().getEntities();
        Boolean testPassed = false;
        for (EntityResponse entityResponse: entityResponses) {
            if (entityResponse.getType().equals("zombie_toast")) {
                testPassed = true;
                break;
            }
        }
        assertTrue(testPassed);
    }

    @Test
    public void spawnZombieNoStorable() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());
        ZombieSpawner spawner = new ZombieSpawner(new Position(0, 1), dungeon);
        assertThrows(InvalidActionException.class, ()->{
            dungeon.interact(spawner.getId());
        });
    }

    @Test
    public void cannotSpawnTest() {
        JSONObject jsonObject = new JSONObject();
        String entitiesString = "["
            + "{\"x\": 0, \"y\": 1, \"type\": \"one_ring\"},"
            + "{\"x\": 1, \"y\": 0, \"type\": \"boulder\"},"
            + "{\"x\": 1, \"y\": 2, \"type\": \"invisibility_potion\"},"
            + "{\"x\": 2, \"y\": 1, \"type\": \"player\"},"
            + "{\"x\": 0, \"y\": 0, \"type\": \"wood\"},"
            + "{\"x\": 0, \"y\": 2, \"type\": \"armour\"},"
            + "{\"x\": 2, \"y\": 2, \"type\": \"arrow\"},"
            + "{\"x\": 2, \"y\": 0, \"type\": \"spider\"}"
            + "]";
        JSONArray entities = new JSONArray(entitiesString);

        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());
        ZombieSpawner spawner = new ZombieSpawner(new Position(1, 1), dungeon);
        for (int i = 0; i < 20; i++) {
            spawner.tick();
        }
        List<EntityResponse> entityResponses = dungeon.getDungeonResponse().getEntities();
        Boolean testPassed = false;
        for (EntityResponse entityResponse: entityResponses) {
            if (entityResponse.getType().equals("zombie_toast")) {
                testPassed = true;
                break;
            }
        }
        assertFalse(testPassed);

    }
}
