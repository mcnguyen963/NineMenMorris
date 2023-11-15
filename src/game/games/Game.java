package game.games;

import game.board.Board;
import game.players.Bot;
import game.players.Person;
import game.players.Player;
import game.players.PlayerID;
import game.tokens.Token;
import game.tokens.TokenBank;
import game.tokens.TokenStatus;
import ui.GamePanel;

import java.awt.*;
import java.util.List;

/**
 * This class represents a game of Nine Men's Morris.
 */
public class Game {
	/**
	 * The first player. Typically, a game is played between the same players, so both players are final.
	 */
	private final Player playerOne;

	/**
	 * The second player.
	 */
	private final Player playerTwo;

	/**
	 * The ID of the player whose turn it is.
	 */
	private PlayerID currentPlayerId;

	/**
	 * The number of completed turns. This can be used to find whose turn it is currently.
	 */
	private int turnCount;

	/**
	 * The token bank for black tokens.
	 */
	private final TokenBank blackTokenBank;

	/**
	 * The token bank for white tokens.
	 */
	private final TokenBank whiteTokenBank;

	/**
	 * The game board.
	 */
	private final Board board;

	/**
	 * The game panel for displaying the game UI.
	 */
	private final GamePanel gamePanel;

	/**
	 * Flag indicating if a mill is formed in the game.
	 */
	public boolean millFormed = false;

	/**
	 * Constructs a new Game object with the specified frame.
	 *
	 * @param frame the frame associated with the game
	 */
	public Game(Frame frame) {
		board = new Board();
		this.turnCount = 1;

		// Create the players
		GameMode gameMode = GameCaretaker.getInstance().getGameMode();
		this.playerOne = new Person(PlayerID.BLACK);
		if (gameMode == GameMode.PLAYER_VS_PLAYER) {
			this.playerTwo = new Person(PlayerID.WHITE);
		} else {
			this.playerTwo = new Bot(PlayerID.WHITE);
		}

		// Create the tokens and set their initial points
		this.blackTokenBank = new TokenBank(this.playerOne, 0, 120);
		this.whiteTokenBank = new TokenBank(this.playerTwo, 880, 120);

		this.gamePanel = new GamePanel(frame, this);


		this.playerOne.setTokens(this.blackTokenBank);
		this.playerTwo.setTokens(this.whiteTokenBank);

		this.currentPlayerId = PlayerID.BLACK;    // Black starts the game

		GameCaretaker gameCaretaker = GameCaretaker.getInstance();
		if (gameCaretaker.getLoadFromHome()) {
			updateFromState(gameCaretaker.getLastSavedState());
		} else {
			GameCaretaker.getInstance().addMemento(new Game.GameMemento(turnCount, this.getBlackTokenBank(), this.getWhiteTokenBank()).getMementoString());
		}
	}

	/**
	 * Retrieves the black token bank.
	 *
	 * @return the black token bank
	 */
	public TokenBank getBlackTokenBank() {
		return this.blackTokenBank;
	}

	/**
	 * Retrieves the white token bank.
	 *
	 * @return the white token bank
	 */
	public TokenBank getWhiteTokenBank() {
		return this.whiteTokenBank;
	}

	/**
	 * Finds whose turn it is.
	 *
	 * @return The player ID of the player whose turn it is
	 */
	public PlayerID getCurrentPlayerId() {
		return this.currentPlayerId;
	}

	/**
	 * Retrieves the first player.
	 *
	 * @return the first player
	 */
	public Player getPlayerOne() {
		return this.playerOne;
	}

	/**
	 * Retrieves the second player.
	 *
	 * @return the second player
	 */
	public Player getPlayerTwo() {
		return this.playerTwo;
	}

	/**
	 * Retrieves the game board.
	 *
	 * @return the gameBoard
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Retrieves the game panel associated with the game.
	 *
	 * @return the gamePanel
	 */
	public GamePanel getGamePanel() {
		return gamePanel;
	}

	/**
	 * Switches turns between the players and updates the UI.
	 */
	public void switchTurns() {
		// Adding memento to the caretaker
		// Change the UI text and current player
		if (this.currentPlayerId == PlayerID.BLACK) {
			this.currentPlayerId = PlayerID.WHITE;
			gamePanel.setTurnText("White's Turn");
			gamePanel.setT2TokenText("Tokens remaining: " +
					(TokenBank.TOTAL_TOKEN_COUNT - this.whiteTokenBank.getByStatus(TokenStatus.DEAD).size()));
		} else {
			this.currentPlayerId = PlayerID.BLACK;
			gamePanel.setTurnText("Black's Turn");
			gamePanel.setT1TokenText("Tokens remaining: " +
					(TokenBank.TOTAL_TOKEN_COUNT - this.blackTokenBank.getByStatus(TokenStatus.DEAD).size()));
		}
		if (playerOne.getPlayerID() == currentPlayerId) {
			playerOne.generateAction(this);
		} else {
			playerTwo.generateAction(this);
		}
		// Update the turns counter
		this.turnCount++;
		GameCaretaker.getInstance().addMemento(new Game.GameMemento(turnCount, this.getBlackTokenBank(), this.getWhiteTokenBank()).getMementoString());
		gamePanel.repaint();
	}

	/**
	 * Performs the game load operation.
	 */
	public void performLoad() {
		String gameToLoad = GameCaretaker.getInstance().loadGame();
		this.updateFromState(gameToLoad);
	}

	/**
	 * Updates the game state from the given previous state.
	 *
	 * @param previousState the previous game state
	 */
	public void updateFromState(String previousState) {
		this.getBoard().resetLocation();
		String turnIndex = previousState.split("\n")[0];
		this.turnCount = Integer.parseInt(turnIndex.trim());
		if (this.turnCount % 2 == 1) {
			this.currentPlayerId = playerOne.getPlayerID();
			gamePanel.setTurnText("Black's Turn");
		} else {
			this.currentPlayerId = playerTwo.getPlayerID();
			gamePanel.setTurnText("White's Turn");
		}
		String tokenBank1 = previousState.split("\n")[1];
		String tokenBank1data = tokenBank1.split("\\?")[1];
		String tokenBank2 = previousState.split("\n")[2];
		String tokenBank2data = tokenBank2.split("\\?")[1];
		if (this.blackTokenBank.getPlayer().getPlayerID().toString().equals(tokenBank1.split("\\?")[0])) {
			this.blackTokenBank.tokenBankLoad(tokenBank1data, this.getBoard());
			this.whiteTokenBank.tokenBankLoad(tokenBank2data, this.getBoard());

		} else if (this.blackTokenBank.getPlayer().getPlayerID().toString().equals(tokenBank2.split("\\?")[0])) {
			this.blackTokenBank.tokenBankLoad(tokenBank2data, this.getBoard());
			this.whiteTokenBank.tokenBankLoad(tokenBank1data, this.getBoard());
		} else {
			System.out.println("error when load");
		}
		gamePanel.setT1TokenText("Tokens remaining: " +
				(TokenBank.TOTAL_TOKEN_COUNT - this.blackTokenBank.getByStatus(TokenStatus.DEAD).size()));
		gamePanel.setT2TokenText("Tokens remaining: " + (TokenBank.TOTAL_TOKEN_COUNT - this.whiteTokenBank.getByStatus(TokenStatus.DEAD).size()));
		gamePanel.repaint();
	}

	/**
	 * Performs the undo operation.
	 */
	public void undo() {
		String previousState = GameCaretaker.getInstance().getLastSavedState();
		if (previousState != null) {
			updateFromState(previousState);
		} else {
			gamePanel.displayUndoError();
		}
	}

	/**
	 * Performs the game save operation.
	 */
	public void performSave() {
		GameCaretaker.getInstance().saveGame();
	}

	/**
	 * Displays the game end dialog and ends the game.
	 */
	public void endGame() {
		Player winner;

		if (this.playerOne.getTokenBank().getByStatus(TokenStatus.DEAD).size() > 6) {
			winner = this.playerTwo;
		} else {
			winner = this.playerOne;
		}

		gamePanel.getGameEndDialog().setWinner(winner.getId());
		gamePanel.getGameEndDialog().setVisible(true);
	}

	/**
	 * Deselects all tokens.
	 */
	public void deselectAllTokens() {
		this.blackTokenBank.deselectAll();
		this.whiteTokenBank.deselectAll();
	}

	/**
	 * Adds a mill with the given tokens to the game.
	 *
	 * @param mill the mill to add
	 */
	public void addMill(List<Token> mill) {
		TokenBank tokenBank = mill.get(0).getPlayer().getTokenBank();
		tokenBank.addMill(mill);
	}

	/**
	 * Checks if the given token is part of a mill in the game.
	 *
	 * @param token the token to check
	 * @return true if the token is not part of a mill, false otherwise
	 */
	public boolean checkIfTokenNotInMill(Token token) {
		TokenBank tokenBank = token.getPlayer().getTokenBank();
		return !tokenBank.isInMill(token);
	}

	/**
	 * Removes any mills containing the given token.
	 *
	 * @param token the token whose mills to remove
	 */
	public void removeMill(Token token) {
		TokenBank tokenBank = token.getPlayer().getTokenBank();
		tokenBank.removeMill(token);
	}

	/**
	 * This nested class represents a memento of the Game object.
	 */
	public static class GameMemento {
		private final String mementoString;

		/**
		 * Constructs a new GameMemento with the specified turns, black token bank, and white token bank.
		 *
		 * @param turns           the number of completed turns
		 * @param blackTokenBank  the black token bank
		 * @param whiteTokenBank  the white token bank
		 */
		public GameMemento(int turns, TokenBank blackTokenBank, TokenBank whiteTokenBank) {
			mementoString = turns + "\n" + blackTokenBank.tokenBankInfor() + "\n" + whiteTokenBank.tokenBankInfor();
		}

		/**
		 * Retrieves the memento string representing the game state.
		 *
		 * @return the memento string
		 */
		public String getMementoString() {
			return mementoString;
		}
	}
}
