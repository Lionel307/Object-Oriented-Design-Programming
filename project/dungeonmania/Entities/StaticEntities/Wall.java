package dungeonmania.Entities.StaticEntities;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.MovingEntity.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Wall extends StaticEntity{

    public Wall(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
    }

    @Override
    public String getType() {
        return "wall";
    }

    @Override
    public void tick() {
        
    }

    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        if (entity instanceof Spider) {
            return true;
        }
        return false;
    }

    @Override
    public void encounter(Entity entity, Direction direction) {

    }
    
}
