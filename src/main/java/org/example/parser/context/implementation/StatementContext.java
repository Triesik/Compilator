package org.example.parser.context.implementation;

import lombok.NoArgsConstructor;
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

    public StatementContext(LetContext letContext, ShowContext showContext, IfStatementContext ifStatementContext, FunctionContext functionContext) {
        this.letContext = letContext;
        this.showContext = showContext;
        this.ifStatementContext = ifStatementContext;

        if (letContext != null) {
            this.addChild(letContext);
        } else if (showContext != null) {
            this.addChild(showContext);
        } else if (functionContext != null) {
            this.addChild(functionContext);
        } else if(ifStatementContext != null) {
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
