package ru.flevix;

import android.test.ActivityInstrumentationTestCase2;
import junit.framework.Assert;

/**
 * Created with IntelliJ IDEA.
 * User: flevix
 * Date: 13.11.12
 * Time: 2:10
 */

public class MyFindPointTest extends ActivityInstrumentationTestCase2<MyCalc> {

    public MyFindPointTest() {
        super("ru.flevix", MyCalc.class);
    }

    Logic logic = new Logic();
    public void testOne() {
        Assert.assertTrue(logic.findPoint(".") == 0);
    }

    public void testTwo() {
        Assert.assertFalse(logic.findPoint("") >= 0);
    }

    public void testThree() {
        Assert.assertTrue(logic.findPoint("12312412412.8980189018") == 11);
    }

    public void testFour() {
        Assert.assertFalse(logic.findPoint("7384927389473294873298472992919191919919191912893712837918") >= 0);
    }
}
