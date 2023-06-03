package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Predicate;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.Sceptre;
import dungeonmania.Entities.MovingEntity.*;
import dungeonmania.Entities.MovingEntity.Boss.Assassin;
import dungeonmania.GameModes.StandardMode;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SceptreTest {
    // test mercerany and assasin are bribed and are allies of player
    @Test
    public void testBribedafterUse() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}, {\"x\": 1, \"y\": 4, \"type\": \"mercenary\"}, {\"x\": 3, \"y\": 4, \"type\": \"mercenary\"}, {\"x\": 6, \"y\": 5, \"type\": \"assassin\"}]}"), new StandardMode());
        Player player = dungeon.getPlayer();
        Inventory inventory = player.getInventory();
        Sceptre sceptre = new Sceptre(inventory);
        player.setItemUsed(sceptre);
        player.setDirection(Direction.RIGHT);
        dungeon.tickWithoutSpawn();
        Predicate<Entity> allBribed = new Predicate<Entity>() {
            @Override
            public boolean test(Entity t) {
                if (t instanceof Mercenary) {
                    return ((Mercenary)t).isBribed();
                } else if (t instanceof Assassin) {
                    return ((Assassin)t).isBribed();
                }
                return true;
            }
        };
        assertEquals(3, player.getAllies().size());
        assertTrue(dungeon.getEntities().values().stream().allMatch(allBribed));
    }

    // test use: last for 10 ticks and will continue to bribed within the 10 ticks
    @Test
    public void testLast10Ticks() {
        Dungeon dungeon = new Dungeon(new JSONObject("{\"entities\":[{\"x\": 1, \"y\": 1, \"type\": \"player\"}, {\"x\": 1, \"y\": 4, \"type\": \"mercenary\"}, {\"x\": 3, \"y\": 4, \"type\": \"mercenary\"}, {\"x\": 6, \"y\": 5, \"type\": \"assassin\"}]}"), new StandardMode());
        Player player = dungeon.getPlayer();
        Inventory inventory = player.getInventory();
        Sceptre sceptre = new Sceptre(inventory);
        Predicate<Entity> allBribed = new Predicate<Entity>() {
            @Override
            public boolean test(Entity t) {
                if (t instanceof Mercenary) {
                    return ((Mercenary)t).isBribed();
                } else if (t instanceof Assassin) {
                    return ((Assassin)t).isBribed();
                }
                return true;
            }
        };
        player.setItemUsed(sceptre);
        player.setDirection(Direction.RIGHT);
        dungeon.tickWithoutSpawn();
        assertTrue(dungeon.getEntities().values().stream().allMatch(allBribed));
        assertTrue(player.getAllies().size() == 3);

        // add a mercenary to see if it will be bribed
        Mercenary mercenary = new Mercenary(new Position(7, 5), dungeon);

        for (int i = 1; i < 9; i++) {
            player.setItemUsed((Usable)null);
            player.setDirection(Direction.RIGHT);
            dungeon.tickWithoutSpawn();
            assertEquals(4, player.getAllies().size());
            assertTrue(mercenary.isBribed());
        }
        player.setItemUsed((Usable)null);
        player.setDirection(Direction.RIGHT);
        dungeon.tickWithoutSpawn();
        assertFalse(dungeon.getEntities().values().stream().allMatch(allBribed));
        assertEquals(0, player.getAllies().size());
    }
}
