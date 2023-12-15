package org.example.domain;


public enum TokenType {
    SHOW(TokenTypeGroup.STATEMENT),
    OPERATOR(TokenTypeGroup.OPERATOR),
    INPUT(TokenTypeGroup.STATEMENT),
    WHILE(TokenTypeGroup.STATEMENT),
    END_WHILE(TokenTypeGroup.STATEMENT),
    IF(TokenTypeGroup.STATEMENT),
    END_IF(TokenTypeGroup.STATEMENT),
    OPEN_BRACE(TokenTypeGroup.DELIMITER),
    CLOSE_BRACE(TokenTypeGroup.DELIMITER),
    RIGHT_PARENTHESIS(TokenTypeGroup.DELIMITER),
    LEFT_PARENTHESIS(TokenTypeGroup.DELIMITER),
    NUMBER(TokenTypeGroup.VALUE),
    TEXT(TokenTypeGroup.VALUE),
    QUOTE(TokenTypeGroup.DELIMITER),
    LET(TokenTypeGroup.STATEMENT),
    ADD(TokenTypeGroup.OPERATOR),
    SUBTRACT(TokenTypeGroup.OPERATOR),
    MULTIPLY(TokenTypeGroup.OPERATOR),
    DIVIDE(TokenTypeGroup.OPERATOR),
    EQUALS(TokenTypeGroup.OPERATOR),
    ELSE(TokenTypeGroup.STATEMENT);

    private final TokenTypeGroup group;

    TokenType(TokenTypeGroup group) {
        this.group = group;
    }

    public TokenTypeGroup getGroup() {
        return group;
    }
}