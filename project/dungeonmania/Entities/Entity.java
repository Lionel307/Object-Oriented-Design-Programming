package dungeonmania.Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dungeonmania.Dungeon;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class Entity {
    private Position position;
    private Dungeon dungeon;
    private String id;
    private boolean isRemoved = false;

    /**
     * default constructor
     * This will add the entity to the dungeon
     * @param pos
     * @param dungeon
     */
    public Entity(Position pos, Dungeon dungeon) {
        id = UUID.randomUUID().toString();
        position = pos;
        this.dungeon = dungeon;
        dungeon.addEntity(this, position);
    }

    public Position getPosition() {
        return position;
    }

    /**
     * @return the map of the dungeon
     */
    public Map<Position, List<Entity>> getMap() {
        return dungeon.getMap();
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    /**
     * set this.position and update position in dungeon
     */
    public void setPosition(Position position) {
        dungeon.updateEntityPosition(this, this.position, position);
        this.position = position;
    }

    /**
     * set this.position and update position in dungeon
     * call encounter of the other entities at the same position after setting the position
     * @param position
     * @param direction
     */
    public void setPositionAndEncounter(Position position, Direction direction) {
        setPosition(position);
        List<Entity> otherEntities = getMap().get(position);
        if (otherEntities != null) {
            for (Entity entity: new ArrayList<>(otherEntities)) {
                if (entity == this) {
                    continue;
                }
                entity.encounter(this, direction);
            }
        }
    }

    /**
     * check whether the entity can move to the position
     * @param position
     * @param direction
     * @return true if we can go to that position
     */
    public boolean checkPositionAllowMove(Position position, Direction direction) {
        List<Entity> otherEntities = getMap().get(position);
        if (otherEntities == null) {
            return true;
        }

        return otherEntities.stream()
            .filter(entity -> entity != this)
            .allMatch(entity -> entity.allowMove(this, direction));
    }

    /**
     *  remove this entity from the dungeon
     */
    public void removeFromDungeon() {
        dungeon.removeEntity(this, this.position);
        isRemoved = true;
    }

    /**
     * 
     * @return the entity response of this entity
     */
    public EntityResponse getEntityResponse() {
        return new EntityResponse(id, getType(), position, isInteractable());
    }

    /**
     * 
     * @return whether this entity is interactable
     */
    public boolean isInteractable() {
        return false;
    }

    public String getId() {
        return id;
    }

    /**
     * 
     * @return the type string that represent the type of the entity
     */
    abstract public String getType();

    /**
     * get called when the Dungeon ticks
     */
    abstract public void tick();

    /**
     * the entity tries to move to the position of this entity from the given direction
     * @param entity the entity that tries to move
     * @param direction the direction that entity is moving
     * @return whether the entity is allowed to go to the position
     */
    abstract public boolean allowMove(Entity entity, Direction direction);

    /**
     * get called when the other entity moves to the position this enitiy is it
     * @param entity the entity that encounters this entity
     * @param direction the direction that entity is moving
     * @pre the entity is allowed to move to this position (allowMove return true previously)
     *      the entity is in the same position of this entity
     */
    abstract public void encounter(Entity entity, Direction direction);

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setIsRemoved(boolean isRemoved) {
        this.isRemoved = isRemoved;
    }
}
