package org.example.parser.context.implementation;

import org.example.parser.context.ParseTree;
import org.example.parser.context.ParserRuleContext;
import org.example.parser.context.StatementsContext;
import org.example.visitor.Visitor;

public class StatementContext extends ParserRuleContext {

    public StatementContext() {}

    public StatementContext(ParseTree node) {
        this.addChild(node);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitStatement(this);
    }
}
