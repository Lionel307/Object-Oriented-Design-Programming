package dungeonmania;

import dungeonmania.Entities.Player;

public interface Usable {
    /**
     * this item is used by the player
     * @param player
     */
    public void use(Player player);
}
