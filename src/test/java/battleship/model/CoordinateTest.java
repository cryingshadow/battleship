package battleship.model;

import org.testng.*;
import org.testng.annotations.*;

public class CoordinateTest {

    @Test
    public void plusColumnTest() {
        Assert.assertEquals(new Coordinate(2, 7).plusColumn(4), new Coordinate(6, 7));
    }

    @DataProvider
    public Object[][] plusData() {
        return new Object[][] {
            {new Coordinate(0, 0), 1, Direction.SOUTH, new Coordinate(0, 1)},
            {new Coordinate(0, 0), 1, Direction.NORTH, new Coordinate(0, -1)},
            {new Coordinate(0, 0), 1, Direction.EAST, new Coordinate(1, 0)},
            {new Coordinate(0, 0), 1, Direction.WEST, new Coordinate(-1, 0)},
            {new Coordinate(3, 4), 5, Direction.EAST, new Coordinate(8, 4)},
            {new Coordinate(3, 4), 5, Direction.NORTH, new Coordinate(3, -1)},
            {new Coordinate(3, 4), -2, Direction.NORTH, new Coordinate(3, 6)}
        };
    }

    @Test
    public void plusRowTest() {
        Assert.assertEquals(new Coordinate(5, 4).plusRow(3), new Coordinate(5, 7));
    }

    @Test(dataProvider="plusData")
    public void plusTest(
        final Coordinate coordinate,
        final int length,
        final Direction direction,
        final Coordinate expected
    ) {
        Assert.assertEquals(coordinate.plus(length, direction), expected);
    }

}
