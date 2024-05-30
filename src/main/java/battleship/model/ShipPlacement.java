package battleship.model;

import java.util.*;
import java.util.stream.*;

public class ShipPlacement extends Event {

    public static Optional<Direction> toDirection(final ShipType type, final Coordinate start, final Coordinate end) {
        if (ShipPlacement.distance(start, end) + 1 != type.length || !ShipPlacement.onOneLine(start, end)) {
            return Optional.empty();
        }
        if (start.column() - end.column() > 0) {
            return Optional.of(Direction.WEST);
        }
        if (start.column() - end.column() < 0) {
            return Optional.of(Direction.EAST);
        }
        if (start.row() - end.row() < 0) {
            return Optional.of(Direction.SOUTH);
        }
        return Optional.of(Direction.NORTH);
    }

    private static int distance(final Coordinate start, final Coordinate end) {
        return Math.abs(start.column() - end.column()) + Math.abs(start.row() - end.row());
    }

    private static boolean onOneLine(final Coordinate start, final Coordinate end) {
        return start.column() == end.column() || start.row() == end.row();
    }

    public final Direction direction;

    public final Player player;

    public final Coordinate start;

    public final ShipType type;

    public ShipPlacement(
        final ShipType type,
        final Coordinate start,
        final Direction direction,
        final Player player
    ) {
        this.type = type;
        this.start = start;
        this.direction = direction;
        this.player = player;
    }

    @Override
    public boolean isShipPlacementEvent(final Player player) {
        return this.player == player;
    }

    @Override
    public boolean isShotEvent(final Player player) {
        return false;
    }

    public Stream<Coordinate> toCoordinates() {
        final Stream.Builder<Coordinate> result = Stream.builder();
        for (int i = 0; i < this.type.length; i++) {
            result.add(this.start.plus(i, this.direction));
        }
        return result.build();
    }

}
