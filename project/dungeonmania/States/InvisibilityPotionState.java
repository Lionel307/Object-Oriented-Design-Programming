package dungeonmania.States;

import dungeonmania.Entities.Player;

public class InvisibilityPotionState extends PlayerState {

    @Override
    public void used(Player player) {
        player.removePlayerState(this);;
    }
}
