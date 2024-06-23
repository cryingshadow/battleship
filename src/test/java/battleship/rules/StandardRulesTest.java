package battleship.rules;

import java.util.*;
import java.util.Optional;

import org.testng.*;
import org.testng.annotations.*;

import battleship.model.*;

public class StandardRulesTest {

    private static final String PROMPT_PATTERN = "Geben Sie die Koordinaten Ihres %s ein!";

    @Test
    public void dimensionTest() {
        Assert.assertEquals(StandardRules.INSTANCE.getHorizontalLength(), 10);
        Assert.assertEquals(StandardRules.INSTANCE.getVerticalLength(), 10);
    }

    @DataProvider
    public Object[][] getImpossibleCoordinatesAfterShotData() {
        final Game gameWithOneWaterHit = GameTest.getPreparedGame(false);
        gameWithOneWaterHit.addEvent(new Shot(new Coordinate(0, 0), Player.FIRST));
        final Game gameWithOneHitSunk = GameTest.getPreparedGame(false);
        gameWithOneHitSunk.addEvent(new Shot(new Coordinate(2, 7), Player.FIRST));
        final Game gameWithThreeHitsSunk = GameTest.getPreparedGame(false);
        gameWithThreeHitsSunk.addEvent(new Shot(new Coordinate(2, 3), Player.SECOND));
        gameWithThreeHitsSunk.addEvent(new Shot(new Coordinate(3, 3), Player.SECOND));
        gameWithThreeHitsSunk.addEvent(new Shot(new Coordinate(4, 3), Player.SECOND));
        final Game gameWithOneHitNotSunk = GameTest.getPreparedGame(false);
        gameWithOneHitNotSunk.addEvent(new Shot(new Coordinate(0, 5), Player.FIRST));
        return new Object[][] {
            {
                Player.FIRST,
                new Coordinate(0, 0),
                gameWithOneWaterHit,
                Set.of()
            },
            {
                Player.FIRST,
                new Coordinate(2, 7),
                gameWithOneHitSunk,
                Set.of(
                    new Coordinate(2, 6),
                    new Coordinate(3, 6),
                    new Coordinate(3, 7),
                    new Coordinate(3, 8),
                    new Coordinate(2, 8),
                    new Coordinate(1, 8),
                    new Coordinate(1, 7),
                    new Coordinate(1, 6)
                )
            },
            {
                Player.SECOND,
                new Coordinate(4, 3),
                gameWithThreeHitsSunk,
                Set.of(
                    new Coordinate(5, 2),
                    new Coordinate(5, 3),
                    new Coordinate(5, 4),
                    new Coordinate(4, 4),
                    new Coordinate(3, 4),
                    new Coordinate(2, 4),
                    new Coordinate(1, 4),
                    new Coordinate(1, 3),
                    new Coordinate(1, 2),
                    new Coordinate(2, 2),
                    new Coordinate(3, 2),
                    new Coordinate(4, 2)
                )
            },
            {
                Player.FIRST,
                new Coordinate(0, 5),
                gameWithOneHitNotSunk,
                Set.of()
            }
        };
    }

    @Test(dataProvider="getImpossibleCoordinatesAfterShotData")
    public void getImpossibleCoordinatesAfterShotTest(
        final Player playerWhoShot,
        final Coordinate shot,
        final Game game,
        final Set<Coordinate> expected
    ) {
        Assert.assertEquals(
            StandardRules.INSTANCE.getImpossibleCoordinatesAfterShot(playerWhoShot, shot, game),
            expected
        );
    }

    @DataProvider
    public Object[][] getNextTurnData() {
        final Game secondCruiser = new Game();
        secondCruiser.addEvent(new ShipPlacement(ShipType.CARRIER, new Coordinate(0, 0), Direction.EAST, Player.FIRST));
        secondCruiser.addEvent(
            new ShipPlacement(ShipType.CARRIER, new Coordinate(0, 0), Direction.EAST, Player.SECOND)
        );
        secondCruiser.addEvent(
            new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(9, 9), Direction.WEST, Player.FIRST)
        );
        secondCruiser.addEvent(
            new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(9, 9), Direction.WEST, Player.SECOND)
        );
        secondCruiser.addEvent(
            new ShipPlacement(ShipType.CRUISER, new Coordinate(0, 9), Direction.NORTH, Player.FIRST)
        );
        final Game firstShot = new Game();
        firstShot.addEvent(new ShipPlacement(ShipType.CARRIER, new Coordinate(0, 0), Direction.EAST, Player.FIRST));
        firstShot.addEvent(new ShipPlacement(ShipType.CARRIER, new Coordinate(0, 0), Direction.EAST, Player.SECOND));
        firstShot.addEvent(new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(9, 9), Direction.WEST, Player.FIRST));
        firstShot.addEvent(new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(9, 9), Direction.WEST, Player.SECOND));
        firstShot.addEvent(new ShipPlacement(ShipType.CRUISER, new Coordinate(0, 9), Direction.NORTH, Player.FIRST));
        firstShot.addEvent(new ShipPlacement(ShipType.CRUISER, new Coordinate(0, 9), Direction.NORTH, Player.SECOND));
        firstShot.addEvent(new ShipPlacement(ShipType.DESTROYER, new Coordinate(9, 0), Direction.SOUTH, Player.FIRST));
        firstShot.addEvent(new ShipPlacement(ShipType.DESTROYER, new Coordinate(9, 0), Direction.SOUTH, Player.SECOND));
        firstShot.addEvent(
            new ShipPlacement(ShipType.CANNON_BOAT, new Coordinate(5, 5), Direction.NORTH, Player.FIRST)
        );
        firstShot.addEvent(
            new ShipPlacement(ShipType.CANNON_BOAT, new Coordinate(5, 5), Direction.NORTH, Player.SECOND)
        );
        final Game secondShot = new Game();
        secondShot.addEvent(new ShipPlacement(ShipType.CARRIER, new Coordinate(0, 0), Direction.EAST, Player.FIRST));
        secondShot.addEvent(new ShipPlacement(ShipType.CARRIER, new Coordinate(0, 0), Direction.EAST, Player.SECOND));
        secondShot.addEvent(new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(9, 9), Direction.WEST, Player.FIRST));
        secondShot.addEvent(
            new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(9, 9), Direction.WEST, Player.SECOND)
        );
        secondShot.addEvent(new ShipPlacement(ShipType.CRUISER, new Coordinate(0, 9), Direction.NORTH, Player.FIRST));
        secondShot.addEvent(new ShipPlacement(ShipType.CRUISER, new Coordinate(0, 9), Direction.NORTH, Player.SECOND));
        secondShot.addEvent(new ShipPlacement(ShipType.DESTROYER, new Coordinate(9, 0), Direction.SOUTH, Player.FIRST));
        secondShot.addEvent(
            new ShipPlacement(ShipType.DESTROYER, new Coordinate(9, 0), Direction.SOUTH, Player.SECOND)
        );
        secondShot.addEvent(
            new ShipPlacement(ShipType.CANNON_BOAT, new Coordinate(5, 5), Direction.NORTH, Player.FIRST)
        );
        secondShot.addEvent(
            new ShipPlacement(ShipType.CANNON_BOAT, new Coordinate(5, 5), Direction.NORTH, Player.SECOND)
        );
        secondShot.addEvent(new Shot(new Coordinate(1, 1), Player.FIRST));
        return new Object[][] {
            {
                new Game(),
                Optional.of(
                    new Turn(
                        new ShipPlacementAction(Player.FIRST, ShipType.CARRIER),
                        String.format(StandardRulesTest.PROMPT_PATTERN, "Flugzeugträgers")
                    )
                )
            },
            {
                secondCruiser,
                Optional.of(
                    new Turn(
                        new ShipPlacementAction(Player.SECOND, ShipType.CRUISER),
                        String.format(StandardRulesTest.PROMPT_PATTERN, "Kreuzers")
                    )
                )
            },
            {
                firstShot,
                Optional.of(
                    new Turn(
                        new ShotAction(Player.FIRST),
                        String.format(StandardRulesTest.PROMPT_PATTERN, "nächsten Schusses")
                    )
                )
            },
            {
                secondShot,
                Optional.of(
                    new Turn(
                        new ShotAction(Player.SECOND),
                        String.format(StandardRulesTest.PROMPT_PATTERN, "nächsten Schusses")
                    )
                )
            },
            {GameTest.getPreparedGame(true), Optional.empty()}
        };
    }

    @Test(dataProvider="getNextTurnData")
    public void getNextTurnTest(final Game game, final Optional<Turn> expected) {
        Assert.assertEquals(StandardRules.INSTANCE.getNextTurn(game), expected);
    }

    @DataProvider
    public Object[][] getWinnerData() throws InterruptedException {
        final Game secondPlayerWins = GameTest.getPreparedGame(false);
        Thread.sleep(1);
        secondPlayerWins.addEvent(new Shot(new Coordinate(8, 2), Player.SECOND));
        Thread.sleep(1);
        secondPlayerWins.addEvent(new Shot(new Coordinate(2, 3), Player.SECOND));
        Thread.sleep(1);
        secondPlayerWins.addEvent(new Shot(new Coordinate(3, 3), Player.SECOND));
        Thread.sleep(1);
        secondPlayerWins.addEvent(new Shot(new Coordinate(4, 3), Player.SECOND));
        Thread.sleep(1);
        secondPlayerWins.addEvent(new Shot(new Coordinate(7, 7), Player.SECOND));
        Thread.sleep(1);
        secondPlayerWins.addEvent(new Shot(new Coordinate(8, 7), Player.SECOND));
        final Game secondPlayerWinsFirst = GameTest.getPreparedGame(false);
        Thread.sleep(1);
        secondPlayerWinsFirst.addEvent(new Shot(new Coordinate(5, 5), Player.FIRST));
        Thread.sleep(1);
        secondPlayerWinsFirst.addEvent(new Shot(new Coordinate(8, 2), Player.SECOND));
        Thread.sleep(1);
        secondPlayerWinsFirst.addEvent(new Shot(new Coordinate(0, 5), Player.FIRST));
        Thread.sleep(1);
        secondPlayerWinsFirst.addEvent(new Shot(new Coordinate(2, 3), Player.SECOND));
        Thread.sleep(1);
        secondPlayerWinsFirst.addEvent(new Shot(new Coordinate(1, 5), Player.FIRST));
        Thread.sleep(1);
        secondPlayerWinsFirst.addEvent(new Shot(new Coordinate(3, 3), Player.SECOND));
        Thread.sleep(1);
        secondPlayerWinsFirst.addEvent(new Shot(new Coordinate(4, 5), Player.FIRST));
        Thread.sleep(1);
        secondPlayerWinsFirst.addEvent(new Shot(new Coordinate(4, 3), Player.SECOND));
        Thread.sleep(1);
        secondPlayerWinsFirst.addEvent(new Shot(new Coordinate(4, 6), Player.FIRST));
        Thread.sleep(1);
        secondPlayerWinsFirst.addEvent(new Shot(new Coordinate(7, 7), Player.SECOND));
        Thread.sleep(1);
        secondPlayerWinsFirst.addEvent(new Shot(new Coordinate(4, 7), Player.FIRST));
        Thread.sleep(1);
        secondPlayerWinsFirst.addEvent(new Shot(new Coordinate(8, 7), Player.SECOND));
        Thread.sleep(1);
        secondPlayerWinsFirst.addEvent(new Shot(new Coordinate(2, 7), Player.FIRST));
        final Game firstPlayerWinsFirst = GameTest.getPreparedGame(true);
        Thread.sleep(1);
        firstPlayerWinsFirst.addEvent(new Shot(new Coordinate(8, 2), Player.SECOND));
        Thread.sleep(1);
        firstPlayerWinsFirst.addEvent(new Shot(new Coordinate(2, 3), Player.SECOND));
        Thread.sleep(1);
        firstPlayerWinsFirst.addEvent(new Shot(new Coordinate(4, 3), Player.SECOND));
        Thread.sleep(1);
        firstPlayerWinsFirst.addEvent(new Shot(new Coordinate(7, 7), Player.SECOND));
        Thread.sleep(1);
        firstPlayerWinsFirst.addEvent(new Shot(new Coordinate(8, 7), Player.SECOND));
        return new Object[][] {
            {GameTest.getPreparedGame(true), Optional.of(Player.FIRST)},
            {secondPlayerWins, Optional.of(Player.SECOND)},
            {GameTest.getPreparedGame(false), Optional.empty()},
            {secondPlayerWinsFirst, Optional.of(Player.SECOND)},
            {firstPlayerWinsFirst, Optional.of(Player.FIRST)},
            {new Game(), Optional.empty()}
        };
    }

    @Test(dataProvider="getWinnerData")
    public void getWinnerTest(final Game game, final Optional<Player> expected) {
        Assert.assertEquals(StandardRules.INSTANCE.getWinner(game), expected);
    }

    @DataProvider
    public Object[][] placementConflictData() {
        return new Object[][] {
            {new Coordinate(0, 0), new Coordinate(9, 9), false},
            {new Coordinate(0, 0), new Coordinate(0, 0), true},
            {new Coordinate(0, 0), new Coordinate(1, 0), true},
            {new Coordinate(0, 0), new Coordinate(1, 1), true},
            {new Coordinate(0, 0), new Coordinate(2, 0), false},
            {new Coordinate(5, 5), new Coordinate(4, 4), true},
            {new Coordinate(6, 4), new Coordinate(4, 4), false}
        };
    }

    @Test(dataProvider="placementConflictData")
    public void placementConflictTest(final Coordinate first, final Coordinate second, final boolean expected) {
        Assert.assertEquals(StandardRules.INSTANCE.placementConflict(first, second), expected);
    }

}
