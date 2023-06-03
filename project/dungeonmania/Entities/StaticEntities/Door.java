package dungeonmania.Entities.StaticEntities;


import java.util.Iterator;

import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.Storable;
import dungeonmania.Entities.*;
import dungeonmania.Entities.MovingEntity.*;
import dungeonmania.Entities.CollectableEntities.Key;
import dungeonmania.Entities.CollectableEntities.SunStone;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Door extends StaticEntity{
    private int keyNum;
    private boolean locked = true;

    public Door(Position pos, Dungeon dungeon, int keyNum) {
        super(pos, dungeon);
        this.keyNum = keyNum;
    }
    
    @Override
    public String getType() {
        return "door";
    }

    @Override
    public void tick() {
        
    }

    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        if (entity instanceof Spider) {
            return true;
        }
        if (locked) {
            if (entity instanceof Player) {
                Inventory inventory = ((Player)entity).getInventory();
                //firstly check for sunstone
                if (inventory.getNumOfTypes(SunStone.class) >= 1) {
                    locked = false;
                    return true;
                }

                Iterator<Storable> it = inventory.iterator();
                while (it.hasNext()) {
                    Storable item = it.next();
                    if (item instanceof Key && 
                        ((Key)item).getKeyNum() == keyNum) {
                        
                        // remove the key 
                        it.remove();
                        locked = false;
                        return true;
                    }
                }
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void encounter(Entity entity, Direction direction) {
        
    }

    public boolean isLocked() {
        return locked;
    }
}
