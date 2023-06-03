package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.*;
import dungeonmania.Entities.StaticEntities.*;
import dungeonmania.GameModes.StandardMode;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BoulderTest {
    @Test
    public void createBoulderTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"boulder\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());

        Boulder boulder = new Boulder(new Position(0, 1), dungeon);

        assertEquals(new Position(0, 1), boulder.getPosition());
    }

    @Test
    public void playerPushBoulder() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"boulder\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());

        Boulder boulder = new Boulder(new Position(0, 1), dungeon);
        Player player = dungeon.getPlayer();


        dungeon.tick(null, Direction.DOWN);
        
        assertEquals(new Position(0, 2), boulder.getPosition());
        assertEquals(new Position(0, 1), player.getPosition());
    }

    @Test
    public void twoBouldersTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"boulder\"}, {\"x\": 0, \"y\": 2, \"type\": \"boulder\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());
        
        Boulder boulder = new Boulder(new Position(0, 1), dungeon);
        Boulder boulder2 = new Boulder(new Position(0, 2), dungeon);
        Player player = dungeon.getPlayer();

        dungeon.tick(null, Direction.DOWN);

        assertEquals(new Position(0, 1), boulder.getPosition());
        assertEquals(new Position(0, 2), boulder2.getPosition());
        assertEquals(new Position(0, 0), player.getPosition());
    }
}

