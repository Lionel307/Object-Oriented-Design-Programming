package dungeonmania.Entities.CollectableEntities;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SunStone extends CollectableEntity {

    public SunStone(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
    }

    @Override
    public String getType() {
        return "sun_stone";
    }

    @Override
    public void tick() {
        return;
    }

    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        return true;
    }
    
}
