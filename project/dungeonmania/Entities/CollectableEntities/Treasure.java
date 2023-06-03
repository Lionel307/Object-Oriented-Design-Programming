package dungeonmania.Entities.CollectableEntities;

import dungeonmania.Entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Dungeon;


public class Treasure extends CollectableEntity {

    public Treasure(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
    }

    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        return true;
    }

    @Override
    public void tick() {
        return;
    }

    @Override
    public String getType() {
        return "treasure";
    }
}
