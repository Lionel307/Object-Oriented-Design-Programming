package dungeonmania.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Position {
    private final int x, y, layer;

    public Position(int x, int y, int layer) {
        this.x = x;
        this.y = y;
        this.layer = layer;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.layer = 0;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(x, y, layer);
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;

        // z doesn't matter
        return x == other.x && y == other.y;
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final int getLayer() {
        return layer;
    }

    public final Position asLayer(int layer) {
        return new Position(x, y, layer);
    }

    public final Position translateBy(int x, int y) {
        return this.translateBy(new Position(x, y));
    }

    public final Position translateBy(Direction direction) {
        return this.translateBy(direction.getOffset());
    }

    public final Position translateBy(Position position) {
        return new Position(this.x + position.x, this.y + position.y, this.layer + position.layer);
    }

    // (Note: doesn't include z)

    /**
     * Calculates the position vector of b relative to a (ie. the direction from a
     * to b)
     * 
     * @return The relative position vector
     */
    public static final Position calculatePositionBetween(Position a, Position b) {
        return new Position(b.x - a.x, b.y - a.y);
    }

    public static final boolean isAdjacent(Position a, Position b) {
        return Math.abs(a.getX() - b.getX()) == 1 || Math.abs(a.getY() - b.getY()) == 1;
    }

    // (Note: doesn't include z)
    public final Position scale(int factor) {
        return new Position(x * factor, y * factor, layer);
    }

    @Override
    public final String toString() {
        return "Position [x=" + x + ", y=" + y + ", z=" + layer + "]";
    }

    // Return Adjacent positions in an array list with the following element
    // positions:
    // 0 1 2
    // 7 p 3
    // 6 5 4
    public List<Position> getAdjacentPositions() {
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(new Position(x - 1, y - 1));
        adjacentPositions.add(new Position(x, y - 1));
        adjacentPositions.add(new Position(x + 1, y - 1));
        adjacentPositions.add(new Position(x + 1, y));
        adjacentPositions.add(new Position(x + 1, y + 1));
        adjacentPositions.add(new Position(x, y + 1));
        adjacentPositions.add(new Position(x - 1, y + 1));
        adjacentPositions.add(new Position(x - 1, y));
        return adjacentPositions;
    }

    public List<Position> getAdjacentPositionsDirect(int height, int width) {
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(new Position(x, y - 1));
        adjacentPositions.add(new Position(x + 1, y));
        adjacentPositions.add(new Position(x, y + 1));
        adjacentPositions.add(new Position(x - 1, y));

        // check it won't concide with boundary
        List<Position> newPositions = new ArrayList<Position>(adjacentPositions);
        for (Position position : adjacentPositions) {
            int x = position.getX();
            int y = position.getY();
            if (x <= 0 || x >= width - 1 || y <= 0 || y >= height - 1) {
                newPositions.remove(position);
            }
        }

        return newPositions;
    }

    public List<Position> getTwoDistanceAwayPositionsWalled(int height, int width, Boolean maze[][]) {
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(new Position(x, y - 2));
        adjacentPositions.add(new Position(x + 2, y));
        adjacentPositions.add(new Position(x, y + 2));
        adjacentPositions.add(new Position(x - 2, y));

        // check it won't concide with boundary
        List<Position> newPositions = new ArrayList<Position>(adjacentPositions);
        for (Position position : adjacentPositions) {
            int x = position.getX();
            int y = position.getY();
            if (x <= 0 || x >= width - 1 || y <= 0 || y >= height - 1 || maze[x][y]) {
                newPositions.remove(position);
            }
        }

        return newPositions;
    }

    public List<Position> getTwoDistanceAwayPositionsEmpty(int height, int width, Boolean maze[][]) {
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(new Position(x, y - 2));
        adjacentPositions.add(new Position(x + 2, y));
        adjacentPositions.add(new Position(x, y + 2));
        adjacentPositions.add(new Position(x - 2, y));

        // check it won't concide with boundary
        List<Position> newPositions = new ArrayList<Position>(adjacentPositions);
        for (Position position : adjacentPositions) {
            int x = position.getX();
            int y = position.getY();
            if (x <= 0 || x >= width - 1 || y <= 0 || y >= height - 1 || !maze[x][y]) {
                newPositions.remove(position);
            }
        }

        return newPositions;
    }

    public Position getPositionBetween(Position other) {
        if (this.x == other.getX()) {
            if (this.y > other.getY()) {
                return new Position(this.x, this.y - 1);
            } else {
                return new Position(this.x, this.y + 1);
            }
        }
        if (this.x > other.getX()) {
            return new Position(this.x - 1, this.y);
        }
        return new Position(this.x + 1, this.y);
    }
}
