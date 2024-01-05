package org.example.visitor;

import org.example.parser.context.ParseTree;
import org.example.parser.context.StatementsContext;
import org.example.parser.context.implementation.*;
import org.example.parser.context.implementation.TerminalNode;

public class BaseVisitor implements Visitor {

  @Override
  public void visitProgram(ProgramContext context) {
    visitChildren(context);
  }

  @Override
  public void visitStatements(StatementsContext context) {
    visitChildren(context);
  }

  @Override
  public void visitStatement(StatementContext context) {
    visitChildren(context);
  }

  @Override
  public void visitLet(LetContext context) {
    visitChildren(context);
  }

  @Override
  public void visitShow(ShowContext context) {
    visitChildren(context);
  }

  @Override
  public void visitExpression(ExpressionContext context) {
    visitChildren(context);
  }

  @Override
  public void visitExpressionNode(ExpressionNode context) {
    visitChildren(context);
  }

  @Override
  public void visitIfStatement(IfStatementContext context) {
    visitChildren(context);
  }

  @Override
  public void visitTerminal(TerminalNode context) {
    defaultResult();
  }

  @Override
  public void visitInput(InputContext inputContext) {
    defaultResult();
  }

  @Override
  public void visitFunction(FunctionContext context) {
    visitChildren(context);
  }

  @Override
  public void visitReturn(ReturnContext context) {
    visitChildren(context);
  }

  @Override
  public void visitFunctionCall(FunctionCallContext functionCallContext) {
    visitChildren(functionCallContext);
  }

  public void visitChildren(ParseTree node) {
    for (int i = 0; i < node.getChildCount(); i++) {
      ParseTree c = node.getChild(i);
       c.accept(this);
    }
    
  }

  protected Object defaultResult() {
   return null;
  }
}
