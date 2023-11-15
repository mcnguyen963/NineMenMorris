package game.tokens;

import game.board.Board;
import game.games.Game;
import game.players.Player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

/**
 * Represents all the tokens assigned to a player for a game. The tokens in it may have any status.
 * The token bank paints itself. This class is responsible for operations on the tokens, including managing mills.
 */
public class TokenBank extends JComponent {
    /**
     * The total number of tokens in a token bank.
     */
    public static final int TOTAL_TOKEN_COUNT = 9;

    /**
     * The width of the token bank in screen pixels.
     */
    private static final int BANK_WIDTH = 108;

    /**
     * The height of the token bank in screen pixels.
     */
    private static final int BANK_HEIGHT = 680;

    /**
     * The player who owns this token bank.
     */
    private final Player player;

    /**
     * A list of all the mills formed by tokens in this token bank.
     */
    private final List<List<Token>> mills;

    /**
     * The tokens in the token bank.
     */
    private final List<Token> tokens;

    /**
     * Creates a new token bank for the given player.
     * @param player The player who will own the tokens.
     * @param xPosition The x-position of the top-left of the token bank.
     * @param yPosition The y-position of the top-left of the token bank.
     */
    public TokenBank(Player player, int xPosition, int yPosition) {
        super();
        this.setBounds(xPosition, yPosition, BANK_WIDTH, BANK_HEIGHT);
        this.player = player;
        this.mills = new ArrayList<>();
        this.tokens = new ArrayList<>();
        this.createTokens();
    }

    /**
     * Creates the tokens in this bank.
     */
    private void createTokens() {
        for (int i = 0; i < TOTAL_TOKEN_COUNT; i++) {
            // Calculate the token's position
            int tokenX = (int) this.getBounds().getX() + BANK_WIDTH / 2;
            int tokenY = (int) (this.getBounds().getY() + (BANK_HEIGHT / TOTAL_TOKEN_COUNT * (i + 0.5)));

            Point point = new Point(tokenX, tokenY);

            this.tokens.add(new Token(this.player, point));
        }
    }

    /**
     * Gets the player who owns this token bank.
     * @return The player who owns these tokens.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Selects the tokens in the bank which can be removed after the opponent creates a mill.
     * These tokens are on the board and not in a mill, or if all the tokens on the board are in mills,
     * they can all be removed.
     * @param game
     */
    public void selectRemovableTokens(Game game) {
        for (Token token : this.tokens) {
            if (token.getStatus() == TokenStatus.ON_BOARD && (game.checkIfTokenNotInMill(token) ||
                    this.isAllMills(game))) {
                token.setSelected(true);
            }
        }
    }

    /**
     * Gets the tokens in the bank which can be removed after the opponent creates a mill.
     * These tokens are on the board and not in a mill, or if all the tokens on the board are in mills,
     * they can all be removed.
     * @param game
     * @return A list of the tokens which can be removed by the opponent.
     */
    public List<Token> getRemovableTokens(Game game) {
        List<Token> removableTokens = new ArrayList<>();

        for (Token token : this.tokens) {
            if (token.getStatus() == TokenStatus.ON_BOARD && (game.checkIfTokenNotInMill(token) ||
                    this.isAllMills(game))) {
                removableTokens.add(token);
            }
        }

        return removableTokens;
    }

    /**
     * Gets the tokens in the bank with the given status.
     * @param status The status of the tokens to select.
     * @return A list of the tokens with the given status.
     */
    public List<Token> getByStatus(TokenStatus status) {
        List<Token> tokens = new ArrayList<>();

        this.tokens.forEach(token -> {
            if (token.getStatus() == status) {
                tokens.add(token);
            }
        });

        return tokens;
    }

    /**
     * Checks if all the tokens from this bank that are on the board are in a mill.
     * @param game
     * @return True if all tokens in the bank that are on the board are in a mill, false otherwise.
     */
    public boolean isAllMills(Game game) {
        int count = 0;

        for (Token token : this.tokens) {
            if (token.getStatus() == TokenStatus.ON_BOARD && game.checkIfTokenNotInMill(token)) {
                count++;
            }
        }

        return count == 0;
    }

    /**
     * Deselects all the tokens.
     */
    public void deselectAll() {
        this.tokens.forEach(token -> token.setSelected(false));
    }

    /**
     * Adds a mill.
     * @param mill A list of three tokens in this bank that form a mill.
     */
    public void addMill(List<Token> mill) {
        this.mills.add(mill);
    }

    /**
     * Checks if the given token is in an existing mill.
     * @param token The token to check for.
     * @return True if the token is in a mill with other tokens in this bank, false otherwise.
     */
    public boolean isInMill(Token token) {
        for (List<Token> mill : this.mills) {
            if (mill.contains(token)) return true;
        }

        return false;
    }

    /**
     * Removes any mills with the given token.
     * @param token The token whose mills to remove.
     */
    public void removeMill(Token token) {
        List<List<Token>> millsToBeRemoved = new ArrayList<>();

        // Find all mills containing the given token
        this.mills.forEach(mill -> {
            if (mill.contains(token)) {
                millsToBeRemoved.add(mill);
            }
        });

        // NOTE: CANNOT REMOVE FROM LIST WHILE ITERATING THROUGH IT
        // That's why the mills to be removed are saved and then removed after the iteration is finished
        millsToBeRemoved.forEach(this.mills::remove);
    }

    public Token getClickedToken(MouseEvent event, int CLICK_PRECISION){
        for (Token token : this.tokens) {
            if (token.getStatus() != TokenStatus.DEAD &&
                    token.getPoint().distance(event.getPoint()) < CLICK_PRECISION) {
                return token;
            }
        }
        return null;
    }

    // Methods for game loading/saving and undoing?

    public String tokenBankInfor() {
        StringBuilder output = new StringBuilder(this.player.getId().toString() + "?");

        for (Token token : this.tokens) {
            output.append(token.tokenInfor()).append("/");
        }

        return output.toString();
    }

    public void tokenBankLoad(String bankData, Board board) {
        for (int i = 0; i < this.tokens.size(); i++) {
            this.tokens.get(i).loadToken(bankData.split("/")[i],board);
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // Paint the token bank background
        graphics.setColor(Color.LIGHT_GRAY);
        Rectangle bounds = this.getBounds();
        graphics.fillRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());

        // Paint the tokens in the token bank
        this.tokens.forEach(token -> token.paintComponent(graphics));
    }
}
