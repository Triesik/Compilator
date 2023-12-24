package org.example.parser;

import org.example.domain.Token;
import org.example.domain.TokenType;
import org.example.domain.TokenTypeGroup;
import org.example.parser.context.ParseTree;
import org.example.parser.context.StatementsContext;
import org.example.parser.context.implementation.*;
import org.example.parser.context.implementation.TerminalNode;
import org.example.scanner.Scanner;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final Scanner scanner;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
    }

    public ProgramContext parseProgram() {
        ProgramContext programContext = new ProgramContext(parseStatements());
        return programContext;
    }

    public StatementsContext parseStatements() {
        List<StatementContext> statements = new ArrayList<>();
        do {
            statements.add(parseStatement());
        } while (scanner.getCurrentToken().getType() != TokenType.CLOSE_BRACE);
        return new StatementsContext(statements);
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
        scanner.nextToken();
        TerminalNode terminal = parseTerminalNode();
        final Token token = (Token) terminal.getPayload();

        if (token.getType().getGroup() == TokenTypeGroup.VALUE || token.getType() == TokenType.LEFT_PARENTHESIS) {
            return new ShowContext(null, null, null, parseExpressionContext());
        } else if (token.getType().equals(TokenType.QUOTE)) {
            scanner.nextToken();
            terminal = parseTerminalNode();
            scanner.nextToken();
            scanner.nextToken();
            return new ShowContext(null, null, terminal, null);
        } else {
            throw new RuntimeException("Show not preceded with variable, string or expression");
        }
    }

    public InputContext parseInputContext() {
        scanner.nextToken();
        scanner.nextToken();
        InputContext inputContext = new InputContext(parseTerminalNode());
        scanner.nextToken();
        scanner.nextToken();
        return inputContext;
    }

    public LetContext parseLet() {
        if(scanner.getCurrentToken().getValue().equals("let")) {
            scanner.nextToken();
        }
        TerminalNode variableNameToken = parseTerminalNode();
        scanner.nextToken();
        scanner.nextToken();
        if(scanner.getCurrentToken().getType().getGroup() == TokenTypeGroup.VALUE || scanner.getCurrentToken().getType() == TokenType.LEFT_PARENTHESIS) {
            ParseTree expressionContext = parseExpressionContext();
            return new LetContext(variableNameToken, expressionContext);
        } else if(scanner.getCurrentToken().getType() == TokenType.INPUT) {
            return new LetContext(variableNameToken, parseInputContext());
        }

        scanner.nextToken();
        TerminalNode valueToken = parseTerminalNode();
        scanner.nextToken();
        scanner.nextToken();
        return new LetContext(variableNameToken, valueToken);
    }

    public IfStatementContext parseIfStatement() {
        scanner.nextToken();
        scanner.nextToken();
        ParseTree condition = parseExpressionContext();

        scanner.nextToken();
        scanner.nextToken();
        StatementsContext ifStatement = parseStatements();

        StatementsContext elseStatement = null;
        scanner.nextToken();
        if (scanner.getCurrentToken().getType() == TokenType.ELSE) {
            scanner.nextToken();
            scanner.nextToken();
            elseStatement = parseStatements();
            scanner.nextToken();
        }

        return new IfStatementContext(condition, ifStatement, elseStatement);
    }

    public TerminalNode parseTerminalNode() {
        TerminalNode token = new TerminalNode();
        token.setSymbol(scanner.getCurrentToken());
        return token;
    }

    public ParseTree parseExpressionContext() {
        ParseTree expression = parseExpression();
        if(scanner.getCurrentToken().getType() == TokenType.EQUALS) {
            scanner.nextToken();
            scanner.nextToken();
            ExpressionContext expressionContext = new ExpressionContext();
            expressionContext.setLeftOperand(expression);
            expressionContext.setOperator(TokenType.EQUALS);
            expressionContext.setRightOperand(parseExpression());
            return expressionContext;
        }
        return expression;
    }

    private ParseTree parseExpression() {
        ParseTree left = parseTerm();

        while (isAdditionOrSubtraction()) {
           TokenType operator = scanner.getCurrentToken().getType();
           scanner.nextToken();
           ParseTree right = parseTerm();
           left = new ExpressionContext(left, operator, right);
        }

        return left;
    }

    private ParseTree parseTerm() {
        ParseTree left = parseFactor();

        while (isMultiplicationOrDivision()) {
            TokenType operator = scanner.getCurrentToken().getType();
            scanner.nextToken();
            ParseTree right = parseFactor();
            left = new ExpressionContext(left, operator, right);
        }

        return left;
    }

    private ParseTree parseFactor() {
        Token currentToken = scanner.getCurrentToken();

        if (currentToken.getType().getGroup() == TokenTypeGroup.VALUE) {
            scanner.nextToken();
            return new ExpressionNode(currentToken);
        } else if (currentToken.getType() == TokenType.LEFT_PARENTHESIS) {
            scanner.nextToken();
            ParseTree expression = parseExpression();
            if (scanner.getCurrentToken().getType() != TokenType.RIGHT_PARENTHESIS) {
                throw new RuntimeException("Expected ')' at index " + scanner.getCurrentIndex() + " got: " + scanner.getCurrentToken());
            }
            scanner.nextToken();
            return expression;
        } else if (currentToken.getType() == TokenType.EQUALS) {
            scanner.nextToken();
            ExpressionNode expressionNode = new ExpressionNode(scanner.getCurrentToken());
            scanner.nextToken();
            return expressionNode;
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

    private void nextToken() {
        if (scanner.getCurrentToken() == null) {
            scanner.nextToken();
        }
    }

    private void getNextToken(int countToSkip) {
        for (int i = 0; i < countToSkip; i++) {
            if (scanner.getCurrentToken() == null) {
                scanner.nextToken();
            }
        }
    }

}