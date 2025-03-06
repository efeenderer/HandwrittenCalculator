package com.enderefe.engineeringcalculator.data_structure;

import com.enderefe.engineeringcalculator.Calculator;
import com.enderefe.engineeringcalculator.Token.*;
import com.enderefe.engineeringcalculator.Tokenize;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Tree{
    public Node root;
    Node currentNode;
    List<Token> postfixExpression;
    private int i;
    public Tree(List<Token> postfixExpression) {
        this.root = buildTree(postfixExpression);
    }
    private Node buildTree(List<Token> postfixExpression) {
        Stack<Node> stack = new Stack<>();
        for (Token token : postfixExpression) {
            Node newNode = new Node(token);
            if (token instanceof NumberToken) {
                stack.push(newNode);
            } else if (token instanceof OperatorToken && !stack.isEmpty()) {
                if(Calculator.isDifferentOperator((OperatorToken) token)){
                    Node rightChild = stack.pop();
                    newNode.setRightNode(rightChild);
                    stack.push(newNode);
                }

                else {
                    Node rightChild = stack.pop();
                    Node leftChild = stack.pop();

                    newNode.setRightNode(rightChild);
                    newNode.setLeftNode(leftChild);

                    stack.push(newNode);
                }
            }
            else if (token instanceof FunctionToken &&!stack.isEmpty()) {
                Node functionNode = stack.pop();
                newNode.setRightNode(functionNode);
                stack.push(newNode);
            }
        }

        return stack.pop();
    }
    public void inorder(){
        Tree.inorder(this.root);
    }
    public static void inorder(Node node) {
        if (node == null) {
            return;
        }
        if (node.hasChildren()) {
            System.out.print("(");
        }
        // Sol alt ağacı ziyaret et
        inorder(node.getLeftNode());

        // Geçerli düğümü işle (örneğin yazdır)
        System.out.print(node.getNodeToken().getContent() + "");

        // Sağ alt ağacı ziyaret et
        inorder(node.getRightNode());

        if (node.hasChildren()) {
            System.out.print(")");
        }
    }
}
