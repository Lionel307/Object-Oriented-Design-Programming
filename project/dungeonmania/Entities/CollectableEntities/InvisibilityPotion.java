package dungeonmania.Entities.CollectableEntities;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.States.InvisibilityPotionState;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Dungeon;
import dungeonmania.Usable;

public class InvisibilityPotion extends CollectableEntity implements Usable{
    public InvisibilityPotion(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
    }
    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        return true;
    }

    public void use(Player player) {
        player.addPlayerState(new InvisibilityPotionState());;
        player.getInventory().remove(this);
    }

    @Override
    public void tick() {
        return;
    }

    @Override
    public String getType() {
        return "invisibility_potion";
    }
}