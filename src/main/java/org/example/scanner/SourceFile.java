package org.example.scanner;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SourceFile {

    public static List<Character> readFile(String filePath) throws IOException {
        List<Character> characters = new ArrayList<>();

        try (FileReader fr = new FileReader(filePath)) {
            int c;
            while ((c = fr.read()) != -1) {
                characters.add((char) c);
            }
        }

        return characters;
    }
}
