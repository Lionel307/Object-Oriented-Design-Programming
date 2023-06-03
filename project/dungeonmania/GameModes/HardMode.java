package dungeonmania.GameModes;

public class HardMode extends GameModeStrategy {
    @Override
    public boolean invincibilityPotionHasEffect() {
        return false;
    }

    @Override
    public int getZombiesSpawnSpeed() {
        return 15;
    }

}
