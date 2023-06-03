package dungeonmania.Entities.MovingEntity;

import dungeonmania.Bribeable;
import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.CollectableEntities.*;
import dungeonmania.States.SceptreState;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class BribeableMovingEntity extends MovingEntity implements Bribeable {
    private boolean bribedStatus;
    private double playerTreasureCount;
    private Position playerPreviousPosition;



    public BribeableMovingEntity(Position position, Dungeon dungeon, double defaultHealth, double defaultDamage) {
        super(position, dungeon, defaultHealth, defaultDamage);
        this.bribedStatus = false;
        this.playerTreasureCount = this.getDungeon().getPlayer().getNumTreasureEquivalents();
        Player player = dungeon.getPlayer();
        SceptreState sceptreState = player.getPlayerStates()
            .stream()
            .filter(state -> state instanceof SceptreState)
            .map(state -> (SceptreState)state)
            .findFirst().orElse(null);

        if (sceptreState != null) {
            sceptreState.mindControl(player, this);
        }
    }

    @Override
    public void encounter(Entity entity, Direction direction) {
        if (!this.isBribed()) {
            super.encounter(entity, direction);
        }
    }

    public double allyAttack() {
        double damage = ((this.getHealth() * this.getDamage())/5);
        return damage;
    }

    public double getPlayerTreasureCount() {
        return playerTreasureCount;
    }

    public void setPlayerTreasureCount(double playerTreasureCount) {
        this.playerTreasureCount = playerTreasureCount;
    }

    @Override
    public boolean isBribed() {
        return bribedStatus;
    }

    @Override
    public void setBribedStatus(boolean bool) {
        this.bribedStatus = bool;
    }

    @Override
    public boolean isInteractable() {
        return true;
    }

    public Position getPlayerPreviousPosition() {
        return playerPreviousPosition;
    }

    public void setPlayerPreviousPosition(Position playerPreviousPosition) {
        this.playerPreviousPosition = playerPreviousPosition;
    }

}
