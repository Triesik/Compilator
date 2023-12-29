package org.example.parser.context;

import org.example.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

public class ParserRuleContext implements ParseTree {

  public ParseTree parent;

  public List<ParseTree> children;

  @Override
  public ParseTree getParent() {
    return this.parent;
  }

  @Override
  public void setParent(ParseTree parent) {
    this.parent = parent;
  }

  @Override
  public String getText() {
    if (getChildCount() == 0) {
      return "";
    }

    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < getChildCount(); i++) {
      builder.append(getChild(i).getText());
    }

    return builder.toString();
  }

  @Override
  public Object getPayload() {
    return this;
  }

  public void addChild(ParseTree child) {
    child.setParent(this);
    if (children == null) children = new ArrayList<>();
    children.add(child);
  }

  @Override
  public ParseTree getChild(int i) {
    return this.children.get(i);
  }

  @Override
  public int getChildCount() {
    return children != null ? children.size() : 0;
  }

  @Override
  public String toStringTree() {
    if (getChildCount() == 0) {
      return "";
    }

    StringBuilder sb = new StringBuilder();

    sb.append("( ");
    sb.append(this.getClass().getSimpleName());
    sb.append("(");
    for (int i = 0; i < getChildCount(); i++) {
      sb.append(" ").append(getChild(i).toStringTree()).append(" ");
    }
    sb.append(")");
    sb.append(" )");

    return sb.toString();
  }

  @Override
  public void accept(Visitor visitor) {

  }
}
