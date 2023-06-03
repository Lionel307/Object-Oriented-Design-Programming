package dungeonmania.BuildableFactories;

import java.util.Collection;

import dungeonmania.Inventory;
import dungeonmania.Entities.Entity;
import dungeonmania.exceptions.InvalidActionException;

public abstract class BuildableFactory {
    /**
     * @param inventory
     * @param entities all entities in the dungeon
     * @return whether the buildableEntity(a Storable) can be built
     */
    abstract public boolean isBuildable(Inventory inventory, Collection<Entity> entities);
    /**
     * build the storable and put it in the inventory
     * @param inventory
     * @param entities all entities in the dungeon
     * @throws InvalidActionException if the object cannot be built
     */
    abstract public void build(Inventory inventory, Collection<Entity> entities) throws InvalidActionException;
}