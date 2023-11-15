package game.utils;

import java.io.*;

/**
 * This class provides utility methods for file input/output operations.
 */
public class FileIOUtil {

    /**
     * Writes the given content to the specified file.
     *
     * @param file    the file to write to
     * @param content the content to write
     * @throws IOException if an I/O error occurs while writing to the file
     */

    public static void writeFile(File file, String content) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    /**
     * Reads the content from the specified file.
     *
     * @param file the file to read from
     * @return the content of the file as a string
     * @throws IOException if an I/O error occurs while reading from the file
     */
    public static String readFile(File file) throws IOException {
        StringBuilder saveData = new StringBuilder();
        String line;
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while ((line = bufferedReader.readLine()) != null) {
                saveData.append(line);
                saveData.append(System.lineSeparator());
            }
        }
        return saveData.toString();
    }
}

