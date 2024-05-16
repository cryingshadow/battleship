package battleship.model;

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
}
