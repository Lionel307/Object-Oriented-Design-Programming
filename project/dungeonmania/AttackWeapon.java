package dungeonmania;

import dungeonmania.Entities.Entity;

public interface AttackWeapon {
    /**
     * return the new attack damage after using this weapon given the original damage
     * @param damage the original attack damage
     * @return the new attack damage
     */
    public double attack(double damage, Battleable enemy);
}
