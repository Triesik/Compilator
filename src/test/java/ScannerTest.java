import org.example.scanner.Scanner;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class ScannerTest {

    @Test
    public void scannerTest() {
        String code = "''";
        List<Character> codeArray = code.chars()
               .mapToObj(c -> (char) c).toList();
        Scanner scanner = new Scanner(codeArray);
        while(scanner.nextToken()) {
            System.out.println(scanner.getCurrentToken());
        }

    }

}
