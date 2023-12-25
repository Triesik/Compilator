package org.example.utils;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static List<Character> chooseAndReadFile() {
        JFileChooser fileChooser = new JFileChooser();

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                return readFile(selectedFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    private static List<Character> readFile(File file) throws IOException {
        try (FileReader reader = new FileReader(file)) {
            List<Character> charList = new ArrayList<>();

            int charCode;
            while ((charCode = reader.read()) != -1) {
                charList.add((char) charCode);
            }

            return charList;
        }
    }
}