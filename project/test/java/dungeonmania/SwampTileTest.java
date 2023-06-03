package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.MovingEntity.Mercenary;
import dungeonmania.Entities.MovingEntity.Spider;
import dungeonmania.Entities.StaticEntities.SwampTile;
import dungeonmania.GameModes.StandardMode;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwampTileTest {
    @Test
    public void createSwampTile() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());

        SwampTile swamp = new SwampTile(new Position(0, 1), dungeon, 1);

        assertEquals(new Position(0, 1), swamp.getPosition());
    }

    // @Test
    // public void slowDownMercenary() {
    //     JSONObject jsonObject = new JSONObject();
    //     jsonObject.put("height", 5);
    //     jsonObject.put("width", 5);
    //     JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
    //     jsonObject.put("entities", entities);
    //     Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());
        
    //     SwampTile swamp = new SwampTile(new Position(0, 3), dungeon, 1);

    //     Mercenary mercenary = new Mercenary(new Position(0, 3), dungeon);
        
    //     assertEquals(new Position(0, 3), swamp.getPosition());

    //     dungeon.tickWithoutSpawn(null, Direction.NONE);
    //     assertEquals(new Position(0, 3), mercenary.getPosition());
    //     dungeon.tickWithoutSpawn(null, Direction.NONE);
    //     assertEquals(new Position(0, 2), mercenary.getPosition());
    // }

    @Test 
    public void slowDownSpider() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());
        
        SwampTile swamp = new SwampTile(new Position(2, 1), dungeon, 1);

        Spider spider = new Spider(new Position(2, 2), dungeon);
        
        assertEquals(new Position(2, 1), swamp.getPosition());

        dungeon.tickWithoutSpawn(null, Direction.NONE);
        assertEquals(new Position(2, 1), spider.getPosition());
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        assertEquals(new Position(2, 1), spider.getPosition());
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        assertEquals(new Position(3, 1), spider.getPosition());
    }

    @Test 
    public void highMovementFactor() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());
        
        SwampTile swamp = new SwampTile(new Position(2, 1), dungeon, 5);

        Spider spider = new Spider(new Position(2, 2), dungeon);
        
        assertEquals(new Position(2, 1), swamp.getPosition());

        dungeon.tickWithoutSpawn(null, Direction.NONE);
        assertEquals(new Position(2, 1), spider.getPosition());
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        // numtick = 1
        assertEquals(new Position(2, 1), spider.getPosition());
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        // numtick = 2
        assertEquals(new Position(2, 1), spider.getPosition());
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        // numtick = 3
        assertEquals(new Position(2, 1), spider.getPosition());
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        // numtick = 4
        assertEquals(new Position(2, 1), spider.getPosition());
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        // numtick = 5
        assertEquals(new Position(2, 1), spider.getPosition());
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        // numtick = 6 and the spider should move
        assertEquals(new Position(3, 1), spider.getPosition());
    }

    @Test
    public void twoSpiders() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());
        
        SwampTile swamp = new SwampTile(new Position(2, 1), dungeon, 1);

        Spider spider = new Spider(new Position(2, 2), dungeon);

        dungeon.tickWithoutSpawn(null, Direction.NONE);
        assertEquals(new Position(2, 1), spider.getPosition());
        
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        assertEquals(new Position(2, 1), spider.getPosition());

        
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        assertEquals(new Position(3, 1), spider.getPosition());
        
        Spider spider2 = new Spider(new Position(2, 2), dungeon);
        dungeon.tickWithoutSpawn(null, Direction.NONE);
        assertEquals(new Position(2, 1), spider2.getPosition());

        dungeon.tickWithoutSpawn(null, Direction.NONE);
        assertEquals(new Position(2, 1), spider2.getPosition());

        dungeon.tickWithoutSpawn(null, Direction.NONE);
        assertEquals(new Position(3, 1), spider2.getPosition());

    }

    @Test
    public void oneEntityinSwamp() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", 5);
        jsonObject.put("width", 5);
        JSONArray entities = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObject.put("entities", entities);
        Dungeon dungeon = new Dungeon(jsonObject, new StandardMode());
        
        SwampTile swamp = new SwampTile(new Position(2, 1), dungeon, 1);

        Spider spider = new Spider(new Position(2, 2), dungeon);

        dungeon.tickWithoutSpawn(null, Direction.NONE);
        assertEquals(new Position(2, 1), spider.getPosition());
        
        Spider spider2 = new Spider(new Position(2, 2), dungeon);

        dungeon.tickWithoutSpawn(null, Direction.NONE);
        assertEquals(new Position(2, 1), spider.getPosition());
        assertEquals(new Position(2, 2), spider2.getPosition());



    }

    
}
