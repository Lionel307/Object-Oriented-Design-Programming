package dungeonmania.BuildableFactories;

import java.util.Collection;

import dungeonmania.Inventory;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Sceptre;
import dungeonmania.Entities.CollectableEntities.*;
import dungeonmania.exceptions.InvalidActionException;

public class SceptreFactory extends BuildableFactory{

    @Override
    public boolean isBuildable(Inventory inventory, Collection<Entity> entities) {
        return
                (inventory.getNumOfTypes(Wood.class) >= 1 
            ||
                inventory.getNumOfTypes(Arrow.class) >= 2)
        &&
                (inventory.getNumOfTypes(Treasure.class) >= 1
            ||
                inventory.getNumOfTypes(Key.class) >= 1)
        &&
            (inventory.getNumOfTypes(SunStone.class) >= 1);
    }

    @Override
    public void build(Inventory inventory, Collection<Entity> entities) throws InvalidActionException {
        if (!isBuildable(inventory, entities)) {
            throw new InvalidActionException("cannot build sceptre");
        }
        
        if (inventory.getNumOfTypes(Wood.class) >= 1) {
            inventory.removeNumOfTypes(1, Wood.class);
        } else {
            inventory.removeNumOfTypes(2, Arrow.class);
        }

        if (inventory.getNumOfTypes(Treasure.class) >= 1) {
            inventory.removeNumOfTypes(1, Treasure.class);
        } else {
            inventory.removeNumOfTypes(1, Key.class);
        }

        inventory.removeNumOfTypes(1, SunStone.class);

        new Sceptre(inventory);
    }
}
