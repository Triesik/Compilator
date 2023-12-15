package org.example.parser;

import org.example.domain.Token;
import org.example.domain.TokenType;
import org.example.domain.TokenTypeGroup;
import org.example.parser.context.ParseTree;
import org.example.parser.context.ProgramContext;
import org.example.parser.context.implementation.*;
import org.example.scanner.Scanner;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final Scanner scanner;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
    }

    public ProgramContext parseProgram() {
        List<StatementContext> statements = new ArrayList<>();
        do {
            statements.add(parseStatement());
        } while (scanner.nextToken());
        return new ProgramContext(statements);
    }

    public StatementContext parseStatement() {
        if (scanner.getCurrentToken() == null) {
            scanner.nextToken();
        }

        Token token = scanner.getCurrentToken();

        if (token.getType() == TokenType.LET || token.getType() == TokenType.TEXT) {
            return new StatementContext(parseLet(), null, null);
        } else if (token.getType() == TokenType.SHOW) {
            return new StatementContext(null, parseShow(), null);
        } else if (token.getType() == TokenType.IF) {
            return new StatementContext(null, null, parseIfStatement());
        } else {
            throw new RuntimeException("Unexpected token type: " + token.getType());
        }
    }

    public ShowContext parseShow() {

        if (scanner.getCurrentToken() == null) {
            scanner.nextToken(); // Current Token =  SHOW
        }

        scanner.nextToken(); // Current Token = VAR | INT

        TerminalNode terminal = parseTerminalNode(); // VAR | INT
        final Token token = (Token) terminal.getPayload();

        if (token.getType() == TokenType.NUMBER || token.getType() == TokenType.LEFT_PARENTHESIS || token.getType() == TokenType.TEXT) {
            return new ShowContext(null, null, null, parseExpressionContext());
        } else if (token.getType().equals(TokenType.QUOTE)) {
            scanner.nextToken();
            terminal = parseTerminalNode();
            scanner.nextToken();
            return new ShowContext(null, null, terminal, null);
        } else if (token.getType() == TokenType.TEXT) {
            return new ShowContext(null, terminal, null, null);
        } else {
            throw new RuntimeException("Show not preceded with variable, string or expression");
        }
    }

    public LetContext parseLet() {
        scanner.nextToken();
        TerminalNode variableNameToken = parseTerminalNode(); // VAR
        scanner.nextToken();
        scanner.nextToken();
        if(scanner.getCurrentToken().getType() == TokenType.NUMBER || scanner.getCurrentToken().getType() == TokenType.LEFT_PARENTHESIS) {
            ParseTree expressionContext = parseExpressionContext();
            return new LetContext(variableNameToken, expressionContext);
        }

        TerminalNode valueToken = parseTerminalNode(); // INT

        return new LetContext(variableNameToken, valueToken);
    }

    public IfStatementContext parseIfStatement() {
        isTokenExpected(TokenType.IF);
        scanner.nextToken();
        scanner.nextToken();
        ParseTree condition = parseExpressionContext();
        scanner.nextToken();
        scanner.nextToken();

        isTokenExpected(TokenType.OPEN_BRACE);
        scanner.nextToken();
        StatementContext ifStatement = parseStatement();
        isTokenExpected(TokenType.CLOSE_BRACE);
        scanner.nextToken();

        StatementContext elseStatement = null;
        if (scanner.getCurrentToken().getType() == TokenType.ELSE) {
            scanner.nextToken();
            isTokenExpected(TokenType.OPEN_BRACE);
            scanner.nextToken();
            elseStatement = parseStatement();
            scanner.nextToken();
            isTokenExpected(TokenType.CLOSE_BRACE);
        }

        return new IfStatementContext(condition, ifStatement, elseStatement);
    }

    public TerminalNode parseTerminalNode() {

        if (scanner.getCurrentToken() == null) {
            scanner.nextToken();
        }

        TerminalNode token = new TerminalNode();
        token.setSymbol(scanner.getCurrentToken());
        return token;
    }

    public ParseTree parseExpressionContext() {
        return parseExpression();
    }

    private ParseTree parseExpression() {
        ParseTree left = parseTerm();

        while (isAdditionOrSubtraction() || isEquality()) {
            if (isAdditionOrSubtraction()) {
                TokenType operator = scanner.getCurrentToken().getType();
                scanner.nextToken(); // Consume the operator
                ParseTree right = parseTerm();
                left = new ExpressionContext(left, operator, right);
            } else if (isEquality()) {
                TokenType operator = scanner.getCurrentToken().getType();
                scanner.nextToken(); // Consume the '='
                ParseTree right = parseTerm();
                left = new ExpressionContext(right, operator, left);
            }
        }

        return left;
    }

    private ParseTree parseTerm() {
        ParseTree left = parseFactor();

        while (isMultiplicationOrDivision()) {
            TokenType operator = scanner.getCurrentToken().getType();
            scanner.nextToken(); // Consume the operator
            ParseTree right = parseFactor();
            left = new ExpressionContext(left, operator, right);
        }

        return left;
    }

    private ParseTree parseFactor() {
        Token currentToken = scanner.getCurrentToken();

        if (currentToken.getType() == TokenType.NUMBER || currentToken.getType() == TokenType.TEXT) {
            if(scanner.getLookAheadToken().getType().getGroup() == TokenTypeGroup.OPERATOR || scanner.getLookAheadToken().getType().getGroup() == TokenTypeGroup.DELIMITER) {
                scanner.nextToken(); // Consume the number
            }
            return new ExpressionNode(currentToken);
        } else if (currentToken.getType() == TokenType.LEFT_PARENTHESIS) {
            scanner.nextToken();
            ParseTree expression = parseExpression();
            if (scanner.getCurrentToken().getType() != TokenType.RIGHT_PARENTHESIS) {
                throw new RuntimeException("Expected ')' at index " + scanner.getCurrentIndex());
            }
            scanner.nextToken();
            return expression;
        } else if (currentToken.getType() == TokenType.EQUALS) {
            scanner.nextToken(); // Consume the '='
            return new ExpressionNode(scanner.getCurrentToken());
        } else {
            throw new RuntimeException("Unexpected token at index " + scanner.getCurrentIndex() +
                   ": " + currentToken.getValue());
        }
    }

    private boolean isAdditionOrSubtraction() {
        TokenType type = scanner.getCurrentToken().getType();
        return type == TokenType.ADD || type == TokenType.SUBTRACT;
    }

    private boolean isMultiplicationOrDivision() {
        TokenType type = scanner.getCurrentToken().getType();
        return type == TokenType.MULTIPLY || type == TokenType.DIVIDE;
    }


    private void isTokenExpected(TokenType expected) {
        if (!scanner.getCurrentToken().getType().equals(expected))
            throw new RuntimeException("Received token of type:" + scanner.getCurrentToken().getType() + " but expected: " + expected + "at position: " + scanner.getCurrentIndex());
    }

    private boolean isEquality() {
        TokenType type = scanner.getCurrentToken().getType();
        return type == TokenType.EQUALS;
    }
}