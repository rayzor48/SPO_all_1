package Lexer;

public final class Token {

    String value;
    TokenType type;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TokenType getType(){
        return type;
    }

    @Override
    public String toString(){
        return "Token: [" + type + ", Value : '" + value + "' ]";
    }
}
