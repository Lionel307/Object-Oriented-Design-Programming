package dungeonmania.Entities.StaticEntities;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.MovingEntity.MovingEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwampTile extends StaticEntity{
    private Integer movementFactor;
    private Integer numTicks = 0;
    private String entityId = null;
    public SwampTile(Position pos, Dungeon dungeon, Integer movementFactor) {
        super(pos, dungeon);
        this.movementFactor = movementFactor;
    }
    @Override
    public String getType() {
        return "swamp_tile";
    }

    public Integer getMovementFactor() {
        return movementFactor;
    }

    public Integer getNumTicks() {
        return numTicks;
    }

    public String getEntityId() {
        return entityId;
    }

    @Override
    public void tick() {
        List<Entity> entities = this.getDungeon().getEntitiesAtPos(this.getPosition());
        for (Entity entity : entities) {
            if (entity instanceof MovingEntity) {
                if (getEntityId() != entity.getId()) {
                    entityId = entity.getId();
                    numTicks = 1;
                    return;
                }
            }
        }
        if (entities.size() > 1) {
            numTicks++;
        } else {
            numTicks = 0;
        }
    }

    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        List<Entity> entities = this.getDungeon().getEntitiesAtPos(this.getPosition());
        if (entities.size() > 1) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void encounter(Entity entity, Direction direction) {
        
    }
    
}
