package Parser;

import Collections.*;
import Lexer.Token;
import Lexer.TokenType;

import java.util.*;

public class ItsAKindOfMagic {
    private List<Token> polis;
    private HashMap<String, Integer> tableOfVar;
    private HashMap<String, myLinkedList> tableOfList;
    private HashMap<String, myHashSet> tableOfSet;
    private Stack <String> myStack = new Stack<>();

    myLinkedList<String> list;
    myHashSet<String> set;

    int a, b, c;
    boolean flag = false;

    public  ItsAKindOfMagic(List<Token> polis, HashMap<String, Integer> tableOfVar, HashMap<String, myLinkedList> tableOfList, HashMap<String, myHashSet> tableOfSet){
        this.polis = polis;
        this.tableOfVar = tableOfVar;
        this.tableOfList = tableOfList;
        this.tableOfSet = tableOfSet;
        GO();
    }

    private void GO(){
        Token tok;
        boolean start = false;
        for(int position = 0; position <= polis.size() - 1; position ++ ){
            tok = polis.get(position);
            //System.out.println( " my Pos = " + position + " - " +  tok.getValue());

            if(tok.getType() == TokenType.VAR){
                myStack.push(tok.getValue());
            } else if (tok.getType() == TokenType.DIGIT){
                myStack.push(tok.getValue());
            } else if (tok.getType() == TokenType.VAR_L){
                myStack.push(tok.getValue());
            } else if (tok.getType() == TokenType.OP){
                operation(tok.getValue());
            } else if (tok.getType() == TokenType.LOG_OP){
                myStack.push(String.valueOf(logicOperation(tok.getValue())));
            } else if (tok.getType() == TokenType.ASSIGN_OP){
                assignOp();
            } else if (tok.getType() == TokenType.FUNCTIONS){
                functions(tok.getValue());
            } else if (tok.getType() == TokenType.END){
                myStack.push(tok.getValue());
            } else if (tok.getType() == TokenType.NF){
                a = valueOrVariable(tableOfVar) -1;
                flag = myStack.pop().equals("true");
                position = flag ? position : a;
            } else if (tok.getType() == TokenType.START){
                myStack.push(tok.getValue());
                start = true;
            } else if (tok.getType() == TokenType.INV){
                a = valueOrVariable(tableOfVar) - 1;
                if(start){
                    position  = a;
                    start = false;}
            } else if (tok.getType() == TokenType.PRINTELEM){
                print();
            } else if (tok.getType() == TokenType.PRINT){
                System.out.println("\nVariables : " +  tableOfVar + "\n");
            } else if (tok.getType() == TokenType.EOF){
                //System.out.println("\n\nEOF!!! ");
            }
        }
    }

    private void operation(String op) {
        setAB();
        switch (op) {
            case "+":
                c = a + b;
                break;
            case "-":
                c = a - b;
                break;
            case "/":
                c = a / b;
                break;
            case "*":
                c = a * b;
                break;
        }

        myStack.push(String.valueOf(c));
    }

    private boolean logicOperation(String logOp){
        boolean flag = false;
        setAB();
        switch (logOp) {
            case "<":
                flag = a < b;
                break;
            case ">":
                flag = a > b;
                break;
            case "==":
                flag = a == b;
                break;
            case "!=":
                flag = a != b;
                break;
            case "<=":
                flag = a <= b;
                break;
            case ">=":
                flag = a >= b;
                break;
        }

        return flag;
    }

    private void functions(String func){
        int ind = -1;
        if(!func.equals("size")){
            a = valueOrVariable(tableOfVar);//System.out.print("a = " + a);
        }

        String s = "";
        if(tableOfList.get(myStack.peek()) != null){
            list = getList();
            ind = 0;
        } else {
            set = tableOfSet.get(myStack.pop());
            ind = 1;
        }

        switch (func) {
            case "add":
                //System.out.print(myStack.peek() );
                if(ind == 0){
                    list.add(String.valueOf(a));
                } else {
                    set.add(String.valueOf(a));
                }
                break;
            case "get":
                if(list != null) {
                    s = list.get(a);
                } else{
                    System.err.println();
                    System.exit(10);
                }
                break;
            case "contains":
                if(ind == 0){
                    s = String.valueOf(list.contains(String.valueOf(a)));
                } else {
                    s = set.contains(String.valueOf(a)) ? "1" : "0";
                }
                break;
            case "remove":
                if(ind == 0){
                    s = list.remove(a);
                } else {
                    s = set.remove(String.valueOf(a)) ? "1" : "0";
                }
                break;
            case "size":
                if(ind == 0){
                    s = String.valueOf(list.getSize());
                } else {
                    s = String.valueOf(set.getSize());
                }
                break;
        }

        if(!s.equals("")){
            myStack.push(String.valueOf(s));
        }
    }

    private void assignOp(){
        a = valueOrVariable(tableOfVar);
        tableOfVar.put(myStack.pop(), a);
    }

    private myLinkedList getList(){
        return tableOfList.get(myStack.pop());
    }

    private int valueOrVariable(Map<String, Integer> table) throws EmptyStackException{
        if(isDigit(myStack.peek())){
            return Integer.valueOf(myStack.pop());
        } else if(!isDigit(myStack.peek())){
            return table.get(myStack.pop());
        } else{
            System.err.println();
            System.exit(10);
        }

        return -1;
    }

    private static boolean isDigit(String s) throws NumberFormatException {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void setAB(){
        b = valueOrVariable(tableOfVar);
        a = valueOrVariable(tableOfVar);
    }

    private void print(){
        System.out.println("Out : " + valueOrVariable(tableOfVar) + "");
    }
}
