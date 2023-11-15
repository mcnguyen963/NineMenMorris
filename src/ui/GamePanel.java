package ui;

import game.games.Game;
import game.games.GameCaretaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import ui.listeners.GameMouseListener;
import ui.menus.PauseMenu;
import ui.painters.DrawControlBar;

/**
 * The GamePanel class is responsible for displaying a game on the screen.
 */
public class GamePanel extends JPanel {
    private final Game game;

    /**
     * The dialog shown at the game end.
     */
    private final GameEndDialog gameEndDialog;

    // game panel properties

    private final DrawControlBar drawControlBar;

    private final PauseMenu pauseMenu;

    //set up the turn display
    private final JLabel turnLabel = new JLabel("Black's Turn",JLabel.RIGHT);

    private final JLabel t1TokensRemainingLabel = new JLabel("Tokens Remaining: 9", JLabel.LEFT);
    private final JLabel t2TokensRemainingLabel = new JLabel("Tokens Remaining: 9", JLabel.RIGHT);

    /**
     * Constructs a new GamePanel by drawing UI (board, pieces and buttons) and adds
     * event listeners where required.
     */
    public GamePanel(Frame owner, Game game) {
        this.game = game;

        this.gameEndDialog = new GameEndDialog(owner);

        //set up display
        turnLabel.setBounds(100, 50, 100, 20);
        // set up the game board
        drawControlBar = new DrawControlBar();


        // Add a listener for mouse events
        MouseAdapter gameMouseListener = new GameMouseListener(game);
        this.addMouseListener(gameMouseListener);
        this.addMouseMotionListener(gameMouseListener);

        // drawing buttons at top
        this.add(t1TokensRemainingLabel);
        // UI properties
        JButton pauseGameButton = new JButton("Pause");
        pauseGameButton.setPreferredSize(new Dimension(100,60));
        this.add(pauseGameButton);
        pauseMenu = new PauseMenu();
        pauseGameButton.addActionListener(e -> pauseMenu.showCenteredToParent(this));
        JButton saveGameButton = new JButton("Save");
        saveGameButton.setPreferredSize(new Dimension(100,60));
        saveGameButton.addActionListener(e -> game.performSave());
        this.add(saveGameButton);
        JButton loadGameButton = new JButton("Load");
        loadGameButton.setPreferredSize(new Dimension(100,60));
        loadGameButton.addActionListener(e -> game.performLoad());
        this.add(loadGameButton);
        JButton newGameButton = new JButton("New Game");
        newGameButton.setPreferredSize(new Dimension(100,60));
        this.add(newGameButton);
        newGameButton.addActionListener(e -> {
            Object[] options = {"New Game & Save", "New Game w/o Save"};
            int optionSelected = JOptionPane.showOptionDialog(this,
                    "Do you want to save the current game?",
                    "New Game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (optionSelected == 0) {
                GameCaretaker.getInstance().saveGame();
            }
            game.updateFromState(GameCaretaker.getInstance().getNewGameString());
        });
        JButton undoGameButton = new JButton("Undo");
        undoGameButton.setPreferredSize(new Dimension(100,60));
        undoGameButton.addActionListener(e -> game.undo());
        this.add(undoGameButton);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 15));
        this.add(turnLabel);
        this.add(t2TokensRemainingLabel);
    }

    public GameEndDialog getGameEndDialog() {
        return gameEndDialog;
    }

    /**
     * The paintComponent() method fills in the components for each player's
     * token bank.
     * @param g graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // paint board
        game.getBoard().paintComponent(g2d);
        // paint control bar
        drawControlBar.paintComponent(g2d);

        // paint tokens in bank 1
        game.getBlackTokenBank().paintComponent(g2d);
        // paint tokens in bank 2
        game.getWhiteTokenBank().paintComponent(g2d);
    }

    public void setTurnText(String text) {
        turnLabel.setText(text);
    }

    public void displayUndoError() {
        JOptionPane.showMessageDialog(this,
                "You cannot undo at this time.",
                "Undo Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void setT1TokenText(String text) { t1TokensRemainingLabel.setText(text); }
    public void setT2TokenText(String text) { t2TokensRemainingLabel.setText(text); }

}
