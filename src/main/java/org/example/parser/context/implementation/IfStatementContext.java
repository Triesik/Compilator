package org.example.parser.context.implementation;

import org.example.parser.context.ParseTree;
import org.example.parser.context.ParserRuleContext;
import org.example.parser.context.StatementsContext;
import org.example.visitor.Visitor;

public class IfStatementContext extends ParserRuleContext {

    private final ParseTree condition;
    private final StatementsContext ifStatement;
    private final StatementsContext elseStatement;

    public IfStatementContext(ParseTree condition, StatementsContext ifStatement, StatementsContext elseStatement) {
        this.condition = condition;
        this.ifStatement = ifStatement;
        this.elseStatement = elseStatement;

        this.addChild(condition);
        this.addChild(ifStatement);
        if (elseStatement != null) {
            this.addChild(elseStatement);
        }
    }

    public ParseTree getCondition() {
        return condition;
    }

    public StatementsContext getIfStatement() {
        return ifStatement;
    }

    public StatementsContext getElseStatement() {
        return elseStatement;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitIfStatement(this);
    }
}
