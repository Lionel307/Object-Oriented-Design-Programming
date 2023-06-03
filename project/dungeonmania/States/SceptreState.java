package dungeonmania.States;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.MovingEntity.BribeableMovingEntity;

public class SceptreState extends PlayerState {
    private Dungeon dungeon;
    private int durabiltiy = 10;
    private List<BribeableMovingEntity> mindControlled = new ArrayList<>();

    public SceptreState(Dungeon dungeon, Player player) {
        this.dungeon = dungeon;
        dungeon.getEntities().values().stream()
        .filter(entity -> entity instanceof BribeableMovingEntity)
        .map(entity -> (BribeableMovingEntity)entity)
        .forEach(entity -> {
            if (!entity.isBribed()) {
                mindControl(player, entity);
            }
        });
    }

    public void mindControl(Player player, BribeableMovingEntity entity) {
        // bribe the entity
        entity.setBribedStatus(true);
        // add to the player's allies
        player.addAllies(entity);
        mindControlled.add(entity);
    }

    // still need to wait for a BribeableMovingEntity interface
    public void used(Player player) {
        durabiltiy -= 1;
        if (durabiltiy <= 0) {
            // remove the state
            player.removePlayerState(this);
            for (BribeableMovingEntity entity: mindControlled) {
                // unbribe the entity
                entity.setBribedStatus(false);
                // remove from the player's allies
                player.removeAllies(entity);
            }
        }
    }
}
