package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.GameModes.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.Entities.*;
import dungeonmania.Entities.MovingEntity.*;
import dungeonmania.Entities.CollectableEntities.*;
import dungeonmania.Entities.StaticEntities.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MercenaryTest extends MovingEntityTest {

    // Test for mercenary movement pattern
    @Test
    public void testMercenaryMovement() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Player player = dungeon.getPlayer();

        Mercenary mercenary = new Mercenary(new Position(5,5), dungeon);

        // Calculate distance between player and mercenary before tick
        double previousDistance = calculateDistance(mercenary, dungeon);

        // Calculate distance between player and mercenary after tick
        dungeon.tick(null, Direction.NONE);
        double currentDistance = calculateDistance(mercenary, dungeon);

        assertTrue((previousDistance >= currentDistance));
    }

    // Test for mercenary movement into a wall
    @Test
    public void testMercenaryWall() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Mercenary mercenary = new Mercenary(new Position(5,5), dungeon);

        // Surround mercenary with walls
        mercenary.getPosition().getAdjacentPositions().stream().forEach(e -> {
            new Wall(e, dungeon);
        });

        dungeon.tick(null, Direction.NONE);
        assertEquals(mercenary.getPosition(), new Position(5,5));
    }

    // Test for mercenary battle and movement while player is invisible
    @Test
    public void testMercenaryInvisibleState() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Mercenary mercenary = new Mercenary(new Position(5,5), dungeon);

        // Add invisibility potion to inventory and consume it
        Player player = dungeon.getPlayer();
        InvisibilityPotion potion = new InvisibilityPotion(new Position(99,99), dungeon);
        player.getInventory().add(potion);

        dungeon.tick(potion.getId(), Direction.NONE);
        assertTrue(dungeon.getEntities().values().contains(mercenary));
        assertEquals(mercenary.getPosition(), new Position(5,5));
    }

    // Test for mercenary movement while player is invincible
    @Test
    public void testMercenaryInvincibleState() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Mercenary mercenary = new Mercenary(new Position(2,2), dungeon);
        double previousDistance = calculateDistance(mercenary, dungeon);

        // Add invincibility potion to inventory and consume it
        Player player = dungeon.getPlayer();
        InvincibilityPotion potion = new InvincibilityPotion(new Position(99,99), dungeon);
        player.getInventory().add(potion);
        dungeon.tick(potion.getId(), Direction.NONE);

        double currentDistance = calculateDistance(mercenary, dungeon);

        assertEquals(new Position(2,3), mercenary.getPosition());
        assertTrue((previousDistance <= currentDistance));
    }

    // Test for mercenary battle
    @Test
    public void testMercenaryBattle() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        // Create mercenary which the player will move into and battle
        Mercenary mercenary = new Mercenary(new Position(5, 6), dungeon, false);
        dungeon.tick(null, Direction.DOWN);

        // mercenary will be killed by player
        // Player will still be alive
        assertFalse(dungeon.getEntities().values().contains(mercenary));
        assertTrue(dungeon.getEntities().values().stream().anyMatch(entity -> entity instanceof Player));

        // Create second mercenary which the player will move into and battle
        Mercenary mercenary2 = new Mercenary(new Position(5, 6), dungeon, false);
        dungeon.tick(null, Direction.NONE);

        // Mercenary will kill the player
        assertFalse(dungeon.getEntities().values().stream().anyMatch(entity -> entity instanceof Player));
    }

    // Test mercenary bribe and movement of bribed mercenary
    @Test
    public void testMercenaryBribe() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        // Give player treasure to bribe mercenary
        Player player = dungeon.getPlayer();
        Treasure treasure = new Treasure(new Position(99,99), dungeon);
        player.getInventory().add(treasure);

        // Create mercenary 
        Mercenary mercenary = new Mercenary(new Position(5,6), dungeon);
        assertFalse(mercenary.isBribed());

        // Bribe mercenary
        dungeon.interact(mercenary.getId());
        assertTrue(mercenary.isBribed());
        
        // Test bribed mercenary movement
        Position playerPreviousPosition = player.getPosition();
        dungeon.tick(null, Direction.DOWN);
        assertEquals(playerPreviousPosition, mercenary.getPosition());

        playerPreviousPosition = player.getPosition();
        dungeon.tick(null, Direction.RIGHT);
        assertEquals(playerPreviousPosition, mercenary.getPosition());
    }

    // Test mercenary bribe when out of range
    @Test
    public void testMercenaryBribeOutOfRange() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        // Give player treasure to bribe mercenary
        Player player = dungeon.getPlayer();
        Treasure treasure = new Treasure(new Position(99,99), dungeon);
        player.getInventory().add(treasure);

        // Create mercenary 
        Mercenary mercenary = new Mercenary(new Position(1,1), dungeon);
        assertFalse(mercenary.isBribed());

        // Bribe mercenary
        assertThrows(InvalidActionException.class, ()-> dungeon.interact(mercenary.getId()));
    }
}