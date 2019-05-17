package Lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Lexer {

    private String string;
    private int position = 0, lenght;
    private TokenType currentType;

    public Lexer(String string){
        this.string = string;
        lenght = string.length();
        System.out.println("\nYour kod:\n" + string + "\n");
    }

    public List<Token> builderKeyToken(){
        List<Token> tokens = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        int currentPosition = 0;

        while (position < lenght) {
            buffer.insert(currentPosition, string.charAt(position));

            if(!checkForTokens(buffer.toString(), true)){
                if(checkLastSimvol(string.charAt(position))) {
                    tokens.add(getNewToken(buffer.substring(0, buffer.toString().length() - 1)));
                    buffer.delete(0, buffer.toString().length());
                    currentPosition = 0;
                } else {
                    System.err.println('\n' + "Can't recognize input '" + string.charAt(position) + "' at position:" + position + "!");
                    System.exit(3);
                }
            } else if(position < lenght){
                position++;
                currentPosition++;
            }
        }

        tokens.add(getNewToken(buffer.toString()));

        return tokens;
    }

    private boolean checkForTokens(String key, boolean flag) {
        for (TokenType type : TokenType.values()) {
            Matcher matcher = type.getPattern().matcher(key);
            if (matcher.matches()) {
                if(flag){currentType = type;}
                return true;
            }
        }
        return false;
    }

    private boolean checkLastSimvol(char lastSimvol){
        return checkForTokens(String.valueOf(lastSimvol), false);
    }

    private Token getNewToken(String value){
        return new Token(currentType, value);
    }
}
