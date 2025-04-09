package com.enderefe.engineeringcalculator.Token;

public class OperatorToken extends Token{
    public enum OperatorType{PREFIX,SUFFIX,LEFT,RIGHT}


    private OperatorType operatortype_var;
    private int isPrecedence;
    public OperatorToken(String content,OperatorType operatortype_var,int isPrecedence){
        super(content);

        this.operatortype_var=operatortype_var;
        this.isPrecedence=isPrecedence;
    }


    public OperatorType getOperatorType() {
        return operatortype_var;
    }

    public int getPrecedence() {
        return isPrecedence;
    }
}
