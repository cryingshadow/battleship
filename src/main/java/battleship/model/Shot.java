package battleship.model;

public class Shot extends Event {

    public final Coordinate coordinate;

    public final Player player;

    public Shot(final Coordinate coordinate, final Player player) {
        this.coordinate = coordinate;
        this.player = player;
    }

    @Override
    public boolean isShipPlacementEvent(final Player player) {
        return false;
    }

    @Override
    public boolean isShotEvent(final Player player) {
        return this.player == player;
    }

}
