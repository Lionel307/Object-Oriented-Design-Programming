package dungeonmania.Entities.CollectableEntities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.Dungeon;
import dungeonmania.GameModes.StandardMode;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ArmourTest {
    /*
    - arrow allows all entities to move over it
    - armour encounter by player picks it up
    - getType returns "Armour"
    - defend returns 1 and reduces durability by 1
    */
    @Test
    public void testPlayerCollectArmour() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        
        new Armour(new Position(5, 4), dungeon);

        dungeon.tick(null, Direction.UP);

        // make sure armour is collected when player moves
        assertTrue(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("armour")));
    }

    @Test
    public void testPlayerDefendArmour() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        
        new Armour(new Position(5, 4), dungeon);

        dungeon.tick(null, Direction.UP);

        dungeon.getPlayer().setHealth(50);

        dungeon.getPlayer().defend(50);

        assertEquals(dungeon.getPlayer().getHealth(), 50/2);
    }
}
