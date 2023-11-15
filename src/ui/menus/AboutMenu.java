package ui.menus;

import javax.swing.*;
import java.awt.*;

public class AboutMenu extends JDialog implements PopUp {

    /**
     * Constructs a new AboutMenu dialog with team information and a button to close the dialog.
     */
    public AboutMenu() {
        // Disabling the main window during popup
        super((Frame) null, "About", true);

        // Setting up the layout and components of the dialog
        setLayout(new BorderLayout());

        // set up main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        // create grid constraints for main panel
        GridBagConstraints mainGridBagConstraints = new GridBagConstraints();
        mainGridBagConstraints.gridx = 0;
        mainGridBagConstraints.gridy = 0;
        mainGridBagConstraints.insets = new Insets(0,0,20,0);

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridBagLayout());

        // Setting constraints for the labels
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(10, 0, 0, 0);

        // Create the title label
        JLabel titleLabel = new JLabel("Nine Men's Morris");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));

        // Create the subtitle label
        JLabel subtitleLabel = new JLabel("Developed by Awesome Games Group for FIT3077");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 20));

        // Adding labels to the panel vertically according to constraints
        labelPanel.add(titleLabel, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        labelPanel.add(subtitleLabel, gridBagConstraints);

        mainPanel.add(labelPanel, mainGridBagConstraints);

        // set up panel for team names
        JPanel teamPanel = new JPanel();
        teamPanel.setLayout(new GridBagLayout());

        // Setting constraints for the labels
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(5, 0, 0, 0);

        // add label for each team member
        String[] teamNames = {"Yu Xuan Yio", "Zareef Masud", "Luke Phillips", "Huynh Nguyen"};
        for (int i = 0; i < teamNames.length; i++) {
            JLabel teamLabel = new JLabel(teamNames[i]);
            teamLabel.setFont(new Font("Arial", Font.PLAIN, 17));
            gridBagConstraints.gridy = i;
            teamPanel.add(teamLabel,gridBagConstraints);
        }

        // add team members to main panel
        mainGridBagConstraints.gridy = 1;
        mainPanel.add(teamPanel, mainGridBagConstraints);

        JButton closeButton = new JButton("Close");
        //newGameButton.setPreferredSize(new Dimension(150, 50));
        mainGridBagConstraints.gridy = 2;
        mainPanel.add(closeButton, mainGridBagConstraints);
        add(mainPanel, BorderLayout.CENTER);

        // Setting size of pop up window
        setPreferredSize(new Dimension(550, 400));
        pack();

        // add listener for close button
        closeButton.addActionListener(e -> dispose());
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
