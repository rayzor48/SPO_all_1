package Parser;

        import Collections.myHashSet;
        import Collections.myLinkedList;
        import Lexer.Token;
        import Lexer.TokenType;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;

public class Parser {

    List<Token> tokens = new ArrayList<>();
    int position = 0, end;
    boolean lang_flag;

    private List<Token> polis = new ArrayList<>();
    private List<Token> buffer = new ArrayList<>();
    private HashMap<String, Integer> tableOfVar = new HashMap<>();
    private HashMap<String, myLinkedList> tableOfList = new HashMap<>();
    private HashMap<String, myHashSet> tableOfSet = new HashMap<>();

    public Parser(List<Token> tokens){
        for (Token token : tokens){
            if(token.getType() != TokenType.WS){
                this.tokens.add(token);
            }
        }

        lang_flag = lang();
        if (lang_flag){
            System.out.println("true");
        } else {
            System.out.println("false" );
        }
    }

    private int getRang(String key){
        int rang = 0;
        switch (key){
            case "+":
                rang = 1;
                break;
            case "-":
                rang = 1;
                break;
            case "/":
                rang = 2;
                break;
            case "*":
                rang = 2;
                break;
        }
        return rang;
    }

    private Token getLast(){
        return buffer.get(buffer.size() - 1);
    }

    private void buffPush(){
        for(int i = buffer.size()-1; i >= 0; i--){
            polis.add(buffer.get(i));
            buffer.remove(getLast());
        }
    }

    public Token getToken() {
        back();
        return tokens.get(position++);
    }

    public List<Token> getPolis() {
        System.out.println("\nPoliz : ");
        polis.add(new Token(TokenType.EOF, "EOF"));
        return polis;
    }

    public void Go(){
        ItsAKindOfMagic magic = new ItsAKindOfMagic(polis, tableOfVar, tableOfList, tableOfSet);
    }


    private void funk(int start){
        polis.add(new Token(TokenType.START, String.valueOf(start)));
        polis.add(new Token(TokenType.INV, "!"));
        end = polis.size() ;
        polis.get(start+3).setValue(String.valueOf(end));

    }

    private void checkInit(){
        if (!tableOfVar.containsKey(getToken().getValue())) {
            System.err.println("Error: Variety " + getToken() + " not initialize");
            System.exit(6);
        }
    }

    private boolean lang(){

        while (this.tokens.size() != position){
            if(!expr()){
                return false;
            }
        }

        return true;
    }

    private boolean expr(){//System.out.println(checkNextToken());back();
        //System.out.println(" - " + checkNextToken() );back();
        if(init() || assign() || ifCycle() || my_if() || myPrint() || list() || set() || varL() ){
            return true;
        }
        return false;
    }

    private boolean init(){//если происходит инициализация переменной
        int old_pos = position;
        if(checkNextToken() == TokenType.TYPE){
            tableOfVar.put(tokens.get(position).getValue(), 0);
            if(assign()){
                return true;
            }
        }

        //System.out.print(position + "- position init(1)");
        position = old_pos;
        //System.out.print(position + "- position init(2)");
        return false;
    }

    private boolean assign(){//если операция присваивания
        int old_pos = position;
        if(checkNextToken() == TokenType.VAR){
            checkInit();
            polis.add(getToken());//System.out.print(polis.get(0));
            if(assign_op()){
                return true;
            }
        }
        position = old_pos;
        return false;
    }

    private boolean assign_op(){
        int old_pos = position;
        if(checkNextToken() == TokenType.ASSIGN_OP){
            buffer.add(getToken());

            if(varL()){
                buffPush();
                return true;
            }

            if(value()){
                return true;
            }

        } else {
            System.err.println("Error: Lexeme \"" + TokenType.VAR + " or " + TokenType.DIGIT + "\" no expected");
            System.exit(4);
        }

        position = old_pos;
        return false;
    }

    private boolean new_expr(){
        if (value()){
            if(checkNextToken() == TokenType.RP){
                for(int i = buffer.size()-1; i >= 0; i--) {
                    if(getLast().getType() == TokenType.LP){
                        buffer.remove(getLast());
                        return true;
                    }
                    polis.add(buffer.get(i));
                    buffer.remove(getLast());

                }
                return true;
            }
        }
        return false;
    }

    private boolean value(){
        if(checkDoV()){//если следующий токен число или переменная
            if(opValue()){//если конец выражения
                return true;
            }
        }

        if(checkNextToken() == TokenType.LP){
            buffer.add(getToken());//если следующий токен ' { '
            if(new_expr()){//начинаем новое подвыражение
                if(opValue()){////если конец выражения
                    return true;
                }
            }
        }
        back();
        return false;
    }

    private boolean opValue(){
        if(!op()) {//если следующий токен не операция(условие выхода из рекурсивной проверки выражения)
            back();
            if (checkNextToken() == TokenType.SEM ) {
                buffPush();
                return true;
            }

            back();
            if(checkNextToken() == TokenType.RP){
                back();
                return true;
            }
        }

        return false;
    }

    private boolean op(){
        if(checkNextToken() == TokenType.OP){//если следующий токен операция

            if(getLast().getType() == TokenType.ASSIGN_OP || getLast().getType() == TokenType.LP) {
                buffer.add(getToken());

            } else if( getRang(getToken().getValue()) > getRang(getLast().getValue())){
                buffer.add(getToken());
            } else {
                while (buffer.size() > 1) {
                    if(getRang(getToken().getValue()) <= getRang(getLast().getValue())) {
                        polis.add(getLast());
                        buffer.remove(getLast());
                    } else break;
                }
                buffer.add(getToken());
            }

            if(valueOfValue()){//это не последняя операция
                return true;
            }
        }
        return false;
    }

    private boolean valueOfValue(){
        if(checkDoV()) {
            if (op()) {
                return true;
            }
        } else if(checkNextToken() == TokenType.LP){//если находим '(', то запускаем рекурсивную проверку выражения в скобках
            buffer.add(getToken());
            if(new_expr()){
                if (op()) {
                    return true;
                }
            }
        } else {//если следующий токен не число и не переменная - ругаемся
            System.err.println("Error: Lexeme \"" + TokenType.VAR + " or " + TokenType.DIGIT + "\" no expected");
            System.exit(4);
        }
        return false;
    }

    private boolean myPrint(){//System.out.print(checkNextToken());back();back();
        int old_pos = position;
        if(checkNextToken() == TokenType.PRINT ){
            //System.out.print("ya tut2");
            if(checkNextToken() == TokenType.SEM ) {
                polis.add(new Token(TokenType.PRINT, "print"));
                return true;
            }
        }
        position = old_pos;

        if(checkNextToken() == TokenType.PRINTELEM ){
            //System.out.print("ya tut2");
            if(checkDoV() ) {
                //polis.add(getToken());
                if(checkNextToken() == TokenType.SEM) {
                    polis.add(new Token(TokenType.PRINTELEM, "printelem"));
                    return true;
                }
            }
        }

        position = old_pos;
        return false;
    }

    private int checkInitCollection(){
        int ind = -1;
        if (tableOfList.containsKey(getToken().getValue())) {
            ind = 0;
        } else if(tableOfSet.containsKey(getToken().getValue())){
            ind = 1;
        } else {
            System.err.println("Error: Variety " + getToken() + " not initialize");
            System.exit(6);
        }
        return ind;
    }

    private boolean list(){
        int old_pos = position;
        if(checkNextToken() == TokenType.LIST){
            if(checkNextToken() == TokenType.VAR_L){
                tableOfList.put(getToken().getValue(), new myLinkedList());
                if(checkNextToken() == TokenType.SEM){
                    return true;
                }
            }
        }
        position = old_pos;
        return false;
    }

    private boolean set(){
        int old_pos = position;
        if(checkNextToken() == TokenType.SET){
            if(checkNextToken() == TokenType.VAR_L){
                tableOfSet.put(getToken().getValue(), new myHashSet<String>());
                if(checkNextToken() == TokenType.SEM){
                    return true;
                }
            }
        }
        position = old_pos;
        return false;
    }

    private boolean varL(){
        int old_pos = position;
        if(checkNextToken() == TokenType.VAR_L) {
            polis.add(getToken());

            if(checkInitCollection() == 0) {
                if (collectionsOperations()) {
                    return true;
                }
            }

            if(checkInitCollection() == 1) {
                if (!tokens.get(position++).getValue().equals("get")) {back();
                    if (collectionsOperations()) {
                        return true;
                    }
                } else {
                    System.err.println();
                    System.exit(10);
                }
            }
        }
        position = old_pos;
        return false;
    }

    private boolean typeSize(){
        back();
        String s = tokens.get(position++).getValue();
        buffer.add(new Token(TokenType.FUNCTIONS, s));
        if(s.equals("size")){
            return true;
        }
        return false;
    }

    private boolean collectionsOperations() {
        int old_pos = position;
        if (checkNextToken() == TokenType.FUNCTIONS) {
            if(!typeSize()) {
                if (checkDoV()) {
                        polis.add(getLast());
                        buffer.remove(getLast());
                    if (checkNextToken() == TokenType.SEM) {
                        return true;
                    }
                }
            } else {
                polis.add(getLast());
                buffer.remove(getLast());
                if (checkNextToken() == TokenType.SEM) {
                    return true;
                }
            }
        }

        position = old_pos;
        return false;
    }

    private boolean ifCycle(){//проверка следующего токена на наличие цикла
        int old_pos = position;
        if(checkNextToken() == TokenType.CYCLE){
            back();//если цикл, то откатываемся назад и выясняем какой(for или while)

            if(tokens.get(position++).getValue().equals("while")){
                my_while();
            } else {
                my_for();
            }

            return true;
        }
        position = old_pos;
        return false;
    }

    private boolean forConditional(){//провекрка условия цикла for
        if(init() || assign()){//System.out.println(" end = " + end);
        end = polis.size();//System.out.println(" end = " + end);
            if(log_expr() && checkNextToken() == TokenType.SEM){
                if(assign()){
                    buffPush();
                    return true;
                }
            }
        }

        return false;
    }

    private boolean log_expr(){//логическое выражение и условие while
        if(checkNextToken() == TokenType.VAR){
            polis.add(getToken());
            if(checkNextToken() == TokenType.LOG_OP){
                buffer.add(getToken());
                if(checkDoV()){
                    polis.add(getLast());
                    buffer.remove(getLast());
                    polis.add(new Token(TokenType.END, "---zamena---"));
                    polis.add(new Token(TokenType.NF, "!F"));
                    return true;

                }
            }
        }

        return false;
    }

    private boolean my_while(){
        if(checkNextToken() == TokenType.LP){
            int start =  polis.size();
            if(log_expr()){
                if(checkNextToken() == TokenType.RP){
                    if(bodyOfCycle()){
                        funk(start);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean bodyOfCycle() {//тело цикла или if-else

        if (checkNextToken() == TokenType.LP_F) {
            while (checkNextToken() != TokenType.RP_F) {
                back();
                if (!expr()) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean my_for(){
        if(checkNextToken() == TokenType.LP) {
            if (forConditional()) {
                int start = end;
                if (checkNextToken() == TokenType.RP) {
                    if (bodyOfCycle()) {
                        funk(start);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean my_if(){
        int old_pos = position;
        if(checkNextToken() == TokenType.IF){
            int start =  polis.size();
            if(ifConditional()){

                if(bodyOfCycle()){//запускаем проверку тела if
                    end = polis.size();
                    polis.get(start+3).setValue(String.valueOf(end +2));
                    if(my_else()) {
                        start =  polis.size();
                        polis.add(new Token(TokenType.START, "---zamena---"));
                        polis.add(new Token(TokenType.INV, "!"));
                        //вызываем my_else и проверяем что у нас (if или if-else)
                        if (bodyOfCycle()) {//если if, то проверяем тело else
                            end = polis.size();
                            polis.get(start).setValue(String.valueOf(end));
                            return true;
                        }

                    } else {//иначе возвращаем true
                        end = polis.size();
                        polis.add(new Token(TokenType.START, String.valueOf(end+2)));
                        polis.add(new Token(TokenType.INV, "!"));
                        return true;
                    }
                }
            }
        }
        position = old_pos;
        return false;
    }

    private boolean my_else(){//проверка если после if есть else
        if(this.tokens.size() != position) {//проверяем, чтобы не выйти за пределы памяти
            if (checkNextToken() == TokenType.ELSE) {
                return true;
            }
            back();
        }
        return false;
    }

    private boolean ifConditional(){//условие if
        if(checkNextToken() == TokenType.LP){
            if(log_expr()) {
                if (checkNextToken() == TokenType.RP) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkDoV(){//проверяем число или переменая
        int old_pos = position;
        //если это число
        if(checkNextToken() == TokenType.DIGIT){//System.out.println(tokens.get(old_pos));
            polis.add(getToken());
            return true;
        } else position = old_pos;//если нет, то откатываемся на старую позицию
        //если это переменная
        if(checkNextToken() == TokenType.VAR){//System.out.println(tokens.get(old_pos));
            polis.add(getToken());
            checkInit();
            return true;
        } else position = old_pos;

        return false;
    }

    private void back(){//костыль, откатываемся назад
        position--;
    }

    private TokenType checkNextToken(){//возвращает следующий токен
        try {
            return tokens.get(position++).getType();
        } catch (IndexOutOfBoundsException ex) {
            System.err.println("Error: Lexeme \"" + TokenType.TYPE + "\" expected");
            System.exit(3);
        }
        return null;
    }
}