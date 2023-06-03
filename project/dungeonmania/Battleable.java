package dungeonmania;

public interface Battleable {
    /**
     * 
     * @return the total damage of an attack
     */
    public double attack(Battleable enemy);

    /**
     * get the damage
     * @param damage the damage from the other entity
     * @return if this entity is still alive
     */
    public boolean defend(double damage);
}
