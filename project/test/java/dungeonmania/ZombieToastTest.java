package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.GameModes.*;
import dungeonmania.Entities.*;
import dungeonmania.Entities.MovingEntity.*;
import dungeonmania.Entities.CollectableEntities.*;
import dungeonmania.Entities.StaticEntities.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToastTest extends MovingEntityTest {

    @Test
    public void testZombieToastMovement() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        ZombieToast zombieToast = new ZombieToast(new Position(5,5), dungeon);
        dungeon.tick(null, Direction.NONE);

        List<Position> possiblePositions = new ArrayList<Position>();
        possiblePositions.add(new Position(5,4));
        possiblePositions.add(new Position(5,6));
        possiblePositions.add(new Position(4,5));
        possiblePositions.add(new Position(6,5));
        assertTrue(possiblePositions.contains(zombieToast.getPosition()));
    }

    // Test zombieToast can go on portal
    @Test
    public void testZombieToastPortal() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        ZombieToast zombieToast = new ZombieToast(new Position(5,5), dungeon);

        // Surround ZombieToast with portals
        Portal portal1 = new Portal(new Position(5,4), dungeon, "colour1");
        Portal portal2 = new Portal(new Position(5,6), dungeon, "colour1");
        Portal portal3 = new Portal(new Position(4,5), dungeon, "colour2");
        Portal portal4 = new Portal(new Position(6,5), dungeon, "colour2");
        
        dungeon.tick(null, Direction.NONE);
        assertNotEquals(zombieToast.getPosition(), new Position(5,5));

        List<Position> possiblePositions = new ArrayList<Position>();
        possiblePositions.add(new Position(5,4));
        possiblePositions.add(new Position(5,6));
        possiblePositions.add(new Position(4,5));
        possiblePositions.add(new Position(6,5));
        assertTrue(possiblePositions.contains(zombieToast.getPosition()));
    }

    // Test zombieToast cannot go on wall
    @Test
    public void testZombieToastWall() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        ZombieToast zombieToast = new ZombieToast(new Position(5,5), dungeon);

        // Surround ZombieToast with walls
        zombieToast.getPosition().getAdjacentPositions().stream().forEach(e -> {
            new Wall(e, dungeon);
        });

        dungeon.tick(null, Direction.NONE);
        assertDoesNotThrow(()->dungeon.tick(null, Direction.NONE));
        assertEquals(zombieToast.getPosition(), new Position(5,5));
    }

    // Test zombieToast when player is invisible
    @Test
    public void testZombieInvisibleState() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        // Add invisibility potion to inventory
        Player player = dungeon.getPlayer();
        InvisibilityPotion potion = new InvisibilityPotion(new Position(99,99), dungeon);
        player.getInventory().add(potion);
        
        // Spawn zombieToast on same position as player
        ZombieToast zombieToast = new ZombieToast(new Position(5,5), dungeon);

        // PLayer will move down into zombieToast
        // Player and zombieToast will not have battled
        dungeon.tick(potion.getId(), Direction.NONE);
        assertEquals(player.getPosition(), new Position(5,5));
        assertTrue(dungeon.getEntities().values().contains(zombieToast));
    }

    // Test for zombieToast movement while player is invincible
    @Test
    public void testZombieToastInvincibleState() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        ZombieToast zombieToast = new ZombieToast(new Position(1,1), dungeon);
        Player player = dungeon.getPlayer();
        double previousDistance = calculateDistance(zombieToast, dungeon);

        // Add invincibility potion to inventory and consume it
        InvincibilityPotion potion = new InvincibilityPotion(new Position(99,99), dungeon);
        player.getInventory().add(potion);
        dungeon.tick(potion.getId(), Direction.NONE);

        double currentDistance = calculateDistance(zombieToast, dungeon);

        assertTrue((previousDistance <= currentDistance));
    }

    // Test for zombieToast movement while player is invincible but walls block movement
    @Test
    public void testZombieToastInvincibleStateWalls() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Wall wall1 = new Wall(new Position(0, 1), dungeon);
        Wall wall2 = new Wall(new Position(1, 0), dungeon);

        ZombieToast zombieToast = new ZombieToast(new Position(1,1), dungeon);
        Player player = dungeon.getPlayer();
        double previousDistance = calculateDistance(zombieToast, dungeon);

        // Add invincibility potion to inventory and consume it
        InvincibilityPotion potion = new InvincibilityPotion(new Position(99,99), dungeon);
        player.getInventory().add(potion);
        dungeon.tick(potion.getId(), Direction.NONE);

        double currentDistance = calculateDistance(zombieToast, dungeon);

        assertEquals(new Position(1,1), zombieToast.getPosition());
        assertTrue((previousDistance <= currentDistance));
    }

    // Test zombieToast battle
    @Test
    public void testZombieToastBattle() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        // Create zombieToast which the player will move into and battle
        ZombieToast zombieToast = new ZombieToast(new Position(5, 6), dungeon, false);
        dungeon.tick(null, Direction.DOWN);

        // zombieToast will be killed by player
        assertFalse(dungeon.getEntities().values().contains(zombieToast));
        assertTrue(dungeon.getEntities().values().stream().anyMatch(entity -> entity instanceof Player));

        // Create second zombieToast which the player will move into and battle
        ZombieToast zombieToast2 = new ZombieToast(new Position(5, 7), dungeon, false);
        dungeon.tick(null, Direction.DOWN);

        // ZombieToast will kill player
        assertFalse(dungeon.getEntities().values().stream().anyMatch(entity -> entity instanceof Player));
    }

    
    
}
