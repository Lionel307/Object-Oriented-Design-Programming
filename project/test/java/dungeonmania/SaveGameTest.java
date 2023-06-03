package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;

public class SaveGameTest {
    @Test
    public void saveGameTest() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "standard");
        controller.saveGame("advanced");

        for (String name: controller.savedGames.keySet()) {
            assertEquals("advanced", name);
        }
    }
}
