package dungeonmania;

import dungeonmania.GameModes.GameModeStrategy;
import dungeonmania.GameModes.HardMode;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DungeonBuilderTest {
    // * - returned dungeon has correct gamemode
    // * - dungeon is correct size of 50x50
    // * - walls surrounding the entire dungeon
    // * - start is correct - end is correct

    @Test
    public void testCorrectGamemode() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.generateDungeon(1, 1, 47, 47, "hard");

        Dungeon dungeon = dmc.runningGame;

        GameModeStrategy gameMode = dungeon.getGameMode();
        System.out.println(gameMode);
        assertTrue(gameMode instanceof HardMode);
    }

    @Test
    public void testCorrectSize() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.generateDungeon(1, 1, 47, 47, "hard");

        assertEquals(50, dmc.runningGame.getHeight());
        assertEquals(50, dmc.runningGame.getWidth());
    }

    @Test
    public void testWallsOnOutside() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.generateDungeon(1, 1, 47, 47, "hard");

        DungeonResponse response = dmc.runningGame.getDungeonResponse();

        List<Position> range = new ArrayList<Position>();
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                if (i == 0 || i == 49 || j == 0 || j == 49) {
                    range.add(new Position(i, j));
                }
            }
        }

        for (EntityResponse entityResponse : response.getEntities()) {
            if (entityResponse.getType().equals("wall")) {
                Position wallPosition = entityResponse.getPosition();
                range.remove(wallPosition);
            }
        }
        assertTrue(range.isEmpty());
    }

    @Test
    public void testCorrectStartAndEndPositions() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.generateDungeon(1, 1, 47, 47, "hard");

        assertEquals(new Position(1, 1), dmc.runningGame.getPlayer().getPosition());

        for (EntityResponse entityResponse : dmc.runningGame.getDungeonResponse().getEntities()) {
            if (entityResponse.getType().equals("exit")) {
                assertEquals(new Position(47, 47), entityResponse.getPosition());
            }
        }
    }

    @Test
    public void printStuff() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.generateDungeon(1, 1, 47, 47, "hard");
    }
}
