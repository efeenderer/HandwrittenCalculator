package com.enderefe.engineeringcalculator;
import android.graphics.Path;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.enderefe.engineeringcalculator.Token.*;
import com.enderefe.engineeringcalculator.data_structure.*;
import com.enderefe.engineeringcalculator.data_structure.Queue;

import java.lang.*;
import java.math.BigInteger;
import java.nio.DoubleBuffer;
import java.util.*;
import java.util.Stack;

public class LatexConvertion {

    public String LaTeX;
    private List<Token> PostFixExpression;

    public LatexConvertion(List<Token> Expression)throws Exception{
        this.PostFixExpression = new ArrayList<>();
        this.PostFixExpression = getPostfixExpression(Expression);
        this.LaTeX = convertToLaTeX(this.PostFixExpression);
    }
    public static List<Token> getPostfixExpression(List<Token> Problem) throws Exception {
        List<Token> outputQueue = new ArrayList<>();
        com.enderefe.engineeringcalculator.data_structure.Stack operatorStack = new com.enderefe.engineeringcalculator.data_structure.Stack();
        int parenthesiscount=0;
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
                   throw new Exception();
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
                throw new Exception();
            }

            outputQueue.add(topOperator);
        }
        return outputQueue;
    }

    public static String convertToLaTeX(List<Token> postfixExpression) throws Exception {
        Stack<String> stack = new Stack<>();

        for (Token token : postfixExpression) {
            if (token instanceof NumberToken) {
                if(token.getContent().equals("π")){
                    stack.push("\\pi");
                }
                else{
                    stack.push(token.getContent());
                }
            } else if (token instanceof OperatorToken) {
                if (Calculator.isDifferentOperator((OperatorToken)token)) {
                    if(token.getContent().equals("-")) {
                        String operand = stack.pop();
                        String latexExpression = "-" + operand;
                        stack.push(latexExpression);
                    }
                    else if(token.equals(Tokenize.OP_SQRT)){
                        String operand = stack.pop();
                        String latexExpression = "\\sqrt{" + operand+"}";
                        stack.push(latexExpression);
                    }
                } else {
                    String operand2 = stack.pop();
                    String operand1 = stack.pop();
                    String latexExpression = getOperatorLaTeX(token.getContent(), operand1, operand2);
                    stack.push(latexExpression);
                }
            } else if (token instanceof FunctionToken) {
                String argument = stack.pop();
                String latexExpression = getFunctionLaTeX(token.getContent(), argument);
                stack.push(latexExpression);
            }
        }
        if (stack.size() != 1) {
            throw new StackOverflowError();
        }
        return stack.pop();
    }

    private static String getFunctionLaTeX(String function, String argument) {
        switch (function) {
            case "sin":
                return "\\sin(" + argument + ")";
            case "cos":
                return "\\cos(" + argument + ")";
            case "ln":
                return "\\ln(" + argument + ")";
            case "tan":
                return "\\tan(" + argument + ")";
            case "arcsin":
                return "\\arcsin("+argument+")";
            case "arccos":
                return "\\text{arccos}("+argument+")";
            case "cot":
                return "\\text{cot}("+argument+")";
            case "arctan":
                return "\\arctan("+argument+")";
            case "arccot":
                return "\\text{arccot}("+argument+")";
            case "sec":
                return "\\sec("+argument+")";
            case "cosec":
                return "\\text{cosec}("+argument+")";
            default:
                return "\\text{UnknownFunction}(" + argument + ")";
        }
    }
    private static String getOperatorLaTeX(String operator, String operand1, String operand2) {
        switch (operator) {
            case "+":
                return operand1 + "+" + operand2;
            case "-":
                return operand1 + "-" + operand2;
            case "*":
                return operand1 + "\\cdot " + operand2;
            case "/":
                return "\\frac{" + operand1 + "}{" + operand2 + "}";
            case "^":
                return "{" + operand1 + "}^{" + operand2 + "}";
            default:
                return null;
        }
    }
}
