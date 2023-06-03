package dungeonmania.Entities.CollectableEntities;

import dungeonmania.Battleable;
import dungeonmania.Dungeon;
import dungeonmania.Entities.MovingEntity.Boss.Assassin;
import dungeonmania.Entities.MovingEntity.Boss.Hydra;
import dungeonmania.util.Position;

public class Anduril extends Sword {

    // Anduril is a type of sword
    public Anduril(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
    }
    
    @Override
    public String getType() {
        return "anduril";
    }

    @Override
    public double attack(double attack, Battleable enemy) {
        System.out.println(enemy.getClass());
        if (enemy.getClass() == Hydra.class || enemy.getClass() == Assassin.class) {
            return (super.attack(attack, enemy) - attack) * 3 + attack;
        };
        return super.attack(attack, enemy);
    }
}
