package org.example.parser.context.implementation;

import org.example.domain.Token;
import org.example.parser.context.ParseTree;
import org.example.visitor.Visitor;

public class ExpressionNode implements ParseTree {

    public ParseTree parent;

    public Token symbol;

    public ExpressionNode(Token token) {
        this.symbol = token;
    }

    @Override
    public ParseTree getParent() {
        return this.parent;
    }

    @Override
    public void setParent(ParseTree parent) {
        this.parent = parent;
    }

    public void setSymbol(Token symbol) {
        this.symbol = symbol;
    }

    @Override
    public Object getPayload() {
        return this.symbol;
    }

    @Override
    public String getText() {
        return this.symbol.getValue();
    }

    @Override
    public void addChild(ParseTree child) {
    }

    @Override
    public ParseTree getChild(int i) {
        return null;
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public String toStringTree() {
        return getText();
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visitExpressionNode(this);
    }

    public Token getSymbol() {
        return symbol;
    }

}

