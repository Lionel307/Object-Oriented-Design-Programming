package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.GameModes.*;
import dungeonmania.Goals.*;
import dungeonmania.Entities.*;
import dungeonmania.Entities.CollectableEntities.Treasure;
import dungeonmania.Entities.MovingEntity.*;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class GoalTest {
    // test NoGoal
    @Test
    public void testNoGoal() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 1, \"type\": \"player\"}, {\"x\": 0, \"y\": 2, \"type\": \"exit\"}]");
        jsonObj.put("entities", entitiesArray);
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        assertEquals("", dungeon.getDungeonResponse().getGoals());
        dungeon.tick(null, Direction.DOWN);
        assertEquals("", dungeon.getDungeonResponse().getGoals());
    }

    // test exit
    @Test
    public void testExitFalse() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 2, \"type\": \"exit\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Goal getToExitGoal = new GetToExit();
        assertEquals(":exit", getToExitGoal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()));;
    }

    @Test
    public void testExitTrue() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        // the player is at the exit
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 2, \"type\": \"player\"}, {\"x\": 0, \"y\": 2, \"type\": \"exit\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"exit\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Goal getToExitGoal = new GetToExit();
        assertEquals("", getToExitGoal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()));;
    }

    // test destroy all enemies
    @Test
    public void testDestroyEnemiesButGoToExit() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"mercenary\"}, {\"x\": 1, \"y\": 0, \"type\": \"exit\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"enemies\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new PeacefulMode());
        // moves right, arrive exit should not change goals
        dungeon.getPlayer().setDirection(Direction.RIGHT);
        dungeon.tickWithoutSpawn();
        // NOTE: the frontend uses :mercenary to display the goal to destroy all enemies
        Goal destoryEnemiesGoal = new DestroyAllEnemies();
        assertEquals(":mercenary", destoryEnemiesGoal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()));
    }

    @Test
    public void testDestroyEnemiesHasZombieToast() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"enemies\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new PeacefulMode());
        // no enemies
        Goal destoryEnemiesGoal = new DestroyAllEnemies();
        assertEquals("", destoryEnemiesGoal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()));
        // add a zombieToast to make goal unsatisfied
        ZombieToast zombieToast = new ZombieToast(new Position(0, 2), dungeon);
        assertEquals(":mercenary", destoryEnemiesGoal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()));
    }

    // test bribe mercernary
    @Test
    public void testBribeMercernary() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 1, \"type\": \"player\"}, {\"x\": 0, \"y\": 2, \"type\": \"mercenary\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"enemies\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        List<EntityResponse> entityResponses = dungeon.getDungeonResponse().getEntities();
        String mercenaryId = null;
        for (EntityResponse entityResponse: entityResponses) {
            if (entityResponse.getType().equals("mercenary")) {
                mercenaryId = entityResponse.getId();
                break;
            }
        }
        // simulate that the player got treasure
        Player player = dungeon.getPlayer();
        Treasure treasure = new Treasure(new Position(0, 1), dungeon);
        treasure.encounter(player, Direction.RIGHT);
        Goal destoryEnemiesGoal = new DestroyAllEnemies();
        player.updateObservers();
        dungeon.interact(mercenaryId);
        // bribed the mercenary
        // assume the mercenary can be bribed by one treasure
        assertEquals("", destoryEnemiesGoal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()));
    }

    // test one floor switches
    @Test
    public void testFloorSwitches() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"boulder\"}, {\"x\": 0, \"y\": 2, \"type\": \"switch\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"boulders\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Goal allFloorSwitch = new AllFloorSwitch();
        assertEquals(":boulder", allFloorSwitch.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()));
        DungeonResponse response = dungeon.tick(null, Direction.DOWN);
        assertEquals("", response.getGoals());
    }

    // test two floor switches
    @Test
    public void testFloorSwitchesTwo() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"boulder\"}, {\"x\": 0, \"y\": 2, \"type\": \"switch\"}, {\"x\": 1, \"y\": 0, \"type\": \"boulder\"}, {\"x\": 1, \"y\": 0, \"type\": \"switch\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"boulders\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Goal allFloorSwitch = new AllFloorSwitch();
        assertEquals(":boulder", allFloorSwitch.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()));
        DungeonResponse response = dungeon.tick(null, Direction.DOWN);
        assertEquals("", response.getGoals());
    }

    // test collect all treasures
    @Test
    public void testTreasures() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"treasure\"}, {\"x\": 1, \"y\": 1, \"type\": \"treasure\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"treasure\"}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Goal goal = new CollectAllTreasure();
        assertEquals(":treasure", goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()));
        dungeon.tick(null, Direction.DOWN);
        assertEquals(":treasure", goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()));
        dungeon.tick(null, Direction.RIGHT);
        assertEquals("", goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()));
    }

    // test and 
    @Test
    public void testAnd() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"treasure\"}, {\"x\": 1, \"y\": 1, \"type\": \"exit\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"AND\",\"subgoals\": [{\"goal\": \"exit\"},{\"goal\": \"treasure\"}]}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Goal goal = new GoalAnd(new GetToExit(), new CollectAllTreasure());
        assertTrue(goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()).equals("(:exit AND :treasure)") || 
            goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()).equals("(:treasure AND :exit)"));
        dungeon.tick(null, Direction.DOWN);
        // satisfies treasure
        assertEquals(":exit", goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()));
        dungeon.tick(null, Direction.RIGHT);
        assertEquals("", goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()));
    }

    // test or
    @Test
    public void testOr() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"treasure\"}, {\"x\": 1, \"y\": 1, \"type\": \"exit\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"OR\",\"subgoals\": [{\"goal\": \"exit\"},{\"goal\": \"treasure\"}]}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Goal goal = new GoalOr(new GetToExit(), new CollectAllTreasure());
        assertTrue(goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()).equals("(:exit OR :treasure)") || 
            goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()).equals("(:treasure OR :exit)"));
        dungeon.tick(null, Direction.DOWN);
        // satisfies treasure
        assertEquals("", goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()));
    }

    // test nested and or
    @Test
    public void testAndOr() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"treasure\"}, {\"x\": 1, \"y\": 1, \"type\": \"exit\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"AND\",\"subgoals\": [{\"goal\": \"OR\", \"subgoals\": [{\"goal\": \"exit\"}, {\"goal\": \"boulders\"}]}, {\"goal\": \"treasure\"}]}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Goal goal = new GoalAnd(new CollectAllTreasure(), new GoalOr(new GetToExit(), new AllFloorSwitch()));
        // satisfied boulders already
        assertTrue(goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()).equals(":treasure"));
        dungeon.tick(null, Direction.DOWN);
        assertEquals("", goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()));
    }

    // test nested or and
    @Test
    public void testOrAnd() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("height", 10);
        jsonObj.put("width", 10);
        JSONArray entitiesArray = new JSONArray("[{\"x\": 0, \"y\": 0, \"type\": \"player\"}, {\"x\": 0, \"y\": 1, \"type\": \"treasure\"}, {\"x\": 1, \"y\": 1, \"type\": \"exit\"}]");
        jsonObj.put("entities", entitiesArray);
        jsonObj.put("goal-condition", new JSONObject("{\"goal\": \"OR\",\"subgoals\": [{\"goal\": \"AND\", \"subgoals\": [{\"goal\": \"exit\"}, {\"goal\": \"boulders\"}]}, {\"goal\": \"treasure\"}]}"));
        Dungeon dungeon = new Dungeon(jsonObj, new StandardMode());
        Goal goal = new GoalOr(new CollectAllTreasure(), new GoalAnd(new GetToExit(), new AllFloorSwitch()));
        // satisfied boulders already
        assertTrue(goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()).equals("(:exit OR :treasure)") 
            || goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()).equals("(:treasure OR :exit)"));
        dungeon.tick(null, Direction.DOWN);
        assertEquals("", goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()));
    }

}
