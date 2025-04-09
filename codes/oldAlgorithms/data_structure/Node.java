package com.enderefe.engineeringcalculator.data_structure;

import com.enderefe.engineeringcalculator.Token.*;
public class Node {
    private Token nodeToken;
    private Node leftNode = null;
    private Node rightNode = null;

    public Node(Token nodeToken){
        this.nodeToken = nodeToken;
    }
    public Node(Token nodeToken,Node leftNode, Node rightNode){
        this.nodeToken = nodeToken;
        this.rightNode = rightNode;
        this.leftNode = leftNode;
    }
    public boolean hasChildren() {
        return leftNode != null || rightNode != null;
    }
    public boolean hasBothChildren() {
        return leftNode != null && rightNode != null;
    }
    public Token getNodeToken() {
        return nodeToken;
    }
    public Node getLeftNode() {
        return leftNode;
    }
    public Node getRightNode() {
        return rightNode;
    }
    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }
    public void setNodeToken(Token nodeToken){
        this.nodeToken = nodeToken;
    }
    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }
}
