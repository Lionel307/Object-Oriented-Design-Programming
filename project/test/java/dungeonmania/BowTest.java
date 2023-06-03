package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.Bow;
import dungeonmania.Entities.MovingEntity.*;
import dungeonmania.Entities.Player;
import dungeonmania.GameModes.StandardMode;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BowTest {
    @Test
    public void twiceAttackTest() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"width\": 10, \"height\": 10, \"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}]}"), new StandardMode());
        Inventory inventory = dungeon.getPlayer().getInventory();
        Bow bow = new Bow(inventory, inventory.getPlayer());
        // check if the inventory add the numOfAttack of player
        assertEquals(2, dungeon.getPlayer().getNumOfAttack());
    }

    @Test
    public void attackDamageTest() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"width\": 10, \"height\": 10, \"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}]}"), new StandardMode());
        Inventory inventory = dungeon.getPlayer().getInventory();
        Bow bow = new Bow(inventory, inventory.getPlayer());
        assertEquals(20, bow.attack(10, null));
        assertEquals(1244.5, bow.attack(1234.5, null), 0.0001);
    }

    @Test
    public void deterioratesTest() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"width\": 10, \"height\": 10, \"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}]}"), new StandardMode());
        Inventory inventory = dungeon.getPlayer().getInventory();
        Bow bow = new Bow(inventory, inventory.getPlayer());
        // assume bow can be used for 5 times
        for (int i = 0; i < 5; i++) {
            assertEquals(1, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "bow"));
            assertEquals(1244.5, bow.attack(1234.5, null), 0.0001);
        }
        assertEquals(0, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "bow"));
    }

    // an integration test
    // deteriorate in one of the battle
    @Test
    public void deteriorateInAttackTest() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"width\": 10, \"height\": 10, \"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}]}"), new StandardMode());
        Player player = dungeon.getPlayer();
        Inventory inventory = player.getInventory();
        new Bow(inventory, inventory.getPlayer());

        player.setHealth(51);
        for (int i = 0; i < 4; i++) {
            (new Mercenary(new Position(1, 1), dungeon)).encounter(player, Direction.NONE);
            player.setHealth(51);
        }

        assertEquals(0, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "bow"));
    }
}
