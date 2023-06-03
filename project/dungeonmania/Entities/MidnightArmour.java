package dungeonmania.Entities;

import java.util.UUID;

import dungeonmania.AttackWeapon;
import dungeonmania.Battleable;
import dungeonmania.DefendWeapon;
import dungeonmania.DeterioratingItem;
import dungeonmania.Inventory;
import dungeonmania.Storable;

public class MidnightArmour implements Storable, AttackWeapon, DefendWeapon, DeterioratingItem {

    private String id;
    private int durability = 10;
    private Inventory inventory;

    public MidnightArmour(Inventory inventory) {
        id = UUID.randomUUID().toString();
        this.inventory = inventory;
        inventory.add(this);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return "midnight_armour";
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

    @Override
    public double defend(double damage) {
        decreaseDurability();
        return damage * 0.5;
    }

    @Override
    public double attack(double damage, Battleable enemy) {
        decreaseDurability();
        return damage + 20;
    }
    
}
