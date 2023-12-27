package org.example.visitor;

import org.example.parser.context.ParseTree;
import org.example.parser.context.StatementsContext;
import org.example.parser.context.implementation.*;
import org.example.parser.context.implementation.TerminalNode;

public class SimplerLangBaseVisitor implements Visitor {

  @Override
  public Object visitProgram(ProgramContext context) {
    return visitChildren(context);
  }

  @Override
  public Object visitStatements(StatementsContext context) {
    return visitChildren(context);
  }

  @Override
  public Object visitStatement(StatementContext context) {
    return visitChildren(context);
  }

  @Override
  public Object visitLet(LetContext context) {
    return visitChildren(context);
  }

  @Override
  public Object visitShow(ShowContext context) {
    return visitChildren(context);
  }

  @Override
  public Object visitExpression(ExpressionContext context) {
    return visitChildren(context);
  }

  @Override
  public Object visitExpressionNode(ExpressionNode context) {
    return visitChildren(context);
  }

  @Override
  public Object visitIfStatement(IfStatementContext context) {
    return visitChildren(context);
  }

  @Override
  public Object visitTerminal(TerminalNode context) {
    return defaultResult();
  }

  @Override
  public Object visitInput(InputContext inputContext) {
    return defaultResult();
  }

  @Override
  public Object visitFunction(FunctionContext context) {
    return visitChildren(context);
  }

  @Override
  public Object visitReturn(ReturnContext context) {
    return visitChildren(context);
  }

  @Override
  public Object visitFunctionCall(FunctionCallContext functionCallContext) {
    return visitChildren(functionCallContext);
  }

  public Object visitChildren(ParseTree node) {
    Object result = defaultResult();
    for (int i = 0; i < node.getChildCount(); i++) {
      ParseTree c = node.getChild(i);
      result = c.accept(this);
    }

    return result;
  }

  protected Object defaultResult() {
    return null;
  }
}
