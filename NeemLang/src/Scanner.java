import java.util.*;

import static sun.nio.ch.IOStatus.EOF;

public class Scanner extends Neem{
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    public Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenTypes.TokenType.EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }


    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(': addToken(TokenTypes.TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenTypes.TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenTypes.TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenTypes.TokenType.RIGHT_BRACE); break;
            case ',': addToken(TokenTypes.TokenType.COMMA); break;
            case '.': addToken(TokenTypes.TokenType.DOT); break;
            case '-': addToken(TokenTypes.TokenType.MINUS); break;
            case '+': addToken(TokenTypes.TokenType.PLUS); break;
            case ';': addToken(TokenTypes.TokenType.SEMICOLON); break;
            case '*': addToken(TokenTypes.TokenType.STAR); break;
            default:
                Neem.error(line, "Unexpected character.");
                break;
        }
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenTypes.TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenTypes.TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
