package battleship.rules;

import battleship.model.*;

public interface Rules {

    public static boolean isBetween(final int number, final int lowerBoundInclusive, final int upperBoundExclusive) {
        return number >= lowerBoundInclusive && number < upperBoundExclusive;
    }

    int getHorizontalLength();

    Turn getNextTurn(Game game);

    int getVerticalLength();

    Player getWinner(Game game);

    default boolean noConflict(final ShipPlacement placement, final Game game) {
        for (final Coordinate existing : game.getShipCoordinates(placement.player)) {
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

    boolean placementConflict(Coordinate first, Coordinate second);

    default void shipPlacement(final Game game, final ShipType type, final Player player, final Event event) {
        if (event.isShipPlacementEvent(player)) {
            final ShipPlacement placement = (ShipPlacement)event;
            if (placement.type == type && this.validCoordinates(placement) && this.noConflict(placement, game)) {
                game.addEvent(event);
            }
        }
    }

    default void shot(final Game game, final Player player, final Event event) {
        if (event.isShotEvent(player)) {
            final Shot shot = (Shot)event;
            if (this.validCoordinate(shot.coordinate)) {
                game.addEvent(event);
            }
        }
    }

    default boolean validCoordinate(final Coordinate coordinate) {
        return Rules.isBetween(coordinate.x(), 0, this.getHorizontalLength())
            && Rules.isBetween(coordinate.y(), 0, this.getVerticalLength());
    }

    default boolean validCoordinates(final ShipPlacement placement) {
        return placement.toCoordinates().allMatch(this::validCoordinate);
    }

}
