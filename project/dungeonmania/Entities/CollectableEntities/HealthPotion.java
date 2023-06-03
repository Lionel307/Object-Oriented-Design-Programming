package dungeonmania.Entities.CollectableEntities;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Dungeon;
import dungeonmania.Usable;

public class HealthPotion extends CollectableEntity implements Usable{
    
    public HealthPotion(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
    }

    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        return true;
    }

    public void use(Player player) {
        player.resetHealth();
        player.getInventory().remove(this);
    }

    @Override
    public void tick() {
        return;
    }

    @Override
    public String getType() {
        return "health_potion";
    }
}
