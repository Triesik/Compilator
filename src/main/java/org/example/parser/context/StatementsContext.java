package org.example.parser.context;

import org.example.parser.context.implementation.StatementContext;
import org.example.visitor.Visitor;

import java.util.List;

public class StatementsContext extends ParserRuleContext {

  private final List<StatementContext> statements;

  public StatementsContext(List<StatementContext> statements) {
    this.statements = statements;

    for (StatementContext statement : statements) {
      this.addChild(statement);
    }
  }

  public List<StatementContext> getStatements() {
    return statements;
  }

  public StatementContext getStatements(int i) {
    return statements.get(i);
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visitStatements(this);
  }
}
