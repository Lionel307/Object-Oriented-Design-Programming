package dungeonmania.Entities.CollectableEntities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.*;
import dungeonmania.GameModes.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class HealthPotionTest {
    @Test
    public void testPlayerCollectHealthPotion() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        
        new HealthPotion(new Position(5, 4), dungeon);
        assertFalse(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("health_potion")));

        dungeon.tick(null, Direction.UP);

        // make sure health potion is collected when player moves
        assertTrue(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("health_potion")));
    }

    @Test
    public void testHealthPotionHealPlayer() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        
        new HealthPotion(new Position(5, 4), dungeon);
        assertFalse(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("health_potion")));

        dungeon.tick(null, Direction.UP);

        // make sure health potion is collected when player moves
        assertTrue(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("health_potion")));

        // player has been hurt
        dungeon.getPlayer().defend(50);
        assertEquals(50, dungeon.getPlayer().getHealth());

        String potionId = dungeon.getPlayer().getInventory().getItemResponses().get(0).getId();

        // use the potion and heal to full health
        dungeon.tick(potionId, Direction.NONE);
        assertEquals(100, dungeon.getPlayer().getHealth());
    }
}
