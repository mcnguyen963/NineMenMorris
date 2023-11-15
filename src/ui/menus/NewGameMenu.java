package ui.menus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The NewGameMenu class is a custom dialog that displays options for starting a new game.
 * It implements the PopUp interface, which defines a method for showing a popup window centered to its parent component.
 */
public class NewGameMenu extends JDialog implements PopUp {

    /**
     * Constructs a new NewGameMenu dialog with "Start New Game w/o Saving", "Start New Game & Save",
     * "Resume Game", and "Exit Game without saving" buttons.
     * Clicking the "Resume Game" button hides the dialog, and clicking the "Exit Game without saving" button exits the program.
     */
    public NewGameMenu() {
        // Disabling the main window during popup
        super((Frame) null, "New Game Menu", true);

        // Setting up the layout and components of the dialog
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());

        // Setting constraints for the buttons
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(10, 0, 10, 0);

        // Creating buttons
        JButton newGameNSButton = new JButton("Start New Game w/o Saving");
        JButton newGameSButton = new JButton("Start New Game & Save");
        JButton resumeButton = new JButton("Resume Game");
        JButton exitButton = new JButton("Exit Game without saving");

        // Adding buttons to the panel vertically according to constraints
        buttonPanel.add(newGameNSButton, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        buttonPanel.add(newGameSButton, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        buttonPanel.add(resumeButton, gridBagConstraints);
        gridBagConstraints.gridy = 3;
        buttonPanel.add(exitButton, gridBagConstraints);

        add(buttonPanel, BorderLayout.CENTER);

        // Setting size of pop up window
        setPreferredSize(new Dimension(550, 400));
        pack();

        ActionListener buttonListener = e -> {
            if (e.getSource() == resumeButton) {
                setVisible(false);
            } else if (e.getSource() == exitButton) {
                System.exit(0);
            }
        };

        // Adding action listeners for the "Resume Game" and "Exit Game without saving" buttons
        resumeButton.addActionListener(buttonListener);
        exitButton.addActionListener(buttonListener);
    }

    /**
     * Displays the NewGameMenu dialog centered to the given parent component.
     *
     * @param parent the component to which the dialog will be centered
     */
    public void showCenteredToParent(Component parent) {
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
