package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.GameModes.*;
import dungeonmania.Entities.*;
import dungeonmania.Entities.MovingEntity.*;
import dungeonmania.Entities.StaticEntities.Boulder;
import dungeonmania.Entities.CollectableEntities.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SpiderTest extends MovingEntityTest {

    // Test spider movement pattern
    @Test
    public void testSpiderMovementPattern() {

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Spider spider = new Spider(new Position(5,5), dungeon);

        // Spider moves up one tile
        spider.tick();
        assertEquals(spider.getPosition(), new Position(5, 4));

        // Spider moves right one tile
        spider.tick();
        assertEquals(spider.getPosition(), new Position(6, 4));

        // Spider moves down one tile
        spider.tick();
        assertEquals(spider.getPosition(), new Position(6, 5));

        // Spider moves down one tile
        spider.tick();
        assertEquals(spider.getPosition(), new Position(6, 6));

        // Spider moves left one tile
        spider.tick();
        assertEquals(spider.getPosition(), new Position(5, 6));

        // Spider moves left one tile
        spider.tick();
        assertEquals(spider.getPosition(), new Position(4, 6));

        // Spider moves up one tile
        spider.tick();
        assertEquals(spider.getPosition(), new Position(4, 5));

        // Spider moves up one tile
        spider.tick();
        assertEquals(spider.getPosition(), new Position(4, 4));

        // Spider moves right one tile
        spider.tick();
        assertEquals(spider.getPosition(), new Position(5, 4));
    }

    @Test
    public void testSpiderWall() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 5, \"y\": 5, \"type\": \"wall\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Spider spider = new Spider(new Position(5,6), dungeon);

        assertDoesNotThrow(()->dungeon.tick(null, Direction.NONE));
        assertEquals(spider.getPosition(), new Position(5,5));
    }

    // Test spider encouters boulder
    @Test
    public void testSpiderBoulder() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 5, \"y\": 5, \"type\": \"boulder\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Spider spider = new Spider(new Position(5,6), dungeon);

        // Boulder blocks spiders path so it cannot move
        // The spiders movement will then be reversed
        spider.tick();
        assertEquals(spider.getPosition(), new Position(5,6));

        // Spider continues with its circular path
        // Spider will move left intstead of right because of reversed direction
        spider.tick();
        assertEquals(spider.getPosition(), new Position(4,6));
    }

    // Test spider battle
    @Test
    public void testSpiderBattle() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        // Create spider which will move up into player and battle
        Spider spider = new Spider(new Position(5, 6), dungeon);
        spider.tick();

        // Spider will be killed by player
        assertFalse(dungeon.getEntities().values().contains(spider));
        assertTrue(dungeon.getEntities().values().stream().anyMatch(entity -> entity instanceof Player));

        // Create second spider which will move up into player and battle
        Spider spider2 = new Spider(new Position(5, 6), dungeon);
        dungeon.getPlayer().tick();
        spider.tick();
        spider2.tick();

        // Spider will kill player
        assertFalse(dungeon.getEntities().values().stream().anyMatch(entity -> entity instanceof Player));
    }

    // Test for spider movement while player is invisible
    @Test
    public void testSpiderInvisibleState() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Spider spider = new Spider(new Position(5,6), dungeon);

        // Add invisibility potion to inventory
        Player player = dungeon.getPlayer();
        InvisibilityPotion potion = new InvisibilityPotion(new Position(99,99), dungeon);
        player.getInventory().add(potion);
        
        // Player will consume potion
        // Spider will move up into player
        // Player and spider will be on same position without battling
        dungeon.tick(potion.getId(), Direction.NONE);
        assertEquals(spider.getPosition(), new Position(5,5));
        assertEquals(player.getPosition(), new Position(5,5));
    }

    // Test for spider movement while player is invincible
    @Test
    public void testSpiderInvincibleState() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Boulder boulder = new Boulder(new Position(1,0), dungeon);
        Spider spider = new Spider(new Position(1,1), dungeon);
        Player player = dungeon.getPlayer();
        double previousDistance = calculateDistance(spider, dungeon);

        // Add invincibility potion to inventory and consume it
        InvincibilityPotion potion = new InvincibilityPotion(new Position(99,99), dungeon);
        player.getInventory().add(potion);
        dungeon.tick(potion.getId(), Direction.NONE);
        assertEquals(new Position(0,1), spider.getPosition());

        double currentDistance = calculateDistance(spider, dungeon);

        assertTrue((previousDistance <= currentDistance));
    }

    // Test for spider battle player while invincible
    @Test
    public void testSpiderInvincibleStateBattle() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Spider spider = new Spider(new Position(5,5), dungeon);
        Player player = dungeon.getPlayer();

        // Add invincibility potion to inventory and consume it
        InvincibilityPotion potion = new InvincibilityPotion(new Position(99,99), dungeon);
        player.getInventory().add(potion);
        dungeon.tick(potion.getId(), Direction.NONE);
        
        // Spider will be killed by player
        assertFalse(dungeon.getEntities().values().contains(spider));
        assertEquals(player.getHealth(), 100);
    }
    
}
