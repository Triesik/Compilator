import org.example.scanner.SourceFile;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class SourceFileTest {


    @Test
    public void sourceFileTest() throws IOException {

        List<Character> sourceFile = SourceFile.readFile("C:\\Users\\Przemek\\Documents\\GenericCompiler\\src\\test\\resources\\test.a");
        sourceFile.forEach(System.out::println);
    }

}
