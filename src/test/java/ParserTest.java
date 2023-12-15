import org.example.parser.Parser;
import org.example.parser.context.ParseTree;
import org.example.scanner.Scanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ParserTest {

  @Test
  public void test_parser() {

    // 1. Arrange
    String sourceCode = "let a = 10 if (10 == a) {\n" +
           "     show 1" +
           "} else {\n" +
           "     show 2" +
           "}";
    List<Character> codeArray = sourceCode.chars()
           .mapToObj(c -> (char) c).toList();

    Scanner lexer = new Scanner(codeArray);

    // 2. Act
    Parser parser = new Parser(lexer);
    ParseTree tree = parser.parseProgram();

    // 3. Assert
    Assertions.assertEquals(3, tree.getChildCount());
    Assertions.assertEquals(1, tree.getChild(0).getChildCount()); // Statement
    Assertions.assertEquals(2, tree.getChild(0).getChild(0).getChildCount()); // LET a=10
    Assertions.assertEquals(1, tree.getChild(1).getChildCount()); // Statement
    Assertions.assertEquals(1, tree.getChild(1).getChild(0).getChildCount()); // SHOW show a

    Assertions.assertEquals(1, tree.getChild(2).getChildCount()); // Statement
    Assertions.assertEquals(1, tree.getChild(2).getChild(0).getChildCount()); // SHOW show 23

    Assertions.assertEquals(
        "( ProgramContext( ( StatementContext( ( LetContext( a  10 ) ) ) )  ( StatementContext( ( ShowContext( a ) ) ) )  ( StatementContext( ( ShowContext( 20 ) ) ) ) ) )",
        tree.toStringTree());
  }
}
