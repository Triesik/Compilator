import org.example.codegenerator.CodeGeneratorVisitor;
import org.example.parser.Parser;
import org.example.parser.context.ParseTree;
import org.example.scanner.Scanner;
import org.example.semantic.SemanticAnalyzer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CkCompilerTest {

  @Test
  public void test_e2e() {

//    String sourceCode = "let a = 10 if (10 + 13 == a - 5) {\n" +
//          "     show 1 let b = 13 show b" +
//          "} else {\n" +
//          "     show 2 if(2 == a) { let g = 5 + 5 show g+12 show 3+13} else {show 2}" +
//          "} let c = 12 let d = 15 }";
//    String sourceCode = "let a = 3 * 12/4*(3+5-2) }";
//    String sourceCode = "let a = input 'test value\" }";
//        String sourceCode = "let a = 12*(15+11) let b = 30 }";
//    String sourceCode = "let a = 10 if (a + a == 10) {\n" +
//          "a = 3 } else { a = 5 show 12 == e} }";
//    String sourceCode = "a = 5 let a = 5 let b = true func numb g(numb a, numb b, boolean c) { if(a+b == 10-b) {return a + 3} else {show b return g(a, b, c)} return b} let c = g(a, a, b)}";
        String sourceCode = "let b = 15}";
    List<Character> codeArray = sourceCode.chars()
           .mapToObj(c -> (char) c).toList();

    // 1. Lexer
    Scanner scanner = new Scanner(codeArray);

    // 2. Parser
    Parser parser = new Parser(scanner);
    ParseTree tree = parser.parseProgram();

    Assertions.assertNotNull(tree);

    // 3. Semantic Analyzer Visitor
    tree.accept(new SemanticAnalyzer());

    // 4.2 Compiler Visitor
//    tree.accept(new CodeGeneratorVisitor());
  }
}
