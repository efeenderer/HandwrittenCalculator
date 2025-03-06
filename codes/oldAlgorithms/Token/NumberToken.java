package com.enderefe.engineeringcalculator.Token;

public class NumberToken extends Token{
    private boolean isConstant;
    public final String name="number";
    public NumberToken(String content, boolean isConstant){
        super(content);
        this.isConstant=isConstant;
    }
    public boolean getIsConstant(){return this.isConstant;}
}
