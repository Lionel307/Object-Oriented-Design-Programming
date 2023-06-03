package dungeonmania;

import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.json.JSONTokener;

import dungeonmania.GameModes.GameModeStrategy;
import dungeonmania.GameModes.HardMode;
import dungeonmania.GameModes.PeacefulMode;
import dungeonmania.GameModes.StandardMode;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class DungeonManiaController {
    Map<String, Dungeon> savedGames = new HashMap<>();
    Dungeon runningGame = null;

    public DungeonManiaController() {
    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    public List<String> getGameModes() {
        return new ArrayList<>(Arrays.asList("Standard", "Peaceful", "Hard"));
    }

    /**
     * /dungeons
     * 
     * Done for you.
     */
    public static List<String> dungeons() {
        try {
            return FileLoader.listFileNamesInResourceDirectory("/dungeons");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public DungeonResponse newGame(String dungeonName, String gameMode) throws IllegalArgumentException {
        List<String> allDungeons = dungeons();

        if (!allDungeons.stream().anyMatch(d -> d.equals(dungeonName))
                || !Arrays.asList("standard", "peaceful", "hard").contains(gameMode.toLowerCase())) {
            throw new IllegalArgumentException();
        }

        String importedJSONString = ""; // scope JSONString outside of try statement
        try { // assume IOError doesn't happen
            importedJSONString = FileLoader.loadResourceFile("dungeons/" + dungeonName + ".json");
            System.out.println(importedJSONString);
        } catch (IOException ignore) {
            System.out.println(importedJSONString);
        }
        JSONObject dungeonJSON = (JSONObject) new JSONObject(importedJSONString);

        GameModeStrategy gameModeStrategy;
        if (gameMode.toLowerCase().equals("standard")) {
            gameModeStrategy = new StandardMode();
        } else if (gameMode.toLowerCase().equals("peaceful")) {
            gameModeStrategy = new PeacefulMode();
        } else { // it could only be hard left
            gameModeStrategy = new HardMode();
        }

        Dungeon newDungeon = new Dungeon(dungeonJSON, gameModeStrategy);
        this.runningGame = newDungeon;
        this.savedGames.put(dungeonName, newDungeon);

        return newDungeon.getDungeonResponse();
    }

    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        this.savedGames.put(name, this.runningGame);
        return this.runningGame.getDungeonResponse();
    }

    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        Dungeon loadedDungeon = this.savedGames.get(name);
        if (loadedDungeon == null) {
            throw new IllegalArgumentException();
        }
        this.runningGame = loadedDungeon;
        return this.runningGame.getDungeonResponse();
    }

    public List<String> allGames() {
        Set<String> keys = this.savedGames.keySet();
        return new ArrayList<String>(keys);
    }

    public DungeonResponse tick(String itemUsed, Direction movementDirection)
            throws IllegalArgumentException, InvalidActionException {
        return this.runningGame.tick(itemUsed, movementDirection);
    }

    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        return this.runningGame.interact(entityId);
    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        return this.runningGame.build(buildable);
    }

    public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd, String gameMode)
            throws IllegalArgumentException {
        Dungeon newDungeon = DungeonBuilder.generateDungeon(xStart, yStart, xEnd, yEnd, gameMode);
        this.runningGame = newDungeon;
        this.savedGames.put(String.valueOf(newDungeon.hashCode()), newDungeon);

        return newDungeon.getDungeonResponse();
    }

}