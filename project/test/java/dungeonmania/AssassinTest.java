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
import dungeonmania.Entities.MovingEntity.Boss.Assassin;
import dungeonmania.Entities.CollectableEntities.*;
import dungeonmania.Entities.StaticEntities.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class AssassinTest extends MovingEntityTest {
    
    // Test for assassin movement pattern
    @Test
    public void testAssassinMovement() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Player player = dungeon.getPlayer();

        Assassin assassin = new Assassin(new Position(5,5), dungeon);

        // Calculate distance between player and assassin before tick
        double previousDistance = calculateDistance(assassin, dungeon);

        // Calculate distance between player and assassin after tick
        dungeon.tick(null, Direction.NONE);
        double currentDistance = calculateDistance(assassin, dungeon);

        assertTrue((previousDistance >= currentDistance));
    }

    // Test for assassin movement into a wall
    @Test
    public void testAssasinWall() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Assassin assassin = new Assassin(new Position(5,5), dungeon);

        // Surround assassin with walls
        assassin.getPosition().getAdjacentPositions().stream().forEach(e -> {
            new Wall(e, dungeon);
        });

        dungeon.tick(null, Direction.NONE);
        assertEquals(assassin.getPosition(), new Position(5,5));
    }

    // Test for assassin movement while player is invisible
    @Test
    public void testAssassinInvisibleState() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Assassin assassin = new Assassin(new Position(5,5), dungeon);

        // Add invisibility potion to inventory and consume it
        Player player = dungeon.getPlayer();
        InvisibilityPotion potion = new InvisibilityPotion(new Position(99,99), dungeon);
        player.getInventory().add(potion);

        dungeon.tick(potion.getId(), Direction.NONE);
        assertTrue(dungeon.getEntities().values().contains(assassin));
        assertEquals(assassin.getPosition(), new Position(5,5));
    }

    // Test for assassin movement while player is invincible
    @Test
    public void testAssassinInvincibleState() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Assassin assassin = new Assassin(new Position(2,2), dungeon);
        double previousDistance = calculateDistance(assassin, dungeon);

        // Add invincibility potion to inventory and consume it
        Player player = dungeon.getPlayer();
        InvincibilityPotion potion = new InvincibilityPotion(new Position(99,99), dungeon);
        player.getInventory().add(potion);
        dungeon.tick(potion.getId(), Direction.NONE);

        double currentDistance = calculateDistance(assassin, dungeon);

        // TODO: May need to change with new algorithm
        assertEquals(new Position(2,3), assassin.getPosition());
        assertTrue((previousDistance <= currentDistance));
    }

    // Test assassin bribe and movement of bribed assassin
    @Test
    public void testassassinBribe() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Player player = dungeon.getPlayer();

        // Create assassin
        Assassin assassin = new Assassin(new Position(5,6), dungeon);
        assertFalse(assassin.isBribed());

        // Bribe assassin but not enough treasure
        assertThrows(InvalidActionException.class, ()-> dungeon.interact(assassin.getId()));
        
        // Give player treasure and one ring to bribe assassin
        Treasure treasure = new Treasure(new Position(99,99), dungeon);
        player.getInventory().add(treasure);
        OneRing oneRing = new OneRing(new Position(99,98), dungeon);
        player.getInventory().add(oneRing);

        // Bribe assassin
        dungeon.interact(assassin.getId());
        assertTrue(assassin.isBribed());
        
        // Test bribed assassinmovement
        Position playerPreviousPosition = player.getPosition();
        dungeon.tick(null, Direction.DOWN);
        assertEquals(playerPreviousPosition, assassin.getPosition());

        playerPreviousPosition = player.getPosition();
        dungeon.tick(null, Direction.RIGHT);
        assertEquals(playerPreviousPosition, assassin.getPosition());
    }

    // Test assasin bribe when out of range
    @Test
    public void testAssasinBribeOutOfRange() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        // Give player treasure to bribe assasin
        Player player = dungeon.getPlayer();
        Treasure treasure = new Treasure(new Position(99,99), dungeon);
        player.getInventory().add(treasure);
        OneRing oneRing = new OneRing(new Position(99,98), dungeon);
        player.getInventory().add(oneRing);

        // Create assassin
        Assassin assassin = new Assassin(new Position(1,1), dungeon);
        assertFalse(assassin.isBribed());

        // Bribe assassin
        assertThrows(InvalidActionException.class, ()-> dungeon.interact(assassin.getId()));
    }

    // Test for assassin battle
    @Test
    public void testAssassinBattle() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        // Create assassin which the player will move into and battle
        Assassin assassin = new Assassin(new Position(5, 6), dungeon);
        dungeon.tick(null, Direction.DOWN);

        // assassin will kill the player
        assertFalse(dungeon.getEntities().values().stream().anyMatch(entity -> entity instanceof Player));
    }

    // Test for Assassin battle with armour
    @Test
    public void testAssassinBattleArmour() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        // Give player armour
        Player player = dungeon.getPlayer();
        Armour armour = new Armour(new Position(99,99), dungeon);
        player.getInventory().add(armour);


        // Create assassin which the player will move into and battle
        Assassin assassin = new Assassin(new Position(5, 6), dungeon);
        dungeon.tick(null, Direction.DOWN);

        // assassin will be killed by player
        // Player will still be alive
        assertFalse(dungeon.getEntities().values().contains(assassin));
        assertTrue(dungeon.getEntities().values().stream().anyMatch(entity -> entity instanceof Player));

        // Create second assassin which the player will move into and battle
        Assassin assassin2 = new Assassin(new Position(5, 6), dungeon);
        dungeon.tick(null, Direction.NONE);

        // assassin will kill the player
        assertFalse(dungeon.getEntities().values().stream().anyMatch(entity -> entity instanceof Player));
    }
}
