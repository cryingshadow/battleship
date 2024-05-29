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
    public Optional<Turn> getNextTurn(final Game game) {
        if (this.getWinner(game).isPresent()) {
            return Optional.empty();
        }
        final Player player = StandardRules.determineCurrentPlayer(game);
        final int eventCount = (int)game.getEventsByPlayer(player).count();
        switch (eventCount) {
        case 0:
            return Optional.of(
                new Turn(
                    player,
                    event -> this.shipPlacement(game, ShipType.CARRIER, player, event),
                    Optional.of(ShipType.CARRIER),
                    "Geben Sie die Koordinaten Ihres Flugzeugträgers ein!"
                )
            );
        case 1:
            return Optional.of(
                new Turn(
                    player,
                    event -> this.shipPlacement(game, ShipType.BATTLESHIP, player, event),
                    Optional.of(ShipType.BATTLESHIP),
                    "Geben Sie die Koordinaten Ihres Schlachtschiffs ein!"
                )
            );
        case 2:
            return Optional.of(
                new Turn(
                    player,
                    event -> this.shipPlacement(game, ShipType.CRUISER, player, event),
                    Optional.of(ShipType.CRUISER),
                    "Geben Sie die Koordinaten Ihres Kreuzers ein!"
                )
            );
        case 3:
            return Optional.of(
                new Turn(
                    player,
                    event -> this.shipPlacement(game, ShipType.DESTROYER, player, event),
                    Optional.of(ShipType.DESTROYER),
                    "Geben Sie die Koordinaten Ihres Zerstörers ein!"
                )
            );
        case 4:
            return Optional.of(
                new Turn(
                    player,
                    event -> this.shipPlacement(game, ShipType.CANNON_BOAT, player, event),
                    Optional.of(ShipType.CANNON_BOAT),
                    "Geben Sie die Koordinaten Ihres Kanonenboots ein!"
                )
            );
        default:
            return Optional.of(
                new Turn(
                    player,
                    event -> this.shot(game, player, event),
                    Optional.empty(),
                    "Geben Sie die Koordinaten Ihres nächsten Schusses ein!"
                )
            );
        }
    }

    @Override
    public int getVerticalLength() {
        return 10;
    }

    @Override
    public Optional<Player> getWinner(final Game game) {
        if (StandardRules.allHit(Player.FIRST, game)) {
            return Optional.of(Player.SECOND);
        }
        if (StandardRules.allHit(Player.SECOND, game)) {
            return Optional.of(Player.FIRST);
        }
        return Optional.empty();
    }

    @Override
    public boolean placementConflict(final Coordinate first, final Coordinate second) {
        return Rules.isBetween(first.column() - second.column(), -1, 1)
            && Rules.isBetween(first.row() - second.row(), -1, 1);
    }

}
