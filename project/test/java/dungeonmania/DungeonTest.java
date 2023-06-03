package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.GameModes.*;
import dungeonmania.Entities.*;
import dungeonmania.Entities.MovingEntity.*;
import dungeonmania.Entities.StaticEntities.*;
import dungeonmania.Entities.CollectableEntities.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import dungeonmania.exceptions.*;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;

public class DungeonTest {

    // Test dungeon constructor with invalid game mode
    @Test
    public void testDungeonInvalidGameMode() {
        DungeonManiaController controller = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, ()-> controller.newGame("advanced", "invalidGameMode"));
        assertDoesNotThrow(()-> controller.newGame("advanced", "standard"));
    }

    // Test dungeon constructor with invalid dungeon name
    @Test
    public void testDungeonInvalidDungeonName() {
        DungeonManiaController controller = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, ()-> controller.newGame("invalidDungeon", "standard"));
        assertDoesNotThrow(()->controller.newGame("advanced", "standard"));
    }

    // Test dungeon tick with invalid itemUsed
    @Test
    public void testDungeonTickInvalidItem() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        // Get player and inventory
        Player player = dungeon.getPlayer();
        Inventory inventory = player.getInventory();

        // Simulate that player picks up wood
        Wood wood = new Wood(new Position(0,1), dungeon);
        dungeon.tick(null, Direction.DOWN);

        // Use wood
        // Should throw exception since wood cannot be used
        assertThrows(IllegalArgumentException.class, ()-> dungeon.tick(wood.getId(), Direction.NONE));

        // Simulate that player picks up bomb
        Bomb bomb = new Bomb(new Position(0,2), dungeon);
        dungeon.tick(null, Direction.DOWN);

        // Use bomb
        // Should not throw exception
        assertDoesNotThrow(()-> dungeon.tick(bomb.getId(), Direction.NONE));
    }
        
    // Test dungeon tick with item not in players inventory
    @Test
    public void testDungeonTickNoItem() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        // Get player and inventory
        Player player = dungeon.getPlayer();
        Inventory inventory = player.getInventory();

        // Add bomb to dungeon
        Bomb bomb = new Bomb(new Position(0,1), dungeon);

        // Use bomb
        // Should throw exception as player does not yet have the bomb
        assertThrows(InvalidActionException.class, ()-> dungeon.tick(bomb.getId(), Direction.NONE));

        // Simulate player picking up bomb and using it
        dungeon.tick(null, Direction.DOWN);
        assertDoesNotThrow(()-> dungeon.tick(bomb.getId(), Direction.NONE));
    }

    // Test dungeon build with entity is not buildable
    @Test
    public void testDungeonBuildNotBuildable() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        // Get player and inventory
        Player player = dungeon.getPlayer();
        Inventory inventory = player.getInventory();

        assertThrows(IllegalArgumentException.class, ()-> dungeon.build("notBuildable"));

        // Add ingredients for bow into inventory
        inventory.add(new Wood(new Position(0,0), dungeon));
        for (int i = 0; i < 3; i++) {
            inventory.add(new Arrow(new Position(0,0), dungeon));
        }

        // Build bow
        assertDoesNotThrow(()-> dungeon.build("bow"));

    }

    // Test for building without the materials required
    @Test
    public void testDungeonBuildNoMaterials() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        // Get player and inventory
        Player player = dungeon.getPlayer();
        Inventory inventory = player.getInventory();

        assertThrows(InvalidActionException.class, ()-> dungeon.build("bow"));

        // Add ingredients for bow into inventory
        inventory.add(new Wood(new Position(0,0), dungeon));
        for (int i = 0; i < 3; i++) {
            inventory.add(new Arrow(new Position(0,0), dungeon));
        }

        // Build bow
        assertDoesNotThrow(()-> dungeon.build("bow"));

    }

    // Test for dungeon interact method with invalid id
    @Test
    public void testDungeonInteractInvalidID() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        // Test for ID which does not exist
        assertThrows(IllegalArgumentException.class, ()-> dungeon.interact("InvalidID"));

        // Test for ID which exists but is not a mercenary or zombiespawner
        Wall wall = new Wall(new Position(0, 1), dungeon);
        assertThrows(IllegalArgumentException.class, ()-> dungeon.interact(wall.getId()));
    }

    // Test for dungeon interact with mercenary
    @Test
    public void testDungeonInteractMercenary() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));

        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Player player = dungeon.getPlayer();
        Inventory inventory = player.getInventory();

        // Create mercenary which is out of range of player
        // Assumption: Out of range exception takes precedence
        Mercenary mercenary = new Mercenary(new Position(0, 3), dungeon);
        assertThrows(InvalidActionException.class, ()-> dungeon.interact(mercenary.getId()));

        // Tick for mercenary1 to move into player range
        dungeon.tick(null, Direction.NONE);

        // Player does not have any gold to bribe mercenary
        assertThrows(InvalidActionException.class, ()-> dungeon.interact(mercenary.getId()));

        // Give player treasure and bribe mercenary
        inventory.add(new Treasure(new Position(0,0), dungeon));
        player.updateObservers();
        assertDoesNotThrow(()-> dungeon.interact(mercenary.getId()));
    }

    // Test for dungeon interact with zombieSpawner
    @Test
    public void testDungeonInteractZombieSpawner() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));

        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Player player = dungeon.getPlayer();
        Inventory inventory = player.getInventory();

        // Add zombieSpawner which is out of range from player
        // Assumption: Out of range exception takes precedence
        ZombieSpawner zombieSpawner = new ZombieSpawner(new Position(0, 2), dungeon);
        assertThrows(InvalidActionException.class, ()-> dungeon.interact(zombieSpawner.getId()));

        // Player moves into range of zombieToastSpawner
        dungeon.tick(null, Direction.DOWN);

        // Player does not have a weapon
        assertThrows(InvalidActionException.class, ()-> dungeon.interact(zombieSpawner.getId()));

        // Give player a bow
        new Bow(inventory, player);
        assertDoesNotThrow(()-> dungeon.interact(zombieSpawner.getId()));

    }

    public boolean equalResponses(EntityResponse response1, EntityResponse response2) {
        return response1.getId().equals(response2.getId());
    }

    @Test
    public void testDungeonPlayerDies() {
        JSONObject jsonObj = new JSONObject();
        String entitiesString = "[" +
            "{\"x\": 1, \"y\": 5, \"type\": \"zombie_toast\"}," + 
            "{\"x\": 2, \"y\": 0, \"type\": \"zombie_toast_spawner\"}," + 
            "{\"x\": 3, \"y\": 4, \"type\": \"player\"}," + 
            "{\"x\": 2, \"y\": 8, \"type\": \"spider\"}," + 
            "{\"x\": 1, \"y\": 3, \"type\": \"one_ring\"}" + 
        "]";
        JSONArray entitiesArray = new JSONArray(entitiesString);
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Player player = dungeon.getPlayer();
        HealthPotion potion = new HealthPotion(new Position(3, 4), dungeon);
        Mercenary mercenary = new Mercenary(new Position(3, 5), dungeon);
        Treasure treasure = new Treasure(new Position(3, 4), dungeon);
        player.getInventory().add(treasure);
        treasure.removeFromDungeon();
        potion.removeFromDungeon();
        player.getInventory().add(potion);
        dungeon.getPlayer().setHealth(-1);
        dungeon.getPlayer().removeFromDungeon();
        List<EntityResponse> oriEntityList = dungeon.getDungeonResponse().getEntities();
        List<EntityResponse> newList1 = dungeon.tick(potion.getId(), Direction.DOWN).getEntities();
        assertTrue(oriEntityList.size() == newList1.size());
        assertTrue(oriEntityList.stream().allMatch(res1->
            newList1.stream().anyMatch(res2-> equalResponses(res1, res2))));
        
        assertThrows(InvalidActionException.class, ()-> dungeon.build("bow"));
        assertThrows(InvalidActionException.class, ()-> dungeon.interact(mercenary.getId()));
        
    }
}
