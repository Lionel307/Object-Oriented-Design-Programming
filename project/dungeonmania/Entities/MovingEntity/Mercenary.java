package dungeonmania.Entities.MovingEntity;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.CollectableEntities.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;
import dungeonmania.States.*;

public class Mercenary extends BribeableMovingEntity implements EntityDrop, PathFinding {
    private static double battleRadius = 4;
    private boolean playerHasBattle;
    private boolean dropArmour;

    public Mercenary(Position position, Dungeon dungeon) {
        super(position, dungeon, 100, 5);
        this.dropArmour = true;
        this.playerHasBattle = false;
    }

    public Mercenary(Position position, Dungeon dungeon, Boolean bool) {
        super(position, dungeon, 100, 5);
        this.dropArmour = bool;
        this.playerHasBattle = false;
    }

    @Override
    public boolean getDropArmour() {
        return this.dropArmour;
    }

    @Override
    public String getType() {
        return "mercenary";
    }

    public double getBattleRadius() {
        return battleRadius;
    }

    // Methods for movement
    @Override
    public Position move() {
        // TODO: Check if this works once everything is merged
        if (isBribed()) {
            return this.getPlayerPreviousPosition();
        } else if (playerHasState(InvisibilityPotionState.class)) {
            return this.getPosition();
        } else {
            return this.firstMove(this.getPosition(), this.getDungeon(), this, this.getPlayerPosition());
        }
    }

    // Methods for interact
    @Override
    public void interact(Player character) throws InvalidActionException {
        int REQUIRED_TREASURE = 1;
        if (this.getPlayerTreasureCount() < REQUIRED_TREASURE) {
            throw new InvalidActionException("Not enough treasure\n");
        } else if (Math.abs(this.getPosition().getX() - this.getPlayerPosition().getX()) > 2) {
            throw new InvalidActionException("Mercenary out of range\n");
        } else if (Math.abs(this.getPosition().getY() - this.getPlayerPosition().getY()) > 2) {
            throw new InvalidActionException("Mercenary out of range\n");
        }

        this.setBribedStatus(true);
        this.getDungeon().getPlayer().addAllies(this);
        this.getDungeon().getPlayer().getInventory().removeNumOfTypes(REQUIRED_TREASURE, Treasure.class);
    }

    // Methods for player observer
    @Override
    // TODO: Make sure this works
    public void update(Player player) {
        this.setPlayerPreviousPosition(this.getPlayerPosition());
        this.setPlayerTreasureCount(player.getInventory().getNumOfTypes(Treasure.class));
        this.setPlayerHasBattle(player.getHasBattle());
        super.update(player);
        if (player.getHasBattle() && !player.getPosition().equals(this.getPosition())) {
            Position positionVector = Position.calculatePositionBetween(this.getPosition(), this.getPlayerPosition());
            double distanceX = Math.abs(positionVector.getX());
            double distanceY = Math.abs(positionVector.getY());
            double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
            if (distance < this.getBattleRadius()) {
                this.tick();
            }
        }
    }

    public boolean getPlayerHasBattle() {
        return playerHasBattle;
    }

    public void setPlayerHasBattle(boolean bool) {
        this.playerHasBattle = bool;
    }

}
