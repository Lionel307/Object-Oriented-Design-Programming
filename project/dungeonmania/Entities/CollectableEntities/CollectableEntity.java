package dungeonmania.Entities.CollectableEntities;

import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.Storable;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class CollectableEntity extends Entity implements Storable {
    protected Inventory inventory;
    public CollectableEntity(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
    }
    @Override
    public void encounter(Entity entity, Direction direction) {
        if (entity instanceof Player == false) {
            return;
        }
        Player player = (Player) entity;
        inventory = player.getInventory();
        inventory.add(this);
        this.removeFromDungeon();
    }
}
