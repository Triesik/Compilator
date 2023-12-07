package org.example.parser.context;

import org.example.parser.context.implementation.StatementContext;
import org.example.visitor.Visitor;

import java.util.List;

public class ProgramContext extends ParserRuleContext {

  private final List<StatementContext> statements;

  public ProgramContext(List<StatementContext> statements) {
    this.statements = statements;

    // Add the statements as  children to this node.
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
  public Object accept(Visitor visitor) {
    return visitor.visitProgram(this);
  }
}
