package dungeonmania.Entities.CollectableEntities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.Entities.MovingEntity.Spider;
import dungeonmania.Entities.MovingEntity.Boss.Hydra;
import dungeonmania.GameModes.StandardMode;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class AndurilTest {
    /*
    - anduril exists and is collected
    - anduril does triple damage against bosses
    */

    @Test
    public void testPlayerCollectsAnduril() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        
        new Anduril(new Position(5, 4), dungeon);
        assertFalse(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("anduril")));

        dungeon.tick(null, Direction.UP);

        // make sure anduril is collected when player moves
        assertTrue(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("anduril")));
    }

    @Test
    public void testAndurilTripleDamageAgainstBosses() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}]}"), new StandardMode());
        Anduril anduril = new Anduril(new Position(5, 4), dungeon);
        Spider spider = new Spider(new Position(5, 4), dungeon);
        assertEquals(20, anduril.attack(10, spider)); //attack takes 10 damage for non-boss

        Hydra hydra = new Hydra(new Position(5, 4), dungeon);
        assertEquals(40, anduril.attack(10, hydra)); 
    }

}
