package battleship.model;

import java.util.concurrent.*;

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
            TimeUnit.MILLISECONDS.sleep(1);
            PLACEMENT2 =
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(0, 0), Direction.SOUTH, Player.FIRST);
            TimeUnit.MILLISECONDS.sleep(1);
            SHOT = new Shot(new Coordinate(0, 0), Player.SECOND);
        } catch (final InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void compareToTest() {
        Assert.assertEquals(EventTest.PLACEMENT.compareTo(EventTest.PLACEMENT2), -1);
        Assert.assertEquals(EventTest.PLACEMENT2.compareTo(EventTest.PLACEMENT), 1);
        Assert.assertEquals(EventTest.PLACEMENT.compareTo(EventTest.SHOT), -1);
        Assert.assertEquals(EventTest.SHOT.compareTo(EventTest.PLACEMENT2), 1);
        Assert.assertEquals(EventTest.PLACEMENT.compareTo(EventTest.PLACEMENT), 0);
        Assert.assertEquals(EventTest.SHOT.compareTo(EventTest.SHOT), 0);
    }

    @Test
    public void equalsTest() {
        Assert.assertEquals(EventTest.PLACEMENT, EventTest.PLACEMENT);
        Assert.assertEquals(EventTest.SHOT, EventTest.SHOT);
        Assert.assertNotEquals(EventTest.PLACEMENT, EventTest.SHOT);
        Assert.assertNotEquals(EventTest.PLACEMENT, EventTest.PLACEMENT2);
    }

    @Test
    public void isShipPlacementEventTest() {
        Assert.assertTrue(EventTest.PLACEMENT.isShipPlacementEvent(Player.FIRST));
        Assert.assertTrue(EventTest.PLACEMENT2.isShipPlacementEvent(Player.FIRST));
        Assert.assertFalse(EventTest.PLACEMENT.isShipPlacementEvent(Player.SECOND));
        Assert.assertFalse(EventTest.SHOT.isShipPlacementEvent(Player.FIRST));
        Assert.assertFalse(EventTest.SHOT.isShipPlacementEvent(Player.SECOND));
    }

    @Test
    public void isShotEventTest() {
        Assert.assertTrue(EventTest.SHOT.isShotEvent(Player.SECOND));
        Assert.assertFalse(EventTest.SHOT.isShotEvent(Player.FIRST));
        Assert.assertFalse(EventTest.PLACEMENT.isShotEvent(Player.FIRST));
        Assert.assertFalse(EventTest.PLACEMENT.isShotEvent(Player.SECOND));
    }

}
