package org.example.semantic;

import org.example.domain.Token;
import org.example.domain.TokenType;
import org.example.domain.TokenTypeGroup;
import org.example.parser.context.ParseTree;
import org.example.parser.context.implementation.*;
import org.example.visitor.SimplerLangBaseVisitor;

import java.util.HashMap;
import java.util.List;
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

//  @Override
//  public void visitExpression(ExpressionContext expressionContext) {
//    if(expressionContext.getOperator() != null && expressionContext.getOperator().getGroup() != TokenTypeGroup.OPERATOR) {
//      throw new RuntimeException("Operator is not valid:" + expressionContext.getOperator());
//    }
//    super.visitExpression(expressionContext);
//  }

  @Override
  public void visitExpressionNode(ExpressionNode expressionNode) {
    Token token = expressionNode.getSymbol();
    if(expressionNode.getSymbol().getType() == TokenType.TEXT) {
      if(variableMap.get(token.getValue()) == null) {
        throw new RuntimeException("Variable referenced before declaration: " + token.getValue());
      }
    }
  }

  @Override
  public void visitFunctionCall(FunctionCallContext functionCallContext) {
  }

  @Override
  public void visitFunction(FunctionContext functionContext) {
    String functionName = functionContext.getFunctionName();
    String returnType = functionContext.getReturnType();
    List<FunctionParameter> parameters = functionContext.getParameters();


  }

  @Override
  public void visitShow(ShowContext context) {

  }
}
