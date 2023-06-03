package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.BuildableFactories.*;
import dungeonmania.Entities.*;
import dungeonmania.Entities.CollectableEntities.*;
import dungeonmania.Entities.MovingEntity.ZombieToast;
import dungeonmania.GameModes.StandardMode;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;

public class BuildableFactoryTest {

    @Test
    public void testBow() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"width\": 10, \"height\": 10, \"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}]}"), new StandardMode());
        Inventory inventory = dungeon.getPlayer().getInventory();
        BowFactory factory = new BowFactory();
        assertFalse(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        assertThrows(InvalidActionException.class, ()->factory.build(inventory, dungeon.getEntities().values()));
        inventory.add(new Arrow(new Position(0, 0), dungeon));
        inventory.add(new Arrow(new Position(0, 0), dungeon));
        inventory.add(new Arrow(new Position(0, 0), dungeon));
        assertFalse(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        assertThrows(InvalidActionException.class, ()->factory.build(inventory, dungeon.getEntities().values()));
        inventory.add(new Wood(new Position(0, 0), dungeon));
        assertTrue(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        inventory.add(new Wood(new Position(0, 0), dungeon));
        assertTrue(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        assertDoesNotThrow(()->factory.build(inventory, dungeon.getEntities().values()));
        assertEquals(1, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "wood"));
        assertEquals(0, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "arrow"));
        assertEquals(1, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "bow"));
        // allow attack twice
        assertEquals(2, inventory.getPlayer().getNumOfAttack());
        inventory.add(new Arrow(new Position(0, 0), dungeon));
        inventory.add(new Arrow(new Position(0, 0), dungeon));
        inventory.add(new Arrow(new Position(0, 0), dungeon));
        assertDoesNotThrow(()->factory.build(inventory, dungeon.getEntities().values()));
        assertEquals(0, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "wood"));
        assertEquals(0, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "arrow"));
        assertEquals(2, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "bow"));
    }

    @Test
    public void testShield() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"width\": 10, \"height\": 10, \"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}]}"), new StandardMode());
        Inventory inventory = dungeon.getPlayer().getInventory();
        ShieldFactory factory = new ShieldFactory();
        assertFalse(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        assertThrows(InvalidActionException.class, ()->factory.build(inventory, dungeon.getEntities().values()));
        inventory.add(new Wood(new Position(0, 0), dungeon));
        inventory.add(new Wood(new Position(0, 0), dungeon));
        inventory.add(new Wood(new Position(0, 0), dungeon));
        assertFalse(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        assertThrows(InvalidActionException.class, ()->factory.build(inventory, dungeon.getEntities().values()));
        inventory.add(new Treasure(new Position(0, 0), dungeon));
        assertTrue(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        assertDoesNotThrow(()->factory.build(inventory, dungeon.getEntities().values()));
        assertEquals(1, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "wood"));
        assertEquals(0, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "treasure"));
        assertEquals(1, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "shield"));
        inventory.add(new Wood(new Position(0, 0), dungeon));
        inventory.add(new Key(new Position(0, 0), dungeon, 1));
        assertDoesNotThrow(()->factory.build(inventory, dungeon.getEntities().values()));
        assertEquals(0, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "wood"));
        assertEquals(0, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "key"));
        assertEquals(2, InventoryTestHelper.countNumOfType(inventory.getItemResponses(), "shield"));
    }

    @Test
    public void testSceptreFacotry() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}]}"), new StandardMode());
        Inventory inventory = dungeon.getPlayer().getInventory();
        BuildableFactory factory = new SceptreFactory();

        // test 1 wood, 1 key, 1 sunstone
        assertFalse(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        assertFalse(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        inventory.add(new Wood(new Position(0, 0), dungeon));
        inventory.add(new Key(new Position(0, 0), dungeon, 1));
        inventory.add(new Key(new Position(0, 0), dungeon, 2));
        inventory.add(new SunStone(new Position(0, 0), dungeon));
        assertTrue(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        factory.build(inventory, dungeon.getEntities().values());
        assertEquals(1, inventory.getNumOfTypes(Sceptre.class));
        assertEquals(1, inventory.getNumOfTypes(Key.class));
        assertEquals(0, inventory.getNumOfTypes(SunStone.class));
        assertEquals(0, inventory.getNumOfTypes(Wood.class));
        // 1 key left

        // test 2 arrows, 1 key, 1 sunstone
        inventory.add(new SunStone(new Position(0, 0), dungeon));
        inventory.add(new SunStone(new Position(0, 0), dungeon));
        inventory.add(new Arrow(new Position(0, 0), dungeon));
        assertFalse(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        inventory.add(new Arrow(new Position(0, 0), dungeon));
        assertTrue(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        inventory.add(new Arrow(new Position(0, 0), dungeon));
        factory.build(inventory, dungeon.getEntities().values());
        assertEquals(2, inventory.getNumOfTypes(Sceptre.class));
        assertEquals(1, inventory.getNumOfTypes(Arrow.class));
        assertEquals(0, inventory.getNumOfTypes(Key.class));
        assertEquals(1, inventory.getNumOfTypes(SunStone.class));
        // 1 sunstone, 1 arrow left

        // test 2 arrows, 1 treasure, 1 sunstone
        inventory.add(new Treasure(new Position(0, 0), dungeon));
        assertFalse(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        inventory.add(new Arrow(new Position(0, 0), dungeon));
        assertTrue(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        factory.build(inventory, dungeon.getEntities().values());
        assertEquals(3, inventory.getNumOfTypes(Sceptre.class));
        assertEquals(0, inventory.getNumOfTypes(Arrow.class));
        assertEquals(0, inventory.getNumOfTypes(SunStone.class));
        assertEquals(0, inventory.getNumOfTypes(Treasure.class));

        // test 1 wood, 1 treasure, 1 sunstone
        inventory.add(new Wood(new Position(0, 0), dungeon));
        inventory.add(new Treasure(new Position(0, 0), dungeon));
        assertFalse(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        assertThrows(InvalidActionException.class, ()->factory.build(inventory, dungeon.getEntities().values()));
        assertEquals(3, inventory.getNumOfTypes(Sceptre.class));
        assertEquals(1, inventory.getNumOfTypes(Wood.class));
        inventory.add(new SunStone(new Position(0, 0), dungeon));
        assertTrue(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        inventory.add(new Wood(new Position(0, 0), dungeon));
        assertTrue(()->factory.isBuildable(inventory, dungeon.getEntities().values()));
        factory.build(inventory, dungeon.getEntities().values());
        assertEquals(4, inventory.getNumOfTypes(Sceptre.class));
        assertEquals(1, inventory.getNumOfTypes(Wood.class));
        assertEquals(0, inventory.getNumOfTypes(SunStone.class));
        assertEquals(0, inventory.getNumOfTypes(Treasure.class));
    }

    public void testMidnightArmour() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}]}"), new StandardMode());
        Player player = dungeon.getPlayer();
        Inventory inventory = player.getInventory();
        BuildableFactory factory = new MidnightArmourFactory();
        // test not enough materials no zombies
        assertFalse(factory.isBuildable(inventory, dungeon.getEntities().values()));
        assertThrows(InvalidActionException.class, ()->factory.build(inventory, dungeon.getEntities().values()));
        inventory.add(new Armour(new Position(0, 0), dungeon));
        assertThrows(InvalidActionException.class, ()->factory.build(inventory, dungeon.getEntities().values()));
        inventory.add(new SunStone(new Position(0, 0), dungeon));
        // test enough materials no zombies
        assertTrue(factory.isBuildable(inventory, dungeon.getEntities().values()));

        // test enough materials there are zombies
        Entity zombie = new ZombieToast(new Position(2, 4), dungeon);
        assertThrows(InvalidActionException.class, ()->factory.build(inventory, dungeon.getEntities().values()));
        dungeon.removeEntity(zombie, zombie.getPosition());

        // test enough materials no zombies and build
        assertDoesNotThrow(()->factory.build(inventory, dungeon.getEntities().values()));
        assertEquals(1, inventory.getNumOfTypes(MidnightArmour.class));
        assertEquals(0, inventory.getNumOfTypes(Armour.class));
        assertEquals(0, inventory.getNumOfTypes(SunStone.class));
    }
}
