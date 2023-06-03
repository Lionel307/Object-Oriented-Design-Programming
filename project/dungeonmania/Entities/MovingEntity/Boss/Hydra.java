package dungeonmania.Entities.MovingEntity.Boss;

import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.CollectableEntities.Anduril;
import dungeonmania.Entities.MovingEntity.MovingEntity;
import dungeonmania.Entities.MovingEntity.RandomMovement;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Hydra extends MovingEntity implements RandomMovement {
    // TODO: Change this to suit boss stats
    private static double defaultHealth = 100;
    private static double defaultDamage = 5;
    private boolean playerHasAnduril;
    private Random random;

    public Hydra(Position position, Dungeon dungeon) {
        super(position, dungeon, defaultHealth, defaultDamage);
        if (this.getDungeon().getPlayer().getInventory().getNumOfTypes(Anduril.class) > 0) {
            this.setPlayerHasAnduril(true);
        } else {
            this.setPlayerHasAnduril(false);
        }
        random = new Random();
    }

    @Override
    public String getType() {
        return "hydra";
    }

    public void setPlayerHasAnduril(boolean bool) {
        this.playerHasAnduril = bool;
    }

    public boolean getPlayerHasAnduril() {
        return playerHasAnduril;
    }

    @Override
    public void update(Player player) {
        if (player.getInventory().getNumOfTypes(Anduril.class) > 0) {
            this.setPlayerHasAnduril(true);
        } else {
            this.setPlayerHasAnduril(false);
        }
        super.update(player);
    }

    @Override
    public Position move() {
        Direction direction = intToDirection(random.nextInt(4));
        return this.getPosition().translateBy(direction);
    }
    
    @Override
    public boolean defend(double damage) {
        if (random.nextInt(2) == 0 && !this.getPlayerHasAnduril()) {
            double currentHealth = this.getHealth();
            this.setHealth(currentHealth + damage);
            return true;
        } else {
            super.defend(damage);
        }
        return true;
    }
}
