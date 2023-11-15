package ui;
import game.games.Game;
import game.games.GameCaretaker;
import game.games.GameMode;
import ui.menus.AboutMenu;

import javax.swing.*;
import java.awt.*;

/**
 * The HomePanel class represents the main menu panel of the Nine Men's Morris game.
 * It contains a title and buttons for starting a new game, loading a saved game, and exiting the program.
 */
public class HomePanel extends JPanel {
    /**
     * The default button size.
     */
    private final Dimension BUTTON_SIZE = new Dimension(150, 50);

    /**
     * The pop-up about menu.
     */
    private final AboutMenu aboutMenu = new AboutMenu();

    /**
     * The new game button.
     */
    private JButton newGameButton;

    /**
     * The load game button.
     */
    private JButton loadGameButton;

    /**
     * The about button.
     */
    private JButton aboutButton;

    /**
     * The exit button.
     */
    private JButton exitButton;

    /**
     * The frame that contains this panel.
     */
    private final Frame owner;

    /**
     * Constructs a new HomePanel with a specified action listener for the "New Game" button.
     * The panel contains a title label, "New Game", "Load Game", and "Exit" buttons.
     *
     * @implNote More action listeners needs to be added for the "Load Game" and "Exit" buttons.
     */
    public HomePanel(JPanel mainPanel, Frame owner) {
        this.owner = owner;
        // Set up the layout and components of the panel
        this.setBackground(Color.LIGHT_GRAY);
        this.setLayout(new GridBagLayout());

        // Create the title label
        JLabel titleLabel = new JLabel("Nine Men's Morris");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));

        // Create the subtitle label
        JLabel subtitleLabel = new JLabel("FIT3077 Group 17");
        titleLabel.setFont(new Font("Arial", Font.ITALIC, 30));

        // Setting constraints for the title label
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(5, 5, 0, 5);
        gridBagConstraints.anchor = GridBagConstraints.CENTER;

        // Adding the title label to the panel
        this.add(titleLabel, gridBagConstraints);

        // add subtitle
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(5,5,128,5);
        this.add(subtitleLabel, gridBagConstraints);

        // Setting constraints for the buttons
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);

        // Create the buttons
        this.createButtons();

        // Adding buttons to the panel vertically according to the constraints
        gridBagConstraints.gridy = 1;
        this.add(newGameButton, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        this.add(loadGameButton, gridBagConstraints);
        gridBagConstraints.gridy = 3;
        this.add(aboutButton, gridBagConstraints);
        gridBagConstraints.gridy = 4;
        this.add(exitButton, gridBagConstraints);

        // Add action listeners for the menu buttons
        this.addActionListeners(mainPanel);
    }

    /**
     * Creates the home menu buttons.
     */
    private void createButtons() {
        this.newGameButton = new JButton("New Game");
        this.newGameButton.setPreferredSize(this.BUTTON_SIZE);

        this.loadGameButton = new JButton("Load Game");
        this.loadGameButton.setPreferredSize(this.BUTTON_SIZE);

        this.aboutButton = new JButton("About");
        this.aboutButton.setPreferredSize(this.BUTTON_SIZE);

        this.exitButton = new JButton("Exit");
        this.exitButton.setPreferredSize(this.BUTTON_SIZE);
    }

    /**
     * Adds listeners to process events on the home panel.
     */
    private void addActionListeners(JPanel mainPanel) {
        this.newGameButton.addActionListener(e -> startNewGame(mainPanel));

        this.loadGameButton.addActionListener(e -> {
            GameCaretaker gameCaretaker = GameCaretaker.getInstance();
            gameCaretaker.loadGame();
            gameCaretaker.setLoadFromHome(true);
            startNewGame(mainPanel);
        });

        this.aboutButton.addActionListener(e -> aboutMenu.showCenteredToParent(this));

        this.exitButton.addActionListener(e -> System.exit(0));
    }

    private void startNewGame(JPanel mainPanel) {
        Object[] options = {"Player vs Bot", "Player vs Player"};
        int optionSelected = JOptionPane.showOptionDialog(mainPanel,
                "Please choose a game mode",
                "New Game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (optionSelected == 0) {
            GameCaretaker.getInstance().setGameMode(GameMode.PLAYER_VS_BOT);
        } else {
            GameCaretaker.getInstance().setGameMode(GameMode.PLAYER_VS_PLAYER);
        }
        Game game = new Game(owner);
        mainPanel.add(game.getGamePanel(), "gamePanel");
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "gamePanel");
    }
}

