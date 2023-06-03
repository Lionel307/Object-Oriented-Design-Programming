package dungeonmania.Entities.StaticEntities;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Entity;
import dungeonmania.util.Position;

public abstract class StaticEntity extends Entity {
    public StaticEntity(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
    }
}
