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

//    String sourceCode = "let a = 10 if (10 == a) {\n" +
//          "     show 1 let b = 13 show b" +
//          "} else {\n" +
//          "     show 2" +
//          "} let c = 12 let d = 15 }";
//    String sourceCode = "(12+15+11*((6-(2+6)))) let b = 15 }";
//    String sourceCode = "let a = 5 if (10 == a) {\n" +
//          "     show 1" +
//          "} else {\n" +
//          "     show 2" +
//          "}}";
    String sourceCode = "let a = 'true' }";
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
    tree.accept(new CodeGeneratorVisitor());
  }
}
