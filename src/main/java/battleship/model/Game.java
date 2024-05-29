package battleship.model;

import java.util.*;
import java.util.stream.*;

public class Game {

    private static boolean hasCoordinate(final Event event, final Coordinate coordinate) {
        if (event instanceof Shot) {
            return ((Shot)event).coordinate.equals(coordinate);
        }
        return ((ShipPlacement)event).toCoordinates().filter(c -> c.equals(coordinate)).findAny().isPresent();
    }

    private static void shot(final Coordinate shot, final Field[][] field) {
        switch (field[shot.row()][shot.column()]) {
        case WATER:
        case WATER_HIT:
            field[shot.row()][shot.column()] = Field.WATER_HIT;
            break;
        case SHIP:
        case SHIP_HIT:
            field[shot.row()][shot.column()] = Field.SHIP_HIT;
            break;
        }
    }

    private final List<Event> events;

    public Game() {
        this.events = new LinkedList<Event>();
    }

    public void addEvent(final Event event) {
        this.events.add(event);
    }

    public Stream<Event> getEventsByPlayer(final Player player) {
        return this.events.stream().filter(event -> event.isShipPlacementEvent(player) || event.isShotEvent(player));
    }

    public Field getField(final Player player, final Coordinate coordinate) {
        return this.events.stream()
            .filter(event -> event.isShipPlacementEvent(player) || event.isShotEvent(player.inverse()))
            .filter(event -> Game.hasCoordinate(event, coordinate))
            .reduce(
                Field.WATER,
                (field, event) -> event.isShipPlacementEvent(player) ?
                    Field.SHIP :
                        (field == Field.WATER || field == Field.WATER_HIT ? Field.WATER_HIT : Field.SHIP_HIT),
                (field1, field2) -> field2);
    }

    public Set<Coordinate> getShipCoordinates(final Player player) {
        return this.events.stream()
            .filter(event -> event.isShipPlacementEvent(player))
            .flatMap(event -> ((ShipPlacement)event).toCoordinates())
            .collect(Collectors.toSet());
    }

    public Set<Coordinate> getShotCoordinates(final Player player) {
        return this.events.stream()
            .filter(event -> event.isShotEvent(player.inverse()))
            .map(event -> ((Shot)event).coordinate)
            .collect(Collectors.toSet());
    }

    public Field[][] toFieldArray(
        final Player player,
        final int horizontalLength,
        final int verticalLength,
        final boolean showShips
    ) {
        final Field[][] result = new Field[horizontalLength][verticalLength];
        for (int column = 0; column < horizontalLength; column++) {
            for (int row = 0; row < verticalLength; row++) {
                result[row][column] = Field.WATER;
            }
        }
        for (final Coordinate ship : this.getShipCoordinates(player)) {
            result[ship.row()][ship.column()] = Field.SHIP;
        }
        for (final Coordinate shot : this.getShotCoordinates(player)) {
            Game.shot(shot, result);
        }
        if (!showShips) {
            for (int column = 0; column < horizontalLength; column++) {
                for (int row = 0; row < verticalLength; row++) {
                    if (result[row][column] == Field.SHIP) {
                        result[row][column] = Field.WATER;
                    }
                }
            }
        }
        return result;
    }

}
