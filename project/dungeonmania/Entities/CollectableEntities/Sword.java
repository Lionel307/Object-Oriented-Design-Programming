package dungeonmania.Entities.CollectableEntities;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.AttackWeapon;
import dungeonmania.Battleable;
import dungeonmania.DeterioratingItem;

public class Sword extends CollectableEntity implements AttackWeapon, DeterioratingItem {
    private int remainingDurability;

    public Sword(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
        this.remainingDurability = 5;
    }
    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        return true;
    }

    @Override
    public void tick() {
        return;
    }

    @Override
    public String getType() {
        return "sword";
    }

    public double attack(double attack, Battleable entity) {
        decreaseDurability();
        return attack + 10;
    }
    @Override
    public int getDurability() {
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
