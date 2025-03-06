package com.enderefe.engineeringcalculator.data_structure;

import com.enderefe.engineeringcalculator.Token.*;


public class Stack{

    private int MAXSIZE=50;
    private Token[] Elements = new Token[MAXSIZE];
    private int size;
    private String SystemMessage;

    public Stack(){
        this.size = 0;
    }

    //Empty-Full kontrolcüleri
    public boolean isEmpty(){
        if(size==0) return true;
        else return false;
    }
    public boolean isFull(){
        if(size==MAXSIZE)   return true;
        else return false;
    }

    //Eleman ekleme-çıkarma-okuma işlemleri

    public void push(Token token){
        if(!isFull()){
            Elements[size] = token;
            size++;
        }
        else this.SystemMessage="The Stack is FULL!";
    }

    public Token pop(){
        if(!isEmpty()){
            Token memory = Elements[size-1];
            size--;
            return memory;
        }
        else{
            this.SystemMessage="The Stack is EMPTY!";
            return null;
        }
    }

    public Token peak(){
        if(!isEmpty()){
            return Elements[size-1];
        }
        else{
            this.SystemMessage="The Stack is EMPTY!";
            return null;
        }
    }
    //Getter
    public int getSize() {
        return size;
    }
    public String getError(){
        return SystemMessage;
    }

    //Print
    public void printStack(){
        if(!this.isEmpty()){
            for (int i = 0; i < this.size; i++) {
                System.out.print(Elements[i].getContent() + "   ");
            }
            System.out.print("\n");
        }
    }


}
