package dungeonmania.Entities.CollectableEntities;

import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.Entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class OneRing extends CollectableEntity {

    public OneRing(Inventory inventory, Dungeon dungeon) {
        super(null, dungeon);
        dungeon.removeEntity(this, null);
        inventory.add(this);
    } 

    public OneRing(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
    }

    @Override
    public String getType() {
        return "one_ring";
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
