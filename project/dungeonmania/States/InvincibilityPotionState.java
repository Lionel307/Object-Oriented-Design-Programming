package dungeonmania.States;

import dungeonmania.Entities.Player;

public class InvincibilityPotionState extends PlayerState{
    private int duration;

    public InvincibilityPotionState() {
        this.duration = 5;
    }

    @Override
    public void used(Player player) {
        this.duration -= 1;
        
        if (this.duration <= 0) {
            player.removePlayerState(this);;
        }
    }
}
