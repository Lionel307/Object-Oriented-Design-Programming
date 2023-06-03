package dungeonmania.Entities.MovingEntity;

import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity implements RandomMovement, EntityDrop {
    private Random random;
    private boolean dropArmour;

    public ZombieToast(Position position, Dungeon dungeon) {
        super(position, dungeon, 100, 5);
        this.dropArmour = true;
        random = new Random();
    }

    public ZombieToast(Position position, Dungeon dungeon, Boolean bool) {
        super(position, dungeon, 100, 5);
        dropArmour = bool;
        random = new Random();
    }

    @Override
    public boolean getDropArmour() {
        return this.dropArmour;
    }

    @Override
    public String getType() {
        return "zombie_toast";
    }


    @Override
    public Position move() {
        Direction direction = intToDirection(random.nextInt(4));
        return this.getPosition().translateBy(direction);
    }
    
}
