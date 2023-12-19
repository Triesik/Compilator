import org.example.scanner.Scanner;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class ScannerTest {

    @Test
    public void scannerTest() {
//        String sourceCode = "let a = 10 if (10 == a) {\n" +
//              "     show 1 let b = 15 show b" +
//              "} else {\n" +
//              "     show 2" +
//              "} let b = 12 }";
        String sourceCode = "show 1 let c = 10";
        List<Character> codeArray = sourceCode.chars()
               .mapToObj(c -> (char) c).toList();
        Scanner scanner = new Scanner(codeArray);
        while(scanner.nextToken()) {
            System.out.println(scanner.getCurrentToken());
        }

    }

}
