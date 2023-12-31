package org.example.domain;


public enum TokenType {
    SHOW(TokenTypeGroup.KEYWORD),
    FUNC(TokenTypeGroup.KEYWORD),
    NUMB(TokenTypeGroup.KEYWORD),
    BOOLEAN(TokenTypeGroup.KEYWORD),
    STRING(TokenTypeGroup.KEYWORD),
    COMA(TokenTypeGroup.DELIMITER),
    OPERATOR(TokenTypeGroup.OPERATOR),
    INPUT(TokenTypeGroup.KEYWORD),
    WHILE(TokenTypeGroup.KEYWORD),
    END_WHILE(TokenTypeGroup.KEYWORD),
    IF(TokenTypeGroup.KEYWORD),
    END_IF(TokenTypeGroup.KEYWORD),
    OPEN_BRACE(TokenTypeGroup.DELIMITER),
    CLOSE_BRACE(TokenTypeGroup.DELIMITER),
    RIGHT_PARENTHESIS(TokenTypeGroup.DELIMITER),
    LEFT_PARENTHESIS(TokenTypeGroup.DELIMITER),
    NUMBER(TokenTypeGroup.VALUE),
    TEXT(TokenTypeGroup.VALUE),
    QUOTE(TokenTypeGroup.DELIMITER),
    LET(TokenTypeGroup.KEYWORD),
    ADD(TokenTypeGroup.OPERATOR),
    SUBTRACT(TokenTypeGroup.OPERATOR),
    MULTIPLY(TokenTypeGroup.OPERATOR),
    DIVIDE(TokenTypeGroup.OPERATOR),
    EQUALS(TokenTypeGroup.OPERATOR),
    TRUE(TokenTypeGroup.VALUE),
    FALSE(TokenTypeGroup.VALUE),
    ELSE(TokenTypeGroup.KEYWORD),
    RETURN(TokenTypeGroup.KEYWORD);

    private final TokenTypeGroup group;

    TokenType(TokenTypeGroup group) {
        this.group = group;
    }

    public TokenTypeGroup getGroup() {
        return group;
    }
}