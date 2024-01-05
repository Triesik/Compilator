package org.example.parser.context.implementation;

import org.example.parser.context.ParserRuleContext;
import org.example.parser.context.StatementsContext;
import org.example.visitor.Visitor;

import java.util.List;

public class FunctionCallContext extends ParserRuleContext {

  private final String functionName;
  private List<FunctionParameter> functionParameters;

  public FunctionCallContext(String functionName) {
    this.functionName = functionName;
  }

  public String getFunctionName() {
    return functionName;
  }

  public List<FunctionParameter> getFunctionParameters() {
    return functionParameters;
  }

  public void setFunctionParameters(List<FunctionParameter> functionParameters) {
    this.functionParameters = functionParameters;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visitFunctionCall(this);
  }
}
