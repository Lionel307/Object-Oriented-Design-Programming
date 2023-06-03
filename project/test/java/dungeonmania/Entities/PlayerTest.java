package dungeonmania.Entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.*;
import dungeonmania.GameModes.*;
import dungeonmania.States.InvisibilityPotionState;
import dungeonmania.Entities.CollectableEntities.*;
import dungeonmania.Entities.MovingEntity.Mercenary;
import dungeonmania.Entities.StaticEntities.Portal;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

// since we're just creating the player, add basic property tests
public class PlayerTest {
    // helpers
    public boolean itemResponsesConatains(List<ItemResponse> list, String id) {
        for (ItemResponse item: list) {
            if (item.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /*
    Basic Test cases:
    - if player's position is correct after creation, and after setting a new position
    - check if walls prevent player from moving
    - check if player picks up items and adds to inventory
    - check potion states 
        - invisibility
        - invincibility
    - check if health potion works
    - check correct amount of damage is outputted from correct weapon/s
    - check teleportation after encountering a portal
    */
    @Test
    public void testCreatePlayer() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Position pos = new Position(0, 0);
        Player player = dungeon.getPlayer();
        assertEquals(pos, player.getPosition());
        assertTrue(dungeon.getEntities().get(player.getId()).equals(player));
        assertTrue(dungeon.getMap().get(pos).contains(player));
    }

    @Test
    public void testMoveWall() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"wall\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Player player = dungeon.getPlayer();
        
        dungeon.tick(null, Direction.DOWN);
        assertEquals(new Position(0, 0), player.getPosition());
        assertTrue(dungeon.getMap().get(new Position(0, 0)).contains(player));
        assertTrue(dungeon.getMap().get(new Position(0, 0)).contains(player));
        assertFalse(dungeon.getMap().get(new Position(0, 1)).contains(player));
    }

    @Test
    public void testPickUp() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"key\", \"key\": 1}]");
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Player player = dungeon.getPlayer();
        Entity key = dungeon.getMap().get(new Position(0, 1)).get(0);
        assertTrue(key instanceof Key);
        dungeon.tick(null, Direction.DOWN);
        assertTrue(itemResponsesConatains(player.getInventory().getItemResponses(), key.getId()));
    }

    @Test
    public void testPotionStateTest() {
        // test for InvisbilityPotion
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"mercenary\"}]");
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new PeacefulMode());
        Player player = dungeon.getPlayer();
        player.addPlayerState(new InvisibilityPotionState());
        player.setDirection(Direction.DOWN);
        player.tick();
        // both player and mercenary will exists at the same spot
        assertEquals(2, dungeon.getMap().get(new Position(0, 1)).size());
        assertFalse(player.hasState(InvisibilityPotionState.class));
        // more thorough tets for PotionTest should be written separately
        // this is just an integration test to see if player can set the potion state

    }

    @Test
    public void testHealthPotion() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"health_potion\"}]");
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Player player = dungeon.getPlayer();
        Entity healthPotion = dungeon.getMap().get(new Position(0, 1)).get(0);
        assertTrue(healthPotion instanceof HealthPotion);
        dungeon.tick(null, Direction.DOWN);
        assertTrue(itemResponsesConatains(player.getInventory().getItemResponses(), healthPotion.getId()));
        double originalHealth = player.getHealth();
        player.setHealth(10);
        player.setDirection(Direction.DOWN);
        player.setItemUsed(healthPotion.getId());
        player.tick();
        assertEquals(originalHealth, player.getHealth());
        assertFalse(itemResponsesConatains(player.getInventory().getItemResponses(), healthPotion.getId()));
    }

    @Test
    public void testAttack() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Player player = dungeon.getPlayer();
        
        // assume original attack damage: 20
        double originalAttackDamage = 20;

        double originalHealth = player.getHealth();
        assertEquals((originalHealth * originalAttackDamage)/5, player.attack(null));
        
        player.setHealth(10);
        assertEquals((10 * originalAttackDamage)/5, player.attack(null));

        player.setHealth(originalHealth);
        // player use weapon
        Sword sword = new Sword(new Position(0, 0), dungeon);
        sword.encounter(player, Direction.NONE);
        assertTrue(player.attack(null) > (originalHealth * originalAttackDamage)/5);
    }

    @Test
    public void testDefend() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Player player = dungeon.getPlayer();
        // assume original health: 100
        player.defend(10);
        assertEquals(90, player.getHealth());
        // player use weapon
        Armour armour = new Armour(new Position(0, 0), dungeon);
        armour.encounter(player, Direction.NONE);
        player.defend(100);
        assertEquals(40, player.getHealth());
    }

    @Test
    public void testPortalAllow() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Player player = dungeon.getPlayer();
        Portal portal1 = new Portal(new Position(1, 0), dungeon, "red");
        Portal portal2 = new Portal(new Position(0, 5), dungeon, "red");
        player.setDirection(Direction.RIGHT);
        player.tick();
        assertEquals(new Position(1, 5), player.getPosition());
    }

    @Test
    public void testPortalNotAllow() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 1, \"y\": 5, \"type\": \"wall\"}]");
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Player player = dungeon.getPlayer();
        Portal portal1 = new Portal(new Position(1, 0), dungeon, "red");
        Portal portal2 = new Portal(new Position(0, 5), dungeon, "red");
        player.setDirection(Direction.RIGHT);
        player.tick();
        // cannot move to other position
        assertEquals(new Position(0, 0), player.getPosition());
    }

    @Test
    public void testBattlePlayerWin() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"mercenary\"}]");
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        boolean hasMercenary = dungeon.getEntities().entrySet().stream().anyMatch(entry-> {
            if (entry.getValue() instanceof Mercenary) {
                return true;
            } else {
                return false;
            }
        });
        assertTrue(dungeon.getEntities().values().stream().anyMatch(entity -> entity instanceof Player) && hasMercenary);
        Player player = dungeon.getPlayer();
        player.setItemUsed((Usable)null);
        player.setDirection(Direction.DOWN);
        player.tick();
        hasMercenary = dungeon.getEntities().entrySet().stream().anyMatch(entry-> {
            if (entry.getValue() instanceof Mercenary) {
                return true;
            } else {
                return false;
            }
        });
        // Player should win
        assertFalse(hasMercenary);
        assertTrue(dungeon.getEntities().values().stream().anyMatch(entity -> entity.equals(player)));
    }

    @Test
    public void testBattlePlayerLose() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"mercenary\"}]");
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        boolean hasMercenary = dungeon.getEntities().entrySet().stream().anyMatch(entry-> {
            if (entry.getValue() instanceof Mercenary) {
                return true;
            } else {
                return false;
            }
        });
        assertTrue(dungeon.getEntities().values().stream().anyMatch(entity -> entity instanceof Player) && hasMercenary);
        Player player = dungeon.getPlayer();
        player.setItemUsed((Usable)null);
        player.setDirection(Direction.DOWN);
        player.setHealth(10);
        player.tick();
        hasMercenary = dungeon.getEntities().entrySet().stream().anyMatch(entry-> {
            if (entry.getValue() instanceof Mercenary) {
                return true;
            } else {
                return false;
            }
        });
        // Player should loose
        assertTrue(hasMercenary);
        assertFalse(dungeon.getEntities().values().stream().anyMatch(entity -> entity instanceof Player));
    }

    @Test
    public void testAllyAttack() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Player player = dungeon.getPlayer();
        Inventory inventory = player.getInventory();
        Treasure treasure = new Treasure(new Position(0, 0), dungeon);
        Mercenary mercenary = new Mercenary(new Position(0, 1), dungeon);

        inventory.add(treasure);
        mercenary.update(player);
        dungeon.interact(mercenary.getId());

        // battle with mercenary with health = 10
        player.setHealth(70);
        Mercenary mercenary2 = new Mercenary(new Position(1, 0), dungeon);
        player.setDirection(Direction.RIGHT);
        player.tick();
        // Player should win as the player have allies
        assertTrue(dungeon.getEntities().values().stream().anyMatch(entity -> entity.equals(player)));
        assertFalse(dungeon.getEntities().values().stream().anyMatch(entity -> entity.equals(mercenary2)));
    }

    @Test
    public void testOneRing() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"mercenary\"}]");
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        boolean hasMercenary = dungeon.getEntities().entrySet().stream().anyMatch(entry-> {
            if (entry.getValue() instanceof Mercenary) {
                return true;
            } else {
                return false;
            }
        });
        assertTrue(dungeon.getEntities().values().stream().anyMatch(entity -> entity instanceof Player) && hasMercenary);
        Player player = dungeon.getPlayer();
        Inventory inventory = player.getInventory();

        // add one ring
        new OneRing(inventory, dungeon);

        player.setItemUsed((Usable)null);
        player.setDirection(Direction.DOWN);
        player.setHealth(1000);
        player.tick();
        hasMercenary = dungeon.getEntities().entrySet().stream().anyMatch(entry-> {
            if (entry.getValue() instanceof Mercenary) {
                return true;
            } else {
                return false;
            }
        });
        // Player should win as the player has the one ring
        assertFalse(hasMercenary);
        assertTrue(dungeon.getEntities().values().stream().anyMatch(entity -> entity.equals(player)));
    }

}
