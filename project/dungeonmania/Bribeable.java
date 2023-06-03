package dungeonmania;

public interface Bribeable extends Interactable {

    /**
     * @return the status of if the entity is bribed
     */
    public boolean isBribed();
    
    /**
     * Setter for the bribedStatus of entity
     * @param bool
     */
    public void setBribedStatus(boolean bool);
 
}
