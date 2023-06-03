package dungeonmania.Entities.MovingEntity;

import java.util.List;

import dungeonmania.Battleable;
import dungeonmania.CharacterObserver;
import dungeonmania.Dungeon;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.StaticEntities.SwampTile;
import dungeonmania.GameModes.GameModeStrategy;
import dungeonmania.States.InvincibilityPotionState;
import dungeonmania.States.InvisibilityPotionState;
import dungeonmania.States.PlayerState;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class MovingEntity extends Entity implements Battleable, CharacterObserver {
    private double health;
    private double damage;
    private GameModeStrategy gameMode;
    private Position playerPosition;
    private List<PlayerState> playerStates;

    // default constructor for moving entity
    public MovingEntity(Position pos, Dungeon dungeon, double defaultHealth, double defaultDamage) {
        super(pos, dungeon);
        this.gameMode = dungeon.getGameMode();
        this.setHealth(defaultHealth);
        this.setDamage(defaultDamage);
        this.setPlayerPosition(dungeon.getPlayer().getPosition());
        this.setPlayerStates(dungeon.getPlayer().getPlayerStates());
        dungeon.getPlayer().addObserver(this);
    }

    public void setDamage(double defaultDamage) {
        this.damage = defaultDamage;
    }

    public double getDamage() {
        return damage;
    }

    public Position getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Position playerPosition) {
        this.playerPosition = playerPosition;
    }

    public List<PlayerState> getPlayerStates() {
        return playerStates;
    }

    public void setPlayerStates(List<PlayerState> playerStates) {
        this.playerStates = playerStates;
    }

    protected boolean playerHasState(Class<? extends PlayerState> stateType) {
        return playerStates.stream().anyMatch(state -> stateType.isAssignableFrom(state.getClass()));
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public boolean shouldAttack() {
        return gameMode.shouldEnemyAttack();
    }

    public Position moveVerticalInvincibleState() {
        Position positionVector = Position.calculatePositionBetween(this.getPosition(), this.getPlayerPosition());
        if (positionVector.getY() < 0) {
            return this.getPosition().translateBy(Direction.DOWN);
        } else if (positionVector.getY() > 0) {
            return this.getPosition().translateBy(Direction.UP);
        } else {
            return this.getPosition();
        }
    }

    public Position moveHorizontalInvincibleState() {
        Position positionVector = Position.calculatePositionBetween(this.getPosition(), this.getPlayerPosition());
        if (positionVector.getX() < 0) {
            return this.getPosition().translateBy(Direction.RIGHT);
        } else if (positionVector.getX() > 0) {
            return this.getPosition().translateBy(Direction.LEFT);
        } else {
            return this.getPosition();
        }
    }
    
    @Override
    public void tick() {
        if (playerHasState(InvincibilityPotionState.class)) {
            this.invincibilityMovement();
            return;
        } 

        if (stuckInSwamp()) {
            setPositionAndEncounter(this.getPosition(), null);
            return;
        }

        Position nextPosition = this.move();
        if (checkPositionAllowMove(nextPosition, null)) {
            setPositionAndEncounter(nextPosition, null);
        }
        
    }


    public boolean stuckInSwamp() {
        List<Entity> entities = this.getDungeon().getEntitiesAtPos(this.getPosition());
        Integer numTicks = 0;
        Integer movementFactor = 0;
        SwampTile swamp;
        for (Entity entity : entities) {
            if (entity instanceof SwampTile) {
                swamp = (SwampTile) entity;
                numTicks = swamp.getNumTicks();
                movementFactor = swamp.getMovementFactor();
                if (numTicks <= movementFactor) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public void invincibilityMovement() {
        if (checkPositionAllowMove(this.moveVerticalInvincibleState(), null)) {
            setPositionAndEncounter(this.moveVerticalInvincibleState(), null);
        } else if (checkPositionAllowMove(this.moveHorizontalInvincibleState(), null)) {
            setPositionAndEncounter(this.moveHorizontalInvincibleState(), null);
        }
    }

    @Override
    public void update(Player player) {
        this.setPlayerPosition(player.getPosition());
        this.setPlayerStates(player.getPlayerStates());
    }

    @Override
    public double attack(Battleable enemy) {
        if (!this.shouldAttack()) {
            return 0;
        }
        double damage = ((this.getHealth() * this.getDamage())/10);
        return damage;
    }

    @Override
    public boolean defend(double damage) {
        double currentHealth = this.getHealth();
        this.setHealth(currentHealth - damage);;
        if (this.getHealth() <= 0) {
            this.removeFromDungeon();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void encounter(Entity entity, Direction direction) {
        if (entity instanceof Player && 
        ! ( ((Player)entity).hasState(InvisibilityPotionState.class))) {

            ((Player)entity).battle(this);
        }
    }

    @Override
    public void removeFromDungeon() {
        super.removeFromDungeon();
        getDungeon().getPlayer().removeObserver(this);
    }

    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        if (entity instanceof Player ||
            entity instanceof MovingEntity) {
            return true;
        }
        return false;
    }
    
    abstract public Position move();

}
