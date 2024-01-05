package org.example.semantic;

import org.example.domain.Token;
import org.example.domain.TokenType;
import org.example.domain.TokenTypeGroup;
import org.example.parser.context.ParseTree;
import org.example.parser.context.implementation.*;
import org.example.visitor.BaseVisitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemanticAnalyzer extends BaseVisitor {

  private Map<String, ParseTree> variableMap;
  private final Map<String, FunctionData> functionMap;

  public SemanticAnalyzer() {
    super();
    this.variableMap = new HashMap<>();
    this.functionMap = new HashMap<>();
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
    if (expressionContext.getOperator() != null && expressionContext.getOperator().getGroup() != TokenTypeGroup.OPERATOR) {
      throw new RuntimeException("Operator is not valid:" + expressionContext.getOperator());
    }
    super.visitExpression(expressionContext);
  }

  @Override
  public void visitExpressionNode(ExpressionNode expressionNode) {
    Token token = expressionNode.getSymbol();
    if (expressionNode.getSymbol().getType() == TokenType.TEXT) {
      if (variableMap.get(token.getValue()) == null) {
        throw new RuntimeException("Variable referenced before declaration: " + token.getValue());
      }
    }
  }

  @Override
  public void visitFunctionCall(FunctionCallContext functionCallContext) {
    String functionName = functionCallContext.getFunctionName();
    List<FunctionParameter> parameters = functionCallContext.getFunctionParameters();
    Map<String, ParseTree> variableMapBeforeFunction = variableMap;
    variableMap = new HashMap<>();
    for(FunctionParameter functionParameter : parameters) {
      variableMap.put(functionParameter.getParameterName(), new TerminalNode());
    }

    if (functionMap.get(functionName) == null) {
      throw new RuntimeException("Error: function '" + functionName + "' has not been been declared.");
    }

    if (parameters.size() != functionMap.get(functionName).getFunctionParameterList().size()) {
      throw new RuntimeException("Error: function call'" + functionName + "' requires " + parameters.size() + " Parameters, provied: " + functionMap.get(functionName).getFunctionParameterList().size());
    }

    int callParametersIndex = 0;
    for (FunctionParameter functionParameter : parameters) {
      if (variableMap.get(functionParameter.getParameterName()) == null
             && functionParameter.getType().equals(determineVariableType(variableMap.get(functionParameter.getParameterName())))
             || functionParameter.getType().equals(functionMap.get(functionName).getFunctionParameterList().get(callParametersIndex).getType())) {
        throw new RuntimeException("Incorrect type of argument passed");
      }
      callParametersIndex++;
    }

    super.visitFunctionCall(functionCallContext);
    variableMap = variableMapBeforeFunction;
  }


  @Override
  public void visitFunction(FunctionContext functionContext) {
    String functionName = functionContext.getFunctionName();
    String returnType = functionContext.getReturnType();
    List<FunctionParameter> parameters = functionContext.getParameters();

    if (functionMap.get(functionName) != null) {
      System.err.println("Error: Variable '" + functionName + "' has already been declared.");
    }

    functionMap.put(functionName, new FunctionData(returnType, parameters));
    super.visitFunction(functionContext);
  }

  private String determineVariableType(ParseTree parseTree) {
    return null;
  }
}
