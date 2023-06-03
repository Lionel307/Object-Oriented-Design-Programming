package dungeonmania.BuildableFactories;


import java.util.Collection;

import dungeonmania.Inventory;
import dungeonmania.Entities.Bow;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.CollectableEntities.*;
import dungeonmania.exceptions.InvalidActionException;

public class BowFactory extends BuildableFactory{

    @Override
    public boolean isBuildable(Inventory inventory, Collection<Entity> entities) {
        return inventory.getNumOfTypes(Wood.class) >= 1 && inventory.getNumOfTypes(Arrow.class) >= 3;
    }

    @Override
    public void build(Inventory inventory, Collection<Entity> entities) throws InvalidActionException {
        if (! isBuildable(inventory, entities)) {
            throw new InvalidActionException("cannot build a bow");
        }

        // remove one wood and 3 arrows
        inventory.removeNumOfTypes(1, Wood.class);
        inventory.removeNumOfTypes(3, Arrow.class);
        new Bow(inventory, inventory.getPlayer());
    }
}
