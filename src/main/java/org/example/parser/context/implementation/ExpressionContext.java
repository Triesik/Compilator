package org.example.parser.context.implementation;

import org.example.domain.TokenType;
import org.example.parser.context.ParseTree;
import org.example.parser.context.ParserRuleContext;
import org.example.visitor.Visitor;

public class ExpressionContext extends ParserRuleContext {
    private ParseTree leftOperand;
    private TokenType operator;
    private ParseTree rightOperand;

    public ExpressionContext(ParseTree leftOperand, TokenType operator, ParseTree rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;

        this.addChild(leftOperand);
        this.addChild(rightOperand);
    }

    public ExpressionContext() {

    }

    public ParseTree getLeftOperand() {
        return leftOperand;
    }

    public TokenType getOperator() {
        return operator;
    }

    public ParseTree getRightOperand() {
        return rightOperand;
    }

    @Override
    public String toStringTree() {
        if (getChildCount() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("( ");
        sb.append(this.getOperator());
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
        visitor.visitExpression(this);
    }

    public void setLeftOperand(ParseTree leftOperand) {
        this.leftOperand = leftOperand;
        addChild(leftOperand);
    }

    public void setOperator(TokenType operator) {
        this.operator = operator;
    }

    public void setRightOperand(ParseTree rightOperand) {
        this.rightOperand = rightOperand;
        addChild(rightOperand);
    }
}