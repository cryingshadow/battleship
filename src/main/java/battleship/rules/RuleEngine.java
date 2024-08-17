package battleship.rules;

import java.util.*;

import javax.swing.*;

import battleship.ai.*;
import battleship.model.*;
import battleship.view.*;

public class RuleEngine {

    private static String getWinnerText(final Player player) {
        return player == Player.FIRST ? "Du hast gewonnen!" : "Der Computer hat gewonnen!";
    }

    private static boolean hasCoordinate(final Event event, final Coordinate coordinate) {
        if (event instanceof Shot) {
            return ((Shot)event).coordinate.equals(coordinate);
        }
        return ((ShipPlacement)event).toCoordinates().filter(c -> c.equals(coordinate)).findAny().isPresent();
    }

    private static void shot(final Coordinate shot, final Field[][] field) {
        switch (field[shot.row()][shot.column()]) {
        case WATER:
        case WATER_HIT:
            field[shot.row()][shot.column()] = Field.WATER_HIT;
            break;
        case SHIP:
        case SHIP_HIT:
            field[shot.row()][shot.column()] = Field.SHIP_HIT;
            break;
        }
    }

    private final Stack<Coordinate> coordinates;

    private Optional<Turn> currentTurn;

    private final ErrorMessenger errorMessenger;

    private final Game game;

    private final AI opponent;

    private final FieldListener opponentFieldListener;

    private final FieldListener ownFieldListener;

    private final Rules rules;

    private final JLabel status;

    public RuleEngine(
        final Rules rules,
        final AI opponent,
        final FieldListener ownFieldListener,
        final FieldListener opponentFieldListener,
        final JLabel status,
        final ErrorMessenger errorMessenger
    ) {
        this.rules = rules;
        this.opponent = opponent;
        this.ownFieldListener = ownFieldListener;
        this.opponentFieldListener = opponentFieldListener;
        this.status = status;
        this.errorMessenger = errorMessenger;
        this.coordinates = new Stack<Coordinate>();
        this.game = new Game();
        this.currentTurn = this.getNextTurn();
    }

    public boolean placement(final Coordinate coordinate, final Player player) {
        if (this.currentTurn.isEmpty()) {
            return false;
        }
        final Turn currentTurn = this.currentTurn.get();
        if (!player.equals(currentTurn.action().player) || !(currentTurn.action() instanceof ShipPlacementAction)) {
            return false;
        }
        final ShipPlacementAction action = (ShipPlacementAction)currentTurn.action();
        if (this.coordinates.isEmpty()) {
            this.coordinates.push(coordinate);
            if (action.shipType.length > 1) {
                return true;
            }
        }
        final Coordinate start = this.coordinates.pop();
        final Optional<Direction> direction = ShipPlacement.toDirection(action.shipType, start, coordinate);
        if (direction.isPresent()) {
            final ShipPlacement placement = new ShipPlacement(action.shipType, start, direction.get(), player);
            if (currentTurn.action().apply(new EventAndState(this.rules, this.game, placement))) {
                if (player == Player.FIRST) {
                    placement.toCoordinates().forEach(c -> this.ownFieldListener.accept(c, Field.SHIP));
                }
                this.currentTurn = this.getNextTurn();
                return true;
            } else {
                this.errorMessenger.accept(
                    "Diese Platzierung verletzt die Abstandsregeln zu bereits platzierten Schiffen!"
                );
            }
        } else {
            this.errorMessenger.accept(
                String.format(
                    "Ein %s hat eine Länge von %d und muss waagerecht oder senkrecht platziert werden!",
                    action.shipType.name,
                    action.shipType.length
                )
            );
        }
        return false;
    }

    public boolean shot(final Coordinate coordinate, final Player player) {
        if (this.currentTurn.isEmpty()) {
            return false;
        }
        final Turn currentTurn = this.currentTurn.get();
        if (!player.equals(currentTurn.action().player) || !(currentTurn.action() instanceof ShotAction)) {
            return false;
        }
        final ShotAction action = (ShotAction)currentTurn.action();
        if (action.apply(new EventAndState(this.rules, this.game, new Shot(coordinate, player)))) {
            final FieldListener listener = player == Player.FIRST ? this.opponentFieldListener : this.ownFieldListener;
            listener.accept(coordinate, this.getField(player.inverse(), coordinate));
            for (
                final Coordinate waterHit :
                    this.rules.getImpossibleCoordinatesAfterShot(player, coordinate, this.game)
            ) {
                listener.accept(waterHit, Field.WATER_HIT);
            }
            this.currentTurn = this.getNextTurn();
            return true;
        }
        return false;
    }

    public Field[][] toFieldArray(final Player player, final boolean showShips) {
        final int horizontalLength = this.rules.getHorizontalLength();
        final int verticalLength = this.rules.getVerticalLength();
        final Field[][] result = new Field[verticalLength][horizontalLength];
        for (int column = 0; column < horizontalLength; column++) {
            for (int row = 0; row < verticalLength; row++) {
                result[row][column] = Field.WATER;
            }
        }
        for (final Coordinate ship : this.game.getShipCoordinates(player)) {
            result[ship.row()][ship.column()] = Field.SHIP;
        }
        for (final Coordinate shot : this.game.getActualShotCoordinates(player)) {
            RuleEngine.shot(shot, result);
            for (
                final Coordinate waterHit :
                    this.rules.getImpossibleCoordinatesAfterShot(player.inverse(), shot, this.game)
            ) {
                result[waterHit.row()][waterHit.column()] = Field.WATER_HIT;
            }
        }
        if (!showShips) {
            for (int column = 0; column < horizontalLength; column++) {
                for (int row = 0; row < verticalLength; row++) {
                    if (result[row][column] == Field.SHIP) {
                        result[row][column] = Field.WATER;
                    }
                }
            }
        }
        return result;
    }

    private Field getField(final Player player, final Coordinate coordinate) {
        final Field result =
            this.game.getEvents()
            .filter(event -> event.isShipPlacementEvent(player) || event.isShotEvent(player.inverse()))
            .filter(event -> RuleEngine.hasCoordinate(event, coordinate))
            .reduce(
                Field.WATER,
                (field, event) -> event.isShipPlacementEvent(player) ?
                    Field.SHIP :
                        (field == Field.WATER || field == Field.WATER_HIT ? Field.WATER_HIT : Field.SHIP_HIT),
                (field1, field2) -> field2);
        if (result != Field.WATER) {
            return result;
        }
        return
            this.game.getActualShotCoordinates(player.inverse()).stream()
            .flatMap(c -> this.rules.getImpossibleCoordinatesAfterShot(player.inverse(), c, this.game).stream())
            .filter(c -> c.equals(coordinate))
            .findAny()
            .isPresent() ? Field.WATER_HIT : Field.WATER;
    }

    private Optional<Turn> getNextTurn() {
        Optional<Turn> result = this.rules.getNextTurn(this.game);
        while (result.isPresent() && result.get().action().player == Player.SECOND) {
            final TurnAction action = result.get().action();
            if (action instanceof ShotAction) {
                final Shot shot = this.opponent.getShot(this.rules, this.toFieldArray(Player.FIRST, false));
                if (action.apply(new EventAndState(this.rules, this.game, shot))) {
                    this.ownFieldListener.accept(
                        shot.coordinate,
                        this.getField(Player.FIRST, shot.coordinate)
                    );
                    for (
                        final Coordinate waterHit :
                            this.rules.getImpossibleCoordinatesAfterShot(Player.SECOND, shot.coordinate, this.game)
                    ) {
                        this.ownFieldListener.accept(waterHit, Field.WATER_HIT);
                    }
                    result = this.rules.getNextTurn(this.game);
                }
            } else if (
                action.apply(
                    new EventAndState(
                        this.rules,
                        this.game,
                        this.opponent.getShipPlacement(
                            this.rules,
                            this.toFieldArray(Player.SECOND, true),
                            ((ShipPlacementAction)action).shipType
                        )
                    )
                )
            ) {
                result = this.rules.getNextTurn(this.game);
            }
        }
        if (result.isPresent()) {
            this.status.setText(result.get().prompt());
        } else {
            this.status.setText(RuleEngine.getWinnerText(this.rules.getWinner(this.game).get()));
        }
        return result;
    }

}
