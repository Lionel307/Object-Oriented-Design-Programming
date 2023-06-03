package dungeonmania;

import dungeonmania.Entities.*;
import dungeonmania.Entities.MovingEntity.*;
import dungeonmania.util.Position;

public abstract class MovingEntityTest {
    
    public double calculateDistance(MovingEntity movingEntity, Dungeon dungeon) {

        Player player = dungeon.getPlayer();
        Position playerPosition = player.getPosition();
        Position movingEntityPosition = movingEntity.getPosition();

        double distanceX = playerPosition.getX() - movingEntityPosition.getX();
        double distanceY = playerPosition.getY() - movingEntityPosition.getY();

        double distance = Math.sqrt((Math.pow(distanceX, 2) + Math.pow(distanceY, 2)));
        
        return distance;
    }

}
