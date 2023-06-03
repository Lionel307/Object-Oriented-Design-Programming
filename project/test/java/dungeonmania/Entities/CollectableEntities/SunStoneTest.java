package dungeonmania.Entities.CollectableEntities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.InventoryTestHelper;
import dungeonmania.BuildableFactories.ShieldFactory;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.MovingEntity.Mercenary;
import dungeonmania.Entities.StaticEntities.Door;
import dungeonmania.GameModes.StandardMode;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SunStoneTest {
    /*
    - sunstone exists and is collected
    - sunstone can be used to replace treasure in bribing mercenaries
    - sunstone doesn't disappear when bribing mercenaries
    - sunstone can be used to open any type of door
    - sunstone doesn't disappear once door is opened
    - sunstone can be used as a buildable to craft shields (and it disappears)
    */

    @Test
    public void testPlayerCollectSunstone() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        
        new SunStone(new Position(5, 4), dungeon);

        dungeon.tick(null, Direction.UP);

        // make sure armour is collected when player moves
        assertTrue(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("sun_stone")));
    }

    @Test
    public void testSunstoneBribesMercenaries() {
        // test bribing mercenaries
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        // Give player treasure to bribe mercenary
        Player player = dungeon.getPlayer();
        SunStone sunstone = new SunStone(new Position(99,99), dungeon);
        player.getInventory().add(sunstone);

        // Create mercenary 
        Mercenary mercenary = new Mercenary(new Position(5,6), dungeon);
        assertFalse(mercenary.isBribed());
    
        // Bribe mercenary
        dungeon.interact(mercenary.getId());
        assertTrue(mercenary.isBribed());

        // test sunstone doesn't disappear once bribed
        assertTrue(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("sun_stone")));
    }

    @Test
    public void testSunstoneOpensDoors() {
        // test opening door
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());
        
        Door door = new Door(new Position(0, 2), dungeon, 1);
        assertFalse(door.allowMove(new Mercenary(new Position(0, 3), dungeon), Direction.UP));
        Player player = dungeon.getPlayer();
        SunStone sunstone = new SunStone(new Position(0, 1), dungeon);

        dungeon.tick(null, Direction.DOWN);
        dungeon.tick(null, Direction.DOWN);

        assertEquals(door.getPosition(), player.getPosition());
        // check the door is opened
        assertFalse(door.isLocked());
        assertTrue(door.allowMove(new Mercenary(new Position(0, 1), dungeon), Direction.DOWN));

        // test player retains stone after
        assertTrue(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("sun_stone")));
    }

    @Test 
    public void testSunstoneCraftsShields() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"width\": 10, \"height\": 10, \"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}]}"), new StandardMode());
        Inventory inventory = dungeon.getPlayer().getInventory();
        ShieldFactory factory = new ShieldFactory();
        assertFalse(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        assertThrows(InvalidActionException.class, ()->factory.build(inventory, dungeon.getEntities().values()));
        inventory.add(new Wood(new Position(0, 0), dungeon));
        inventory.add(new Wood(new Position(0, 0), dungeon));
        inventory.add(new Wood(new Position(0, 0), dungeon));
        assertFalse(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        assertThrows(InvalidActionException.class, ()->factory.build(inventory, dungeon.getEntities().values()));
        inventory.add(new SunStone(new Position(0, 0), dungeon));
        assertTrue(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        assertDoesNotThrow(()->factory.build(inventory, dungeon.getEntities().values()));
        assertEquals(1, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "wood"));
        assertEquals(0, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "sun_stone"));
        assertEquals(1, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "shield"));
        inventory.add(new Wood(new Position(0, 0), dungeon));
        inventory.add(new Key(new Position(0, 0), dungeon, 1));
        assertDoesNotThrow(()->factory.build(inventory, dungeon.getEntities().values()));
        assertEquals(0, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "wood"));
        assertEquals(0, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "key"));
        assertEquals(2, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "shield"));
    }
}
