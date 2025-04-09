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


public class Derivative {

    public Node DerivativeTree;
    public List<Token> DerivativeList;
    public String LaTex;
    public Derivative(List<Token> PostFixExpression) throws Exception{
        Node node = buildTree(PostFixExpression);

        this.DerivativeList = new ArrayList<>();
        this.DerivativeTree = BasicSimplification(NumericalSimplification(takeDerivative(node)));

        InOrder(this.DerivativeTree);
        List<Token> DerivativePostfix = LatexConvertion.getPostfixExpression(DerivativeList);
        this.LaTex = LatexConvertion.convertToLaTeX(DerivativePostfix);
    }
    private Node NumericalSimplification(Node node) throws Exception {
        if(node == null){
            return null;
        }
        Node Simplified;
        Node NodeLeft = null;
        NodeLeft = node.getLeftNode();
        Node NodeRight = node.getRightNode();

        if(!variableCheck(node)){
            if (node.getNodeToken() instanceof NumberToken) {
                return node;
            }
            else if (node.getNodeToken() instanceof OperatorToken) {

                if(!Calculator.isDifferentOperator((OperatorToken) node.getNodeToken())){
                    //Parent Node'un her iki tarafı da number ise;
                    if ((NodeRight.getNodeToken() instanceof NumberToken)
                            && (NodeLeft.getNodeToken() instanceof NumberToken)) {
                        String Number1 = NodeLeft.getNodeToken().getContent();
                        String Number2 = NodeRight.getNodeToken().getContent();
                        String OP = node.getNodeToken().getContent();
                        if (Calculator.basic_operation(Number1, Number2, OP) % 1 != 0) {
                            double result = Calculator.basic_operation(Number1, Number2, OP);
                            return new Node(new NumberToken(Double.toString(result), true));
                        } else {
                            int result = (int) Calculator.basic_operation(Number1, Number2, OP);
                            return new Node(new NumberToken(Integer.toString(result), true));
                        }
                    }
                    //Parent Node'un sağ tarafı operatör/function, sol tarafı number ise;
                    else if ((NodeRight.getNodeToken() instanceof OperatorToken || NodeRight.getNodeToken() instanceof FunctionToken)
                            && (NodeLeft.getNodeToken() instanceof NumberToken)) {
                        node.setRightNode(NumericalSimplification(NodeRight));
                        return NumericalSimplification(node);
                    }
                    //Parent Node'un sol tarafı operatör/function, sağ tarafı number ise;
                    else if ((NodeLeft.getNodeToken() instanceof OperatorToken || NodeLeft.getNodeToken() instanceof FunctionToken)
                            && (NodeRight.getNodeToken() instanceof NumberToken)) {
                        node.setLeftNode(NumericalSimplification(NodeLeft));
                        return NumericalSimplification(node);
                    }
                    //Parent Node'un her iki tarafı da Operatör/Function ise;
                    else if ((NodeLeft.getNodeToken() instanceof OperatorToken || NodeLeft.getNodeToken() instanceof FunctionToken)
                            && (NodeRight.getNodeToken() instanceof OperatorToken || NodeRight.getNodeToken() instanceof FunctionToken)) {
                        node.setRightNode(NumericalSimplification(NodeRight));
                        node.setLeftNode(NumericalSimplification(NodeLeft));
                        return NumericalSimplification(node);
                    }
                }
                else{
                    if(NodeRight.getNodeToken() instanceof NumberToken){
                        String Number = NodeRight.getNodeToken().getContent();
                        String Operator = node.getNodeToken().getContent();
                        double result = Calculator.cmplx_operation(Number,Operator);
                        return new Node(new NumberToken(Double.toString(result),true));
                    }
                    else{
                        node.setRightNode(NumericalSimplification(NodeRight));
                        return NumericalSimplification(node);
                    }
                }
            }
            else if(node.getNodeToken() instanceof FunctionToken){
                //Parent Node'un sağ tarafı Number ise
                if(NodeRight.getNodeToken() instanceof NumberToken){
                    String Number = NodeRight.getNodeToken().getContent();
                    String Function = node.getNodeToken().getContent();
                    double result = Calculator.func_operation(Number,Function);
                    return new Node(new NumberToken(Double.toString(result),true));
                }
                else{
                    node.setRightNode(NumericalSimplification(NodeRight));
                    return NumericalSimplification(node);
                }
            }
        }
        else {
            if( variableCheck(NodeLeft) && !variableCheck(NodeRight)){
                node.setRightNode(NumericalSimplification(NodeRight));
            }
            else if(!variableCheck(NodeLeft) && variableCheck(NodeRight)){
                node.setLeftNode(NumericalSimplification(NodeLeft));
            }
            else if(variableCheck(NodeLeft) && variableCheck(NodeRight)){
                node.setLeftNode(NumericalSimplification(NodeLeft));
                node.setRightNode(NumericalSimplification(NodeRight));
            }
        }

        return node;
    }
    private Node BasicSimplification(Node node) throws Exception{
        if(node == null){
            return null;
        }
        Node NodeRight = node.getRightNode();

        if(node.getNodeToken() instanceof OperatorToken){
            if(!Calculator.isDifferentOperator((OperatorToken) node.getNodeToken())){
                Node NodeLeft = node.getLeftNode();
                if (node.getNodeToken().equals(Tokenize.OP_POW)) {
                    if (NodeRight.getNodeToken() instanceof NumberToken) {
                        if (NodeRight.getNodeToken().getContent().equals("1")
                                || NodeRight.getNodeToken().getContent().equals("1.0")) {
                            return NodeLeft;
                        } else if (NodeRight.getNodeToken().getContent().equals("0")) {
                            return new Node(new NumberToken("1", true));
                        }
                    }
                }
                else if (node.getNodeToken().equals(Tokenize.OP_MUL)) {
                    if (NodeRight.getNodeToken() instanceof NumberToken) {
                        if (NodeRight.getNodeToken().getContent().equals("1")
                                || NodeRight.getNodeToken().getContent().equals("1.0")) {
                            return NodeLeft;
                        } else if (NodeRight.getNodeToken().getContent().equals("0")
                                || NodeRight.getNodeToken().getContent().equals("0.0")) {
                            return new Node(new NumberToken("0", true));
                        }
                    }
                    if (NodeLeft.getNodeToken() instanceof NumberToken) {
                        if (NodeLeft.getNodeToken().getContent().equals("1")
                                || NodeLeft.getNodeToken().getContent().equals("1.0")) {
                            return NodeRight;
                        } else if (NodeLeft.getNodeToken().getContent().equals("0")
                                || NodeLeft.getNodeToken().getContent().equals("0.0")) {
                            return new Node(new NumberToken("0", true));
                        }
                    }
                }
                else if (node.getNodeToken().equals(Tokenize.OP_DIV)) {
                    if (NodeRight.getNodeToken() instanceof NumberToken) {
                        if (NodeRight.getNodeToken().getContent().equals("1")
                                || NodeRight.getNodeToken().getContent().equals("1.0")) {
                            return NodeLeft;
                        }
                    }
                }

                if (NodeLeft.getNodeToken() instanceof NumberToken && NodeRight.getNodeToken() instanceof NumberToken
                        && ((NumberToken) NodeLeft.getNodeToken()).getIsConstant() && ((NumberToken) NodeRight.getNodeToken()).getIsConstant()
                        && !Calculator.isDifferentOperator((OperatorToken) node.getNodeToken())) {
                    return new Node(new NumberToken(
                            Double.toString(Calculator.basic_operation(NodeLeft.getNodeToken().getContent(), NodeRight.getNodeToken().getContent(), node.getNodeToken().getContent()))
                            , true));
                }
                node.setRightNode(BasicSimplification(NodeRight));
                node.setLeftNode(BasicSimplification(NodeLeft));
            }
            else{
                if(node.getNodeToken().equals(Tokenize.OP_SQRT)){
                    if (NodeRight.getNodeToken() instanceof NumberToken) {
                        if (NodeRight.getNodeToken().getContent().equals("1")
                                || NodeRight.getNodeToken().getContent().equals("1.0")) {
                            return new Node(new NumberToken("1",true));
                        } else if (NodeRight.getNodeToken().getContent().equals("0")) {
                            return new Node(new NumberToken("0", true));
                        }
                    }
                    else{
                        node.setRightNode(BasicSimplification(NodeRight));
                    }
                }
            }
        }
        else if(node.getNodeToken() instanceof FunctionToken){
            node.setRightNode(BasicSimplification(NodeRight));
        }
        return node;
    }
    private Node buildTree(List<Token> PostFixExpression) {
        java.util.Stack<Node> stack = new Stack<>();
        for (Token token : PostFixExpression) {
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
    private Node takeDerivative(Node node){
        System.out.println("Deneme");
        if(Derivative.variableCheck(node)){
            Node DerivativeNode = null;
            if (!(node.getNodeToken() instanceof NumberToken)) {
                if(node.getNodeToken() instanceof OperatorToken){
                    //Operator ise

                    if(node.getNodeToken().getContent().equals("^")){
                        //Üslü en temel algoritma x^2, x^5 gibi şeyleri çözüyor
                        if(Derivative.variableCheck(node.getLeftNode())&&
                                (!Derivative.variableCheck(node.getRightNode())) ){
                            //Sadece tabanda değişken var ise ve

                            //DerivativeNode'un left kısmı
                            Node LeftNode = new Node(new NumberToken(node.getRightNode().getNodeToken().getContent(),true));

                            //DerivativeNode'un right kısmı
                            double number = Double.parseDouble(node.getRightNode().getNodeToken().getContent()); // üssün değerini double a çevirdim
                            number--;
                            String result;
                            if(number%1 == 0) { // Eğer değer tam sayı ise gereksiz noktalara ihtiyaç yok
                                result = Integer.toString((int)number); // sonucu tekrar string yaptım ki content olarak girebileyim.
                                }
                            else{
                                result = Double.toString(number);
                            }

                            Node RightNode = new Node(Tokenize.OP_POW,
                                    node.getLeftNode(), // x
                                    new Node(new NumberToken(result,true)));
                             if(node.getLeftNode().getNodeToken() instanceof OperatorToken
                             || node.getLeftNode().getNodeToken() instanceof FunctionToken) {

                                 Node RightRightNode = takeDerivative(node.getLeftNode());

                                 RightNode = new Node(Tokenize.OP_MUL, RightNode, RightRightNode);
                             }

                            DerivativeNode = new Node(Tokenize.OP_MUL,LeftNode,RightNode);
                            return DerivativeNode;
                        }
                        else if(!Derivative.variableCheck(node.getLeftNode())&&
                                (Derivative.variableCheck(node.getRightNode()))){
                            DerivativeNode = new Node(Tokenize.OP_MUL);
                            DerivativeNode.setLeftNode(node);

                            Node RightNode = new Node(Tokenize.LN,null,node.getLeftNode());
                            if(!(node.getRightNode().getNodeToken() instanceof NumberToken)){
                                RightNode = new Node(Tokenize.OP_MUL,
                                        new Node(Tokenize.LN,null,node.getLeftNode()),
                                        takeDerivative(node.getRightNode()));
                            }
                            DerivativeNode.setRightNode(RightNode);
                            return DerivativeNode;
                        }
                    }
                    else if(node.getNodeToken().getContent().equals("*")){
                        //çarpma
                        if( Derivative.variableCheck(node.getRightNode())&&!Derivative.variableCheck(node.getLeftNode()) ){
                            if(  node.getRightNode().getNodeToken() instanceof NumberToken
                                    &&!((NumberToken) node.getRightNode().getNodeToken()).getIsConstant() ){
                                return (node.getLeftNode());
                            }
                            else if(node.getRightNode().getNodeToken() instanceof OperatorToken
                            || node.getRightNode().getNodeToken() instanceof FunctionToken){
                                return new Node(Tokenize.OP_MUL, node.getLeftNode(),takeDerivative(node.getRightNode()));
                            }
                        }
                        else if(  !Derivative.variableCheck(node.getRightNode())&&Derivative.variableCheck(node.getLeftNode())  ){
                            if(  node.getLeftNode().getNodeToken() instanceof NumberToken
                                    &&!((NumberToken) node.getLeftNode().getNodeToken()).getIsConstant() ){
                                return (node.getRightNode());
                            }
                            else if(node.getLeftNode().getNodeToken() instanceof OperatorToken
                            ||node.getLeftNode().getNodeToken() instanceof FunctionToken){
                                return new Node(Tokenize.OP_MUL
                                        ,takeDerivative(node.getLeftNode())
                                        ,node.getRightNode());
                            }
                        }
                        else if( Derivative.variableCheck(node.getRightNode())&& Derivative.variableCheck(node.getLeftNode()) ){
                            Node LeftNode = new Node(Tokenize.OP_MUL,takeDerivative(node.getLeftNode()),node.getRightNode());
                            Node RightNode = new Node(Tokenize.OP_MUL,node.getLeftNode(),takeDerivative(node.getRightNode()));
                            return new Node(Tokenize.OP_ADD,LeftNode,RightNode);
                        }
                    }
                    else if(node.getNodeToken().getContent().equals("+")){
                        return new Node(node.getNodeToken()
                                ,takeDerivative(node.getLeftNode())
                                ,takeDerivative(node.getRightNode()));
                    }
                    else if( node.getNodeToken().getContent().equals("-") && node.hasBothChildren() ){
                        return new Node(node.getNodeToken()
                                ,takeDerivative(node.getLeftNode())
                                ,takeDerivative(node.getRightNode()));
                    }
                    else if( node.getNodeToken().getContent().equals("-") && !node.hasBothChildren() ){
                        return new Node(node.getNodeToken()
                                ,null
                                ,takeDerivative(node.getRightNode()));
                    }
                    else if(node.getNodeToken().getContent().equals("/")){
                        if( Derivative.variableCheck(node.getRightNode())&& !Derivative.variableCheck(node.getLeftNode()) ){
                            Node LeftNode = null;
                            Node RightNode = takeDerivative(node.getRightNode());
                            if(node.getRightNode().getNodeToken().getContent().equals("^")){ //işler karıştı :)
                                LeftNode = new Node(Tokenize.OP_DIV);
                                Node LeftRightNode = new Node(Tokenize.OP_POW);
                                Node LeftLeftNode = new Node(Tokenize.OP_MUL);
                                Node LeftLeftLeftNode = new Node(Tokenize.OP_NEGATIVE);

                                double PowerOfDenominator = Double.parseDouble(node.getRightNode().getRightNode().getNodeToken().getContent());
                                PowerOfDenominator++;
                                String POD;
                                if(PowerOfDenominator%1 == 0) {     //Eğer değer 3.5 gibi bir şey değilse gereksiz noktaları yok etmek için kontrol yapılıyor
                                    POD = Integer.toString((int) PowerOfDenominator);
                                }
                                else {
                                   POD = Double.toString(PowerOfDenominator);
                                }

                                NumberToken LeftLeftLeftRight = new NumberToken(node.getRightNode().getRightNode().getNodeToken().getContent(),true);
                                LeftLeftLeftNode.setRightNode(new Node(LeftLeftLeftRight));

                                LeftLeftNode.setLeftNode(LeftLeftLeftNode);
                                LeftLeftNode.setRightNode(node.getLeftNode());


                                LeftRightNode.setRightNode(new Node(new NumberToken(POD,true)));
                                LeftRightNode.setLeftNode(node.getRightNode().getLeftNode());


                                LeftNode.setRightNode(LeftRightNode);
                                LeftNode.setLeftNode(LeftLeftNode);

                                RightNode = takeDerivative(node.getRightNode().getLeftNode());
                            }else{
                                LeftNode = new Node(Tokenize.OP_DIV);

                                Node LeftLeftNode = new Node(Tokenize.OP_NEGATIVE);
                                Node LeftLeftRightNode = node.getLeftNode();
                                LeftLeftNode.setRightNode(LeftLeftRightNode);



                                Node LeftRightNode = new Node(Tokenize.OP_POW,null,new Node(new NumberToken("2",true)));
                                LeftRightNode.setLeftNode(node.getRightNode());

                                LeftNode.setRightNode(LeftRightNode);
                                LeftNode.setLeftNode(LeftLeftNode);

                            }



                            return new Node(Tokenize.OP_MUL,LeftNode,RightNode);

                        }
                        else if(  !Derivative.variableCheck(node.getRightNode())&&Derivative.variableCheck(node.getLeftNode())  ){
                            if(  node.getLeftNode().getNodeToken() instanceof NumberToken){
                                return (node.getRightNode());
                            }
                            else if(node.getLeftNode().getNodeToken() instanceof OperatorToken
                            || node.getLeftNode().getNodeToken() instanceof FunctionToken){
                                return new Node(Tokenize.OP_DIV
                                        ,takeDerivative(node.getLeftNode())
                                        ,node.getRightNode());
                            }
                        }
                        else if( Derivative.variableCheck(node.getRightNode())&& Derivative.variableCheck(node.getLeftNode()) ){
                            Node F = node.getLeftNode();
                            Node G = node.getRightNode();

                            Node DeltaF = takeDerivative(F);
                            Node DeltaG = takeDerivative(G);

                            Node LeftLeftNode = new Node(Tokenize.OP_MUL,DeltaF,G);
                            Node LeftRightNode = new Node(Tokenize.OP_MUL,F,DeltaG);
                            Node LeftNode = new Node(Tokenize.OP_SUB,LeftLeftNode,LeftRightNode);

                            Node RightNode = new Node(Tokenize.OP_POW,G,new Node(new NumberToken("2",true)));

                            return new Node(Tokenize.OP_DIV, LeftNode,RightNode);
                        }
                    }
                    else if(node.getNodeToken().equals(Tokenize.OP_SQRT)){

                        DerivativeNode = new Node(Tokenize.OP_DIV,
                                null,
                                new Node(Tokenize.OP_MUL,new Node(new NumberToken("2",true)),node));
                        if(node.getRightNode().getNodeToken() instanceof OperatorToken
                        ||node.getRightNode().getNodeToken() instanceof FunctionToken){
                            DerivativeNode.setLeftNode(takeDerivative(node.getRightNode()));
                            return DerivativeNode;
                        }
                        DerivativeNode.setLeftNode(new Node(new NumberToken("1",true)));

                        return DerivativeNode;
                    }
                }
                else if(node.getNodeToken() instanceof FunctionToken){
                    if(variableCheck(node)){

                            if (node.getNodeToken().equals(Tokenize.SIN)){
                                DerivativeNode = new Node(Tokenize.COS,null,node.getRightNode());

                                if (node.getRightNode().getNodeToken() instanceof OperatorToken
                                 ||node.getRightNode().getNodeToken() instanceof FunctionToken) {
                                    return new Node(Tokenize.OP_MUL,DerivativeNode,takeDerivative(node.getRightNode()));
                                }
                                return DerivativeNode;
                            }
                            else if (node.getNodeToken().equals(Tokenize.COS)){
                                DerivativeNode = new Node(Tokenize.OP_NEGATIVE,null,new Node(Tokenize.SIN,null,node.getRightNode()));
                                if (node.getRightNode().getNodeToken() instanceof OperatorToken
                                        ||node.getRightNode().getNodeToken() instanceof FunctionToken) {
                                    return new Node(Tokenize.OP_MUL,DerivativeNode,takeDerivative(node.getRightNode()));
                                }
                                return DerivativeNode;
                            }
                            else if (node.getNodeToken().equals(Tokenize.TAN)){
                                DerivativeNode = new Node(Tokenize.OP_ADD);
                                Node LeftNode = new Node(new NumberToken("1",true));
                                Node RightNode = new Node(Tokenize.OP_POW, node, new Node(new NumberToken("2",true)));
                                DerivativeNode.setRightNode(RightNode);
                                DerivativeNode.setLeftNode(LeftNode);
                                if (node.getRightNode().getNodeToken() instanceof OperatorToken
                                        ||node.getRightNode().getNodeToken() instanceof FunctionToken) {
                                    return new Node(Tokenize.OP_MUL,DerivativeNode,takeDerivative(node.getRightNode()));
                                }
                                return DerivativeNode;
                            }
                            else if (node.getNodeToken().equals(Tokenize.COT)){

                                Node DerivativeNodeRight = new Node(Tokenize.OP_ADD);
                                Node LeftNode = new Node(new NumberToken("1",true));
                                Node RightNode = new Node(Tokenize.OP_POW, node, new Node(new NumberToken("2",true)));
                                DerivativeNodeRight.setRightNode(RightNode);
                                DerivativeNodeRight.setLeftNode(LeftNode);
                                DerivativeNode = new Node(Tokenize.OP_NEGATIVE, null, DerivativeNodeRight);
                                if (node.getRightNode().getNodeToken() instanceof OperatorToken
                                        ||node.getRightNode().getNodeToken() instanceof FunctionToken) {
                                    return new Node(Tokenize.OP_MUL,DerivativeNode,takeDerivative(node.getRightNode()));
                                }
                                return DerivativeNode;
                            }
                            else if(node.getNodeToken().equals(Tokenize.LN)){
                                DerivativeNode = new Node(Tokenize.OP_DIV,new Node(new NumberToken("1",true)), node.getRightNode());
                                if (node.getRightNode().getNodeToken() instanceof OperatorToken
                                        ||node.getRightNode().getNodeToken() instanceof FunctionToken) {
                                    return new Node(Tokenize.OP_MUL,DerivativeNode,takeDerivative(node.getRightNode()));
                                }
                                return DerivativeNode;
                            }
                            else if(node.getNodeToken().equals(Tokenize.LOG)){
                                DerivativeNode = new Node(Tokenize.OP_DIV,new Node(new NumberToken("1",true)), null);
                                Node RightNode = new Node(Tokenize.OP_MUL,
                                        new Node(Tokenize.LN, null,new Node(new NumberToken("10",true))),
                                        node.getRightNode());
                                DerivativeNode.setRightNode(RightNode);
                                if (node.getRightNode().getNodeToken() instanceof OperatorToken
                                        ||node.getRightNode().getNodeToken() instanceof FunctionToken) {
                                    return new Node(Tokenize.OP_MUL,DerivativeNode,takeDerivative(node.getRightNode()));
                                }
                                return DerivativeNode;
                            }
                            else if(node.getNodeToken().equals(Tokenize.ARCSIN)){
                                DerivativeNode = new Node(Tokenize.OP_DIV);

                                Node RightRightNode = new Node(Tokenize.OP_SUB,
                                        new Node(new NumberToken("1",true)),
                                        new Node(Tokenize.OP_POW,node.getRightNode(),new Node(new NumberToken("2",true))));

                                Node RightNode = new Node(Tokenize.OP_SQRT,
                                        null,
                                        RightRightNode);
                                Node LeftNode = new Node( new NumberToken("1",true));
                                if(!(node.getRightNode().getNodeToken() instanceof NumberToken)){
                                    LeftNode = takeDerivative(node.getRightNode());
                                }
                                DerivativeNode.setLeftNode(LeftNode);
                                DerivativeNode.setRightNode(RightNode);
                                return DerivativeNode;
                            }
                            else if(node.getNodeToken().equals(Tokenize.ARCCOS)){
                                DerivativeNode = new Node(Tokenize.OP_DIV);

                                Node RightRightNode = new Node(Tokenize.OP_SUB,
                                        new Node(new NumberToken("1",true)),
                                        new Node(Tokenize.OP_POW,node.getRightNode(),new Node(new NumberToken("2",true))));

                                Node RightNode = new Node(Tokenize.OP_SQRT,
                                        null,
                                        RightRightNode);
                                Node LeftNode = new Node(Tokenize.OP_NEGATIVE, null ,new Node(new NumberToken("1",true)));
                                if(!(node.getRightNode().getNodeToken() instanceof NumberToken)){
                                    LeftNode.setRightNode(takeDerivative(node.getRightNode()));
                                }
                                DerivativeNode.setLeftNode(LeftNode);
                                DerivativeNode.setRightNode(RightNode);
                                return DerivativeNode;
                            }
                            else if(node.getNodeToken().equals(Tokenize.ARCTAN)){
                                DerivativeNode = new Node(Tokenize.OP_DIV);

                                Node RightNode = new Node(Tokenize.OP_ADD,
                                        new Node(new NumberToken("1",true)),
                                        new Node(Tokenize.OP_POW,node.getRightNode(),new Node(new NumberToken("2",true))));

                                Node LeftNode = new Node( new NumberToken("1",true));
                                if(!(node.getRightNode().getNodeToken() instanceof NumberToken)){
                                    LeftNode = takeDerivative(node.getRightNode());
                                }
                                DerivativeNode.setLeftNode(LeftNode);
                                DerivativeNode.setRightNode(RightNode);
                                return DerivativeNode;
                            }
                            else if(node.getNodeToken().equals(Tokenize.ARCCOT)){
                                DerivativeNode = new Node(Tokenize.OP_DIV);

                                Node RightNode = new Node(Tokenize.OP_ADD,
                                        new Node(new NumberToken("1",true)),
                                        new Node(Tokenize.OP_POW,node.getRightNode(),new Node(new NumberToken("2",true))));

                                Node LeftNode = new Node(Tokenize.OP_NEGATIVE, null ,new Node(new NumberToken("1",true)));
                                if(!(node.getRightNode().getNodeToken() instanceof NumberToken)){
                                    LeftNode.setRightNode(takeDerivative(node.getRightNode()));
                                }
                                DerivativeNode.setLeftNode(LeftNode);
                                DerivativeNode.setRightNode(RightNode);
                                return DerivativeNode;
                            }
                            else if(node.getNodeToken().equals(Tokenize.SEC)){
                                Node RightNode = new Node(Tokenize.TAN,null,node.getRightNode());
                                Node LeftNode  = new Node(Tokenize.SEC,null,node.getRightNode());

                                if(!(node.getRightNode().getNodeToken() instanceof NumberToken)){
                                    RightNode = new Node(Tokenize.OP_MUL,LeftNode,new Node(Tokenize.TAN,null,node.getRightNode()));
                                    LeftNode = takeDerivative(node.getRightNode());
                                }
                                DerivativeNode = new Node(Tokenize.OP_MUL,LeftNode,RightNode);
                                return DerivativeNode;
                            }
                            else if(node.getNodeToken().equals(Tokenize.COSEC)){
                                Node RightNode = new Node(Tokenize.COT,null,node.getRightNode());
                                Node LeftNode  = new Node(Tokenize.COSEC,null,node.getRightNode());

                                if(!(node.getRightNode().getNodeToken() instanceof NumberToken)){
                                    RightNode = new Node(Tokenize.OP_MUL,LeftNode,new Node(Tokenize.TAN,null,node.getRightNode()));
                                    LeftNode = takeDerivative(node.getRightNode());
                                }
                                DerivativeNode = new Node(Tokenize.OP_MUL,LeftNode,RightNode);
                                return new Node(Tokenize.OP_NEGATIVE,null,DerivativeNode);
                            }
                    }
                }
            }
            else if ((node.getNodeToken() instanceof NumberToken) &&
                    !((NumberToken) node.getNodeToken()).getIsConstant()) {
                return new Node(new NumberToken("1", true));
                }//Eğer ağacın kökü (root'u) Number ise ve Constant değil ise (değişkense)
                //Yaptığım ağaç oluşturucu sayıları ve değişkenleri mutlaka en altta topluyor
                //Dolayısıyla en üstte Number varken sol veya sağ çocukta bir şey olamaz
            }

        return new Node(new NumberToken("0",true));
    }
    private static boolean variableCheck(Node node){
        if(node != null){
            if("x".equals(node.getNodeToken().getContent())){
                return true;
            }
            else if(variableCheck(node.getRightNode())){
                return true;
            }
            else if(variableCheck(node.getLeftNode())){
                return true;
            }
        }
        return false;
    }

    private void InOrder(Node node){
        if(node == null){
            return;
        }
        if(node.getNodeToken() instanceof FunctionToken){
            DerivativeList.add(Tokenize.OP_LPAR);
        }
        if(node.hasBothChildren() && node.getNodeToken()instanceof OperatorToken){
            DerivativeList.add(Tokenize.OP_LPAR);
        }
        InOrder(node.getLeftNode());
        DerivativeList.add((Token)node.getNodeToken());
        InOrder(node.getRightNode());

        if(node.hasBothChildren() && node.getNodeToken()instanceof OperatorToken){
            DerivativeList.add(Tokenize.OP_RPAR);
        }
        if(node.getNodeToken() instanceof FunctionToken){
            DerivativeList.add(Tokenize.OP_RPAR);
        }
    }


}
