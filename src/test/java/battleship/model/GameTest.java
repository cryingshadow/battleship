package battleship.model;

import java.util.*;
import java.util.stream.*;

import org.testng.*;
import org.testng.annotations.*;

public class GameTest {

    public static final Game GAME;

    private static final ShipPlacement PLACEMENT01;

    private static final ShipPlacement PLACEMENT02;

    private static final ShipPlacement PLACEMENT03;

    private static final ShipPlacement PLACEMENT04;

    private static final ShipPlacement PLACEMENT05;

    private static final ShipPlacement PLACEMENT06;

    private static final Shot SHOT01;

    private static final Shot SHOT02;

    private static final Shot SHOT03;

    private static final Shot SHOT04;

    private static final Shot SHOT05;

    private static final Shot SHOT06;

    private static final Shot SHOT07;

    private static final Shot SHOT08;

    private static final Shot SHOT09;

    private static final Shot SHOT10;

    private static final Shot SHOT11;

    private static final Shot SHOT12;

    private static final Shot SHOT13;

    static {
        PLACEMENT01 = new ShipPlacement(ShipType.CRUISER, new Coordinate(2, 3), Direction.EAST, Player.FIRST);
        PLACEMENT02 = new ShipPlacement(ShipType.CRUISER, new Coordinate(4, 5), Direction.SOUTH, Player.SECOND);
        PLACEMENT03 = new ShipPlacement(ShipType.DESTROYER, new Coordinate(8, 7), Direction.WEST, Player.FIRST);
        PLACEMENT04 = new ShipPlacement(ShipType.DESTROYER, new Coordinate(1, 5), Direction.WEST, Player.SECOND);
        PLACEMENT05 = new ShipPlacement(ShipType.CANNON_BOAT, new Coordinate(8, 2), Direction.NORTH, Player.FIRST);
        PLACEMENT06 = new ShipPlacement(ShipType.CANNON_BOAT, new Coordinate(2, 7), Direction.EAST, Player.SECOND);
        SHOT01 = new Shot(new Coordinate(5, 5), Player.FIRST);
        SHOT02 = new Shot(new Coordinate(0, 0), Player.SECOND);
        SHOT03 = new Shot(new Coordinate(0, 5), Player.FIRST);
        SHOT04 = new Shot(new Coordinate(1, 1), Player.SECOND);
        SHOT05 = new Shot(new Coordinate(1, 5), Player.FIRST);
        SHOT06 = new Shot(new Coordinate(2, 2), Player.SECOND);
        SHOT07 = new Shot(new Coordinate(4, 5), Player.FIRST);
        SHOT08 = new Shot(new Coordinate(3, 3), Player.SECOND);
        SHOT09 = new Shot(new Coordinate(4, 6), Player.FIRST);
        SHOT10 = new Shot(new Coordinate(4, 4), Player.SECOND);
        SHOT11 = new Shot(new Coordinate(4, 7), Player.FIRST);
        SHOT12 = new Shot(new Coordinate(5, 5), Player.SECOND);
        SHOT13 = new Shot(new Coordinate(2, 7), Player.FIRST);
        GAME = new Game();
        GameTest.GAME.addEvent(GameTest.PLACEMENT01);
        GameTest.GAME.addEvent(GameTest.PLACEMENT02);
        GameTest.GAME.addEvent(GameTest.PLACEMENT03);
        GameTest.GAME.addEvent(GameTest.PLACEMENT04);
        GameTest.GAME.addEvent(GameTest.PLACEMENT05);
        GameTest.GAME.addEvent(GameTest.PLACEMENT06);
        GameTest.GAME.addEvent(GameTest.SHOT01);
        GameTest.GAME.addEvent(GameTest.SHOT02);
        GameTest.GAME.addEvent(GameTest.SHOT03);
        GameTest.GAME.addEvent(GameTest.SHOT04);
        GameTest.GAME.addEvent(GameTest.SHOT05);
        GameTest.GAME.addEvent(GameTest.SHOT06);
        GameTest.GAME.addEvent(GameTest.SHOT07);
        GameTest.GAME.addEvent(GameTest.SHOT08);
        GameTest.GAME.addEvent(GameTest.SHOT09);
        GameTest.GAME.addEvent(GameTest.SHOT10);
        GameTest.GAME.addEvent(GameTest.SHOT11);
        GameTest.GAME.addEvent(GameTest.SHOT12);
        GameTest.GAME.addEvent(GameTest.SHOT13);
    }

    @Test
    public void getActualShotCoordinatesTest() {
        Assert.assertEquals(
            GameTest.GAME.getActualShotCoordinates(Player.FIRST),
            Set.of(
                GameTest.SHOT02.coordinate,
                GameTest.SHOT04.coordinate,
                GameTest.SHOT06.coordinate,
                GameTest.SHOT08.coordinate,
                GameTest.SHOT10.coordinate,
                GameTest.SHOT12.coordinate
            )
        );
        Assert.assertEquals(
            GameTest.GAME.getActualShotCoordinates(Player.SECOND),
            Set.of(
                GameTest.SHOT01.coordinate,
                GameTest.SHOT03.coordinate,
                GameTest.SHOT05.coordinate,
                GameTest.SHOT07.coordinate,
                GameTest.SHOT09.coordinate,
                GameTest.SHOT11.coordinate,
                GameTest.SHOT13.coordinate
            )
        );
    }

    @Test
    public void getEventsByPlayerTest() {
        final Set<Event> actual1 = GameTest.GAME.getEventsByPlayer(Player.FIRST).collect(Collectors.toSet());
        final Set<Event> expected1 =
            Set.of(
                GameTest.PLACEMENT01,
                GameTest.PLACEMENT03,
                GameTest.PLACEMENT05,
                GameTest.SHOT01,
                GameTest.SHOT03,
                GameTest.SHOT05,
                GameTest.SHOT07,
                GameTest.SHOT09,
                GameTest.SHOT11,
                GameTest.SHOT13
            );
        Assert.assertEquals(actual1, expected1);
        final Set<Event> actual2 = GameTest.GAME.getEventsByPlayer(Player.SECOND).collect(Collectors.toSet());
        final Set<Event> expected2 =
            Set.of(
                GameTest.PLACEMENT02,
                GameTest.PLACEMENT04,
                GameTest.PLACEMENT06,
                GameTest.SHOT02,
                GameTest.SHOT04,
                GameTest.SHOT06,
                GameTest.SHOT08,
                GameTest.SHOT10,
                GameTest.SHOT12
            );
        Assert.assertEquals(actual2, expected2);
    }

    @Test
    public void getShipCoordinatesTest() {
        final Set<Coordinate> expected1 = new LinkedHashSet<Coordinate>();
        expected1.addAll(GameTest.PLACEMENT01.toCoordinates().toList());
        expected1.addAll(GameTest.PLACEMENT03.toCoordinates().toList());
        expected1.addAll(GameTest.PLACEMENT05.toCoordinates().toList());
        Assert.assertEquals(GameTest.GAME.getShipCoordinates(Player.FIRST), expected1);
        final Set<Coordinate> expected2 = new LinkedHashSet<Coordinate>();
        expected2.addAll(GameTest.PLACEMENT02.toCoordinates().toList());
        expected2.addAll(GameTest.PLACEMENT04.toCoordinates().toList());
        expected2.addAll(GameTest.PLACEMENT06.toCoordinates().toList());
        Assert.assertEquals(GameTest.GAME.getShipCoordinates(Player.SECOND), expected2);
    }

}
