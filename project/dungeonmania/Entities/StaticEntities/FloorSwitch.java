package dungeonmania.Entities.StaticEntities;


import dungeonmania.Dungeon;
import dungeonmania.Entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity{
    public FloorSwitch(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
    }

    @Override
    public String getType() {
        return "switch";
    }

    @Override
    public void tick() {
        
    }

    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        return true;
    }

    @Override
    public void encounter(Entity entity, Direction direction) {
        
    }
}
