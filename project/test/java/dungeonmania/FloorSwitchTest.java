package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.StaticEntities.Boulder;
import dungeonmania.Entities.StaticEntities.FloorSwitch;
import dungeonmania.GameModes.StandardMode;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class FloorSwitchTest {
    @Test
    public void createFloorSwitchTest() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());

        FloorSwitch floorSwitch = new FloorSwitch(new Position(0, 1), dungeon);

        assertEquals(new Position(0, 1), floorSwitch.getPosition());
    }
}
