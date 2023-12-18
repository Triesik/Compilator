package org.example.parser.context.implementation;

import org.example.parser.context.ParseTree;
import org.example.parser.context.ParserRuleContext;
import org.example.parser.context.ProgramContext;
import org.example.visitor.Visitor;

public class IfStatementContext extends ParserRuleContext {

    private final ParseTree condition;
    private final ProgramContext ifStatement;
    private final ProgramContext elseStatement;

    public IfStatementContext(ParseTree condition, ProgramContext ifStatement, ProgramContext elseStatement) {
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

    public ProgramContext getIfStatement() {
        return ifStatement;
    }

    public ProgramContext getElseStatement() {
        return elseStatement;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visitIfStatement(this);
    }
}
