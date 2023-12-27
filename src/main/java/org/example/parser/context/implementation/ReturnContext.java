package org.example.parser.context.implementation;

import org.example.parser.context.ParseTree;
import org.example.parser.context.ParserRuleContext;
import org.example.visitor.Visitor;

public class ReturnContext extends ParserRuleContext {

    public ReturnContext(ParseTree parseTree) {
        addChild(parseTree);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visitReturn(this);
    }
}
