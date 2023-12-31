package org.example.scanner;

import org.example.domain.Token;
import org.example.domain.TokenType;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Scanner {

    private final List<Character> sourceCode;
    private final int codeLength;
    private int currentIndex;

    private Token currentToken;
    private Token previousToken;

    public Scanner(List<Character> sourceCode) {
        this.sourceCode = sourceCode;
        codeLength = sourceCode.size();
        currentIndex = 0;
    }

    public void nextToken() {

        while (currentIndex < codeLength) {

            previousToken = currentToken;
            Character currentChar = sourceCode.get(currentIndex);

            if (Character.isWhitespace(currentChar)) {
                skipWhiteSpace();
                continue;
            } else if (Pattern.matches("[-+=/*(){}]", String.valueOf(currentChar))) {
                currentToken = determineMathOperator();
                currentIndex++;
            } else if (Character.isDigit(currentChar)) {
               currentToken = readNumber();
            } else if (currentChar == '\'' || currentChar == '\"') {
                currentToken = new Token(TokenType.QUOTE, currentChar.toString());
                currentIndex++;
            } else if (currentChar == ',') {
                currentToken = new Token(TokenType.QUOTE, currentChar.toString());
                currentIndex++;
            } else if (Character.isLetter(currentChar)) {
                currentToken = readLetter();
            } else {
                throw new RuntimeException("Unexpected character at index " + currentIndex + ":  + currentChar");
            }
        }
    }

    private Token readLetter() {
        StringBuilder textValue = new StringBuilder();
        Token startingToken = previousToken;

        while ((currentIndex < codeLength && (Character.isLetter(sourceCode.get(currentIndex)) ||
               (startingToken != null && startingToken.getValue().equals("'")) && sourceCode.get(currentIndex) != '\"'))) {
            textValue.append(sourceCode.get(currentIndex));
            currentIndex++;
        }
        return new Token(determineTextType(textValue.toString()), textValue.toString());
    }

    private Token readNumber() {
        StringBuilder fullNumber = new StringBuilder();

        while (currentIndex < codeLength && Character.isDigit(sourceCode.get(currentIndex))) {
            fullNumber.append(sourceCode.get(currentIndex));
            currentIndex++;
        }
        return new Token(TokenType.NUMBER, fullNumber.toString());
    }

    private TokenType determineTextType(String textValue) {
        return Arrays.stream(TokenType.values())
               .filter(type -> type.name().equalsIgnoreCase(textValue))
               .findFirst()
               .orElse(TokenType.TEXT);
    }

    private Token determineMathOperator() {
        String currentChar = sourceCode.get(currentIndex).toString();

        return switch (currentChar) {
            case "-" -> new Token(TokenType.SUBTRACT, currentChar);
            case "+" -> new Token(TokenType.ADD, currentChar);
            case "*" -> new Token(TokenType.MULTIPLY, currentChar);
            case "/" -> new Token(TokenType.DIVIDE, currentChar);
            case "=" -> new Token(TokenType.EQUALS, currentChar);
            case ")" -> new Token(TokenType.RIGHT_PARENTHESIS, currentChar);
            case "(" -> new Token(TokenType.LEFT_PARENTHESIS, currentChar);
            case "{" -> new Token(TokenType.OPEN_BRACE, currentChar);
            case "}" -> new Token(TokenType.CLOSE_BRACE, currentChar);
            default -> throw new RuntimeException("Unknown math operator " + currentChar + " at " + currentIndex);
        };
    }

    private void skipWhiteSpace() {
        while (currentIndex < codeLength) {
            if (Character.isWhitespace(sourceCode.get(currentIndex))) {
                currentIndex++;
            } else {
                break;
            }
        }
    }

    public Token getCurrentToken() {
        return currentToken;
    }
    public int getCurrentIndex() {
        return currentIndex;
    }

    public Token getLookAheadToken() {
        int indexBeforePeek = currentIndex;
        Token tokenBeforePeek = getCurrentToken();
        nextToken();
        Token lookAheadToken = currentToken;
        currentIndex = indexBeforePeek;
        currentToken = tokenBeforePeek;
        return lookAheadToken;
    }

}

