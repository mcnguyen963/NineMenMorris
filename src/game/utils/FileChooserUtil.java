package game.utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * This class provides utility methods for file choosing operations.
 */
public class FileChooserUtil {

    /**
     * Displays a save file dialog and returns the selected file.
     *
     * @return the selected file to save, or null if no file is selected
     */
    public static File getSaveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt"));
        fileChooser.setDialogTitle("Specify a file to save");
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }
            return fileToSave;
        }
        return null;
    }

    /**
     * Displays a load file dialog and returns the selected file.
     *
     * @return the selected file to load, or null if no file is selected
     */
    public static File getLoadFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt"));
        fileChooser.setDialogTitle("Specify a file to load");
        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
}

