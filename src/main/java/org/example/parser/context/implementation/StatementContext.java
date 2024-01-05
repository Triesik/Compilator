package org.example.parser.context.implementation;

import lombok.NoArgsConstructor;
import org.example.parser.context.ParseTree;
import org.example.parser.context.ParserRuleContext;
import org.example.visitor.Visitor;

@NoArgsConstructor
public class StatementContext extends ParserRuleContext {

    public StatementContext(ParseTree child) {
        addChild(child);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitStatement(this);
    }
}
