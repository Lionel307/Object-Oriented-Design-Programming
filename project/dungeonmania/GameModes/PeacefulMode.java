package dungeonmania.GameModes;

public class PeacefulMode extends GameModeStrategy{
    @Override
    public boolean shouldEnemyAttack() {
        return false;
    }
}
