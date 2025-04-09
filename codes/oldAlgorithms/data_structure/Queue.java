package com.enderefe.engineeringcalculator.data_structure;

import com.enderefe.engineeringcalculator.Token.*;
import java.util.*;

public class Queue {
    private int MAXSIZE = 50;
    private Token[] elements = new Token[MAXSIZE];
    private int size;
    private String systemMessage;

    public Queue() {
        this.size = 0;
    }

    // Kuyruk boş mu kontrolü
    public boolean isEmpty() {
        return size == 0;
    }

    // Kuyruk dolu mu kontrolü
    public boolean isFull() {
        return size == MAXSIZE;
    }

    // Kuyruğa eleman ekleme
    public void enqueue(Token token) {
        if (!isFull()) {
            elements[size] = token;
            size++;
        } else {
            systemMessage = "The Queue is FULL!";
        }
    }

    // Kuyruktan eleman çıkarma
    public Token dequeue() {
        if (!isEmpty()) {
            Token memory = elements[0];
            // Kuyruktaki diğer elemanları kaydırma
            for (int i = 0; i < size - 1; i++) {
                elements[i] = elements[i + 1];
            }
            size--;
            return memory;
        } else {
            systemMessage = "The Queue is EMPTY!";
            return null;
        }
    }

    // Kuyruğun başındaki elemanı alma
    public Token front() {
        if (!isEmpty()) {
            return elements[0];
        } else {
            systemMessage = "The Queue is EMPTY!";
            return null;
        }
    }

    // Kuyruğun boyutunu alma
    public int getSize() {
        return size;
    }

    public List<Token> toList() {
        List<Token> tokenList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            tokenList.add(elements[i]);
        }
        return tokenList;
    }

    // Hata mesajını alma
    public String getError() {
        return systemMessage;
    }

}

