package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.CollectableEntities.Treasure;
import dungeonmania.util.Position;

public class CollectAllTreasure extends Goal {

    private boolean isSatisfied(Map<String, Entity> entities) {
        return !(entities.values().stream()
            .anyMatch(entity->entity instanceof Treasure));
    }

    @Override
    public String getUnsatisfiedGoals(Map<Position, List<Entity>> map, Map<String, Entity> entities, Player player) {
        if (!isSatisfied(entities)) {
            return ":treasure";
        } else {
            return "";
        }
    }

}
