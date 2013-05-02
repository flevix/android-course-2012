package ru.flevix;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyCalc extends Activity implements OnClickListener {
    /**
     * Called when the activity is first created.
     */
    static private final char MINUS = '\u2212';
    Logic logic = new Logic();
    TextView textView1, textView0;
    Button  zero, one, two, three, four, five, six, seven,
            eight, nine, add, subtract, multiply, division, allClear, clear,
            equals, point, delete, minus, leftBracket, rightBracket, button;
    //String acceptedChar = "0123456789." + MINUS;
    String operation = "+-/*";
    String digits = "0123456789";
    int maxIntPart = 10, maxRealPart = 10, x;
    boolean solved = false;
    CharSequence txt, txtRes;
    int bracketCount = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }

    @Override
    public void onClick(View view) {
        //contract: view is it button always
        button = (Button) view;
        txt = textView1.getText();
        txtRes = textView0.getText();
        if (txt.length() > 0) {
            if (logic.findPoint(txt) == -1 && txt.length() == maxIntPart) {
                if (digits.contains(button.getText())) {
                    return;
                }
            }
            if (logic.findPoint(txt) >= 0 && txt.length() - logic.findPoint(txt) == maxRealPart) {
                if (digits.contains(button.getText())) {
                    return;
                }
            }
        }
        if (txtRes.length() > 0 && txtRes.charAt(txtRes.length() - 1) == ')') { //so bad code
            x = button.getId();
            if (!(x == R.id.clear || x == R.id.allClear || x == R.id.add || x == R.id.subtract ||
                    x == R.id.multiply || x == R.id.division || x == R.id.equal || x == R.id.rightBracket)) {
                return; //can be (x)y, it's bad
            }
        }
        if (txt.length() == 1 && txt.charAt(0) == MINUS) {
            x = button.getId();
            if (x == R.id.rightBracket || x == R.id.add || x == R.id.subtract ||
                    x == R.id.multiply || x == R.id.division || x == R.id.point || x == R.id.equal) {
                return;
            }
            if (x == R.id.leftBracket) {
                if (solved) {
                    textView0.setText("");
                    solved = false;
                }
                textView0.setText(txtRes + Character.toString(MINUS) + "(");
                textView1.setText("");
                bracketCount++;
                return;
            }
        }
        switch (view.getId()) {
            case R.id.clear: {
                textView1.setText("");
                txt = "";
                break;
            }
            case R.id.allClear: {
                textView0.setText("");
                textView1.setText("");
                txt = "";
                txtRes = "";
                bracketCount = 0;
                break;
            }
            case R.id.delete: {
                if (txt.length() > 0) {
                    if (txt.length() == 2) {
                        if (txt.charAt(0) == MINUS) {
                            textView1.setText("");
                        } else {
                            textView1.setText(txt.subSequence(0, txt.length() - 1));
                        }
                    } else {
                        textView1.setText(txt.subSequence(0, txt.length() - 1));
                    }
                }
                break;
            }
            case R.id.minus: {
                if (txt.length() > 0) {
                    if (txt.charAt(0) != MINUS) {
                        textView1.setText(Character.toString(MINUS) + txt);
                    } else {
                        textView1.setText(txt.subSequence(1, txt.length()));
                    }
                }
                if (txt.length() == 0) { //if it is unary
                    if (txtRes.length() > 0) {
                        if (txtRes.charAt(txtRes.length() - 1) == ')' || txtRes.charAt(txtRes.length() - 1) == '(') {
                            textView1.setText(Character.toString(MINUS));
                        }
                    } else {
                        textView1.setText(Character.toString(MINUS));
                    }
                }
                break;
            }
            case R.id.zero: {
                if (txt.length() == 0 || (txt.length() == 1 && txt.charAt(0) != '0')) {
                    textView1.append("0");
                } else if (logic.findPoint(txt) != -1) {
                    textView1.append("0");
                } else if (txt.length() > 1) {
                    textView1.append("0");
                }
                break;
            }
            case R.id.point: {
                if (txt.length() > 0 && logic.findPoint(txt) == -1) {
                    textView1.append(".");
                }
                break;
            }
            case R.id.equal: {
                if (txt.length() > 0) {
                    if (txt.charAt(0) == MINUS && txt.length() > 1) txt = "(" + txt + ")";
                    if (solved) {
                        textView0.setText(txt);
                    } else {
                        textView0.append(txt);
                    }
                    textView1.setText("");
                }
                textView0.setText(logic.solve(textView0.getText()));
                bracketCount = 0;
                solved = true;
                break;
            }
            case R.id.add: {
                //down
            }
            case R.id.subtract: {
                //down
            }
            case R.id.multiply: {
                //down
            }
            case R.id.division: {
                //this will be realisation
                if (txtRes.length() > 0 && txt.length() == 0) { //change operation
                    if (operation.contains(txtRes.subSequence(txtRes.length() - 1, txtRes.length()))) {
                        if (solved) {
                            solved = false;
                        }
                        textView0.setText(txtRes.subSequence(0, txtRes.length() - 1) + button.getText().toString());
                    }
                }
                if (txt.length() > 0) {
                    if (solved) {
                        textView0.setText("");
                        solved = false;
                    }
                    if (txt.charAt(0) == MINUS && txt.length() > 1) txt = "(" + txt + ")";
                    textView0.append(txt + button.getText().toString());
                    textView1.setText("");
                } else if (txtRes.length() > 0) {
                    if (txtRes.charAt(txtRes.length() - 1) == ')') {
                        if (solved) {
                            textView0.setText("");
                            solved = false;
                        }
                        textView0.append(button.getText().toString());
                    }
                }

                break;
            }
            case R.id.leftBracket: {
                if (solved) {
                    textView0.setText("");
                    txtRes = "";
                    solved = false;
                }
                if (txtRes.length() > 0) {
                    if (operation.contains(txtRes.subSequence(txtRes.length() - 1, txtRes.length())) ||
                            txtRes.charAt(txtRes.length() - 1) == '(') {
                        textView0.append("(");
                        bracketCount++;
                    }
                } else {
                    textView0.append("(");
                    bracketCount++;
                }
                break;
            }
            case R.id.rightBracket: {
                if (solved) {
                    textView0.setText("");
                    solved = false;
                }
                if (bracketCount > 0) {
                    if (txt.length() > 0) {
                        if (txt.charAt(0) == MINUS && txt.length() > 1) txt = "(" + txt + ")";
                        textView0.append(txt + ")");
                        textView1.setText("");
                        bracketCount--;
                    } else {
                        if (txtRes.charAt(txtRes.length() - 1) == '(') {
                            textView0.append("0)");
                            bracketCount--;
                        } else if (txtRes.charAt(txtRes.length() - 1) == ')') {
                            textView0.append(")");
                            bracketCount--;
                        }
                    }
                }
                break;
            }
            default: {  //123456789
                if (txt.length() == 1 && txt.charAt(0) == '0') {
                    textView1.setText(button.getText());
                    break;
                }
                textView1.append(button.getText());
                break;
            }
        }
    }

    void init() {
        textView0 = (TextView) findViewById(R.id.textView0);
        textView1 = (TextView) findViewById(R.id.textView1);
        allClear = (Button) findViewById(R.id.allClear);
        allClear.setOnClickListener(this);
        clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(this);
        leftBracket = (Button) findViewById(R.id.leftBracket);
        leftBracket.setOnClickListener(this);
        rightBracket = (Button) findViewById(R.id.rightBracket);
        rightBracket.setOnClickListener(this);
        minus = (Button) findViewById(R.id.minus);
        minus.setOnClickListener( this);
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener( this);
        seven = (Button) findViewById(R.id.seven);
        seven.setOnClickListener(this);
        eight = (Button) findViewById(R.id.eight);
        eight.setOnClickListener(this);
        nine = (Button) findViewById(R.id.nine);
        nine.setOnClickListener(this);
        division = (Button) findViewById(R.id.division);
        division.setOnClickListener( this);
        four = (Button) findViewById(R.id.four);
        four.setOnClickListener(this);
        five = (Button) findViewById(R.id.five);
        five.setOnClickListener(this);
        six = (Button) findViewById(R.id.six);
        six.setOnClickListener(this);
        multiply = (Button) findViewById(R.id.multiply);
        multiply.setOnClickListener(this);
        one = (Button) findViewById(R.id.one);
        one.setOnClickListener(this);
        two = (Button) findViewById(R.id.two);
        two.setOnClickListener(this);
        three = (Button) findViewById(R.id.three);
        three.setOnClickListener(this);
        subtract = (Button) findViewById(R.id.subtract);
        subtract.setOnClickListener(this);
        point = (Button) findViewById(R.id.point);
        point.setOnClickListener(this);
        zero = (Button) findViewById(R.id.zero);
        zero.setOnClickListener(this);
        equals = (Button) findViewById(R.id.equal);
        equals.setOnClickListener(this);
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(this);
    }

}