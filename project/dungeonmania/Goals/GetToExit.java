package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.StaticEntities.Exit;
import dungeonmania.util.Position;

public class GetToExit extends Goal {

    private boolean isSatisfied(Map<Position, List<Entity>> map, Player player) {
        return map.get(player.getPosition())
            .stream().anyMatch(entity->entity instanceof Exit);
    }

    @Override
    public String getUnsatisfiedGoals(Map<Position, List<Entity>> map, Map<String, Entity> entities, Player player) {
        if (!isSatisfied(map, player)) {
            return ":exit";
        } else {
            return "";
        }
    }

}
