package game.players;

import game.board.Location;
import game.games.Game;
import game.tokens.Token;
import game.tokens.TokenStatus;
import game.actions.PlaceAction;
import game.actions.RemoveAction;

import java.util.*;

/**
 * A bot player that generates actions automatically.
 */
public class Bot extends Player {

    /**
     * Creates a new bot player with the specified ID.
     * @param id The ID of the bot player.
     */
    public Bot(PlayerID id) {
        super(id);
    }

    @Override
    public void generateAction(Game game) {
        game.getGamePanel().repaint();

        if (this.getTokenBank().getByStatus(TokenStatus.DEAD).size() > 6) {
            return;
        }

        if (this.placedAll()) {
            if (this.canJump()) {
                jumpToken(game);
            } else {
                moveToken(game);
            }
        } else {
            placeToken(game);
        }

        if (game.millFormed && game.getCurrentPlayerId() == this.getPlayerID()) {
            handleMill(game);
        }
    }

    /**
     * Selects a token randomly from the bot player's available tokens.
     * @return The selected token, or null if no tokens are available.
     */
    private Token selectToken() {
        Random random = new Random();
        List<Token> undeadTokens = this.getTokenBank().getByStatus(TokenStatus.ON_BOARD);
        undeadTokens.addAll(this.getTokenBank().getByStatus(TokenStatus.OFF_BOARD));

        if (undeadTokens.size() > 0) {
            int randomTokenIndex = random.nextInt(undeadTokens.size());
            return undeadTokens.get(randomTokenIndex);
        }
        return null;
    }

    /**
     * Selects a highlighted location randomly from the available highlighted locations on the game board.
     * @param game The current game.
     * @return The selected highlighted location, or null if no highlighted locations are available.
     */
    private Location selectHighLightLocation(Game game) {
        Random random = new Random();
        ArrayList<Location> locationTemp = new ArrayList<>();
        for (Location location : game.getBoard().getLocations()) {
            if (location.isHighlighted()) {
                locationTemp.add(location);
            }
        }
        if (locationTemp.size() > 0) {
            int randomLocationIndex = random.nextInt(locationTemp.size());
            return locationTemp.get(randomLocationIndex);
        }
        return null;
    }

    /**
     * Selects a token to remove from the opponent's removable tokens.
     * @param game The current game.
     * @return The selected token to remove, or null if no removable tokens are available.
     */
    private Token selectedRemoveToken(Game game) {
        Random random = new Random();
        List<Token> temp;
        if (this.getPlayerID() == PlayerID.WHITE) {
            temp = game.getBlackTokenBank().getRemovableTokens(game);
        } else {
            temp = game.getWhiteTokenBank().getRemovableTokens(game);
        }
        if (temp.size() > 0) {
            int randomTokenIndex = random.nextInt(temp.size());
            return temp.get(randomTokenIndex);
        }
        return null;
    }

    /**
     * Handles the removal of a token in case a mill is formed.
     * @param game The current game.
     */
    private void handleMill(Game game) {
        Token tokenTemp = selectedRemoveToken(game);
        if (tokenTemp != null) {
            new RemoveAction(game, tokenTemp).execute();
        }
    }

    /**
     * Moves a token to an adjacent free location on the game board.
     * @param game The current game.
     */
    private void moveToken(Game game) {
        Token tokenTemp = this.selectToken();
        tokenTemp.getBoardLocation().highlightAdjacentFreeLocations();
        while (this.selectHighLightLocation(game) == null || tokenTemp.getStatus() != TokenStatus.ON_BOARD) {
            tokenTemp = this.selectToken();
            tokenTemp.getBoardLocation().highlightAdjacentFreeLocations();
        }
        new PlaceAction(game, tokenTemp, selectHighLightLocation(game)).execute();
    }

    /**
     * Places a token on a free location on the game board.
     * @param game The current game.
     */
    private void placeToken(Game game) {
        game.getBoard().highlightAllFreeLocations();
        Token tokenTemp = selectToken();
        if (tokenTemp != null) {
            while (tokenTemp.getStatus() != TokenStatus.OFF_BOARD) {
                tokenTemp = selectToken();
            }
            new PlaceAction(game, tokenTemp, selectHighLightLocation(game)).execute();
        }
        game.getBoard().clearAllLocationHighlights();
    }

    /**
     * Moves a token to an available highlighted location, allowing the token to jump on the game board.
     * @param game The current game.
     */
    private void jumpToken(Game game) {
        Token tokenTemp = selectToken();
        if (tokenTemp != null) {
            while (tokenTemp.getStatus() != TokenStatus.ON_BOARD) {
                tokenTemp = selectToken();
            }
            game.getBoard().highlightAllFreeLocations();
            new PlaceAction(game, tokenTemp, selectHighLightLocation(game)).execute();
            game.getBoard().clearAllLocationHighlights();
        }
        game.getGamePanel().repaint();
    }
}
