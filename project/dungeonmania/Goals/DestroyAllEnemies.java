package dungeonmania.Goals;

import dungeonmania.Entities.MovingEntity.*;
import java.util.List;
import java.util.Map;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.util.Position;

public class DestroyAllEnemies extends Goal {

    private boolean isSatisfied(Map<String, Entity> entities) {
        // true if every enemies except Mercenary are killed 
        // and every mercenary isBribed
        return entities.values().stream()
            .filter(entity->entity instanceof MovingEntity)
            .allMatch(entity->entity instanceof Mercenary && 
                ((Mercenary)entity).isBribed());
    }

    @Override
    public String getUnsatisfiedGoals(Map<Position, List<Entity>> map, Map<String, Entity> entities, Player player) {
        if (!isSatisfied(entities)) {
            return ":mercenary";
        } else {
            return "";
        }
    }

}
