package dungeonmania;

import dungeonmania.Entities.Player;

public interface CharacterObserver {
    /**
     * tell the oberver that the character has updated
     * @param character the player
     */
    abstract public void update(Player character);
}
