package com.enderefe.engineeringcalculator;

import com.enderefe.engineeringcalculator.Token.*;
import com.enderefe.engineeringcalculator.data_structure.*;
import com.enderefe.engineeringcalculator.data_structure.Stack;

import java.util.*;
import java.util.function.DoubleUnaryOperator;

public class Tokenize {
    private String expression;
    public List<Token> tokenizedExpression;
    private char[] FunctionCharacters = {'s','i','n','c','o','t','l','a','r','g','e'};
    public boolean isCharInArray(char c){
        for(char character : this.FunctionCharacters){
            if(c == character)  return true;
        }

        return false;
    }
    public Tokenize(String expression){
        this.expression=expression;
        {
            List<Token> tokens = new ArrayList<>();
            StringBuilder currentToken = new StringBuilder();

            for (int i = 0; i < expression.length(); i++) {
                char c = expression.charAt(i);
                //   System.out.println("Hata Burada mı? "+i+" "+c); //TEST*****************************
                if (Character.isDigit(c) || c == '.') {
                    currentToken.append(c); //sayıları ayırt edebilmek için currentToken içerisine sayılar koyuyoruz.
                } else if (isOperator(c)) {
                    if (currentToken.length() > 0) {
                        tokens.add(createNumberToken(currentToken.toString()));
                        currentToken.setLength(0);
                    }
                    switch (c) {
                        case '(':
                            tokens.add(OP_LPAR);
                            continue;
                        case ')':
                            tokens.add(OP_RPAR);
                            continue;
                        case '+':
                            tokens.add(OP_ADD);
                            continue;
                        case '*':
                            tokens.add(OP_MUL);
                            continue;
                        case '/':
                            tokens.add(OP_DIV);
                            continue;
                        case '!':
                            tokens.add(OP_FACT);
                            continue;
                        case '%':
                            tokens.add(OP_PERC);
                            continue;
                        case '^':
                            tokens.add(OP_POW);
                            continue;
                        case '√':
                            tokens.add(OP_SQRT);
                            continue;
                        case '-':
                            OperatorToken memory_token = OP_NEGATIVE;
                            if (!tokens.isEmpty()) {
                                Token last_token = tokens.get(tokens.size() - 1);
                                if (last_token instanceof NumberToken) {
                                    memory_token = OP_SUB;
                                } else if (last_token instanceof OperatorToken) {
                                    OperatorToken last_operator_token = (OperatorToken) last_token;
                                    if (last_operator_token.getOperatorType() == OperatorToken.OperatorType.SUFFIX) {
                                        memory_token = OP_SUB;
                                    }
                                }
                            }
                            tokens.add(memory_token);
                            continue;
                    }
                } else if (Character.isLetter(c)) {

                    if (currentToken.length() > 0) {
                        tokens.add(createNumberToken(currentToken.toString()));
                        currentToken.setLength(0);
                    }

                    if (tokens.size() > 0) {

                        if (tokens.get(tokens.size() - 1) instanceof NumberToken) {
                            tokens.add(OP_MUL);
                        }

                        if (i < expression.length() - 1) {    //Expression ifadesinin son değerine bakmıyorsak...

                            if (!Character.isLetter(expression.charAt(i + 1)) || isOperator(expression.charAt(i + 1))) {
                                //     System.out.println("burası test i:"+i); //TEST*****************************

                                currentToken.append(c);
                                //  System.out.println("burası test i:"+i); //TEST*****************************

                                tokens.add(createNumberToken(currentToken.toString()));
                                currentToken.setLength(0);
                                continue;
                            }

                            /**
                             * sin, cos, ln, log, tan, cot, sec, cosec,
                             * arcsin, arccos, arctan, arccot,*/

                            while (!(isOperator(c)) && isCharInArray(c) ) {
                                currentToken.append(c);
                                i++;
                                c = expression.charAt(i);
                                }
                            i--;
                            tokens.add(FunctionChooser(currentToken.toString()));

                            currentToken.setLength(0);
                        } else if (i == expression.length() - 1) {//expression ifadesinin son değerine bakıyorsak


                            currentToken.append(c);

                            tokens.add(createNumberToken(currentToken.toString()));
                            currentToken.setLength(0);
                        }
                    }
                    else {

                        if (i < expression.length() - 1) {    //Expression ifadesinin son değerine bakmıyorsak...

                            if (!Character.isLetter(expression.charAt(i + 1)) || isOperator(expression.charAt(i + 1))) {
                           /* if (currentToken.length() > 0) {
                                tokens.add(createNumberToken(currentToken.toString()));
                                currentToken.setLength(0);
                            }*/
                                currentToken.append(c);


                                tokens.add(createNumberToken(currentToken.toString()));
                                currentToken.setLength(0);
                                continue;
                            }
                        /*
                        if (currentToken.length() > 0) {
                            tokens.add(createNumberToken(currentToken.toString()));
                            currentToken.setLength(0);
                        }*/

                            while (!(isOperator(c)) && isCharInArray(c)) {

                                currentToken.append(c);
                                i++;
                                c = expression.charAt(i);
                            }
                            i--;
                            tokens.add(FunctionChooser(currentToken.toString()));

                            currentToken.setLength(0);
                        } else if (i == expression.length() - 1) {//expression ifadesinin son değerine bakıyorsak

                            currentToken.append(c);

                            tokens.add(createNumberToken(currentToken.toString()));
                            currentToken.setLength(0);
                        }
                    }
                } else if (Character.isWhitespace(c)) {
                    // Boşluk karakteri, atlanabilir
                    continue;
                } else {
                    // Bilinmeyen karakter
                    throw new IllegalArgumentException("Invalid character: " + c);
                }
            }
            if (currentToken.length() > 0) {
                tokens.add(createNumberToken(currentToken.toString()));
            }

            this.tokenizedExpression = tokens;
        }
    }
    // Operatör tokenlarını oluşturuyoruz

    public static final OperatorToken OP_ADD = new OperatorToken("+", OperatorToken.OperatorType.LEFT, 2);
    public static final OperatorToken OP_SUB = new OperatorToken("-", OperatorToken.OperatorType.LEFT, 2);
    public static final OperatorToken OP_MUL = new OperatorToken("*", OperatorToken.OperatorType.LEFT, 3);
    public static final OperatorToken OP_DIV = new OperatorToken("/", OperatorToken.OperatorType.LEFT, 3);
    public static final OperatorToken OP_POW = new OperatorToken("^", OperatorToken.OperatorType.RIGHT, 5);

    public static final OperatorToken OP_NEGATIVE = new OperatorToken("-", OperatorToken.OperatorType.PREFIX, 4);
    public static final OperatorToken OP_FACT = new OperatorToken("!", OperatorToken.OperatorType.SUFFIX, 6);
    public static final OperatorToken OP_PERC = new OperatorToken("%", OperatorToken.OperatorType.SUFFIX, 7);
    public static final OperatorToken OP_SQRT = new OperatorToken("√", OperatorToken.OperatorType.PREFIX, 8);
    public static final OperatorToken OP_LPAR = new OperatorToken("(", OperatorToken.OperatorType.PREFIX, 0);
    public static final OperatorToken OP_RPAR = new OperatorToken(")", OperatorToken.OperatorType.SUFFIX, 0);


    //Function Tokenlar
    public static final FunctionToken SIN = new FunctionToken("sin");
    public static final FunctionToken COS = new FunctionToken("cos");
    public static final FunctionToken TAN = new FunctionToken("tan");
    public static final FunctionToken COT = new FunctionToken("cot");
    public static final FunctionToken LN = new FunctionToken("ln");
    public static final FunctionToken LOG = new FunctionToken("log");
    public static final FunctionToken ARCSIN = new FunctionToken("arcsin");
    public static final FunctionToken ARCCOS = new FunctionToken("arccos");
    public static final FunctionToken ARCTAN = new FunctionToken("arctan");
    public static final FunctionToken ARCCOT = new FunctionToken("arccot");
    public static final FunctionToken SEC = new FunctionToken("sec");
    public static final FunctionToken COSEC = new FunctionToken("cosec");
   
    public static FunctionToken FunctionChooser(String Func) {
        switch (Func) {
            case "sin":
                return SIN;
            case "cos":
                return COS;
            case "tan":
                return TAN;
            case "cot":
                return COT;
            case "arcsin":
                return ARCSIN;
            case "arccos":
                return ARCCOS;
            case "arctan":
                return ARCTAN;
            case "arccot":
                return ARCCOT;
            case "ln":
                return LN;
            case "log":
                return LOG;
            case "sec":
                return SEC;
            case "cosec":
                return COSEC;
            default:
                return null;
        }
    }


    // Bir karakterin operatör olup olmadığını kontrol ediyoruz
    public static boolean isOperator ( char c){
        return c == '*' || c == '-' || c == '+' || c == '/' || c == '^' || c == '√' || c == '%' || c == '(' || c == ')' || c == '!';
    }
    //Operatörün tipini belli ediyor.
    private static OperatorToken.OperatorType getOperatorType ( char c){
        OperatorToken.OperatorType operatorType;

        if (c == '(') {
            operatorType = OperatorToken.OperatorType.PREFIX;
        } else if (c == ')') {
            operatorType = OperatorToken.OperatorType.SUFFIX;
        } else if (c == '^') {
            operatorType = OperatorToken.OperatorType.RIGHT;
        } else if (c == '√') {
            operatorType = OperatorToken.OperatorType.PREFIX;
        } else if (c == '!') {
            operatorType = OperatorToken.OperatorType.SUFFIX;
        } else if (c == '%') {
            operatorType = OperatorToken.OperatorType.SUFFIX;
        } else {
            operatorType = OperatorToken.OperatorType.LEFT; // Varsayılan olarak LEFT tipini kullanabilirsiniz
        }

        return operatorType;
    }

    //Operatörün Önceliğini belli eden metod
    private static int getPrecedence ( char c){
        int precedence;
        if (c == '+' || c == '-') {
            precedence = 2;
        } else if (c == '*' || c == '/' || c=='÷') {
            precedence = 3;
        } else if (c == '^') {
            precedence = 5;
        } else if (c == '√') {
            precedence = 8;
        } else if (c == '%'){
            precedence = 7;
        }else if(c=='!'){
            precedence = 6;
        }else {
            precedence = 0; // Varsayılan olarak 0 önceliği kullanabilirsiniz
        }
        return precedence;
    }

    // Sayı tokenı oluşturmak için bir yardımcı metot
    private static Token createNumberToken (String content){
        // Burada content değişkeni bir sayı olduğunu varsayarak NumberToken oluşturabilirsiniz.
        boolean isConstant = true; // Burada duruma göre sabit sayı kontrolü yapabilirsiniz.
        try {
            double number = Double.parseDouble(content);

        } catch (NumberFormatException e) {
        //    System.out.println("Değişken bu mu? : "+content);   //TEST*************************
            isConstant = false;
        }
        return new NumberToken(content,isConstant);
    }


    public static void main(String[] args) {
        List<Token> deneme = new ArrayList<>();
        String islem="dene";
        Tokenize deneme_token = new Tokenize(islem);
        deneme = deneme_token.tokenizedExpression;

        for(int i=0;i<deneme.size();i++){
            if(deneme.get(i) instanceof NumberToken)
                System.out.println(i+"th Token is Number Token : "+deneme.get(i).getContent());
            else if(deneme.get(i) instanceof OperatorToken)
                System.out.println(i+"th Token is Operator Token : "+deneme.get(i).getContent());
            else if(deneme.get(i) instanceof FunctionToken)
                System.out.println(i+"th Token is Function Token : "+deneme.get(i).getContent());
        }



    }
}
