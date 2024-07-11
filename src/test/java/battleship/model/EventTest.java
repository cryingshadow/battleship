package battleship.model;

import org.testng.*;
import org.testng.annotations.*;

public class EventTest {

    private static final Event PLACEMENT;
    private static final Event PLACEMENT2;
    private static final Event SHOT;

    static {
        try {
            PLACEMENT =
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(0, 0), Direction.SOUTH, Player.FIRST);
            Thread.sleep(1);
            PLACEMENT2 =
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(0, 0), Direction.SOUTH, Player.FIRST);
            Thread.sleep(1);
            SHOT = new Shot(new Coordinate(0, 0), Player.SECOND);
        } catch (final InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @DataProvider
    public Object[][] compareToData() {
        return new Object[][] {
            {EventTest.PLACEMENT, EventTest.PLACEMENT2, -1},
            {EventTest.PLACEMENT2, EventTest.PLACEMENT, 1},
            {EventTest.PLACEMENT, EventTest.SHOT, -1},
            {EventTest.SHOT, EventTest.PLACEMENT2, 1},
            {EventTest.PLACEMENT, EventTest.PLACEMENT, 0},
            {EventTest.SHOT, EventTest.SHOT, 0}
        };
    }

    @Test(dataProvider="compareToData")
    public void compareToTest(final Event first, final Event second, final int expected) {
        Assert.assertEquals(first.compareTo(second), expected);
    }

    @DataProvider
    public Object[][] equalsData() {
        return new Object[][] {
            {EventTest.PLACEMENT, EventTest.PLACEMENT, true},
            {EventTest.SHOT, EventTest.SHOT, true},
            {EventTest.PLACEMENT, EventTest.SHOT, false},
            {EventTest.PLACEMENT, EventTest.PLACEMENT2, false}
        };
    }

    @Test(dataProvider="equalsData")
    public void equalsTest(final Event first, final Event second, final boolean expected) {
        if (expected) {
            Assert.assertEquals(first, second);
        } else {
            Assert.assertNotEquals(first, second);
        }
    }

    @DataProvider
    public Object[][] isShipPlacementEventData() {
        return new Object[][] {
            {EventTest.PLACEMENT, Player.FIRST, true},
            {EventTest.PLACEMENT2, Player.FIRST, true},
            {EventTest.PLACEMENT, Player.SECOND, false},
            {EventTest.SHOT, Player.FIRST, false},
            {EventTest.SHOT, Player.SECOND, false}
        };
    }

    @Test(dataProvider="isShipPlacementEventData")
    public void isShipPlacementEventTest(final Event event, final Player player, final boolean expected) {
        Assert.assertEquals(event.isShipPlacementEvent(player), expected);
    }

    @DataProvider
    public Object[][] isShotEventData() {
        return new Object[][] {
            {EventTest.SHOT, Player.SECOND, true},
            {EventTest.SHOT, Player.FIRST, false},
            {EventTest.PLACEMENT, Player.FIRST, false},
            {EventTest.PLACEMENT, Player.SECOND, false}
        };
    }

    @Test(dataProvider="isShotEventData")
    public void isShotEventTest(final Event event, final Player player, final boolean expected) {
        Assert.assertEquals(event.isShotEvent(player), expected);
    }

}
