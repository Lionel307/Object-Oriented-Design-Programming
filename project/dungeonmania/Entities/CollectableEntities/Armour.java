package dungeonmania.Entities.CollectableEntities;

import dungeonmania.DefendWeapon;
import dungeonmania.DeterioratingItem;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Dungeon;
import dungeonmania.Inventory;

public class Armour extends CollectableEntity implements DefendWeapon, DeterioratingItem {
    private int remainingDurability;
    
    public Armour(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
        this.remainingDurability = 5;
    }

    public Armour(Inventory inventory, Dungeon dungeon) {
        super(null, dungeon);
        this.remainingDurability = 5;
        dungeon.removeEntity(this, null);
        inventory.add(this);
    }

    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        return true;
    }

    @Override
    public void tick() {

    }

    @Override
    public String getType() {
        return "armour";
    }

    public double defend(double damage) {
        decreaseDurability();
        return damage/2;
    }

    public int getDurability(){
        return remainingDurability;
    }

    @Override
    public void setDurability(int durability) {
        this.remainingDurability = durability;
    }

    @Override
    public void removeFromInventory() {
        inventory.remove(this);
    }

}
