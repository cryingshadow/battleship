package battleship.rules;

import java.util.*;
import java.util.Optional;

import org.testng.*;
import org.testng.annotations.*;

import battleship.model.*;

public class StandardRulesTest {

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

    @Test
    public void getNextTurnTest() {
        //TODO
    }

    @DataProvider
    public Object[][] getWinnerData() {
        return new Object[][] {
            {GameTest.getPreparedGame(true), Optional.of(Player.FIRST)},
            {GameTest.getPreparedGame(false), Optional.empty()},
            {new Game(), Optional.empty()}
        };
    }

    @Test(dataProvider="getWinnerData")
    public void getWinnerTest(final Game game, final Optional<Player> expected) {
        Assert.assertEquals(StandardRules.INSTANCE.getWinner(game), expected);
    }

    @Test
    public void placementConflictTest() {
        //TODO
    }

}
