package ui.menus;

import game.games.GameCaretaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The PauseMenu class is a custom JDialog that displays options for pausing a game.
 * It implements the PopUp interface, which defines a method for showing a popup window centered to its parent component.
 */
public class PauseMenu extends JDialog implements PopUp {

    /**
     * Constructs a new PauseMenu dialog with "Resume", "Save", and "Exit Game without saving" buttons.
     * Clicking the "Resume" button hides the dialog, and clicking the "Exit Game without saving" button exits the program.
     */
    public PauseMenu() {
        // Disabling the main window during popup
        super((Frame) null, "Pause Menu", true);

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
        JButton resumeButton = new JButton("Resume");
        JButton saveButton = new JButton("Save");
        JButton exitButton = new JButton("Exit Game without saving");

        // Adding buttons to the panel vertically
        buttonPanel.add(resumeButton, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        buttonPanel.add(saveButton, gridBagConstraints);
        gridBagConstraints.gridy = 2;
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
            } else if (e.getSource() == saveButton) {
                GameCaretaker.getInstance().saveGame();
            }
        };

        resumeButton.addActionListener(buttonListener);
        exitButton.addActionListener(buttonListener);
        saveButton.addActionListener(buttonListener);
    }

    /**
     * Displays the PauseMenu dialog centered to the given parent component.
     *
     * @param parent the component to which the dialog will be centered
     */
    public void showCenteredToParent(Component parent) {
        setLocationRelativeTo(parent);
        setVisible(true);
    }

}
