package com.enderefe.engineeringcalculator;

import com.enderefe.engineeringcalculator.Token.*;
import com.enderefe.engineeringcalculator.data_structure.*;
import com.enderefe.engineeringcalculator.Derivative;
import com.enderefe.engineeringcalculator.data_structure.Stack;
import java.lang.*;
import java.util.*;

public class Calculator {
    private List<Token> outputQueue;
    private Stack operatorStack ;
    private List<Token> Problem;
    private List<Token> PostFix;

    private String result;
    private int parenthesiscount;
    public Calculator(List<Token> Problem){
        this.Problem=Problem;
        outputQueue = new ArrayList<>();
        operatorStack = new Stack();
        parenthesiscount=0;
    }
    public Calculator(String Problem){
        this.Problem=((new Tokenize(Problem).tokenizedExpression));
        outputQueue = new ArrayList<>();
        operatorStack = new Stack();
        parenthesiscount=0;
    }

    public String getResult() throws Exception,NumberFormatException {
        this.result = Double.toString(function("0"));
        if((function("0")<0.000000001) && (function("0")>-0.000000001)){
            return "0";
        }
        if((function("0")>5.0e15)||Double.isInfinite(function("0"))){
            return "Infinity";
        }
        return this.result;
    }

    public int getParenthesiscount() {
        return parenthesiscount;
    }

    //Infix to Profix-Shunting Yard algoritması
    public List<Token> getPostfixExpression() throws Exception{

        for(Token token : Problem){
            if(token instanceof NumberToken){
                outputQueue.add(token);
            }
            else if(token instanceof FunctionToken){
                operatorStack.push(token);
            }
            else if(token instanceof OperatorToken && (token.getContent() != "("&&token.getContent() != ")")){


                OperatorToken o1 = (OperatorToken) token;

                while(!operatorStack.isEmpty()&&operatorStack.peak() instanceof OperatorToken ){
                    OperatorToken o2 = (OperatorToken) operatorStack.peak();
                    if((o1).getPrecedence()<(o2).getPrecedence() ||
                            ( (o1).getPrecedence()==(o2).getPrecedence() &&
                                    (o1).getOperatorType() == OperatorToken.OperatorType.LEFT )){

                         // pop o2 from the operator stack into the output queue
                         // push o1 onto the operator stack

                        outputQueue.add(operatorStack.pop());
                    }else{
                        break;
                    }
                }
                if(!operatorStack.isEmpty() && operatorStack.peak() instanceof FunctionToken){
                    outputQueue.add(operatorStack.pop());
                    if((!operatorStack.isEmpty() && operatorStack.peak() instanceof OperatorToken)&&
                            ((OperatorToken)operatorStack.peak()).getPrecedence()>o1.getPrecedence()){
                        outputQueue.add(operatorStack.pop());
                    }
                }
                operatorStack.push(token);
            }
            else if(token instanceof OperatorToken && token.getContent() == "("){

                operatorStack.push(token);
                parenthesiscount++;

            }
            else if(token instanceof OperatorToken && token.getContent() == ")"){

                if (parenthesiscount <= 0) {
                    throw new Exception("Mismatched paranthesis");
                }

                while(!operatorStack.isEmpty()){
                    Token topOperator = operatorStack.pop();
                    if(topOperator instanceof OperatorToken && topOperator.getContent() == "("){
                        break;
                    }
                    if(topOperator instanceof FunctionToken){
                        outputQueue.add(topOperator);
                    }
                    else{
                        outputQueue.add(topOperator);
                    }
                }

                parenthesiscount--;
            }
        }



        while (!operatorStack.isEmpty()) {
            Token topOperator = operatorStack.pop();

            if (topOperator instanceof OperatorToken && parenthesiscount!=0) {
              throw new Exception("Mismatched paranthesis");
            }

            outputQueue.add(topOperator);
        }
        return this.outputQueue;
    }

    //Matematiksel işlemlerin metodları burada gerçekleşecek. + - * / ^ sin cos
    public static boolean isDifferentOperator(OperatorToken operator){
        return (operator.getOperatorType() == OperatorToken.OperatorType.SUFFIX || operator.getOperatorType() == OperatorToken.OperatorType.PREFIX);
    }
    public static double specialNumber(String number){
        switch (number){
            case "π":
                return Math.PI;
            case "e":
                return Math.exp(1);
            default:
                return 0;
        }
    }
    public static double fact(double x){
        double result=1;
        for(int i=1;i<=x;i++){
            result*=i;
        }
        return result;
    }   //Math'de faktöriyel yok
    public static double basic_operation(String number_1 , String number_2,String operator) {
        double number1;
        double number2;
        if(number_1.equals("e") || number_1.equals("π") ){
            number1 = specialNumber(number_1);
        }else {
            number1 = Double.parseDouble(number_1);
        }
        if(number_2.equals("e") || number_2.equals("π") ){
            number2 = specialNumber(number_2);
        }else {
            number2 = Double.parseDouble(number_2);
        }
        switch (operator){
            case "+":
                return number1+number2;
            case "-":
                return number1-number2;
            case "*":
                return number1*number2;
            case "/":
                return number1/number2;
            case "^":
                return Math.pow(number1,number2);
            default:
                throw new NumberFormatException("İşlemlerde Bir sıkıntı var");
        }
    }

    public static double cmplx_operation(String number_s, String operator) {
        double number_d;
        if(number_s.equals("e") || number_s.equals("π") ){
            number_d = specialNumber(number_s);
        }
        else {
            number_d = Double.parseDouble(number_s);
        }
        switch (operator){
            case "%":
                return number_d/100.0;
            case "!":
                return fact(number_d);
            case "√":
                return Math.sqrt(number_d);
            case "-":
                return number_d *= (-1.0);
        }
        return number_d;
    }

    public static double func_operation(String number_s, String function){
        double number_d;
        if(number_s.equals("e") || number_s.equals("π") ){
            number_d = specialNumber(number_s);
        }
        else {
            number_d = Double.parseDouble(number_s);
        }
        try{
            switch (function){
                case "sin":
                    return Math.sin(number_d);
                case "arcsin":
                    return Math.asin(number_d);
                case "cos":
                    return Math.cos(number_d);
                case "arccos":
                    return Math.acos(number_d);
                case "tan":
                    return Math.tan(number_d);
                case "arctan":
                    return Math.atan(number_d);
                case "cot":
                    return 1/Math.tan(number_d);
                case "arccot":
                    return Math.atan(1/number_d);
                case "log":
                    return Math.log10(number_d);
                case "ln":
                    return Math.log(number_d);
                case "sec":
                    return 1/Math.cos(number_d);
                case "cosec":
                    return 1/Math.sin(number_d);

            }
        } catch (ArithmeticException e) {
            throw new NumberFormatException("İşlemlerde Bir sıkıntı var");
        }
        return number_d;
    }


    //Fonksiyon haline getirip yazmak burada gerçekleşecek. bir iki veya üç bilinmeyenli.

    private int i=0;

    public double function(String X) throws Exception{
        double x = Double.parseDouble(X);
        int index = 0;
        Stack operation = new Stack();
        List<Token> POSTFIX = getPostfixExpression();
        for(Token token : POSTFIX){
            if(token instanceof NumberToken){

                if(token.getContent().length()==1 &&
                        (! (token.getContent().charAt(0)>='a'&&token.getContent().charAt(0)<='z'||
                                token.getContent().charAt(0)>='A'&&token.getContent().charAt(0)<='Z')||
                                        token.getContent().charAt(0) == 'e') && !Character.isDigit(token.getContent().charAt(0))){
                    operation.push(new NumberToken(Double.toString(specialNumber(token.getContent())),true));
                    index++;
                    continue;
                }
                else if(!((NumberToken)token).getIsConstant()){
                    operation.push(new NumberToken(Double.toString(x),true));
                    continue;
                }

                operation.push(token);
            }

            else if(token instanceof OperatorToken //&& operation.getSize()>1
            ){
                if(isDifferentOperator((OperatorToken)token)){  //Operatörün prefix veya suffix olup olmadıgına bakıyor
                    String number = operation.pop().getContent();
                    double currentOperation = cmplx_operation(number,token.getContent());
                    String currentOperation_string = Double.toString(currentOperation);
                    operation.push(new NumberToken(currentOperation_string,true));
                    i++;
                    continue;
                }

                String number_2 = operation.pop().getContent();
                String number_1 = operation.pop().getContent();

                double currentOperation = basic_operation(number_1,number_2,token.getContent());
                String currentOperation_string = Double.toString(currentOperation);

                operation.push(new NumberToken(currentOperation_string,true));
                i++;


            }
            else if(token instanceof FunctionToken){
                String number = operation.pop().getContent();
                double currentOperation = func_operation(number,token.getContent());
                String currentOperation_string = Double.toString(currentOperation);
                operation.push(new NumberToken(currentOperation_string,true));
                index++;
                continue;

            }
            index++;
        }

        return Double.parseDouble(operation.pop().getContent());
    }

    public double function(double x,double y) throws Exception{

        Stack operation = new Stack();
        char[] vars = new char[2];
        int i=0;
        vars[0]='0';
        vars[1]='1';


        for(Token token : getPostfixExpression()){
            boolean flag = true;
            if(i==2){
                flag = false;
            }
            if(token instanceof NumberToken){
                if(!((NumberToken) token).getIsConstant()){
                    for(int j = i;j<2;j++){
                        if(vars[j]==token.getContent().charAt(0)){
                            flag=false;
                            break;
                        }
                    }
                    if(flag){
                        vars[i]=token.getContent().charAt(0);
                        i++;
                    }
                }
            }

        }

        for(Token token : getPostfixExpression()){
            if(token instanceof NumberToken){

                if(token.getContent().length()==1 &&
                        (!(token.getContent().charAt(0)>='a'&&token.getContent().charAt(0)<='z'||
                                token.getContent().charAt(0)>='A'&&token.getContent().charAt(0)<='Z')||
                                token.getContent().charAt(0) == 'e') && !Character.isDigit(token.getContent().charAt(0))){
                    operation.push(new NumberToken(Double.toString(specialNumber(token.getContent())),true));
                    continue;
                }
                else if(!((NumberToken)token).getIsConstant() && token.getContent().charAt(0)==vars[0]){
                    operation.push(new NumberToken(Double.toString(x),true));
                    continue;
                }
                else if(!((NumberToken)token).getIsConstant() && token.getContent().charAt(0)==vars[1]){
                    operation.push(new NumberToken(Double.toString(y),true));
                    continue;
                }

                operation.push(token);
            }

            else if(token instanceof OperatorToken &&//&& ile % ! gibi operatörlerin olmadıgını belirteceğiz. isDifferentOperator ile
                    operation.getSize()>1 ){       //eğer tek bir tane varsa ve - ise negatif yapacak. Burası biraz karışık farklı yol gerekebilir
                if(isDifferentOperator((OperatorToken)token)){
                    String number = operation.pop().getContent();
                    double currentOperation = cmplx_operation(number,token.getContent());
                    String currentOperation_string = Double.toString(currentOperation);
                    operation.push(new NumberToken(currentOperation_string,true));
                    continue;
                }


                String number_2 = operation.pop().getContent();
                String number_1 = operation.pop().getContent();

                double currentOperation = basic_operation(number_1,number_2,token.getContent());
                String currentOperation_string = Double.toString(currentOperation);

                operation.push(new NumberToken(currentOperation_string,true));
                i++;            //TEST **********************************

            }
            else if(token instanceof FunctionToken){
                String number = operation.pop().getContent();
                double currentOperation = func_operation(number,token.getContent());
                String currentOperation_string = Double.toString(currentOperation);
                operation.push(new NumberToken(currentOperation_string,true));
                continue;

            }

        }

        return Double.parseDouble(operation.pop().getContent());
    }

    public double numericalLimit(double x){

        return 0.0;
    }

    public double numericalDerivative(double x){
        return 0.0;
    }

    //Test için Main kısmı
    public static void main(String[] args) throws Exception {

        Tokenize token = new Tokenize("x^2");
        Calculator calculator = new Calculator(token.tokenizedExpression);
        Derivative Turev = new Derivative(calculator.getPostfixExpression());

        Calculator deriv = new Calculator(Turev.DerivativeList);

        System.out.println(deriv.function("15"));


        for(Token e: Turev.DerivativeList){

            System.out.print(e.getContent() + " ");

        }





    }

}