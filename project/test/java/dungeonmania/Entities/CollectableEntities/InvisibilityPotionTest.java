package dungeonmania.Entities.CollectableEntities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.Dungeon;
import dungeonmania.GameModes.StandardMode;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class InvisibilityPotionTest {
    @Test
    public void testPlayerCollectInvisibilityPotion() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        
        new InvisibilityPotion(new Position(5, 4), dungeon);
        assertFalse(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("invisibility_potion")));

        dungeon.tick(null, Direction.UP);

        // make sure health potion is collected when player moves
        assertTrue(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("invisibility_potion")));
    }

    @Test
    public void testPlayerBypassEnemies() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        
        new InvisibilityPotion(new Position(5, 4), dungeon);
        assertFalse(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("invisibility_potion")));

        dungeon.tick(null, Direction.UP);

        // make sure health potion is collected when player moves
        assertTrue(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("invisibility_potion")));
        String potionId = dungeon.getPlayer().getInventory().getItemResponses().get(0).getId();
        
        // create a random spider
        // there should be no encounter with the spider
        dungeon.createEntity(new JSONObject("{\"x\": 5, \"y\": 4, \"type\": \"spider\"}"));
        // use the potion
        dungeon.tick(potionId, Direction.NONE);

        assertEquals(100, dungeon.getPlayer().getHealth());
    }
}
