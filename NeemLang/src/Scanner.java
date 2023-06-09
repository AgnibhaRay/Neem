import java.util.*;

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
            case '!':
                addToken(match('=') ? TokenTypes.TokenType.BANG_EQUAL : TokenTypes.TokenType.BANG);
                break;
            case '=':
                addToken(match('=') ? TokenTypes.TokenType.EQUAL_EQUAL : TokenTypes.TokenType.EQUAL);
                break;
            case '<':
                addToken(match('=') ? TokenTypes.TokenType.LESS_EQUAL : TokenTypes.TokenType.LESS);
                break;
            case '>':
                addToken(match('=') ? TokenTypes.TokenType.GREATER_EQUAL : TokenTypes.TokenType.GREATER);
                break;
            case '/':
                if (match('/')) {
                    // A comment goes until the end of the line.
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(TokenTypes.TokenType.SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;

            case '\n':
                line++;
                break;

            case '"': string(); break;

            default:
            if (isDigit(c)) {
                number();
            } else {
                Neem.error(line, "Unexpected character.");
            }
        }
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void number() {
        while (isDigit(peek())) advance();

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();

            while (isDigit(peek())) advance();
        }

        addToken(TokenTypes.TokenType.NUMBER,
                Double.parseDouble(source.substring(start, current)));
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        // Unterminated string.
        if (isAtEnd()) {
            Neem.error(line, "Unterminated string.");
            return;
        }

        // The closing ".
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(TokenTypes.TokenType.STRING, value);
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
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

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        current++;
        return true;
    }
}
