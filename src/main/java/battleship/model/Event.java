package battleship.model;

import java.util.*;

public abstract class Event implements Comparable<Event> {

    public final UUID id;

    public final long timestamp;

    public Event() {
        this(UUID.randomUUID(), System.currentTimeMillis());
    }

    public Event(final UUID id, final long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(final Event other) {
        final int result = Long.compare(this.timestamp, other.timestamp);
        if (result == 0) {
            return this.id.compareTo(other.id);
        }
        return result;
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
