package org.example.visitor;

import org.example.parser.context.StatementsContext;
import org.example.parser.context.implementation.*;

public interface Visitor {

    Object visitProgram(ProgramContext context);
    Object visitStatements(StatementsContext context);
    Object visitStatement(StatementContext context);
    Object visitLet(LetContext context);
    Object visitShow(ShowContext context);
    Object visitTerminal(TerminalNode context);
    Object visitExpression(ExpressionContext context);
    Object visitExpressionNode(ExpressionNode context);

    Object visitIfStatement(IfStatementContext context);
}
