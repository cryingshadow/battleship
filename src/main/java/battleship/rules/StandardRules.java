package battleship.rules;

import java.util.*;
import java.util.stream.*;

import battleship.model.*;

public class StandardRules implements Rules {

    public static final StandardRules INSTANCE = new StandardRules();

    private static final String PROMPT_PATTERN = "Geben Sie die Koordinaten Ihres %s ein!";

    private static Player determineCurrentPlayer(final Game game) {
        if (game.getEventsByPlayer(Player.FIRST).count() > game.getEventsByPlayer(Player.SECOND).count()) {
            return Player.SECOND;
        }
        return Player.FIRST;
    }

    private static Stream<Coordinate> toSurroundingCoordinates(final Coordinate coordinate) {
        final Stream.Builder<Coordinate> result = Stream.builder();
        result.accept(coordinate.plusColumn(1));
        result.accept(coordinate.plusColumn(-1));
        result.accept(coordinate.plusRow(1));
        result.accept(coordinate.plusRow(-1));
        result.accept(coordinate.plusColumn(1).plusRow(1));
        result.accept(coordinate.plusColumn(1).plusRow(-1));
        result.accept(coordinate.plusColumn(-1).plusRow(1));
        result.accept(coordinate.plusColumn(-1).plusRow(-1));
        return result.build();
    }

    private StandardRules() {}

    @Override
    public int getHorizontalLength() {
        return 10;
    }

    @Override
    public Set<Coordinate> getImpossibleCoordinatesAfterShot(
        final Player playerWhoShot,
        final Coordinate shot,
        final Game game
    ) {
        final Player hitPlayer = playerWhoShot.inverse();
        final Optional<Event> placementCandidate =
            game
            .getEventsByPlayer(hitPlayer)
            .filter(event -> (event instanceof ShipPlacement))
            .filter(event -> ((ShipPlacement)event).toCoordinates().anyMatch(coordinate -> coordinate.equals(shot)))
            .findAny();
        if (placementCandidate.isEmpty()) {
            return Collections.emptySet();
        }
        final ShipPlacement placement = (ShipPlacement)placementCandidate.get();
        final Set<Coordinate> shots = game.getActualShotCoordinates(hitPlayer);
        if (placement.toCoordinates().allMatch(coordinate -> shots.contains(coordinate))) {
            return placement
                .toCoordinates()
                .flatMap(coordinate -> StandardRules.toSurroundingCoordinates(coordinate))
                .filter(coordinate -> !placement.toCoordinates().anyMatch(c -> c.equals(coordinate)))
                .filter(coordinate -> this.validCoordinate(coordinate))
                .collect(Collectors.toSet());
        }
        return Collections.emptySet();
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
                    new ShipPlacementAction(player, ShipType.CARRIER),
                    String.format(StandardRules.PROMPT_PATTERN, "Flugzeugträgers")
                )
            );
        case 1:
            return Optional.of(
                new Turn(
                    new ShipPlacementAction(player, ShipType.BATTLESHIP),
                    String.format(StandardRules.PROMPT_PATTERN, "Schlachtschiffs")
                )
            );
        case 2:
            return Optional.of(
                new Turn(
                    new ShipPlacementAction(player, ShipType.CRUISER),
                    String.format(StandardRules.PROMPT_PATTERN, "Kreuzers")
                )
            );
        case 3:
            return Optional.of(
                new Turn(
                    new ShipPlacementAction(player, ShipType.DESTROYER),
                    String.format(StandardRules.PROMPT_PATTERN, "Zerstörers")
                )
            );
        case 4:
            return Optional.of(
                new Turn(
                    new ShipPlacementAction(player, ShipType.CANNON_BOAT),
                    String.format(StandardRules.PROMPT_PATTERN, "Kanonenboots")
                )
            );
        default:
            return Optional.of(
                new Turn(
                    new ShotAction(player),
                    String.format(StandardRules.PROMPT_PATTERN, "nächsten Schusses")
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
        if (
            game.getEventsByPlayer(Player.FIRST).findAny().isEmpty()
            || game.getEventsByPlayer(Player.SECOND).findAny().isEmpty()
        ) {
            return Optional.empty();
        }
        final Set<Coordinate> shipCoordinatesOfFirstPlayer = game.getShipCoordinates(Player.FIRST);
        final Set<Coordinate> shipCoordinatesOfSecondPlayer = game.getShipCoordinates(Player.SECOND);
        final List<Event> shots = game
            .getEvents()
            .filter(event -> (event instanceof Shot)).toList();
        final Set<Event> events =
            shots.stream()
            .collect(Collectors.toCollection(TreeSet<Event>::new));
        for (final Event event : events) {
            final Shot shot = (Shot)event;
            switch (shot.player) {
            case FIRST:
                shipCoordinatesOfSecondPlayer.remove(shot.coordinate);
                if (shipCoordinatesOfSecondPlayer.isEmpty()) {
                    return Optional.of(Player.FIRST);
                }
                break;
            case SECOND:
                shipCoordinatesOfFirstPlayer.remove(shot.coordinate);
                if (shipCoordinatesOfFirstPlayer.isEmpty()) {
                    return Optional.of(Player.SECOND);
                }
                break;
            default:
                throw new IllegalStateException("Found shot event without player!");
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean placementConflict(final Coordinate first, final Coordinate second) {
        return Rules.isBetween(-1, first.column() - second.column(), 2)
            && Rules.isBetween(-1, first.row() - second.row(), 2);
    }

}
