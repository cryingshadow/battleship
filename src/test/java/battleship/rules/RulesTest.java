package battleship.rules;

import org.testng.*;
import org.testng.annotations.*;

public class RulesTest {

    @Test
    public void isBetweenTest() {
        Assert.assertTrue(Rules.isBetween(0, 0, 1));
        Assert.assertTrue(Rules.isBetween(0, 1, 2));
        Assert.assertTrue(Rules.isBetween(0, 5, 10));
        Assert.assertTrue(Rules.isBetween(-2, -1, 0));
        Assert.assertTrue(Rules.isBetween(-3, -3, -2));
        Assert.assertTrue(Rules.isBetween(-10, -5, 2));
        Assert.assertTrue(Rules.isBetween(-2, 1, 2));
        Assert.assertFalse(Rules.isBetween(0, 0, 0));
        Assert.assertFalse(Rules.isBetween(0, 1, 1));
        Assert.assertFalse(Rules.isBetween(2, 1, 0));
        Assert.assertFalse(Rules.isBetween(-2, -1, -1));
        Assert.assertFalse(Rules.isBetween(-1, 0, 0));
    }

}
