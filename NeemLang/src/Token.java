public class Token extends Neem{
    final TokenTypes.TokenType type;
    final String neemes;
    final Object literal;
    final int line;

    public Token(TokenTypes.TokenType type, String neemes, Object literal, int line) {
        this.type = type;
        this.neemes = neemes;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return type + " " + neemes + " " + literal;
    }
}
