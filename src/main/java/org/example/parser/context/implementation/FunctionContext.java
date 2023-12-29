package org.example.parser.context.implementation;

import org.example.domain.TokenType;
import org.example.parser.context.ParserRuleContext;
import org.example.parser.context.StatementsContext;
import org.example.visitor.Visitor;

import java.util.List;

public class FunctionContext extends ParserRuleContext {

  private final StatementsContext statements;
  private final String functionName;
  private final String returnType;
  private final List<FunctionParameter> parameters;

  public FunctionContext(StatementsContext statements, List<FunctionParameter> parameters, String functionName, String returnType) {
    this.statements = statements;
    this.parameters = parameters;
    this.functionName = functionName;
    this.returnType = returnType;
    this.addChild(statements);
  }

  public List<FunctionParameter> getParameters() {
    return parameters;
  }

  public String getReturnType() {
    return returnType;
  }

  public String getFunctionName() {
    return functionName;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visitFunction(this);
  }
}
