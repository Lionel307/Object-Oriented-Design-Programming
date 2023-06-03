package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.GameModes.StandardMode;
import dungeonmania.Entities.*;
import dungeonmania.Entities.StaticEntities.Wall;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class WallTest {
    @Test
    public void createWallTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());

        Wall wall = new Wall(new Position(0, 1), dungeon);

        assertEquals(new Position(0, 1), wall.getPosition());
    }

    @Test 
    public void blockPlayerTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"wall\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());

        Player player = dungeon.getPlayer();

        dungeon.tick(null, Direction.DOWN);
        assertEquals(new Position(0, 0), player.getPosition());
    }
}
