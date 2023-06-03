package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.GameModes.*;
import dungeonmania.Entities.*;
import dungeonmania.Entities.MovingEntity.Boss.Hydra;
import dungeonmania.Entities.CollectableEntities.*;
import dungeonmania.Entities.StaticEntities.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class HydraTest extends MovingEntityTest {
    
    // Test movement of hydra
    @Test
    public void testHydraMovement() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Hydra hydra = new Hydra(new Position(5,5), dungeon);
        dungeon.tick(null, Direction.NONE);

        List<Position> possiblePositions = new ArrayList<Position>();
        possiblePositions.add(new Position(5,4));
        possiblePositions.add(new Position(5,6));
        possiblePositions.add(new Position(4,5));
        possiblePositions.add(new Position(6,5));
        assertTrue(possiblePositions.contains(hydra.getPosition()));
    }

    // Test hydra movement onto portal
    @Test
    public void testHydraPortal() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Hydra hydra = new Hydra(new Position(5,5), dungeon);

        // Surround hydra with portals
        Portal portal1 = new Portal(new Position(5,4), dungeon, "colour1");
        Portal portal2 = new Portal(new Position(5,6), dungeon, "colour1");
        Portal portal3 = new Portal(new Position(4,5), dungeon, "colour2");
        Portal portal4 = new Portal(new Position(6,5), dungeon, "colour2");
        
        dungeon.tick(null, Direction.NONE);
        assertNotEquals(hydra.getPosition(), new Position(5,5));

        List<Position> possiblePositions = new ArrayList<Position>();
        possiblePositions.add(new Position(5,4));
        possiblePositions.add(new Position(5,6));
        possiblePositions.add(new Position(4,5));
        possiblePositions.add(new Position(6,5));
        assertTrue(possiblePositions.contains(hydra.getPosition()));
    }

    // Test hydra movement into walls
    @Test
    public void testHydraWall() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Hydra hydra = new Hydra(new Position(5,5), dungeon);

        // Surround hydra with walls
        hydra.getPosition().getAdjacentPositions().stream().forEach(e -> {
            new Wall(e, dungeon);
        });

        dungeon.tick(null, Direction.NONE);
        assertDoesNotThrow(()->dungeon.tick(null, Direction.NONE));
        assertEquals(hydra.getPosition(), new Position(5,5));
    }

    // Test hydra movement while player is invisible
    @Test
    public void testHydraInvisibleState() {
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
        
        // Spawn hydra on same position as player
        Hydra hydra = new Hydra(new Position(5,5), dungeon);

        // Player and zombieToast will not have battled
        dungeon.tick(potion.getId(), Direction.NONE);
        assertEquals(player.getPosition(), new Position(5,5));
        assertTrue(dungeon.getEntities().values().contains(hydra));
    }

    // Test hydra movement while player is invincible
    @Test
    public void testHydraInvincibleState() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Hydra hydra = new Hydra(new Position(1,1), dungeon);
        Player player = dungeon.getPlayer();
        double previousDistance = calculateDistance(hydra, dungeon);

        // Add invincibility potion to inventory and consume it
        InvincibilityPotion potion = new InvincibilityPotion(new Position(99,99), dungeon);
        player.getInventory().add(potion);
        dungeon.tick(potion.getId(), Direction.NONE);

        double currentDistance = calculateDistance(hydra, dungeon);
        assertTrue((previousDistance <= currentDistance));
    }

    // TODO: Test hydra battle mechanics
}
