package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.MidnightArmour;
import dungeonmania.Entities.Player;
import dungeonmania.GameModes.StandardMode;

public class MidnightArmourTest {
    @Test
    public void attackTest() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}]}"), new StandardMode());
        Player player = dungeon.getPlayer();
        Inventory inventory = player.getInventory();
        MidnightArmour armour = new MidnightArmour(inventory);
        assertEquals(120, armour.attack(100, null));
        assertEquals(3020.1, armour.attack(3000.1, null));
    }

    @Test
    public void defendTest() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}]}"), new StandardMode());
        Player player = dungeon.getPlayer();
        Inventory inventory = player.getInventory();
        MidnightArmour armour = new MidnightArmour(inventory);

        assertEquals(50, armour.defend(100));
        assertEquals(1500.05, armour.defend(3000.1));
    }

    @Test
    public void durabilityTest() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}]}"), new StandardMode());
        Player player = dungeon.getPlayer();
        Inventory inventory = player.getInventory();
        MidnightArmour armour = new MidnightArmour(inventory);

        assertEquals(1500.05, armour.defend(3000.1));
        assertEquals(120, armour.attack(100, null));
        assertEquals(3020.1, armour.attack(3000.1, null));
        assertEquals(1500.05, armour.defend(3000.1));
        assertEquals(120, armour.attack(100, null));
        assertEquals(120, armour.attack(100, null));
        assertEquals(120, armour.attack(100, null));
        assertEquals(1500.05, armour.defend(3000.1));
        assertEquals(1500.05, armour.defend(3000.1));
        assertEquals(1, inventory.getNumOfTypes(MidnightArmour.class));
        assertEquals(1500.05, armour.defend(3000.1));
        assertEquals(0, inventory.getNumOfTypes(MidnightArmour.class));
    }
}
