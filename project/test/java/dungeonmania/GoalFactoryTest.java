package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.GameModes.StandardMode;
import dungeonmania.Goals.*;

public class GoalFactoryTest {
    // if there is no goal-condition key, pass a null to the factory
    @Test
    public void NoGoalTest() {
        assertTrue(Goal.parse(null) instanceof NoGoal);
        assertTrue(Goal.parse(new JSONObject("{\"goal\": \"\"}")) instanceof NoGoal);
    }

    @Test
    public void AllFloorSwitchTest() {
        assertTrue(Goal.parse(new JSONObject("{\"goal\": \"boulders\"}")) instanceof AllFloorSwitch);
    }

    @Test
    public void CollectAllTreasureTest() {
        assertTrue(Goal.parse(new JSONObject("{\"goal\": \"treasure\"}")) instanceof CollectAllTreasure);
    }

    @Test
    public void DestroyAllEnemiesTest() {
        assertTrue(Goal.parse(new JSONObject("{\"goal\": \"enemies\"}")) instanceof DestroyAllEnemies);
    }

    @Test
    public void GetToExitTest() {
        assertTrue(Goal.parse(new JSONObject("{\"goal\": \"exit\"}")) instanceof GetToExit);
    }

    @Test
    public void AndTest() {
        Goal goal = Goal.parse(new JSONObject("{\"goal\": \"AND\",\"subgoals\": [{\"goal\": \"exit\"},{\"goal\": \"treasure\"}]}"));
        Dungeon dungeon = new Dungeon(new JSONObject("{\"width\": 10, \"height\": 10, \"entities\": [{\"x\": 2, \"y\": 2, \"type\": \"player\"}, {\"x\":1, \"y\": 2, \"type\":\"treasure\"}, {\"x\": 0, \"y\": 0, \"type\": \"exit\"}]}"), new StandardMode());
        assertTrue(goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()).equals("(:exit AND :treasure)") || 
            goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()).equals("(:treasure AND :exit)"));
    }

    @Test
    public void OrTest() {
        Goal goal = Goal.parse(new JSONObject("{\"goal\": \"OR\",\"subgoals\": [{\"goal\": \"exit\"},{\"goal\": \"treasure\"}]}"));
        Dungeon dungeon = new Dungeon(new JSONObject("{\"width\": 10, \"height\": 10, \"entities\": [{\"x\": 2, \"y\": 2, \"type\": \"player\"}, {\"x\":1, \"y\": 2, \"type\":\"treasure\"}, {\"x\": 0, \"y\": 0, \"type\": \"exit\"}]}"), new StandardMode());
        assertTrue(goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()).equals("(:exit OR :treasure)") || 
            goal.getUnsatisfiedGoals(dungeon.getMap(), dungeon.getEntities(), dungeon.getPlayer()).equals("(:treasure OR :exit)"));
    }

}
