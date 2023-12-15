package org.example.parser.context.implementation;

import org.example.visitor.Visitor;
import org.example.parser.context.ParserRuleContext;

/**
 * `Statement` Syntax ParseRuleContext.
 *
 * <p>Eg:- LET | SHOW // ie either Let or Show statement would be the value of statement.
 */
public class StatementContext extends ParserRuleContext {

    private final LetContext letContext;
    private final ShowContext showContext;
    private final IfStatementContext ifStatementContext;

    public StatementContext(LetContext letContext, ShowContext showContext, IfStatementContext ifStatementContext) {
        this.letContext = letContext;
        this.showContext = showContext;
        this.ifStatementContext = ifStatementContext;

        // Conditionally add child node
        if (letContext != null) {
            this.addChild(letContext);
        } else if (showContext != null) {
            this.addChild(showContext);
        } else {
            this.addChild(ifStatementContext);
        }
    }

    public LetContext getLetContext() {
        return letContext;
    }

    public ShowContext getShowContext() {
        return showContext;
    }

    public IfStatementContext getIfStatementContext() {
        return ifStatementContext;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visitStatement(this);
    }
}
