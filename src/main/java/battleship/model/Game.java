package battleship.model;

import java.util.*;
import java.util.stream.*;

public class Game {

    private static boolean hasConflict(final Coordinate first, final Coordinate second) {
        return Game.isBetween(first.x() - second.x(), -1, 1) && Game.isBetween(first.y() - second.y(), -1, 1);
    }

    private static boolean isBetween(final int number, final int lowerBound, final int upperBound) {
        return number >= lowerBound && number <= upperBound;
    }

    private static boolean isPlacementEvent(final Event event, final Player player) {
        return event instanceof ShipPlacement && ((ShipPlacement)event).player == player;
    }

    private static boolean isShotEvent(final Event event, final Player player) {
        return event instanceof Shot && ((Shot)event).player != player;
    }

    private static Stream<Coordinate> toCoordinates(final ShipPlacement placement) {
        final List<Coordinate> result = new LinkedList<Coordinate>();
        for (int i = 0; i < placement.type.length; i++) {
            result.add(placement.start.plus(i, placement.direction));
        }
        return result.stream();
    }

    private static boolean validCoordinate(final Coordinate coordinate) {
        return Game.isBetween(coordinate.x(), 0, 10) && Game.isBetween(coordinate.y(), 0, 10);
    }

    private static boolean validCoordinates(final ShipPlacement placement) {
        return Game.toCoordinates(placement).allMatch(Game::validCoordinate);
    }

    private final List<Event> events;

    public Game() {
        this.events = new LinkedList<Event>();
    }

    public Player getWinner() {
        if (this.allHit(Player.FIRST)) {
            return Player.SECOND;
        }
        if (this.allHit(Player.SECOND)) {
            return Player.FIRST;
        }
        return Player.NONE;
    }

    public boolean placeShip(
        final ShipType type,
        final Coordinate start,
        final Direction direction,
        final Player player
    ) {
        final ShipPlacement placement = new ShipPlacement(type, start, direction, player);
        if (Game.validCoordinates(placement) && this.noConflict(placement)) {
            this.events.add(placement);
            return true;
        }
        return false;
    }

    public boolean shot(final Coordinate coordinate, final Player player) {
        if (Game.validCoordinate(coordinate)) {
            this.events.add(new Shot(coordinate, player));
            return true;
        }
        return false;
    }

    private boolean allHit(final Player player) {
        final Set<Coordinate> ships = this.getShipCoordinates(player);
        ships.removeAll(this.getShotCoordinates(player));
        return ships.isEmpty();
    }

    private Set<Coordinate> getShipCoordinates(final Player player) {
        return this.events.stream()
            .filter(event -> Game.isPlacementEvent(event, player))
            .flatMap(event -> Game.toCoordinates((ShipPlacement)event))
            .collect(Collectors.toSet());
    }

    private Set<Coordinate> getShotCoordinates(final Player player) {
        return this.events.stream()
            .filter(event -> Game.isShotEvent(event, player))
            .map(event -> ((Shot)event).coordinate)
            .collect(Collectors.toSet());
    }

    private boolean noConflict(final ShipPlacement placement) {
        for (final Coordinate existing : this.getShipCoordinates(placement.player)) {
            if (
                Game.toCoordinates(placement)
                .filter(coordinate -> Game.hasConflict(coordinate, existing))
                .findAny()
                .isPresent()
            ) {
                return false;
            }
        }
        return true;
    }

}
