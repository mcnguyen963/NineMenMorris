package ui.listeners;

import game.actions.DeselectAction;
import game.actions.RemoveAction;
import game.actions.SelectAction;
import game.board.Location;
import game.games.Game;
import game.tokens.Token;
import game.tokens.TokenStatus;
import game.actions.PlaceAction;
import game.players.PlayerID;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A listener to handle mouse events on the game panel.
 */
public class GameMouseListener extends MouseAdapter {
	/**
	 * The maximum distance from a token's point to click it from.
	 */
	private static final int CLICK_PRECISION = 50;

	/**
	 * The JPanel displaying the game.
	 */
	private final Game game;

	/**
	 * A token that has been previously selected for a move, or null if no token has been selected for a move.
	 */
	private Token selectedToken;

	/**
	 * Constructs a game mouse listener for a given game panel.
	 * @param game The panel displaying the game.
	 */
	public GameMouseListener(Game game) {
		this.game = game;
		this.selectedToken = null;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		Token clickedToken = this.getClickedToken(event);
		int SNAP_THRESHOLD = 80;
		Location releaseLocation = this.game.getBoard().findNearbyEmptyLocation(event.getPoint(), SNAP_THRESHOLD);
		if (clickedToken != null) {
			if (this.canDeselect(clickedToken)) {
				// Case 1: Mouse clicked on token to deselect token
				this.selectedToken = null;
				new DeselectAction(this.game, clickedToken).execute();
			} else if (this.canSelect(clickedToken)) {
				// Case 2: Mouse clicked on token to select token, and token can be selected
				this.selectedToken = clickedToken;
				new SelectAction(this.game, clickedToken).execute();
			} else if (this.canRemove(clickedToken)) {
				// Case 3: Mouse clicked on valid token to remove token
				new RemoveAction(this.game, clickedToken).execute();
			}
		} else if (this.selectedToken != null && releaseLocation != null) {
			// Case 6: Mouse clicked off token to place selected token, and nearby empty location found
			new PlaceAction(this.game, this.selectedToken, releaseLocation).execute();
			this.selectedToken = null;
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		// Can implement dragging tokens later: not required for gameplay
	}

	private boolean canDeselect(Token token) {
		boolean isOpponentsToken = token.getPlayer().getId() != this.game.getCurrentPlayerId();
		return token.isSelected() && !isOpponentsToken && !this.game.millFormed;
	}

	private boolean canSelect(Token token) {
		boolean isOpponentsToken = token.getPlayer().getId() != this.game.getCurrentPlayerId();
		boolean canSelect = token.getStatus() != TokenStatus.ON_BOARD || token.getPlayer().placedAll();
		return !isOpponentsToken && canSelect && !this.game.millFormed;
	}

	private boolean canRemove(Token token) {
		boolean isOpponentsToken = token.getPlayer().getId() != this.game.getCurrentPlayerId();
		return isOpponentsToken && this.game.millFormed && token.getStatus() == TokenStatus.ON_BOARD &&
				(this.game.checkIfTokenNotInMill(token) ||
						token.getPlayer().getTokenBank().isAllMills(this.game));
	}

	/**
	 * Finds which token was pressed on. This will locate tokens with any status.
	 * @param event The event where the mouse was pressed.
	 * @return The token that was clicked on, or null if no token was pressed on.
	 */
	private Token getClickedToken(MouseEvent event){
		if(this.game.getCurrentPlayerId()== PlayerID.BLACK){
			if(this.game.millFormed){
				return this.game.getWhiteTokenBank().getClickedToken(event, CLICK_PRECISION);
			}
			return this.game.getBlackTokenBank().getClickedToken(event, CLICK_PRECISION);
		}else if(this.game.getCurrentPlayerId()== PlayerID.WHITE){
			if(this.game.millFormed){
				return this.game.getBlackTokenBank().getClickedToken(event, CLICK_PRECISION);
			}
			return this.game.getWhiteTokenBank().getClickedToken(event, CLICK_PRECISION);
		}
		return null;
	}
}
