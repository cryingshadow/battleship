package battleship.model;

import java.util.*;
import java.util.stream.*;

public class Game {

    private final List<Event> events;

    public Game() {
        this.events = new LinkedList<Event>();
    }

    public void addEvent(final Event event) {
        this.events.add(event);
    }

    public Set<Coordinate> getActualShotCoordinates(final Player hitPlayer) {
        return this.events.stream()
            .filter(event -> event.isShotEvent(hitPlayer.inverse()))
            .map(event -> ((Shot)event).coordinate)
            .collect(Collectors.toSet());
    }

    public Stream<Event> getEvents() {
        return this.events.stream();
    }

    public Stream<Event> getEventsByPlayer(final Player player) {
        return this.events.stream().filter(event -> event.isShipPlacementEvent(player) || event.isShotEvent(player));
    }

    public Set<Coordinate> getShipCoordinates(final Player player) {
        return this.events.stream()
            .filter(event -> event.isShipPlacementEvent(player))
            .flatMap(event -> ((ShipPlacement)event).toCoordinates())
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
