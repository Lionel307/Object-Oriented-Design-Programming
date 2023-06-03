package dungeonmania.Entities;

import java.util.UUID;

import dungeonmania.Inventory;
import dungeonmania.Storable;
import dungeonmania.Usable;
import dungeonmania.States.SceptreState;

public class Sceptre implements Storable, Usable {
    private String id;
    private Inventory inventory;

    public Sceptre(Inventory inventory) {
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
        return "sceptre";
    }

    @Override
    public void use(Player player) {
        // remove from the player
        inventory.remove(this);

        // add a sceptre state to player
        player.addPlayerState(new SceptreState(player.getDungeon(), player));
    }

}
