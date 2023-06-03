package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dungeonmania.*;
import dungeonmania.response.models.DungeonResponse;

public class NewGameTest {
    // @Test
    // public void newGameTest() {
    //     DungeonManiaController dungeon = new DungeonManiaController();
    //     Dungeon dungeon2 = new Dungeon("Dungeon", "Standard");
    //     assertDoesNotThrow(()-> {
    //         dungeon.newGame("Dungeon", "Standard");
    //     });
    // }

    // // invalid game mode
    // @Test
    // public void invalidGameMode() {
    //     DungeonManiaController dungeon = new DungeonManiaController();
    //     Dungeon dungeon2 = new Dungeon("Dungeon", "invalidGameMode");
    //     assertThrows(IllegalArgumentException.class,() -> {
    //         dungeon.newGame("Dungeon", "invalidGameMode");
    //     });

    // }

    // // invalid dungeon name
    // @Test 
    // public void dungeonNameDoesNotExist() {
    //     DungeonManiaController dungeon = new DungeonManiaController();
    //     Dungeon dungeon2 = new Dungeon("Dungeon", "Standard");
    //     assertThrows(IllegalArgumentException.class,() -> {
    //         dungeon.newGame("name", "Standard");
    //     });
    // }

}
