package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.*;
import dungeonmania.Entities.StaticEntities.Portal;
import dungeonmania.GameModes.StandardMode;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PortalTest {
    @Test
    public void createPortalTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());
        
        Portal portal = new Portal(new Position(0, 1), dungeon, "Blue");
        Portal portal2 = new Portal(new Position(0, 3), dungeon, "Blue");

        assertEquals(new Position(0, 1), portal.getPosition());
        assertEquals(new Position(0, 3), portal2.getPosition());
    }
    @Test
    public void usePortalTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());
        
        Portal portal = new Portal(new Position(0, 1), dungeon, "Blue");
        Portal portal2 = new Portal(new Position(0, 3), dungeon, "Blue");
        Player player = dungeon.getPlayer();


        dungeon.tick(null, Direction.DOWN);

        assertEquals(new Position(0, 4), player.getPosition());
        
        dungeon.tick(null, Direction.UP);

        assertEquals(new Position(0, 0), player.getPosition());
    }

    @Test
    public void multiplePortalsTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"portal\", \"colour\": \"Blue\"}, {\"x\": 0, \"y\": 3, \"type\": \"portal\", \"colour\": \"Blue\"}, {\"x\": 1, \"y\": 4, \"type\": \"portal\", \"colour\": \"Red\"}, {\"x\": 3, \"y\": 0, \"type\": \"portal\", \"colour\": \"Red\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());

        Player player = dungeon.getPlayer();
        dungeon.tick(null, Direction.DOWN);

        assertEquals(new Position(0, 4), player.getPosition());

        dungeon.tick(null, Direction.RIGHT);

        assertEquals(new Position(4, 0), player.getPosition());

    }
}
