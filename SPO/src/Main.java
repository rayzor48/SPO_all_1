import Lexer.Lexer;
        import Lexer.Token;
        import Parser.Parser;

        import java.util.List;

public class Main {
    static int a = 0;

    public static void main(String[] args) {

        String input1 = "int a =0 ; \nint t = 4 +324; \nwhile(i>1){ while( i > 1 ){ while( i > 1 ){while( i > 1  ){int vodka = spirt + voda;}}}}\n";
        String input2 = "int f = 0; for(int j = 3; j == 3; j = j + 1){for (int i = 0 ; i < 2 ;i = i + 1){f = f + 1;}}";
        String input3 = "int dgf = 98; int a =((34 +((8/8)-3) *7) *5);\nint t = 4                +                    324 / (dgf - 34 + 5*7-(7-3)); print;\n";
        String input4 = "if(s > 1){if(s > 1){if(s > 1){\nint a = 34 +8/2 *2;\nint t = 4 +324;\n}}}else{\nint df =1-0;\n}";//int i = 2; int f = 8; if(i > 8){f  = 231;}else{f = 0;}
        String input5 = "int f = 0; int sad = 2-((f+9)/2 + 6)/3;";
        String input6 = "if(f<=0){int true = 23;} else{int false = 20;}";
        String input7 = "int f = 1;for (int i = 0 ; i < 2 ;i = i + 1){f = f * 2;}";
        String input8 = "int a = 0;int dddd = 12; int b = 0;list List; List add 1; List add dddd; a = List remove 0; a = List get 0; b = List size; printelem b; print;";
        //Lexer lexer = new Lexer("int i = 0; int c = 99; list List; List add 1;List add 2;List add 3; int b = List size; for(i =0; i < b; i = i){c = List get i; printelem c;i=i+1;}print;");
        String input = input1 + input2 + input3 + input4 + input5;

        Lexer lexer = new Lexer("set Set; Set add 1; Set add 2; int c = 0;  c = Set contains 2; printelem c;");
        //Lexer lexer = new Lexer("set Set; Set add 1;");
        //Lexer lexer = new Lexer("int i =0; int f = 0; while (i < 11){while(f < 3){f = f+ 1;  if(i< 5){i = i + 3;} else {i = i + 6;} }} ");
        //Lexer lexer = new Lexer(" int i = 2; int f = 8; if(i > 8){f  = 231;}");


        List<Token> tokens = lexer.builderKeyToken();
        if(false){
            for (Token token : tokens) {
                if(!token.getValue().equals(" ")) {
                    System.out.print(a);
                    a++;
                    System.out.println("\t " + token);}
            }
        }

        Parser pars = new Parser(tokens);

        int j = 0;
        for(Token tok : pars.getPolis()){
            System.out.print(tok.getValue()  + ", ");
            //j ++;j + " - " + tok.getType() + " - " +
        }

        System.out.print("\n\n");
        pars.Go();

        System.out.println("\nПрограмма выполнена успешно");
    }
}