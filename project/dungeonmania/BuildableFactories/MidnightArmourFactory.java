package dungeonmania.BuildableFactories;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import dungeonmania.Inventory;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.MidnightArmour;
import dungeonmania.Entities.CollectableEntities.Armour;
import dungeonmania.Entities.CollectableEntities.SunStone;
import dungeonmania.Entities.MovingEntity.ZombieToast;
import dungeonmania.exceptions.InvalidActionException;

public class MidnightArmourFactory extends BuildableFactory{

    @Override
    public boolean isBuildable(Inventory inventory, Collection<Entity> entities) {
        return 
            inventory.getNumOfTypes(Armour.class) >= 1 
        &&
            inventory.getNumOfTypes(SunStone.class) >= 1
        &&
            entities.stream().allMatch(entity -> !(entity instanceof ZombieToast));
    }

    @Override
    public void build(Inventory inventory, Collection<Entity> entities) throws InvalidActionException {
        if (!isBuildable(inventory, entities)) {
            throw new InvalidActionException("cannot build midnight armour");
        }

        inventory.removeNumOfTypes(1, Armour.class);
        inventory.removeNumOfTypes(1, SunStone.class);

        new MidnightArmour(inventory);
    }
}
