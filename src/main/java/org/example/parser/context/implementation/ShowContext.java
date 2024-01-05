package org.example.parser.context.implementation;

import lombok.NoArgsConstructor;
import org.example.domain.TokenType;
import org.example.parser.context.ParseTree;
import org.example.visitor.Visitor;
import org.example.parser.context.ParserRuleContext;

@NoArgsConstructor
public class ShowContext extends ParserRuleContext {

    private TokenType type;

    public ShowContext(ParseTree child, TokenType type) {
        addChild(child);
        this.type = type;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitShow(this);
    }
}
