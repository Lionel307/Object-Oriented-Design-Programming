package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.Shield;
import dungeonmania.GameModes.StandardMode;

public class ShieldTest {
    @Test
    public void decreaseDamageTest() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"width\": 10, \"height\": 10, \"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}]}"), new StandardMode());
        Inventory inventory = dungeon.getPlayer().getInventory();
        Shield shield = new Shield(inventory);
        // assume shield decrease 25% of the damage
        assertEquals(75, shield.defend(100), 0.0001);
        assertEquals(925.5, shield.defend(1234), 0.0001);
    }

    @Test
    public void deterioratesTest() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"width\": 10, \"height\": 10, \"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}]}"), new StandardMode());
        Inventory inventory = dungeon.getPlayer().getInventory();
        Shield shield = new Shield(inventory);
        // assume shield is gone after 10 defense
        for (int i = 0; i < 10; i++) {
            // check if the inventory still contains the shield
            assertEquals(1, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "shield"));
            assertEquals(925.5, shield.defend(1234), 0.0001);
        }
        // check if the shield is no longer in the inventory
        assertEquals(0, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "shield"));
    }
}
