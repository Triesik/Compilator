package org.example.visitor;

import org.example.parser.context.StatementsContext;
import org.example.parser.context.implementation.*;
import org.example.parser.context.implementation.TerminalNode;

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

    Object visitInput(InputContext inputContext);

   Object visitFunction(FunctionContext functionContext);

    Object visitReturn(ReturnContext returnContext);

    Object visitFunctionCall(FunctionCallContext functionCallContext);
}
