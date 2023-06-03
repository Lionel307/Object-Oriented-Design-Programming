package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;

public class AllGamesTest {
    @Test
    public void allGamesTest() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "standard");
        controller.newGame("maze", "peaceful");
        controller.saveGame("advanced");
        controller.saveGame("maze");

        List<String> gameList = new ArrayList<>();
        gameList.add("advanced");
        gameList.add("maze");

        assertEquals(gameList, controller.allGames());
    }
}
