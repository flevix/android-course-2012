package ru.flevix;

import java.util.Stack;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: flevix
 * Date: 12.11.12
 * Time: 20:06
 */

public class Logic {

    static private final char MINUS = '\u2212';
    private StringBuilder tmp = new StringBuilder();
    private String digits = "0123456789." + MINUS, operation = "+-*/" + MINUS;
    private Stack<String> stackOperand = new Stack<String>();
    private Stack<Double> stackDouble = new Stack<Double>();
    private Vector<String> stringVector = new Vector<String>();
    private double x, y;

    CharSequence solve(CharSequence input) {
        stackOperand.clear();
        stringVector.clear();
        stackDouble.clear();
        if (input.length() == 0) return "";
        try{
            //pre inv: correct input
            try{
                infixToPostfix(input);
            } catch (Exception e) {
                return "Error";
            }
            //post inv: correct postfix in stringVector, notNull contract

            //pre inv: correct postfix in stringVector
            postfixToEvaluate();
            //post inv: correct double in stackDouble.peek()
            if (Math.abs(stackDouble.peek() - 0.0000001) < 0.000001) {
                return Double.toString(0);
            } else {
                return Double.toString(stackDouble.peek());
            }
        } catch (Exception e) {
            return "Error";
        }
    }

    private void infixToPostfix(CharSequence inputSt) throws Exception {
        tmp.setLength(0);
        for (int i = 0; i < inputSt.length(); i++) {
            try{
                if (inputSt.charAt(i) == MINUS && !digits.contains(inputSt.subSequence(i + 1, i + 2))) {
                    if (tmp.length() > 0) {
                        stringVector.add(tmp.toString());
                        tmp.setLength(0);
                    }
                    while (!stackOperand.empty() && (stackOperand.peek().equals("*") || stackOperand.peek().equals("/") ||
                    stackOperand.peek().equals("+") || stackOperand.peek().equals("-"))) {
                        stringVector.add(stackOperand.pop());
                    }
                    stackOperand.push(inputSt.subSequence(i, i + 1).toString());
                } else if (digits.contains(inputSt.subSequence(i, i + 1))) {
                    tmp.append(inputSt.charAt(i));
                } else if (inputSt.charAt(i) == '(') {
                    if (tmp.length() > 0) {
                        stringVector.add(tmp.toString());
                        tmp.setLength(0);
                    }
                    stackOperand.push(inputSt.subSequence(i, i + 1).toString());
                } else if (inputSt.charAt(i) == ')') {
                    if (tmp.length() > 0) {
                        stringVector.add(tmp.toString());
                        tmp.setLength(0);
                    }
                    while (!stackOperand.peek().equals("(")) {
                        stringVector.add(stackOperand.pop());
                    }
                    stackOperand.pop();
                } else if (inputSt.charAt(i) == '+' || inputSt.charAt(i) == '-') {
                    if (tmp.length() > 0) {
                        stringVector.add(tmp.toString());
                        tmp.setLength(0);
                    }
                    while (!stackOperand.empty() && (stackOperand.peek().equals("*") || stackOperand.peek().equals("/"))) {
                        stringVector.add(stackOperand.pop());
                    }
                    stackOperand.push(inputSt.subSequence(i, i + 1).toString());
                } else if (inputSt.charAt(i) == '*' || inputSt.charAt(i) == '/') {
                    if (tmp.length() > 0) {
                        stringVector.add(tmp.toString());
                        tmp.setLength(0);
                    }
                    stackOperand.push(inputSt.subSequence(i, i + 1).toString());
                }
            } catch (Exception e) {
                throw e;
            }
        }
        if (tmp.length() > 0) {
            stringVector.add(tmp.toString());
            tmp.setLength(0);
        }
        while (!stackOperand.empty()) {
            if (stackOperand.peek().equals("(") || stackOperand.peek().equals(")")) {
                throw new Exception("Error");
            }
            stringVector.add(stackOperand.pop());
        }
    }

    private void postfixToEvaluate() {
        for (String s : stringVector) {
            try {
                if (s.length() > 1 || !operation.contains(s.subSequence(0,1))) {
                    if (s.charAt(0) == MINUS) s = "-" + s.substring(1);
                    stackDouble.push(Double.parseDouble(s));
                } else {
                    if (s.charAt(0) == MINUS) {
                        x = stackDouble.pop();
                        stackDouble.push(-x);
                    } else {
                        y = stackDouble.pop();
                        x = stackDouble.pop();
                        switch (s.charAt(0)) {
                            case '+':
                                x += y;
                                break;
                            case '-':
                                x -= y;
                                break;
                            case '*':
                                x *= y;
                                break;
                            case '/':
                                //x / 0 == Infinity
                                x /= y;
                                break;
                        }
                        stackDouble.push(x);
                    }
                }
            } catch (Exception e) {
                return;
            }
        }
    }

    public int findPoint(CharSequence charSequence) {
        for (int i = 0; i < charSequence.length(); i++) {
            if (charSequence.charAt(i) == '.') {
                return i;
            }
        }
        return -1;
    }

}
