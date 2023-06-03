package dungeonmania.Entities.StaticEntities;

import java.util.List;

import dungeonmania.AttackWeapon;
import dungeonmania.DefendWeapon;
import dungeonmania.Dungeon;
import dungeonmania.Interactable;
import dungeonmania.Inventory;
import dungeonmania.Storable;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.MovingEntity.ZombieToast;
import dungeonmania.GameModes.GameModeStrategy;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieSpawner extends StaticEntity implements Interactable {

    private int numOfTick = 0;
    private GameModeStrategy gameMode;

    public ZombieSpawner(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
        gameMode = dungeon.getGameMode();
    }

    public boolean spawnZombie() {
        List<Position> positions = this.getPosition().getAdjacentPositions();
        for (Position pos: positions) {
            List<Entity> entitiesAtSamePos = getMap().get(pos);
            if (entitiesAtSamePos == null || 
            entitiesAtSamePos.isEmpty()) {
                new ZombieToast(pos, getDungeon());
                return true;
            }
        }
        return false;
    }


    @Override
    public String getType() {
        return "zombie_toast_spawner";
    }

    @Override
    public void tick() {
        numOfTick++;
        if (numOfTick >= gameMode.getZombiesSpawnSpeed()) {
            if (spawnZombie()) {
                numOfTick = 0;
            }
        }
    }

    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        // no entity can encounter this
        return false;
    }

    @Override
    public void encounter(Entity entity, Direction direction) {
        // don't need to check allowMove (Design by contract)
        // nothing can encounter this
    }

    @Override
    public void interact(Player character) throws InvalidActionException {
        if ( ! Position.isAdjacent(character.getPosition(), this.getPosition())) {
            throw new InvalidActionException("the player is not cardinally adjacent to the spawner");
        }

        Inventory inventory = character.getInventory();
        for (Storable item: inventory) {
            // should both AttackWeapon and DefendWeapon be counted as a weapon
            if (item instanceof AttackWeapon || item instanceof DefendWeapon) {
                removeFromDungeon();
                return;
            }
        }
        throw new InvalidActionException("the player does not have any weapons");
    }

    @Override
    public boolean isInteractable() {
        return true;
    }
}
