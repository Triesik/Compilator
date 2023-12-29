package org.example.parser.context.implementation;

import org.example.parser.context.ParserRuleContext;
import org.example.visitor.Visitor;

public class InputContext extends ParserRuleContext {

    private final TerminalNode inputText;

    public InputContext(TerminalNode inputText) {
        this.inputText = inputText;
    }

    public TerminalNode getInputText() {
        return inputText;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitInput(this);
    }
}
