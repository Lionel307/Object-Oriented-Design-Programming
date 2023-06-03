package dungeonmania.Entities;

import java.util.UUID;

import dungeonmania.AttackWeapon;
import dungeonmania.Battleable;
import dungeonmania.DeterioratingItem;
import dungeonmania.Inventory;
import dungeonmania.Storable;

public class Bow implements Storable, DeterioratingItem, AttackWeapon{
    private String id;
    private int durability = 5;
    private Inventory inventory;
    private Player player;

    /**
     * The constructor will set the numOfAtatck of the player and will add this 
     * to the inventory
     * @param inventory
     * @param player
     */
    public Bow(Inventory inventory, Player player) {
        id = UUID.randomUUID().toString();
        this.inventory = inventory;
        this.player = player;
        inventory.add(this);
        player.setNumOfAttack(player.getNumOfAttack() + 1);
    }

    @Override
    public double attack(double damage, Battleable entity) {
        decreaseDurability();
        return damage + 10;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return "bow";
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
        player.setNumOfAttack(player.getNumOfAttack() - 1);
    }

}
