package dungeonmania.Entities.CollectableEntities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.*;
import dungeonmania.GameModes.*;
import dungeonmania.States.InvincibilityPotionState;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class InvincibilityPotionTest {
    @Test
    public void testPlayerCollectInvincibilityPotion() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        
        new InvincibilityPotion(new Position(5, 4), dungeon);
        assertFalse(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("invincibility_potion")));

        dungeon.tick(null, Direction.UP);

        // make sure health potion is collected when player moves
        assertTrue(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("invincibility_potion")));
    }

    @Test
    public void testInvincibilityState() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        
        new InvincibilityPotion(new Position(5, 4), dungeon);
        assertFalse(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("invincibility_potion")));

        dungeon.tickWithoutSpawn(null, Direction.UP);

        // make sure invincibility potion is collected when player moves
        assertTrue(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("invincibility_potion")));
        String potionId = dungeon.getPlayer().getInventory().getItemResponses().get(0).getId();
        assertEquals(100, dungeon.getPlayer().getHealth());

        // have the potion
        dungeon.tickWithoutSpawn(potionId, Direction.NONE);

        // even after battle, nothing happens
        dungeon.createEntity(new JSONObject("{\"x\": 5, \"y\": 4, \"type\": \"spider\"}"));
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        assertEquals(100, dungeon.getPlayer().getHealth());

        dungeon.tickWithoutSpawn(null, Direction.NONE);
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        dungeon.tickWithoutSpawn(null, Direction.NONE);

        // potion expires
        dungeon.createEntity(new JSONObject("{\"x\": 5, \"y\": 4, \"type\": \"spider\"}"));
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        assertEquals(50, dungeon.getPlayer().getHealth());
    }

    @Test
    public void testInvincibilityPotionHardMode() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new HardMode());
        
        InvincibilityPotion potion = new InvincibilityPotion(new Position(5, 4), dungeon);
        dungeon.getPlayer().getInventory().add(potion);
        potion.removeFromDungeon();
        dungeon.tick(potion.getId(), Direction.UP);
        assertFalse(dungeon.getPlayer().hasState(InvincibilityPotionState.class));
    }


}
