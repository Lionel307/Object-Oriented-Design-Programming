package dungeonmania.Entities.MovingEntity;

import java.util.LinkedList;
import java.util.Queue;

import dungeonmania.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Spider extends MovingEntity {
    private Queue<Direction> movementQueue = new LinkedList<Direction>();

    public Spider(Position position, Dungeon dungeon) {
        super(position, dungeon, 100, 5);
        this.setDefaultMovementQueue();
    }

    @Override
    public String getType() {
        return "spider";
    }

    @Override
    public Position move() {
        Direction direction = this.getMovementQueue().poll();
        Position nextPosition = this.getPosition().translateBy(direction);
        if (movementQueue.size() < 8) {
            this.getMovementQueue().add(direction);
        }

        return nextPosition;
    }    

    // Movement methods

    public void setMovementQueue(Queue<Direction> movementQueue) {
        this.movementQueue = movementQueue;
    }

    public void setDefaultMovementQueue() {
        Queue<Direction> defaultMovementQueue = new LinkedList<Direction>();
        defaultMovementQueue.add(Direction.UP);
        defaultMovementQueue.add(Direction.RIGHT);
        defaultMovementQueue.add(Direction.DOWN);
        defaultMovementQueue.add(Direction.DOWN);
        defaultMovementQueue.add(Direction.LEFT);
        defaultMovementQueue.add(Direction.LEFT);
        defaultMovementQueue.add(Direction.UP);
        defaultMovementQueue.add(Direction.UP);
        defaultMovementQueue.add(Direction.RIGHT);
        this.setMovementQueue(defaultMovementQueue);
    }

    public void reverseSpiderMovement() {
        Queue<Direction> reversedMovementQueue = new LinkedList<Direction>();
        while (!movementQueue.isEmpty()) {
            Direction direction = movementQueue.poll();
            reversedMovementQueue.add(direction.reverseDirection());
        }
        this.setMovementQueue(reversedMovementQueue);
    }

    public Queue<Direction> getMovementQueue() {
        return this.movementQueue;
    }

    
}
