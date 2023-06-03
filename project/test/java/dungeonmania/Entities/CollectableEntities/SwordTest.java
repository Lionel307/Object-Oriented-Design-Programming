package dungeonmania.Entities.CollectableEntities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.Dungeon;
import dungeonmania.GameModes.StandardMode;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwordTest {
    /*
    - sword allows all entities to mover over it
    - sword encounter by player picks it up
    - getType returns "Sword"
    - attack returns 1 and reduces durability by 1
    */
    @Test
    public void testSwordCollection() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        
        new Sword(new Position(5, 4), dungeon);
        assertFalse(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("sword")));

        dungeon.tick(null, Direction.UP);

        // make sure health potion is collected when player moves
        assertTrue(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("sword")));
    }

}
