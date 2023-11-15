package game.actions;

import game.games.Game;
import game.tokens.Token;

/**
 * The base action represents something that a player does during their turn in a game.
 */
public abstract class Action {
	/**
	 * The panel displaying the game.
	 */
	protected final Game game;

	/**
	 * The token being altered by the action.
	 */
	protected final Token token;

	/**
	 * Creates an action for a given token.
	 * @param game The panel displaying the game.
	 * @param token The token being acted on.
	 */
	public Action(Game game, Token token) {
		this.game = game;
		this.token = token;
	}

	/**
	 * Executes the action.
	 */
	public abstract void execute();
}
