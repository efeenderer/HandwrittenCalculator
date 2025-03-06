package com.enderefe.engineeringcalculator;
import com.enderefe.engineeringcalculator.Token.*;
import com.enderefe.engineeringcalculator.data_structure.*;
import com.enderefe.engineeringcalculator.data_structure.Queue;
import com.enderefe.engineeringcalculator.data_structure.Stack;

import java.util.*;
public class Deneme {
    private List<Token> outputQueue;
    private Stack operatorStack;
    private List<Token> Problem;

    public Deneme()throws Exception{
        for (Token token : Problem) {
            if (token instanceof NumberToken) {
                outputQueue.add(token);
            } else if (token instanceof FunctionToken) {
                operatorStack.push(token);
            } else if (token instanceof OperatorToken) {
                OperatorToken o1 = (OperatorToken) token;

                while (!operatorStack.isEmpty() && operatorStack.peak() instanceof OperatorToken) {
                    OperatorToken o2 = (OperatorToken) operatorStack.peak();

                    if ((o1.getPrecedence() < o2.getPrecedence()) ||
                            (o1.getPrecedence() == o2.getPrecedence() && o1.getOperatorType() == OperatorToken.OperatorType.LEFT)) {
                        outputQueue.add(operatorStack.pop());
                    } else {
                        break;
                    }
                }

                operatorStack.push(token);
            } else if (token instanceof OperatorToken && token.getContent() == "(") {
                operatorStack.push(token);
            } else if (token instanceof OperatorToken && token.getContent() == ")") {
                boolean leftParenthesisFound = false;

                while (!operatorStack.isEmpty()) {
                    Token topOperator = operatorStack.pop();

                    if (topOperator instanceof OperatorToken && topOperator.getContent() == "(") {
                        leftParenthesisFound = true;
                        break;
                    } else {
                        outputQueue.add(topOperator);
                    }
                }

                if (!leftParenthesisFound) {
                    throw new Exception("There is a mismatched parenthesis!");
                }

                if (!operatorStack.isEmpty() && operatorStack.peak() instanceof FunctionToken) {
                    outputQueue.add(operatorStack.pop());
                }
            }
        }
    }

}
