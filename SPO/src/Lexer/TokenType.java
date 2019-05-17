package Lexer;

import java.util.regex.Pattern;

public enum TokenType {

    //key_words
    TYPE(Pattern.compile("^int$")),//|float
    CYCLE(Pattern.compile("^for|while$")),
    IF(Pattern.compile("^if$")),
    ELSE(Pattern.compile("^else$")),
    START(Pattern.compile("^start$")),
    END(Pattern.compile("^end$")),
    NF(Pattern.compile("^!F$")),
    PRINT(Pattern.compile("^print$")),
    PRINTELEM(Pattern.compile("^printelem$")),
    LIST(Pattern.compile("^list$")),
    SET(Pattern.compile("^set")),
    FUNCTIONS(Pattern.compile("^add|get|size|remove|contains")),

    LOG_OP(Pattern.compile("^<|>|<=|>=|!=|==$")),
    INV(Pattern.compile("!")),

    LP_F(Pattern.compile("^\\{$")),
    RP_F(Pattern.compile("^}$")),
    LP(Pattern.compile("^\\($")),
    RP(Pattern.compile("^\\)$")),

    DIGIT(Pattern.compile("^0|[1-9][0-9]*$")),//(0|[1-9][0-9]*[.]?[0-9]*)
    VAR_L(Pattern.compile("^[A-Z][a-z]*$")),
    VAR(Pattern.compile("^[a-z]+$")),
    ASSIGN_OP(Pattern.compile("^=$")),
    OP(Pattern.compile("^\\+|\\-|\\*|\\/$")),
    WS(Pattern.compile("^\\s+")),

    SEM(Pattern.compile("^;$")),
    EOF(Pattern.compile("^EOF$"));

    private Pattern pattern;

    TokenType(Pattern pattern){
        this.pattern = pattern;
    }

    public Pattern getPattern(){
        return pattern;
    }

}
