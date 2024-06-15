package battleship.rules;

import java.util.*;
import java.util.Optional;
import java.util.stream.*;

import org.testng.*;
import org.testng.annotations.*;

import battleship.model.*;

public class RulesTest {

    private static final Rules RULES_STUB = RulesTest.getRulesStub(10, 10);

    private static Rules getRulesStub(final int horizontalLength, final int verticalLength) {
        return new Rules() {

            @Override
            public int getHorizontalLength() {
                return horizontalLength;
            }

            @Override
            public Set<Coordinate> getImpossibleCoordinatesAfterShot(
                final Player playerWhoShot,
                final Coordinate shot,
                final Game game
            ) {
                return Collections.emptySet();
            }

            @Override
            public Optional<Turn> getNextTurn(final Game game) {
                return Optional.empty();
            }

            @Override
            public int getVerticalLength() {
                return verticalLength;
            }

            @Override
            public Optional<Player> getWinner(final Game game) {
                return Optional.empty();
            }

            @Override
            public boolean placementConflict(final Coordinate first, final Coordinate second) {
                return first.equals(second);
            }

        };
    }

    @DataProvider
    public Object[][] betweenData() {
        return new Object[][] {
            {0, 0, 1, true},
            {0, 1, 2, true},
            {0, 5, 10, true},
            {-2, -1, 0, true},
            {-3, -3, -2, true},
            {-10, -5, 2, true},
            {-2, 1, 2, true},
            {0, 0, 0, false},
            {0, 1, 1, false},
            {2, 1, 0, false},
            {-2, -1, -1, false},
            {-1, 0, 0, false}
        };
    }

    @DataProvider
    public Object[][] coordinateData() {
        return new Object[][] {
            {RulesTest.RULES_STUB, new Coordinate(0, 0), true},
            {RulesTest.RULES_STUB, new Coordinate(9, 9), true},
            {RulesTest.RULES_STUB, new Coordinate(0, 1), true},
            {RulesTest.RULES_STUB, new Coordinate(1, 0), true},
            {RulesTest.RULES_STUB, new Coordinate(5, 4), true},
            {RulesTest.RULES_STUB, new Coordinate(0, -1), false},
            {RulesTest.RULES_STUB, new Coordinate(10, 1), false},
            {RulesTest.RULES_STUB, new Coordinate(0, 10), false},
            {RulesTest.RULES_STUB, new Coordinate(5, -4), false},
            {RulesTest.RULES_STUB, new Coordinate(-4, -5), false},
            {RulesTest.RULES_STUB, new Coordinate(-1, 1), false},
            {RulesTest.getRulesStub(5, 5), new Coordinate(5, 4), false}
        };
    }

    @Test(dataProvider="betweenData")
    public void isBetweenTest(
        final int lowerBoundInclusive,
        final int number,
        final int upperBoundExclusive,
        final boolean expected
    ) {
        Assert.assertEquals(Rules.isBetween(lowerBoundInclusive, number, upperBoundExclusive), expected);
    }

    @DataProvider
    public Object[][] placementData() {
        return new Object[][] {
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(0, 0), Direction.SOUTH, Player.FIRST),
                Collections.emptySet(),
                true
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(0, 0), Direction.EAST, Player.FIRST),
                Collections.emptySet(),
                true
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(0, 0), Direction.WEST, Player.FIRST),
                Collections.emptySet(),
                false
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(0, 0), Direction.NORTH, Player.FIRST),
                Collections.emptySet(),
                false
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.CANNON_BOAT, new Coordinate(0, 0), Direction.NORTH, Player.FIRST),
                Collections.emptySet(),
                true
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(5, 5), Direction.NORTH, Player.FIRST),
                Collections.emptySet(),
                true
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(5, 5), Direction.SOUTH, Player.FIRST),
                Collections.emptySet(),
                true
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(5, 5), Direction.WEST, Player.FIRST),
                Collections.emptySet(),
                true
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(5, 5), Direction.EAST, Player.FIRST),
                Collections.emptySet(),
                true
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(3, 5), Direction.WEST, Player.FIRST),
                Collections.emptySet(),
                true
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(2, 5), Direction.WEST, Player.FIRST),
                Collections.emptySet(),
                false
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(5, 6), Direction.SOUTH, Player.FIRST),
                Collections.emptySet(),
                true
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(5, 7), Direction.SOUTH, Player.FIRST),
                Collections.emptySet(),
                false
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.CARRIER, new Coordinate(5, 6), Direction.SOUTH, Player.FIRST),
                Collections.emptySet(),
                false
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.CRUISER, new Coordinate(5, 7), Direction.SOUTH, Player.FIRST),
                Collections.emptySet(),
                true
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(5, 5), Direction.SOUTH, Player.FIRST),
                new ShipPlacement(
                    ShipType.CARRIER,
                    new Coordinate(0, 0),
                    Direction.SOUTH,
                    Player.FIRST
                ).toCoordinates().collect(Collectors.toSet()),
                true
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(5, 5), Direction.SOUTH, Player.FIRST),
                Set.of(new Coordinate(5, 7)),
                false
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(5, 5), Direction.WEST, Player.FIRST),
                Set.of(new Coordinate(3, 5)),
                false
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(5, 5), Direction.SOUTH, Player.FIRST),
                Set.of(new Coordinate(5, 9)),
                true
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(5, 5), Direction.WEST, Player.FIRST),
                Set.of(new Coordinate(1, 5)),
                true
            }
        };
    }

    @Test(dataProvider="coordinateData")
    public void validCoordinateTest(final Rules rules, final Coordinate coordinate, final boolean expected) {
        Assert.assertEquals(rules.validCoordinate(coordinate), expected);
    }

    @Test(dataProvider="placementData")
    public void validShipPlacementTest(
        final Rules rules,
        final ShipPlacement placement,
        final Set<Coordinate> shipCoordinates,
        final boolean expected
    ) {
        Assert.assertEquals(rules.validShipPlacement(placement, shipCoordinates), expected);
    }

}
