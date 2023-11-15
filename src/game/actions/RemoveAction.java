package game.actions;

import game.games.Game;
import game.tokens.Token;
import game.tokens.TokenStatus;

/**
 * An action to remove a token from the board.
 */
public class RemoveAction extends Action {
    /**
     * Creates a remove action to remove a given token.
     * @param game The panel displaying the game
     * @param token The token to be removed.
     */
    public RemoveAction(Game game, Token token) {
        super(game, token);
    }

    /**
     * Executes the remove action by removing the token from the board.
     * If the token is part of a mill, the mill is also processed.
     * If the game is over, it ends the game.
     */
    @Override
    public void execute() {
       if(this.token.isSelected()){
           // Remove the token
           this.token.killToken();
           this.game.deselectAllTokens();

           // Remove any mills this token was part of
           this.game.removeMill(this.token);
           this.game.switchTurns();
           this.game.getGamePanel().repaint();

           // Check if the game is over
           if (this.token.getPlayer().getTokenBank().getByStatus(TokenStatus.DEAD).size() > 6) {
               this.game.endGame();
           }
           this.game.millFormed = false;
       }
    }
}
