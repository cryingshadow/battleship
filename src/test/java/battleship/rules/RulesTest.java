package battleship.rules;

import java.util.*;
import java.util.Optional;
import java.util.function.*;
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
    public Object[][] isBetweenData() {
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

    @Test(dataProvider="isBetweenData")
    public void isBetweenTest(
        final int lowerBoundInclusive,
        final int number,
        final int upperBoundExclusive,
        final boolean expected
    ) {
        Assert.assertEquals(Rules.isBetween(lowerBoundInclusive, number, upperBoundExclusive), expected);
    }

    @DataProvider
    public Object[][] shipPlacementData() {
        final Predicate<Game> isEmpty = new Predicate<Game>() {

            @Override
            public boolean test(final Game game) {
                return game.getEvents().count() == 0;
            }

        };
        return new Object[][] {
            {
                RulesTest.RULES_STUB,
                new Game(),
                ShipType.CARRIER,
                Player.FIRST,
                new ShipPlacement(ShipType.CARRIER, new Coordinate(0, 0), Direction.EAST, Player.FIRST),
                true,
                new Predicate<Game>() {

                    @Override
                    public boolean test(final Game game) {
                        final Set<Coordinate> shipCoordinates = game.getShipCoordinates(Player.FIRST);
                        return game.getEvents().count() == 1
                            && shipCoordinates.size() == 5
                            && shipCoordinates.containsAll(
                                Set.of(
                                    new Coordinate(0, 0),
                                    new Coordinate(1, 0),
                                    new Coordinate(2, 0),
                                    new Coordinate(3, 0),
                                    new Coordinate(4, 0)
                                )
                            );
                    }

                }
            },
            {
                RulesTest.RULES_STUB,
                new Game(),
                ShipType.CARRIER,
                Player.FIRST,
                new ShipPlacement(ShipType.CARRIER, new Coordinate(0, 0), Direction.EAST, Player.SECOND),
                false,
                isEmpty
            },
            {
                RulesTest.RULES_STUB,
                new Game(),
                ShipType.CARRIER,
                Player.FIRST,
                new ShipPlacement(ShipType.BATTLESHIP, new Coordinate(0, 0), Direction.EAST, Player.FIRST),
                false,
                isEmpty
            },
            {
                RulesTest.RULES_STUB,
                new Game(),
                ShipType.CARRIER,
                Player.FIRST,
                new Shot(new Coordinate(0, 0), Player.FIRST),
                false,
                isEmpty
            },
            {
                RulesTest.RULES_STUB,
                GameTest.getPreparedGame(false),
                ShipType.CARRIER,
                Player.FIRST,
                new ShipPlacement(ShipType.CARRIER, new Coordinate(1, 9), Direction.NORTH, Player.FIRST),
                true,
                new Predicate<Game>() {

                    @Override
                    public boolean test(final Game game) {
                        final Set<Coordinate> shipCoordinates = game.getShipCoordinates(Player.FIRST);
                        return game.getEvents().count() == 7
                            && shipCoordinates.size() == 11
                            && shipCoordinates.containsAll(
                                Set.of(
                                    new Coordinate(1, 9),
                                    new Coordinate(1, 8),
                                    new Coordinate(1, 7),
                                    new Coordinate(1, 6),
                                    new Coordinate(1, 5),
                                    new Coordinate(2, 3),
                                    new Coordinate(3, 3),
                                    new Coordinate(4, 3),
                                    new Coordinate(8, 7),
                                    new Coordinate(7, 7),
                                    new Coordinate(8, 2)
                                )
                            );
                    }

                }
            },
            {
                RulesTest.RULES_STUB,
                GameTest.getPreparedGame(false),
                ShipType.CARRIER,
                Player.FIRST,
                new ShipPlacement(ShipType.CARRIER, new Coordinate(5, 2), Direction.EAST, Player.FIRST),
                false,
                new Predicate<Game>() {

                    @Override
                    public boolean test(final Game game) {
                        final Set<Coordinate> shipCoordinates = game.getShipCoordinates(Player.FIRST);
                        return game.getEvents().count() == 6
                            && shipCoordinates.size() == 6
                            && shipCoordinates.containsAll(
                                Set.of(
                                    new Coordinate(2, 3),
                                    new Coordinate(3, 3),
                                    new Coordinate(4, 3),
                                    new Coordinate(8, 7),
                                    new Coordinate(7, 7),
                                    new Coordinate(8, 2)
                                )
                            );
                    }

                }
            }
        };
    }

    @Test(dataProvider="shipPlacementData")
    public void shipPlacementTest(
        final Rules rules,
        final Game game,
        final ShipType type,
        final Player player,
        final Event event,
        final boolean expectedResult,
        final Predicate<Game> expectedState
    ) {
        Assert.assertEquals(rules.shipPlacement(game, type, player, event), expectedResult);
        Assert.assertTrue(expectedState.test(game));
    }

    @DataProvider
    public Object[][] shotData() {
        final Predicate<Game> isEmpty = new Predicate<Game>() {

            @Override
            public boolean test(final Game game) {
                return game.getEvents().count() == 0;
            }

        };
        return new Object[][] {
            {
                RulesTest.RULES_STUB,
                new Game(),
                Player.FIRST,
                new Shot(new Coordinate(0, 0), Player.FIRST),
                true,
                new Predicate<Game>() {

                    @Override
                    public boolean test(final Game game) {
                        final Set<Coordinate> shotCoordinates = game.getActualShotCoordinates(Player.SECOND);
                        return game.getEvents().count() == 1
                            && shotCoordinates.size() == 1
                            && shotCoordinates.contains(new Coordinate(0, 0));
                    }

                }
            },
            {
                RulesTest.RULES_STUB,
                new Game(),
                Player.FIRST,
                new Shot(new Coordinate(0, 0), Player.SECOND),
                false,
                isEmpty
            },
            {
                RulesTest.RULES_STUB,
                new Game(),
                Player.FIRST,
                new Shot(new Coordinate(10, 10), Player.FIRST),
                false,
                isEmpty
            },
            {
                RulesTest.RULES_STUB,
                GameTest.getPreparedGame(true),
                Player.FIRST,
                new Shot(new Coordinate(5, 2), Player.FIRST),
                true,
                new Predicate<Game>() {

                    @Override
                    public boolean test(final Game game) {
                        final Set<Coordinate> shotCoordinates = game.getActualShotCoordinates(Player.SECOND);
                        return game.getEvents().count() == 20
                            && shotCoordinates.size() == 8
                            && shotCoordinates.containsAll(
                                Set.of(
                                    new Coordinate(5, 5),
                                    new Coordinate(0, 5),
                                    new Coordinate(1, 5),
                                    new Coordinate(4, 5),
                                    new Coordinate(4, 6),
                                    new Coordinate(4, 7),
                                    new Coordinate(2, 7),
                                    new Coordinate(5, 2)
                                )
                            );
                    }

                }
            },
            {
                RulesTest.RULES_STUB,
                GameTest.getPreparedGame(true),
                Player.FIRST,
                new Shot(new Coordinate(5, 5), Player.FIRST),
                true,
                new Predicate<Game>() {

                    @Override
                    public boolean test(final Game game) {
                        final Set<Coordinate> shotCoordinates = game.getActualShotCoordinates(Player.SECOND);
                        return game.getEvents().count() == 20
                            && shotCoordinates.size() == 7
                            && shotCoordinates.containsAll(
                                Set.of(
                                    new Coordinate(5, 5),
                                    new Coordinate(0, 5),
                                    new Coordinate(1, 5),
                                    new Coordinate(4, 5),
                                    new Coordinate(4, 6),
                                    new Coordinate(4, 7),
                                    new Coordinate(2, 7)
                                )
                            );
                    }

                }
            }
        };
    }

    @Test(dataProvider="shotData")
    public void shotTest(
        final Rules rules,
        final Game game,
        final Player player,
        final Event event,
        final boolean expectedResult,
        final Predicate<Game> expectedState
    ) {
        Assert.assertEquals(rules.shot(game, player, event), expectedResult);
        Assert.assertTrue(expectedState.test(game));
    }

    @DataProvider
    public Object[][] validCoordinateData() {
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

    @Test(dataProvider="validCoordinateData")
    public void validCoordinateTest(final Rules rules, final Coordinate coordinate, final boolean expected) {
        Assert.assertEquals(rules.validCoordinate(coordinate), expected);
    }

    @DataProvider
    public Object[][] validShipPlacementData() {
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
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.CANNON_BOAT, new Coordinate(5, 5), Direction.SOUTH, Player.FIRST),
                Stream.of(
                    new ShipPlacement(
                        ShipType.CARRIER,
                        new Coordinate(1, 1),
                        Direction.SOUTH,
                        Player.FIRST
                    ).toCoordinates(),
                    new ShipPlacement(
                        ShipType.BATTLESHIP,
                        new Coordinate(7, 8),
                        Direction.WEST,
                        Player.FIRST
                    ).toCoordinates(),
                    new ShipPlacement(
                        ShipType.CRUISER,
                        new Coordinate(3, 6),
                        Direction.EAST,
                        Player.FIRST
                    ).toCoordinates(),
                    new ShipPlacement(
                        ShipType.DESTROYER,
                        new Coordinate(9, 9),
                        Direction.NORTH,
                        Player.FIRST
                    ).toCoordinates()
                ).flatMap(Function.identity())
                .collect(Collectors.toSet()),
                true
            },
            {
                RulesTest.RULES_STUB,
                new ShipPlacement(ShipType.CANNON_BOAT, new Coordinate(5, 8), Direction.SOUTH, Player.FIRST),
                Stream.of(
                    new ShipPlacement(
                        ShipType.CARRIER,
                        new Coordinate(1, 1),
                        Direction.SOUTH,
                        Player.FIRST
                    ).toCoordinates(),
                    new ShipPlacement(
                        ShipType.BATTLESHIP,
                        new Coordinate(7, 8),
                        Direction.WEST,
                        Player.FIRST
                    ).toCoordinates(),
                    new ShipPlacement(
                        ShipType.CRUISER,
                        new Coordinate(3, 6),
                        Direction.EAST,
                        Player.FIRST
                    ).toCoordinates(),
                    new ShipPlacement(
                        ShipType.DESTROYER,
                        new Coordinate(9, 9),
                        Direction.NORTH,
                        Player.FIRST
                    ).toCoordinates()
                ).flatMap(Function.identity())
                .collect(Collectors.toSet()),
                false
            }
        };
    }

    @Test(dataProvider="validShipPlacementData")
    public void validShipPlacementTest(
        final Rules rules,
        final ShipPlacement placement,
        final Set<Coordinate> shipCoordinates,
        final boolean expected
    ) {
        Assert.assertEquals(rules.validShipPlacement(placement, shipCoordinates), expected);
    }

}
