package dungeonmania.GameModes;

public abstract class GameModeStrategy {

    /**
     * @return whether the enemy should attack
     */
    public boolean shouldEnemyAttack() {
        return true;
    }

    /**
     * @return return the speed to spawn zombies
     */
    public int getZombiesSpawnSpeed() {
        return 20;
    }

    /**
     * @return if the invincibility potion has effect
     */
    public boolean invincibilityPotionHasEffect() {
        return true;
    }
}
