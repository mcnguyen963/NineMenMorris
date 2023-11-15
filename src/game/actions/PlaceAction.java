package game.actions;

import game.board.Location;
import game.games.Game;
import game.tokens.Token;
import game.tokens.TokenStatus;
import game.players.Player;
import game.players.PlayerID;
import java.awt.Point;
import java.util.List;

/**
 * This is an action where a player places a selected token onto an empty location on the board.
 */
public class PlaceAction extends Action {
	/**
	 * The destination location.
	 */
	private final Location destination;

	/**
	 * Creates a place action to place a given token on a given location.
	 * @param game The panel displaying the game
	 * @param token The token to be placed.
	 * @param destination The new location to place the token on.
	 */
	public PlaceAction(Game game, Token token, Location destination) {
		super(game, token);
		this.destination = destination;
	}

	/**
	 * Executes the place action by placing the token on the specified destination location.
	 * If a mill is formed, it processes the mill and highlights the opponent's tokens that can be removed.
	 */
	@Override
	public void execute() {
		if (this.destination.hasToken()) return;

		// Check that the player can reach the destination based on game jump/slide rules
		if (this.token.getStatus() == TokenStatus.ON_BOARD && !this.token.getPlayer().canJump() &&
				!this.destination.hasNeighbour(this.token.getBoardLocation())) {
			return;
		}

		this.moveToken();
		new DeselectAction(this.game, this.token).execute();

		// Check if a mill has been formed
		List<Token> mill = this.game.getBoard().checkForMill(this.token);

		if (mill != null) {
			this.processMill(mill);
		} else {
			this.game.switchTurns();
		}

		this.game.getGamePanel().repaint();
	}

	/**
	 * Moves the token to the new location.
	 */
	private void moveToken() {
		// Remove the token from any previous location
		Location previousLocation = this.token.getBoardLocation();

		if (previousLocation != null) {
			previousLocation.removeToken();
		}

		// Place the token on the new location
		this.token.setBoardLocation(this.destination);
		this.destination.setToken(this.token);
		this.token.setPoint(new Point(this.destination.getX(), this.destination.getY()));
		this.token.setStatus(TokenStatus.ON_BOARD);

		// Remove any mills the token was part of
		this.game.removeMill(this.token);
	}

	/**
	 * Handles if a mill was formed.
	 *
	 * @param mill the list of tokens forming the mill
	 */
	private void processMill(List<Token> mill) {
		this.game.millFormed = true;
		this.game.addMill(mill);

		Player player = this.token.getPlayer();

		// Highlight the opponent's tokens which can be removed
		if (player.getId() == PlayerID.BLACK) {
			this.game.getPlayerTwo().getTokenBank().selectRemovableTokens(this.game);
		} else {
			this.game.getPlayerOne().getTokenBank().selectRemovableTokens(this.game);
		}
	}
}
