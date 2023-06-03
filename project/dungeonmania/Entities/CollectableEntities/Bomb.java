package dungeonmania.Entities.CollectableEntities;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.MovingEntity.Spider;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dungeonmania.Dungeon;
import dungeonmania.Usable;

public class Bomb extends CollectableEntity implements Usable {
    private boolean hasBeenCollected = false;
    public Bomb(Position pos, Dungeon dungeon) {
        super(pos, dungeon);
    }

    // used to place bomb back at players' current location
    @Override
    public void use(Player player) {
        Position bombPos = player.getPosition();
        player.getInventory().remove(this);
        this.setIsRemoved(false);
        setPosition(bombPos);
    }

    @Override
    public boolean allowMove(Entity entity, Direction direction) {
        if (entity instanceof Spider) {
            return true;
        }
        if (hasBeenCollected) {
            return false;
        }
        return true;
    }

    // can use observer pattern to make the logic simpler
    @Override
    public void tick() {
        if (!hasBeenCollected) {
            return;
        }
        // check if switch is nearby and a boulder is on top
        Stream<Position> switchesPos = this.getDungeon().getEntities().values()
            .stream()
            .filter(e -> e.getType().equals("switch"))
            .filter(e -> Position.isAdjacent(e.getPosition(), this.getPosition()))
            .map(e -> e.getPosition());
        
        Stream<Position> bouldersPos = this.getDungeon().getEntities().values()
        .stream()
        .filter(e -> e.getType().equals("boulder"))
        .filter(e -> Position.isAdjacent(e.getPosition(), this.getPosition()))
        .map(e -> e.getPosition());

        // if any floorswitch position is the same as any boulder position
        boolean activated = switchesPos.anyMatch(floorSwitch -> bouldersPos.anyMatch(
            boulder -> boulder.equals(floorSwitch)
        ));

        // radius is all adjacent objects
        if (activated) {
            new ArrayList<>(this.getDungeon().getEntities().values())
            .stream()
            .filter(e -> Position.isAdjacent(e.getPosition(), this.getPosition()))
            .filter(e -> ! (e instanceof Player))
            .forEach(e -> e.removeFromDungeon());
            this.removeFromDungeon();
        }

        return;
    }

    @Override
    public String getType() {
        return "bomb";
    }

    @Override
    public void encounter(Entity entity, Direction direction) {
        if (entity instanceof Player == false) {
            return;
        }
        if (hasBeenCollected) {
            return;
        }
        Player player = (Player) entity;
        player.getInventory().add(this);
        this.removeFromDungeon();
        this.hasBeenCollected = true;
    }

}
