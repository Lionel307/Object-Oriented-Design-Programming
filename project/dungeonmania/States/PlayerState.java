package dungeonmania.States;

import dungeonmania.Entities.Player;

public abstract class PlayerState {
    /**
     * get called when the player has used the state
     * The state should set back the state when it is done
     * @param player
     */
    abstract public void used(Player player);
}
