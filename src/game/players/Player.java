package game.players;

import game.games.Game;
import game.tokens.TokenBank;
import game.tokens.TokenStatus;


/**
 * An abstract base class for a player who plays the game.
 */
public abstract class Player {
    /**
     * The ID of this player.
     */
    private final PlayerID id;

    /**
     * The tokens belonging to this player. Includes dead tokens.
     */
    private TokenBank tokenBank;

    /**
     * Creates a new player with the given tokens.
     * @param id The ID of this player.
     */
    public Player(PlayerID id) {
        this.id = id;
    }

    /**
     * Assigns the given tokens to this player, and this player to the tokens.
     * @param tokenBank A list of tokens for this player.
     */
    public void setTokens(TokenBank tokenBank) {
        this.tokenBank = tokenBank;
    }

    /**
     * Finds the player ID of this player.
     * @return The ID of this player.
     */
    public PlayerID getId() {
        return this.id;
    }

    /**
     * Checks if the player can jump pieces.
     * @return True if the player can jump pieces, false if they must slide.
     */
    public boolean canJump() {
        return this.tokenBank.getByStatus(TokenStatus.ON_BOARD).size() < 4;
    }

    /**
     * Checks if the player has placed all their tokens.
     * @return True if the player has placed all their tokens, false otherwise.
     */
    public boolean placedAll() {
        return this.tokenBank.getByStatus(TokenStatus.OFF_BOARD).size() == 0;
    }

    /**
     * Gets the id of the player.
     * @return The id of the player.
     */
    public PlayerID getPlayerID(){
        return this.id;
    }

    /**
     * Generates the player's action in the game.
     * @param game The game instance in which the player is playing.
     */
    public void generateAction(Game game){}

    /**
     * Gets the token bank of this player.
     * @return The token bank of this player.
     */
    public TokenBank getTokenBank(){
        return this.tokenBank;
    }
}
