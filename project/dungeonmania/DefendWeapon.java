package dungeonmania;

public interface DefendWeapon {
    /**
     * return the damage after defending with this weapon
     * @param damage the damage before defending with this weapon
     * @return damage received after defending
     */
    public double defend(double damage);
}
