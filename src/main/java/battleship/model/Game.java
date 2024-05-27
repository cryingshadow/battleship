package battleship.model;

import java.util.*;
import java.util.stream.*;

public class Game {

    private static void shot(final Coordinate shot, final Field[][] field) {
        switch (field[shot.y()][shot.x()]) {
        case WATER:
        case WATER_HIT:
            field[shot.y()][shot.x()] = Field.WATER_HIT;
            break;
        case SHIP:
        case SHIP_HIT:
            field[shot.y()][shot.x()] = Field.SHIP_HIT;
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

    public Field[][] toFieldArray(final Player player, final int horizontalLength, final int verticalLength) {
        final Field[][] result = new Field[horizontalLength][verticalLength];
        for (int x = 0; x < horizontalLength; x++) {
            for (int y = 0; y < verticalLength; y++) {
                result[y][x] = Field.WATER;
            }
        }
        for (final Coordinate ship : this.getShipCoordinates(player)) {
            result[ship.y()][ship.x()] = Field.SHIP;
        }
        for (final Coordinate shot : this.getShotCoordinates(player)) {
            Game.shot(shot, result);
        }
        return result;
    }

}
