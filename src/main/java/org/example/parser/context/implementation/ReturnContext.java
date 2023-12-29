package org.example.parser.context.implementation;

import org.example.parser.context.ParseTree;
import org.example.parser.context.ParserRuleContext;
import org.example.visitor.Visitor;

public class ReturnContext extends ParserRuleContext {

    public ReturnContext(ParseTree parseTree) {
        addChild(parseTree);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitReturn(this);
    }
}
