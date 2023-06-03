package dungeonmania.Entities.StaticEntities;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.MovingEntity.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Boulder extends StaticEntity{
    public Boulder(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
    }

    @Override
    public String getType() {
        return "boulder";
    }

    @Override
    public void tick() {
        
    }

    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        if (entity instanceof Player) {
            // the player must move as nothing can be at this position except this boulder
            Position adjPos = this.getPosition().translateBy(direction);
            return this.checkPositionAllowMove(adjPos, direction);
        } else if (entity instanceof Spider) {
            ((Spider)entity).reverseSpiderMovement();
            return false;
        }
        return false;
    }

    @Override
    public void encounter(Entity entity, Direction direction) {
        if (entity instanceof Player) {
            Position adjPos = this.getPosition().translateBy(direction);
            this.setPositionAndEncounter(adjPos, direction);
        }
    }
}
