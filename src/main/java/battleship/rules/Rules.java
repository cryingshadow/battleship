package battleship.rules;

import java.util.*;

import battleship.model.*;

public interface Rules {

    public static boolean isBetween(final int number, final int lowerBoundInclusive, final int upperBoundExclusive) {
        return number >= lowerBoundInclusive && number < upperBoundExclusive;
    }

    int getHorizontalLength();

    Optional<Turn> getNextTurn(final Game game);

    int getVerticalLength();

    Optional<Player> getWinner(final Game game);

    default boolean noConflict(final ShipPlacement placement, final Collection<Coordinate> shipCoordinates) {
        for (final Coordinate existing : shipCoordinates) {
            if (
                placement
                .toCoordinates()
                .filter(coordinate -> this.placementConflict(coordinate, existing))
                .findAny()
                .isPresent()
            ) {
                return false;
            }
        }
        return true;
    }

    boolean placementConflict(final Coordinate first, final Coordinate second);

    default boolean shipPlacement(final Game game, final ShipType type, final Player player, final Event event) {
        if (event.isShipPlacementEvent(player)) {
            final ShipPlacement placement = (ShipPlacement)event;
            if (
                placement.type == type
                && this.validShipPlacement(placement, game.getShipCoordinates(placement.player))
            ) {
                game.addEvent(event);
                return true;
            }
        }
        return false;
    }

    default boolean shot(final Game game, final Player player, final Event event) {
        if (event.isShotEvent(player)) {
            final Shot shot = (Shot)event;
            if (this.validCoordinate(shot.coordinate)) {
                game.addEvent(event);
                return true;
            }
        }
        return false;
    }

    default boolean validCoordinate(final Coordinate coordinate) {
        return Rules.isBetween(coordinate.column(), 0, this.getHorizontalLength())
            && Rules.isBetween(coordinate.row(), 0, this.getVerticalLength());
    }

    default boolean validCoordinates(final ShipPlacement placement) {
        return placement.toCoordinates().allMatch(this::validCoordinate);
    }

    default boolean validShipPlacement(final ShipPlacement placement, final Collection<Coordinate> shipCoordinates) {
        return this.validCoordinates(placement) && this.noConflict(placement, shipCoordinates);
    }

}
