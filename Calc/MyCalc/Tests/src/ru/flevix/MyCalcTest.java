package ru.flevix;

import android.test.ActivityInstrumentationTestCase2;
import junit.framework.Assert;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class ru.flevix.MyCalcTest \
 * ru.flevix.tests/android.test.InstrumentationTestRunner
 */
public class MyCalcTest extends ActivityInstrumentationTestCase2<MyCalc> {

    public MyCalcTest() {
        super("ru.flevix", MyCalc.class);
    }

    private Logic logic = new Logic();
    private Double x, y, eps = 1e-5; //precision 10^-5
    private String infinity = "Infinity", nan = "NaN", error = "Error", badBrackets = "BadBrackets", string;
    private final char MINUS = '\u2212';
    private final String StMINUS = Character.toString(MINUS);

    public void testOne() {
        x = Double.parseDouble(logic.solve("(5+5)+10*4-120/3").toString());
        y = 10.0;
        Assert.assertTrue(equals(x, y));
    }
    public void testTwo() {
        x = Double.parseDouble(logic.solve("120/564").toString());
        y = 0.21276;
        Assert.assertTrue(equals(x, y));
    }

    public void testFour() {
        x = Double.parseDouble(logic.solve("434.232*0.12321/(0.111+362324/23432-99.878)+324*(((21.87832/9814.23)))").toString());
        y = 0.0876483;
        Assert.assertTrue(equals(x, y));
    }

    public void testFive() {
        string = MINUS + "100+100+100-100";
        x = Double.parseDouble(logic.solve(string).toString());
        Assert.assertTrue(equals(x, 0.0));
    }

    public void testSix() {
        string = "0.00001 * 0.00001 * 0.00001 * 0.00001 * 0.00001 * 0.00001 * 0.00001 * 0.00001 * 0.00001 * 0.00001"; //10 times
        x = Double.parseDouble(logic.solve(string).toString());
        y = 1.E-50;
        Assert.assertTrue(equals(x, y));
    }

    public void testDivideZeroOne() {
        string = logic.solve("120/0").toString();
        Assert.assertTrue(string.equals(infinity));
    }

    public void testDivideZeroTwo() {
        Assert.assertTrue(logic.solve("120/3+(34*(0.33))/0").toString().equals(infinity));
    }

    public void testNaN() {
        Assert.assertTrue(logic.solve("0/0").toString().equals(nan));
    }

    public void testBadInsertOne() {
        Assert.assertTrue(logic.solve("(((((").toString().equals(error));
    }

    public void testBadInsertTwo() {
        Assert.assertTrue(logic.solve("5+").toString().equals(error));
    }

    public void testBadInsertThree() {
        Assert.assertTrue(logic.solve("+").toString().equals(error));
    }

    public void testBadInsertFour() {
        string = logic.solve(StMINUS + StMINUS + StMINUS + "1").toString();
        Assert.assertTrue(string.equals("Error"));
    }

    public void testBadInsertFive() {
        string = logic.solve(StMINUS + StMINUS + "1").toString();
        Assert.assertTrue(string.equals("Error"));
    }

    public void testBadInsertSix() {
        string = logic.solve("(((5)+(4))) + (4").toString();
        Assert.assertTrue(string.equals(error));
    }

    public void testBadInsertSeven() {
        string = logic.solve("((5)+(4)))").toString();
        Assert.assertTrue(string.equals(error));
    }

    public void testUnaryOne() {
        Assert.assertTrue(logic.solve(StMINUS + "(" + StMINUS + "5)").toString().equals("5.0"));
    }

    public void testUnaryTwo() {
        Assert.assertTrue(logic.solve("(" + StMINUS + "(" + StMINUS + "(5)))").toString().equals("5.0"));
    }

    public void testUnaryThree() {
        Assert.assertTrue(logic.solve("(" + StMINUS + "(" + StMINUS + "(" + StMINUS + "(5))))").toString().equals("-5.0"));
    }

    public void testUnaryFour() {
        Assert.assertTrue(logic.solve("(" + StMINUS + "(5))").toString().equals("-5.0"));
    }

    public void testUnaryFive() {
        Assert.assertTrue(logic.solve("(" + StMINUS + "(" + StMINUS + "(" + StMINUS + "(0))))").toString().equals("0.0"));
    }

    //utils
    private boolean equals(Double x, Double y) {
        return Math.abs(x - y) < eps;
    }
}
