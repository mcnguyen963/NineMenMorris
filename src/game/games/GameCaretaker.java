package game.games;

import game.utils.FileChooserUtil;
import game.utils.FileIOUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The caretaker class for managing game states and saving/loading game progress.
 */
public class GameCaretaker {
    /**
     * The singleton instance of the GameCaretaker.
     */
    private static GameCaretaker instance;

    /**
     * The list of memento strings representing the game states.
     */
    private final List<String> mementoStringList = new ArrayList<>();

    /**
     * The current game mode.
     */
    private GameMode gameMode;

    /**
     * Boolean indicator for whether to load the game from the home screen.
     */
    private boolean loadFromHome;

    /**
     * Private default constructor for the singleton object.
     */
    private GameCaretaker() {}

    /**
     * Retrieves the instance of the GameCaretaker.
     * @return The instance of GameCaretaker.
     */
    public static GameCaretaker getInstance() {
        if (GameCaretaker.instance == null) {
            GameCaretaker.instance = new GameCaretaker();
            instance.loadFromHome = false;
        }
        return GameCaretaker.instance;
    }

    /**
     * Retrieves the current game mode.
     * @return The current game mode.
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Sets the game mode.
     * @param gameMode The game mode to set.
     */
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Retrieves the flag indicating whether to load the game from the home screen.
     * @return True if the game is loaded from the home screen, false otherwise.
     */
    public boolean getLoadFromHome() {
        return loadFromHome;
    }

    /**
     * Sets the flag indicating whether to load the game from the home screen.
     * @param loadFromHome True to load the game from the home screen, false otherwise.
     */
    public void setLoadFromHome(boolean loadFromHome) {
        this.loadFromHome = loadFromHome;
    }

    /**
     * Adds a memento (game state) to the caretaker.
     * @param memento The memento (game state) to add.
     */
    public void addMemento(String memento) {
        mementoStringList.add(memento);
    }

    /**
     * Retrieves the string representation of a new game state.
     * @return The string representation of a new game state.
     */
    public String getNewGameString() {
        String newGameString = """
                1
                BLACK?OFF_BOARD-54.0-157.0/OFF_BOARD-54.0-232.0/OFF_BOARD-54.0-307.0/OFF_BOARD-54.0-382.0/OFF_BOARD-54.0-457.0/OFF_BOARD-54.0-532.0/OFF_BOARD-54.0-607.0/OFF_BOARD-54.0-682.0/OFF_BOARD-54.0-757.0/\s
                WHITE?OFF_BOARD-934.0-157.0/OFF_BOARD-934.0-232.0/OFF_BOARD-934.0-307.0/OFF_BOARD-934.0-382.0/OFF_BOARD-934.0-457.0/OFF_BOARD-934.0-532.0/OFF_BOARD-934.0-607.0/OFF_BOARD-934.0-682.0/OFF_BOARD-934.0-757.0/""";
        mementoStringList.clear();
        mementoStringList.add(newGameString);
        return newGameString;
    }

    /**
     * Saves the current game state to a file.
     */
    public void saveGame() {
        File fileToSave = FileChooserUtil.getSaveFile();
        if (fileToSave != null) {
            writeGameState(fileToSave);
        }
    }

    /**
     * Writes the game state to a file.
     * @param fileToSave The file to save the game state to.
     */
    private void writeGameState(File fileToSave) {
        String gameState = getGameStateAsString();
        try {
            FileIOUtil.writeFile(fileToSave, gameState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the game state as a string.
     * @return The game state as a string.
     */
    private String getGameStateAsString() {
        StringBuilder gameStateString = new StringBuilder();
        for (String memento : mementoStringList) {
            gameStateString.append(memento).append("\n");
        }
        return gameStateString.toString();
    }

    /**
     * Loads a game state from a file.
     * @return The last move in the loaded game state.
     */
    public String loadGame() {
        File fileToLoad = FileChooserUtil.getLoadFile();
        String gameState = "";
        if (fileToLoad != null) {
            gameState = readGameState(fileToLoad);
            assert gameState != null;
            String[] lines = gameState.split("\n");
            for (int i = 0; i < lines.length; i += 3) {
                String turn = lines[i];
                String blackStatus = lines[i+1];
                String whiteStatus = lines[i+2];
                mementoStringList.add(turn + "\n" + blackStatus + "\n" + whiteStatus);
            }
        }
        return retrieveLastMove(gameState);
    }

    /**
     * Reads the game state from a file.
     * @param fileToLoad The file to load the game state from.
     * @return The game state as a string.
     */
    private String readGameState(File fileToLoad) {
        try {
            return FileIOUtil.readFile(fileToLoad);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves the last move from the game state.
     * @param memento The game state as a string.
     * @return The last move in the game state.
     */
    private String retrieveLastMove(String memento) {
        int intDex = memento.split("\n").length;
        String turnIndex = memento.split("\n")[intDex-3];
        String turnsData1 = memento.split("\n")[intDex-2];
        String turnsData2 = memento.split("\n")[intDex-1];
        return turnIndex+"\n"+turnsData1+"\n"+turnsData2;
    }

    /**
     * Retrieves the last saved game state.
     * @return The last saved game state.
     */
    public String getLastSavedState() {
        if (mementoStringList.size() == 1 && loadFromHome) {
            return mementoStringList.get(0);
        } else if (mementoStringList.size() > 1 && loadFromHome) {
            loadFromHome = false;
            return mementoStringList.get(mementoStringList.size() - 1);
        }
        if (mementoStringList.size() > 1 && gameMode == GameMode.PLAYER_VS_PLAYER) {
            String lastStateString = mementoStringList.get(mementoStringList.size() - 2);
            mementoStringList.remove(mementoStringList.size() - 1);
            return lastStateString;
        } else if (mementoStringList.size() > 2 && gameMode == GameMode.PLAYER_VS_BOT) {
            String lastStateString = mementoStringList.get(mementoStringList.size() - 3);
            mementoStringList.remove(mementoStringList.size() - 1);
            mementoStringList.remove(mementoStringList.size() - 1);
            return lastStateString;
        }
        return null;
    }
}
