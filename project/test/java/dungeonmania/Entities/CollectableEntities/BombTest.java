package dungeonmania.Entities.CollectableEntities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.*;
import dungeonmania.GameModes.*;
import dungeonmania.Entities.*;
import dungeonmania.Entities.MovingEntity.Mercenary;
import dungeonmania.Entities.MovingEntity.Spider;
import dungeonmania.Entities.StaticEntities.Wall;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BombTest {
    @Test
    public void testPlayerCollectBomb() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        
        new Bomb(new Position(5, 4), dungeon);
        assertFalse(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("bomb")));

        dungeon.tick(null, Direction.UP);

        // make sure armour is collected when player moves
        assertTrue(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("bomb")));
    }

    void simulatedungeonTick(Dungeon dungeon, Usable item, Direction movementDirection) {
        dungeon.getPlayer().setItemUsed(item);
        dungeon.getPlayer().setDirection(movementDirection);
        dungeon.tickWithoutSpawn();
    }

    @Test
    public void testBombDetonation() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}, {\"x\": 2, \"y\": 2, \"type\": \"wall\"}, {\"x\": 4, \"y\": 2, \"type\": \"boulder\"}, {\"x\": 3, \"y\": 2, \"type\": \"switch\"}, {\"x\": 1, \"y\": 1, \"type\": \"wall\"},]");
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());

        Bomb bomb = new Bomb(new Position(5, 4), dungeon);
        assertFalse(dungeon.getDungeonResponse().getInventory().stream().anyMatch(item -> item.getType().equals("bomb")));

        // bomb has been collected
        simulatedungeonTick(dungeon, null, Direction.UP);

        // move towards the wall
        simulatedungeonTick(dungeon, null, Direction.LEFT);
        simulatedungeonTick(dungeon, null, Direction.LEFT);
        simulatedungeonTick(dungeon, null, Direction.UP);
        
        //place bomb
        simulatedungeonTick(dungeon, bomb, Direction.RIGHT);

        // move to push boulder
        simulatedungeonTick(dungeon, null, Direction.RIGHT);
        simulatedungeonTick(dungeon, null, Direction.UP);
        simulatedungeonTick(dungeon, null, Direction.LEFT);

        // bomb explodes, remove all other items - one other random wall remains
        long numOfEntitiesExcludePlayer = dungeon.getEntities().values().stream().filter(e -> ! (e instanceof Player)).count();
        assertTrue(numOfEntitiesExcludePlayer == 1);
    }

    @Test
    public void testBombHasNotBeenCollected() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Mercenary mercenary = new Mercenary(new Position(5, 3), dungeon);

        Bomb bomb = new Bomb(new Position(5, 4), dungeon);
        simulatedungeonTick(dungeon, null, Direction.DOWN);
        assertEquals(new Position(5, 4), mercenary.getPosition());
    }

    @Test
    public void testHasBeenCollected() {
        JSONObject jsonObj = new JSONObject();
        JSONArray entitiesArray = new JSONArray("[{\"x\": 5, \"y\": 5, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Bomb bomb = new Bomb(new Position(5, 4), dungeon);
        simulatedungeonTick(dungeon, null, Direction.UP);
        simulatedungeonTick(dungeon, bomb, Direction.NONE);
        assertTrue(dungeon.getMap().get(new Position(5, 4)).get(0).equals(bomb));
        Spider spider = new Spider(new Position(5, 5), dungeon);
        simulatedungeonTick(dungeon, null, Direction.LEFT);
        assertEquals(new Position(5 ,4), spider.getPosition());
        assertFalse(bomb.allowMove(dungeon.getPlayer(), Direction.RIGHT));
    }


}
