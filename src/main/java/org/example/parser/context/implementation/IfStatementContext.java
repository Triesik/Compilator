package org.example.parser.context.implementation;

import org.example.parser.context.ParseTree;
import org.example.parser.context.ParserRuleContext;
import org.example.visitor.Visitor;

public class IfStatementContext extends ParserRuleContext {

    private final ParseTree condition;
    private final StatementContext ifStatement;
    private final StatementContext elseStatement;

    public IfStatementContext(ParseTree condition, StatementContext ifStatement, StatementContext elseStatement) {
        this.condition = condition;
        this.ifStatement = ifStatement;
        this.elseStatement = elseStatement;

        // Add the arguments as children to this node.
        this.addChild(condition);
        this.addChild(ifStatement);
        if (elseStatement != null) {
            this.addChild(elseStatement);
        }
    }

    public ParseTree getCondition() {
        return condition;
    }

    public StatementContext getIfStatement() {
        return ifStatement;
    }

    public StatementContext getElseStatement() {
        return elseStatement;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visitIfStatement(this);
    }
}
