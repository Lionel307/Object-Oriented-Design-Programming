package dungeonmania.Entities.CollectableEntities;

import dungeonmania.Entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Dungeon;

public class Key extends CollectableEntity {
    private int keyNum;

    public Key(Position pos, Dungeon dungeon, int keyNum) {
        super(pos, dungeon);
        this.keyNum = keyNum;
    }

    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        return true;
    }

    public int getKeyNum() {
        return this.keyNum;
    }

    @Override
    public void tick() {
        return;
    }

    @Override
    public String getType() {
        return "key";
    }

}
