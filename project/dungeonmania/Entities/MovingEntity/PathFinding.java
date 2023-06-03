package dungeonmania.Entities.MovingEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public interface PathFinding {
    public default Map<Position, Position> dijkstra(Position position, Dungeon dungeon, MovingEntity entity) {
        Map<Position, Double> dist = new HashMap<>();
        Map<Position, Position> prev = new HashMap<>();
        Queue<Position> queue = new PriorityQueue<>(new ShortestDistComparator(position));
        List<Position> grid = new ArrayList<>();

        // add all possible positions to grid
        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                grid.add(new Position(x, y));
            }
        }

        for (Position p : grid) {
            dist.put(p, Double.POSITIVE_INFINITY);
            prev.put(p, null);
            queue.add(p);
        }
        
        dist.put(position, 0.0);

        while(!queue.isEmpty()) {
            Position u = queue.poll();
            for (Position v : u.getAdjacentPositionsDirect(dungeon.getHeight(), dungeon.getWidth())) {
                Position positionVector = Position.calculatePositionBetween(u, v);
                double distanceX = Math.abs(positionVector.getX());
                double distanceY = Math.abs(positionVector.getY());
                double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
                List<Entity> entities = dungeon.getEntitiesAtPos(v);

                // update shortest distance
                if (dist.get(v) != null && dist.get(u) + distance < dist.get(v)) {
                    // no entities at that position
                    if (entities == null || entities.isEmpty()) {
                        dist.put(v, dist.get(u) + distance);
                        prev.put(v, u);
                    } else {
                        for (Entity OtherEntity : entities) {
                            // check the direction of the entity
                            Direction direction = Direction.NONE;
                            if (v.equals(new Position(entity.getPosition().getX() - 1, entity.getPosition().getY()))) {
                                direction = Direction.LEFT;
                            } else if (v.equals(new Position(entity.getPosition().getX() + 1, entity.getPosition().getY()))) {
                                direction = Direction.RIGHT;
                            } else if (v.equals(new Position(entity.getPosition().getX(), entity.getPosition().getY() - 1))) {
                                direction = Direction.DOWN;
                            } else if (v.equals(new Position(entity.getPosition().getX(), entity.getPosition().getY() + 1))) {
                                direction = Direction.UP;
                            }
                            // mercenary has to avoid swamptiles and entities it cannot move into
                            if ( /*!(OtherEntity instanceof SwampTile) && */ OtherEntity.allowMove(entity, direction)) {
                                dist.put(v, dist.get(u) + distance);
                                prev.put(v, u);
                            }
                        }
                    }
                }
            }
        }
        return prev;
    }

    public default Position firstMove(Position position, Dungeon dungeon, MovingEntity entity, Position goalPosition) {
        Map<Position, Position> prev = dijkstra(position, dungeon, entity);
        Position temp = goalPosition;
        while (prev.get(temp) != null) {
            if (prev.get(temp).equals(position)) {
                System.out.println("next move: " + temp);
                return temp;
            }
            temp = prev.get(temp);
        }
        System.out.println("next position is null");
        return position;
    }

    class ShortestDistComparator implements Comparator<Position> {
        Position source;
        public ShortestDistComparator(Position source) {
            this.source = source;
        }

        @Override
        public int compare(Position o1, Position o2) {
            Position positionVector = Position.calculatePositionBetween(o1, source);
            double distanceX = Math.abs(positionVector.getX());
            double distanceY = Math.abs(positionVector.getY());
            double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));

            Position positionVector2 = Position.calculatePositionBetween(o2, source);
            double distanceX2 = Math.abs(positionVector2.getX());
            double distanceY2 = Math.abs(positionVector2.getY());
            double distance2 = Math.sqrt(Math.pow(distanceX2, 2) + Math.pow(distanceY2, 2));
            
            return distance < distance2 ? -1 : ((distance == distance2)) ? 0 : 1;
        }
    }
}
