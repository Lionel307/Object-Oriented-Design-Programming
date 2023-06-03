package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.util.Position;

public abstract class Goal {
    /**
     * @param map the map of the dungeon
     * @param entities the entities map in the dungeon
     * @param player the player of the dungeon
     * @return the string that represents the unsatisfied goals
     */
    abstract public String getUnsatisfiedGoals(Map<Position, List<Entity>> map, Map<String, Entity> entities, Player player);

    /**
     * use the goalJson to create a Goal object
     * @param goalJson the json that represents the goal
     * @return the correcponding Goal object
     */
    static public Goal parse(JSONObject goalJson) {
        if (goalJson == null) {
            return new NoGoal();
        }
        String goalType = goalJson.getString("goal");
        if (goalType.equals("boulders")) {
            return new AllFloorSwitch();
        } else if (goalType.equals("treasure")) {
            return new CollectAllTreasure();
        } else if (goalType.equals("enemies")) {
            return new DestroyAllEnemies();
        } else if (goalType.equals("exit")) {
            return new GetToExit();
        } else if (goalType.equals("AND")) {
            JSONArray subgoals = goalJson.getJSONArray("subgoals");
            return new GoalAnd(parse(subgoals.getJSONObject(0)), parse(subgoals.getJSONObject(1)));
        } else if (goalType.equals("OR")) {
            JSONArray subgoals = goalJson.getJSONArray("subgoals");
            return new GoalOr(parse(subgoals.getJSONObject(0)), parse(subgoals.getJSONObject(1)));
        } else {
            return new NoGoal();
        }
    }
}
