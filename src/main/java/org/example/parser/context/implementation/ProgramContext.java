package org.example.parser.context.implementation;

import org.example.parser.context.ParserRuleContext;
import org.example.parser.context.StatementsContext;
import org.example.visitor.Visitor;

import java.util.List;

public class ProgramContext extends ParserRuleContext {

  private final StatementsContext statements;

  public ProgramContext(StatementsContext statements) {
    this.statements = statements;
    this.addChild(statements);
  }

  public StatementsContext getStatements() {
    return statements;
  }

  @Override
  public Object accept(Visitor visitor) {
    return visitor.visitProgram(this);
  }
}
