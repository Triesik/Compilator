package org.example.visitor;

import org.example.parser.context.StatementsContext;
import org.example.parser.context.implementation.*;
import org.example.parser.context.implementation.TerminalNode;

public interface Visitor {

    void visitProgram(ProgramContext context);
    void visitStatements(StatementsContext context);
    void visitStatement(StatementContext context);
    void visitLet(LetContext context);
    void visitShow(ShowContext context);
    void visitTerminal(TerminalNode context);
    void visitExpression(ExpressionContext context);
    void visitExpressionNode(ExpressionNode context);

    void visitIfStatement(IfStatementContext context);

    void visitInput(InputContext inputContext);

    void visitFunction(FunctionContext functionContext);

    void visitReturn(ReturnContext returnContext);

    void visitFunctionCall(FunctionCallContext functionCallContext);
}
