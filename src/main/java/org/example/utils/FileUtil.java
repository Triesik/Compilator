package org.example.utils;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static List<Character> chooseAndReadFile() {
        // Create a file chooser
        JFileChooser fileChooser = new JFileChooser();

        // Show the file chooser dialog
        int result = fileChooser.showOpenDialog(null);

        // Check if a file was selected
        if (result == JFileChooser.APPROVE_OPTION) {
            // Get the selected file
            File selectedFile = fileChooser.getSelectedFile();

            try {
                // Read the contents of the file into a List<Character>
                return readFile(selectedFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Return an empty list if no file was selected or an error occurred
        return new ArrayList<>();
    }

    private static List<Character> readFile(File file) throws IOException {
        try (FileReader reader = new FileReader(file)) {
            List<Character> charList = new ArrayList<>();

            // Read the contents of the file into the List<Character>
            int charCode;
            while ((charCode = reader.read()) != -1) {
                charList.add((char) charCode);
            }

            return charList;
        }
    }
}