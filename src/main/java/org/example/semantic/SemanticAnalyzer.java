package org.example.semantic;

import org.example.domain.Token;
import org.example.domain.TokenType;
import org.example.domain.TokenTypeGroup;
import org.example.parser.context.ParseTree;
import org.example.parser.context.implementation.*;
import org.example.visitor.SimplerLangBaseVisitor;

import java.util.HashMap;
import java.util.Map;

public class SemanticAnalyzer extends SimplerLangBaseVisitor {

  private final Map<String, ParseTree> variableMap;

  public SemanticAnalyzer() {
    super();
    this.variableMap = new HashMap<>();
  }


  @Override
  public void visitLet(LetContext letContext) {
    TerminalNode variableNameNode = letContext.getVariableName();

    String variableName = variableNameNode.getText();

    if (variableMap.get(variableName) != null) {
      System.err.println("Error: Variable '" + variableName + "' has already been declared.");
    }

    super.visitLet(letContext);
    variableMap.put(variableNameNode.getText(), letContext.getVariableValue());
  }

  @Override
  public void visitExpression(ExpressionContext expressionContext) {
    if(expressionContext.getOperator().getGroup() != TokenTypeGroup.OPERATOR) {
      throw new RuntimeException("Operator is not valid:" + expressionContext.getOperator());
    }
  }

  @Override
  public void visitExpressionNode(ExpressionNode expressionNode) {
    Token token = expressionNode.getSymbol();
    if(expressionNode.getSymbol().getType() == TokenType.TEXT) {
      if(variableMap.get(token.getValue()) == null) {
        throw new RuntimeException("Variable referenced before declaration: " + token.getValue());
      }
    }
  }

  private void analyzeTerminalNode(TerminalNode terminalNode) {
    Token symbol = terminalNode.getSymbol();

    // Example: Perform type checking for variables or literals
    if (symbol.getType().getGroup() != TokenTypeGroup.VALUE) {
      // Handle error: Invalid use of a non-value token
      System.err.println("Error: Invalid use of a non-value token");
    }
  }

  private void analyzeFunctionCall(FunctionCallContext functionCallContext) {
    // Example: Perform semantic analysis for function calls
    // You may need to check if the function exists, match parameter types, etc.
  }

  // Other helper methods for type checking, etc.

  private boolean isValidBinaryOperation(ParseTree leftOperand, TokenType operator, ParseTree rightOperand) {
    // Example: Check if the binary operation is valid based on operand types and operator
    // You may need to implement more sophisticated type checking logic
    return true;
  }

  @Override
  public void visitShow(ShowContext context) {


  }
}
