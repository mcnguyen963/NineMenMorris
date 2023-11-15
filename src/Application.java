import game.games.Game;

import ui.HomePanel;
import ui.MainFrame;

import java.awt.CardLayout;

import javax.swing.*;

/**
 * The Application class manages the user interface and multiple games of Nine Men's Morris.
 */
public class Application {

    /**
     * The application entry point method. It creates the main application window.
     * @param arguments Command-line arguments.
     */
    public static void main(String[] arguments) {
        MainFrame frame = new MainFrame();  // Create the main application window
        JPanel mainPanel = new JPanel(new CardLayout());

        JPanel homePanel = new HomePanel(mainPanel, frame);

        mainPanel.add(homePanel, "homePanel");

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }
}