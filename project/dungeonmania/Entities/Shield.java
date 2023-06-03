package dungeonmania.Entities;

import java.util.UUID;

import dungeonmania.DefendWeapon;
import dungeonmania.DeterioratingItem;
import dungeonmania.Inventory;
import dungeonmania.Storable;

public class Shield implements Storable, DeterioratingItem, DefendWeapon {
    private String id;
    private int durability = 10;
    private Inventory inventory;

    /**
     * This constructor will add this object to the inventory
     * @param inventory
     */
    public Shield(Inventory inventory) {
        id = UUID.randomUUID().toString();
        this.inventory = inventory;
        inventory.add(this);
    }

    @Override
    public double defend(double damage) {
        decreaseDurability();
        return damage * 0.75;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return "shield";
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public void removeFromInventory() {
        inventory.remove(this);
    }
    
}
