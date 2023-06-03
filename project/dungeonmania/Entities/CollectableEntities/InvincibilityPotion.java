package dungeonmania.Entities.CollectableEntities;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.GameModes.GameModeStrategy;
import dungeonmania.States.InvincibilityPotionState;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Dungeon;
import dungeonmania.Usable;

public class InvincibilityPotion extends CollectableEntity implements Usable{
    
    private GameModeStrategy gameMode;

    public InvincibilityPotion(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
        gameMode = dungeon.getGameMode();

    }

    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        return true;
    }

    public void use(Player player) {
        if (gameMode.invincibilityPotionHasEffect()) {
            player.addPlayerState(new InvincibilityPotionState());
        }
        player.getInventory().remove(this);
    }

    @Override
    public void tick() {
        return;
    }

    @Override
    public String getType() {
        return "invincibility_potion";
    }
}
