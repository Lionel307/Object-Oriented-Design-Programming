package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.util.Position;

public class GoalAnd extends Goal {
    private Goal goal1;
    private Goal goal2;

    public GoalAnd(Goal goal1, Goal goal2) {
        this.goal1 = goal1;
        this.goal2 = goal2;
    }

    @Override
    public String getUnsatisfiedGoals(Map<Position, List<Entity>> map, Map<String, Entity> entities, Player player) {
        String unsatisfiedGoals1 = goal1.getUnsatisfiedGoals(map, entities, player);
        String unsatisfiedGoals2 = goal2.getUnsatisfiedGoals(map, entities, player);
        if (unsatisfiedGoals1.equals("")) {
            return unsatisfiedGoals2;
        } else if (unsatisfiedGoals2.equals("")) {
            return unsatisfiedGoals1;
        } else {
            return "(" + unsatisfiedGoals1 + " AND " + unsatisfiedGoals2 + ")";
        }
    }
}