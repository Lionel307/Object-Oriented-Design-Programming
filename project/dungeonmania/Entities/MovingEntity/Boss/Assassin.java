package dungeonmania.Entities.MovingEntity.Boss;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.CollectableEntities.*;
import dungeonmania.Entities.MovingEntity.BribeableMovingEntity;
import dungeonmania.Entities.MovingEntity.PathFinding;
import dungeonmania.States.*;
import dungeonmania.exceptions.*;
import dungeonmania.util.*;

public class Assassin extends BribeableMovingEntity implements Boss, PathFinding {
    private static double defaultHealth = 100;
    private static double defaultDamage = 10;
    private static double battleRadius = 4;
    private boolean playerHasBattle;
    private double playerOneRingCount;
    
    public Assassin(Position position, Dungeon dungeon) {
        super(position, dungeon, defaultHealth, defaultDamage);
        this.playerHasBattle = false;
        this.playerOneRingCount = this.getDungeon().getPlayer().getInventory().getNumOfTypes(Treasure.class);
    }

    @Override
    public String getType() {
        return "assassin";
    }

    public double getBattleRadius() {
        return battleRadius;
    }

    public double getPlayerOneRingCount() {
        return playerOneRingCount;
    }

    public void setPlayerOneRingCount(double OneRingCount) {
        this.playerOneRingCount = OneRingCount;
    }

    // Methods for interact
    @Override
    public void interact(Player character) throws InvalidActionException {
        int REQUIRED_TREASURE = 1;
        if (this.getPlayerTreasureCount() < REQUIRED_TREASURE) {
            throw new InvalidActionException("Not enough treasure\n");
        } else if (this.getPlayerOneRingCount() < 1) {
            throw new InvalidActionException("No One Ring\n");
        } else if (Math.abs(this.getPosition().getX() - this.getPlayerPosition().getX()) > 2) {
            throw new InvalidActionException("Assassin out of range\n");
        } else if (Math.abs(this.getPosition().getY() - this.getPlayerPosition().getY()) > 2) {
            throw new InvalidActionException("Assassin out of range\n");
        }

        this.setBribedStatus(true);
        this.getDungeon().getPlayer().addAllies(this);

        long numSunStones = this.getDungeon().getPlayer().getInventory().getNumOfTypes(SunStone.class);
        if (numSunStones < REQUIRED_TREASURE) {
            int removedTreasure = (int)(REQUIRED_TREASURE - numSunStones);
            this.getDungeon().getPlayer().getInventory().removeNumOfTypes(removedTreasure, Treasure.class);
        }
        this.getDungeon().getPlayer().getInventory().removeNumOfTypes(REQUIRED_TREASURE, OneRing.class);
    }

    // Methods for player observer
    @Override
    public void update(Player player) {
        this.setPlayerPreviousPosition(this.getPlayerPosition());
        this.setPlayerTreasureCount(player.getNumTreasureEquivalents());
        this.setPlayerOneRingCount(player.getInventory().getNumOfTypes(OneRing.class));
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
}
