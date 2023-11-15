package game.actions;

import game.games.Game;
import game.tokens.Token;

/**
 * This class represents a deselect action to deselect a token in the game.
 */
public class DeselectAction extends Action {
	/**
	 * Creates a deselect action to deselect a given token.
	 * @param game The panel displaying the game
	 * @param token The token to be deselected.
	 */
	public DeselectAction(Game game, Token token) {
		super(game, token);
	}

	/**
	 * Executes the deselect action by clearing all location highlights on the game board and
	 * deselecting the associated token.
	 */
	@Override
	public void execute() {
		this.game.getBoard().clearAllLocationHighlights();
		this.token.setSelected(false);
		this.game.getGamePanel().repaint();
	}
}
