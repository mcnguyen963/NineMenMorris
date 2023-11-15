package game.actions;

import game.games.Game;
import game.tokens.Token;
import game.tokens.TokenStatus;

/**
 * An action to select a token.
 */
public class SelectAction extends Action {
	/**
	 * Creates a select action to select a given token.
	 * @param game The panel displaying the game
	 * @param token The token to be selected.
	 */
	public SelectAction(Game game, Token token) {
		super(game, token);
	}

	/**
	 * Executes the select action by selecting the token and highlighting the valid move locations.
	 */
	@Override
	public void execute() {
		if (this.token.getStatus() == TokenStatus.DEAD) return;

		this.game.deselectAllTokens();
		this.game.getBoard().clearAllLocationHighlights();
		this.token.setSelected(true);

		// Highlight locations the token can be moved to
		if (this.token.getStatus() == TokenStatus.OFF_BOARD ||
				this.token.getStatus() == TokenStatus.ON_BOARD && this.token.getPlayer().canJump()) {
			this.game.getBoard().highlightAllFreeLocations();
		} else {
			this.token.getBoardLocation().highlightAdjacentFreeLocations();
		}

		this.game.getGamePanel().repaint();
	}
}
