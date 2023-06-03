package dungeonmania.Entities.MovingEntity;

import dungeonmania.util.Direction;

public interface RandomMovement {
    
    /**
     * Converts Integer to Direction
     * @param integer
     * @pre integer <= 3
     */
    public default Direction intToDirection(Integer integer) {
        if (integer == 0) {
            return Direction.UP;
        } else if (integer == 1) {
            return Direction.DOWN;
        } else if (integer == 2) {
            return Direction.LEFT;
        } else if (integer == 3) {
            return Direction.RIGHT;
        }
        return Direction.NONE;
    }

}
