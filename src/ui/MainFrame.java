package ui;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * The main window.
 */
public class MainFrame extends JFrame {
    /**
     * Initializes the main window.
     */
    public MainFrame() {
        this.setTitle("Nine Men's Morris");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(200, 10, 1000, 835);
        this.setLayout(new BorderLayout());
        this.setResizable(false);
    }
}
