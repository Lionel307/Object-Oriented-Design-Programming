package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.StaticEntities.Boulder;
import dungeonmania.Entities.StaticEntities.FloorSwitch;
import dungeonmania.util.Position;

public class AllFloorSwitch extends Goal{

    private boolean isSatisfied(Map<Position, List<Entity>> map) {
        return map.values().stream()
            .filter(value->
                value.stream().anyMatch(entity -> entity instanceof FloorSwitch)
            ).allMatch(value->
                value.stream().anyMatch(entity -> entity instanceof Boulder));
    }
    
    @Override
    public String getUnsatisfiedGoals(Map<Position, List<Entity>> map, Map<String, Entity> entities, Player player) {
        if (!isSatisfied(map)) {
            return ":boulder";
        } else {
            return "";
        }
    }
    
}
