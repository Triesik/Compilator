package org.example.parser.context.implementation;

import org.example.visitor.Visitor;
import org.example.parser.context.ParserRuleContext;

/**
 * `Show` Syntax ParseRuleContext.
 *
 * <p>Eg:- show INT or show VAR
 */
public class ShowContext extends ParserRuleContext {

    private final TerminalNode integerValue;

    private final TerminalNode variableName;

    private final TerminalNode stringValue;

    private final ExpressionContext expressionContext;

    public ShowContext(TerminalNode integerValue, TerminalNode variableName, TerminalNode stringValue, ExpressionContext expressionContext) {
        this.integerValue = integerValue;
        this.variableName = variableName;
        this.stringValue = stringValue;
        this.expressionContext = expressionContext;

        // Conditionally add child node
        if (expressionContext != null) {
            this.addChild(expressionContext);
        } else if (integerValue != null) {
            this.addChild(integerValue);
        } else if (stringValue != null) {
            this.addChild(stringValue);
        } else {
            this.addChild(variableName);
        }
    }

    public TerminalNode getIntegerValue() {
        return integerValue;
    }

    public TerminalNode getVariableName() {
        return variableName;
    }

    public ExpressionContext getExpressionContext() {
        return expressionContext;
    }

    public TerminalNode getStringValue() {
        return stringValue;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visitShow(this);
    }
}
