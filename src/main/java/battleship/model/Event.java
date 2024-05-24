package battleship.model;

import java.util.*;

public abstract class Event implements Comparable<Event> {

    public final UUID id;

    public final long timestamp;

    public Event() {
        this.id = UUID.randomUUID();
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public int compareTo(final Event other) {
        return Long.compare(this.timestamp, other.timestamp);
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Event) {
            return this.id.equals(((Event)o).id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    public abstract boolean isShipPlacementEvent(Player player);

    public abstract boolean isShotEvent(Player player);

}
