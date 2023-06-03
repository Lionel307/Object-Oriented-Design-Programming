package dungeonmania.Entities.StaticEntities;

import java.util.Map;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.MovingEntity.*;
import dungeonmania.Entities.MovingEntity.Boss.Hydra;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Portal extends StaticEntity{
    private String colour;
    private Portal otherPortal = null;
    public Portal(Position pos, Dungeon dungeon, String colour) {
        super(pos, dungeon);
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }

    public void setOtherPortal(Portal othePortal) {
        this.otherPortal = othePortal;
    }

    public Portal getOthePortal() {
        if (otherPortal == null) {
            Map<String, Entity> entities = this.getDungeon().getEntities();
            for (Entity entity: entities.values()) {
                if (entity instanceof Portal && entity != this && 
                    ((Portal) entity).getColour().equals(colour)){
                    // find portal with same colour but different position
                    Portal portal = (Portal) entity;
                    this.setOtherPortal(portal);
                    otherPortal.setOtherPortal(this);
                }
            }
        }
        return otherPortal;
    }

    @Override
    public String getType() {
        return "portal";
    }

    @Override
    public void tick() {
        
    }

    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        if (entity instanceof ZombieToast) {
            return true;
        } else if (entity instanceof Hydra) {
            return true;
        }

        Portal exitPortal = getOthePortal();
        Position newPos = exitPortal.getPosition().translateBy(direction);
        if (entity.checkPositionAllowMove(newPos, direction)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void encounter(Entity entity, Direction direction) {
        if (! ((entity instanceof ZombieToast) || (entity instanceof Hydra))) {
            Position newPos = getOthePortal().getPosition().translateBy(direction);
            entity.setPositionAndEncounter(newPos, direction);
        }
    }
}
