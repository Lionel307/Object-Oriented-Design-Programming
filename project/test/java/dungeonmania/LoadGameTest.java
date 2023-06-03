package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;


public class LoadGameTest {
    @Test
    public void loadGameTest() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "standard");
        controller.saveGame("advanced");

        assertDoesNotThrow(()-> controller.loadGame("advanced"));
        
    }

    // invalid id
    @Test
    public void invalidId() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "standard");
        controller.saveGame("advanced");
        controller.loadGame("advanced");
        assertThrows(IllegalArgumentException.class,() -> {
            controller.loadGame("name");
        });
    }
}
