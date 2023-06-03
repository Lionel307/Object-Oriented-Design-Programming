package dungeonmania;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.GameModes.GameModeStrategy;
import dungeonmania.GameModes.HardMode;
import dungeonmania.GameModes.PeacefulMode;
import dungeonmania.GameModes.StandardMode;
import dungeonmania.util.Position;

public class DungeonBuilder {
    private JSONObject dungeonJson;
    private GameModeStrategy gameMode;

    public DungeonBuilder(GameModeStrategy gameMode) {
        this.dungeonJson = new JSONObject();
        this.dungeonJson.put("entities", new JSONArray());
        this.gameMode = gameMode;
    }

    public void addWall(int x, int y) {
        JSONObject newEntity = new JSONObject();
        newEntity.put("x", x);
        newEntity.put("y", y);
        newEntity.put("type", "wall");
        JSONArray arr = this.dungeonJson.getJSONArray("entities");
        arr.put(newEntity);
        this.dungeonJson.put("entities", arr);
        return;
    }

    public void addPlayer(int x, int y) {
        JSONObject playerJSON = new JSONObject();
        playerJSON.put("x", x);
        playerJSON.put("y", y);
        playerJSON.put("type", "player");
        JSONArray arr = this.dungeonJson.getJSONArray("entities");
        arr.put(playerJSON);
        this.dungeonJson.put("entities", arr);
        return;
    }

    public void addExit(int x, int y) {
        JSONObject exitJSON = new JSONObject();
        exitJSON.put("x", x);
        exitJSON.put("y", y);
        exitJSON.put("type", "exit");
        JSONArray arr = this.dungeonJson.getJSONArray("entities");
        arr.put(exitJSON);
        this.dungeonJson.put("entities", arr);
        return;
    }

    public void reset() {
        this.dungeonJson = new JSONObject();
        return;
    }

    public Dungeon getResult() {
        return new Dungeon(this.dungeonJson, this.gameMode);
    }

    private static Boolean[][] generateMaze(int xStart, int yStart, int xEnd, int yEnd) {
        // function RandomizedPrims(width, height, start, end):
        // let maze be a 2D array of booleans (of size width and height) default false
        // false representing a wall and true representing empty space
        Boolean maze[][] = new Boolean[50][50];
        for (Boolean[] row : maze) {
            Arrays.fill(row, false);
        }

        // maze[start] = empty
        maze[xStart][yStart] = true;

        // let options be a list of positions
        List<Position> options = new ArrayList<Position>();
        // options.add(new Position(xStart, yStart));

        // add to options all neighbours of 'start' not on boundary that are of
        // distance 2 away and are walls
        options.addAll(new Position(xStart, yStart).getTwoDistanceAwayPositionsWalled(50, 50, maze));

        // while options is not empty:
        Random random = new Random();
        while (!options.isEmpty()) {
            // let next = remove random from options
            Position next = options.remove(random.nextInt(options.size()));

            // let neighbours = each neighbour of distance 2 from next not on boundary that
            // are empty
            List<Position> neighbours = next.getTwoDistanceAwayPositionsEmpty(50, 50, maze);

            // if neighbours is not empty:
            if (!neighbours.isEmpty()) {
                // let neighbour = random from neighbours
                Position neighbour = neighbours.remove(random.nextInt(neighbours.size()));

                // maze[ next ] = empty (i.e. true)
                // maze[ position inbetween next and neighbour ] = empty (i.e. true)
                // maze[ neighbour ] = empty (i.e. true)
                maze[next.getX()][next.getY()] = true;
                Position between = next.getPositionBetween(neighbour);
                maze[between.getX()][between.getY()] = true;
                maze[neighbour.getX()][neighbour.getY()] = true;

            }

            // add to options all neighbours of 'next' not on boundary that are of distance
            // 2 away and are walls
            options.addAll(next.getTwoDistanceAwayPositionsWalled(50, 50, maze));
        }

        // at the end there is still a case where our end position isn't connected to
        // the map
        // we don't necessarily need this, you can just keep randomly generating maps
        // (was original intention)
        // but this will make it consistently have a pathway between the two.
        // if maze[end] is a wall:
        if (!maze[xEnd][yEnd]) {
            // maze[end] = empty
            maze[xEnd][yEnd] = true;
        }

        // let neighbours = neighbours not on boundary of distance 1 from maze[end]
        List<Position> neighbours = new Position(xEnd, yEnd).getAdjacentPositionsDirect(50, 50);
        // if there are no cells in neighbours that are empty:
        if (!neighbours.stream().anyMatch(cell -> maze[cell.getX()][cell.getY()])) {
            // let's connect it to the grid
            // let neighbour = random from neighbours
            Position neighbour = neighbours.remove(random.nextInt(neighbours.size()));
            // maze[neighbour] = empty
            maze[neighbour.getX()][neighbour.getY()] = true;
        }

        return maze;
    }

    public static Dungeon generateDungeon(int xStart, int yStart, int xEnd, int yEnd, String gameMode) {
        // set strategy
        GameModeStrategy gameModeStrategy;
        if (gameMode.toLowerCase().equals("standard")) {
            gameModeStrategy = new StandardMode();
        } else if (gameMode.toLowerCase().equals("peaceful")) {
            gameModeStrategy = new PeacefulMode();
        } else { // it could only be hard left
            gameModeStrategy = new HardMode();
        }
        DungeonBuilder dungeonBuilder = new DungeonBuilder(gameModeStrategy);

        // generate maze
        Boolean maze[][] = generateMaze(xStart, yStart, xEnd, yEnd);

        // convert maze into json entities
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                if (!maze[i][j]) {
                    dungeonBuilder.addWall(i, j);
                }
            }
        }

        // add player and exit
        dungeonBuilder.addPlayer(xStart, yStart);
        dungeonBuilder.addExit(xEnd, yEnd);

        return dungeonBuilder.getResult();
    }
}
