package org.example.parser.context.implementation;

import org.example.domain.Token;
import org.example.visitor.Visitor;
import org.example.parser.context.ParseTree;

public class TerminalNode implements ParseTree {

  public ParseTree parent;

  public Token symbol;

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
  public java.lang.String getText() {
    return this.symbol.getValue();
  }

  @Override
  public void addChild(ParseTree child) {}

  @Override
  public ParseTree getChild(int i) {
    return null;
  }

  @Override
  public int getChildCount() {
    return 0;
  }

  @Override
  public java.lang.String toStringTree() {
    return getText();
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visitTerminal(this);
  }

  public Token getSymbol() {
    return symbol;
  }

}
