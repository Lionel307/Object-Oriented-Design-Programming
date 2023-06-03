package dungeonmania;

public interface DeterioratingItem {

    public int getDurability();

    public void setDurability(int durability);

    /**
     * remove the element from the inventory and remove the effect of this item
     */
    public void removeFromInventory();

    /**
     * decrease the durability and remove from the inventory when the element is
     * deteriorated
     */
    default public void decreaseDurability() {
        setDurability(getDurability() - 1);
        if (getDurability() <= 0) {
            removeFromInventory();
        }
    }
}
