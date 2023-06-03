package dungeonmania.util;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    NONE(0, 0)
    ;

    private final Position offset;

    private Direction(Position offset) {
        this.offset = offset;
    }

    private Direction(int x, int y) {
        this.offset = new Position(x, y);
    }

    public Position getOffset() {
        return this.offset;
    }

    /**
     * Return the reverse of a direction
     * @return
     */
    public Direction reverseDirection() {
        if (this.equals(Direction.UP)) {
            return Direction.DOWN;
        } else if (this.equals(Direction.DOWN)) {
            return Direction.UP;
        } else if (this.equals(Direction.LEFT)) {
            return Direction.RIGHT;
        } else if (this.equals(Direction.RIGHT)) {
            return Direction.LEFT;
        } else {
            return Direction.NONE;
        }
    }
}
