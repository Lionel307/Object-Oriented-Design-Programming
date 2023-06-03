package dungeonmania;

import dungeonmania.Entities.Player;

public interface Interactable {

    /**
     * The character interact with this entity
     * @param character
     */
    public void interact(Player character);

    /**
     * @return true to show that this entity is interactable
     */
    default public boolean isInteractable() {
        return true;
    }
}
