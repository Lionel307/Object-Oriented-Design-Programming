package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.util.Position;

public class NoGoal extends Goal{

    @Override
    public String getUnsatisfiedGoals(Map<Position, List<Entity>> map, Map<String, Entity> entities, Player player) {
        return "";
    }
    
}
