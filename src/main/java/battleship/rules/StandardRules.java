package battleship.rules;

import java.util.*;

import battleship.model.*;

public class StandardRules implements Rules {

    private static boolean allHit(final Player player, final Game game) {
        final Set<Coordinate> ships = game.getShipCoordinates(player);
        ships.removeAll(game.getShotCoordinates(player));
        return ships.isEmpty();
    }

    private static Player determineCurrentPlayer(final Game game) {
        if (game.getEventsByPlayer(Player.FIRST).count() > game.getEventsByPlayer(Player.SECOND).count()) {
            return Player.SECOND;
        }
        return Player.FIRST;
    }

    @Override
    public int getHorizontalLength() {
        return 10;
    }

    @Override
    public Turn getNextTurn(final Game game) {
        final Player player = StandardRules.determineCurrentPlayer(game);
        final int eventCount = (int)game.getEventsByPlayer(player).count();
        switch (eventCount) {
        case 0:
            return new Turn(
                player,
                event -> this.shipPlacement(game, ShipType.CARRIER, player, event),
                "Geben Sie die Koordinaten Ihres Flugzeugträgers ein!"
            );
        case 1:
            return new Turn(
                player,
                event -> this.shipPlacement(game, ShipType.BATTLESHIP, player, event),
                "Geben Sie die Koordinaten Ihres Schlachtschiffs ein!"
            );
        case 2:
            return new Turn(
                player,
                event -> this.shipPlacement(game, ShipType.CRUISER, player, event),
                "Geben Sie die Koordinaten Ihres Kreuzers ein!"
            );
        case 3:
            return new Turn(
                player,
                event -> this.shipPlacement(game, ShipType.DESTROYER, player, event),
                "Geben Sie die Koordinaten Ihres Zerstörers ein!"
            );
        case 4:
            return new Turn(
                player,
                event -> this.shipPlacement(game, ShipType.CANNON_BOAT, player, event),
                "Geben Sie die Koordinaten Ihres Kanonenboots ein!"
            );
        default:
            return new Turn(
                player,
                event -> this.shot(game, player, event),
                "Geben Sie die Koordinaten Ihres nächsten Schusses ein!"
            );
        }
    }

    @Override
    public int getVerticalLength() {
        return 10;
    }

    @Override
    public Player getWinner(final Game game) {
        if (StandardRules.allHit(Player.FIRST, game)) {
            return Player.SECOND;
        }
        if (StandardRules.allHit(Player.SECOND, game)) {
            return Player.FIRST;
        }
        return Player.NONE;
    }

    @Override
    public boolean placementConflict(final Coordinate first, final Coordinate second) {
        return Rules.isBetween(first.x() - second.x(), -1, 1)
            && Rules.isBetween(first.y() - second.y(), -1, 1);
    }

}
