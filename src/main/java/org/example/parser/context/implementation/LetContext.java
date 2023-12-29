package org.example.parser.context.implementation;

import org.example.parser.context.ParseTree;
import org.example.visitor.Visitor;
import org.example.parser.context.ParserRuleContext;

/**
 * `Let` Syntax ParseRuleContext.
 *
 * <p>Eg:- a = 10 ( VAR = INT)
 */
public class LetContext extends ParserRuleContext {

  private final TerminalNode variableName;

  private final ParseTree variableValue;

  public LetContext(TerminalNode variableName, ParseTree variableValue) {
    this.variableName = variableName;
    this.variableValue = variableValue;

    // Add the arguments as  children to this node.
    this.addChild(variableName);
    this.addChild(variableValue);
  }

  public TerminalNode getVariableName() {
    return variableName;
  }

  public ParseTree getVariableValue() {
    return variableValue;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visitLet(this);
  }
}
