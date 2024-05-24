package battleship.model;

import java.util.stream.*;

public class ShipPlacement extends Event {

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
