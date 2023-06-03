package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.*;
import dungeonmania.Entities.CollectableEntities.Key;
import dungeonmania.Entities.MovingEntity.Mercenary;
import dungeonmania.Entities.MovingEntity.Spider;
import dungeonmania.Entities.StaticEntities.Door;
import dungeonmania.GameModes.StandardMode;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class DoorTest {
    @Test
    public void createDoorTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());

        Door door = new Door(new Position(0, 2), dungeon, 1);
        Key key = new Key(new Position(0, 1), dungeon, 1);

        assertEquals(new Position(0, 2), door.getPosition());
    }

    @Test 
    public void closedDoorTest() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());

        Player player = dungeon.getPlayer();
        Door door = new Door(new Position(0, 2), dungeon, 1);

        dungeon.tick(null, Direction.DOWN);
        dungeon.tick(null, Direction.DOWN);
        // door is locked so player cant go through it
        assertEquals(new Position(0, 1), player.getPosition());
        // check the door is closed
        assertEquals(true, door.isLocked());
    }

    @Test
    public void correctKeyTest() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());
        
        Door door = new Door(new Position(0, 2), dungeon, 1);
        assertFalse(door.allowMove(new Mercenary(new Position(0, 3), dungeon), Direction.UP));
        Player player = dungeon.getPlayer();
        Key key = new Key(new Position(0, 1), dungeon, 1);

        dungeon.tick(null, Direction.DOWN);
        dungeon.tick(null, Direction.DOWN);

        assertEquals(door.getPosition(), player.getPosition());
        // check the door is opened
        assertFalse(door.isLocked());
        assertTrue(door.allowMove(new Mercenary(new Position(0, 1), dungeon), Direction.DOWN));
        
        // check the key is removed from the inventory
        assertTrue(player.getInventory().getCollections().stream().allMatch(item -> {
            if (item instanceof Key) {
                return false;
            } else {
                return true;
            }
        }));

    }

    @Test
    public void incorrectKeyTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"key\", \"key\": \"2\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());

        Door door = new Door(new Position(0, 2), dungeon, 1);
        Player player = dungeon.getPlayer();

        dungeon.tick(null, Direction.DOWN);
        dungeon.tick(null, Direction.DOWN);

        assertEquals(new Position(0, 1), player.getPosition());
        // check the door is still closed
        assertTrue(door.isLocked());
        // check the key is still in the inventory
        assertTrue(player.getInventory().getCollections().stream().anyMatch(item -> {
            if (item instanceof Key) {
                return true;
            } else {
                return false;
            }
        }));
        
    }

    @Test
    public void SpiderAllowMove() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"key\", \"key\": \"2\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());

        Door door = new Door(new Position(0, 2), dungeon, 1);
        door.allowMove(new Spider(new Position(0, 3), dungeon), Direction.UP);

    }
}
