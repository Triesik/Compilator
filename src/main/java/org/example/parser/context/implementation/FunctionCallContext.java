package org.example.parser.context.implementation;

import org.example.parser.context.ParserRuleContext;
import org.example.parser.context.StatementsContext;
import org.example.visitor.Visitor;

import java.util.List;

public class FunctionCallContext extends ParserRuleContext {

  private final String functionName;

  public FunctionCallContext(String functionName) {
    this.functionName = functionName;
  }

  public String getFunctionName() {
    return functionName;
  }

  @Override
  public Object accept(Visitor visitor) {
    return visitor.visitFunctionCall(this);
  }
}
