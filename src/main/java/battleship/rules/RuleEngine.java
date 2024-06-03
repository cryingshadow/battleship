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
        if (
            this.currentTurn.isEmpty()
            || !player.equals(this.currentTurn.get().player())
            || this.currentTurn.get().toPlace().isEmpty()
        ) {
            return false;
        }
        final Turn currentTurn = this.currentTurn.get();
        if (this.coordinates.isEmpty()) {
            this.coordinates.push(coordinate);
            if (currentTurn.toPlace().get().length > 1) {
                return true;
            }
        }
        final Coordinate start = this.coordinates.pop();
        final ShipType toPlace = currentTurn.toPlace().get();
        final Optional<Direction> direction = ShipPlacement.toDirection(toPlace, start, coordinate);
        if (direction.isPresent()) {
            final ShipPlacement placement = new ShipPlacement(toPlace, start, direction.get(), player);
            if (currentTurn.action().apply(placement)) {
                placement.toCoordinates().forEach(c -> this.ownFieldListener.accept(c, Field.SHIP));
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
                    toPlace.name,
                    toPlace.length
                )
            );
        }
        return false;
    }

    public boolean shot(final Coordinate coordinate, final Player player) {
        if (
            this.currentTurn.isEmpty()
            || !player.equals(this.currentTurn.get().player())
            || this.currentTurn.get().toPlace().isPresent()
        ) {
            return false;
        }
        if (this.currentTurn.get().action().apply(new Shot(coordinate, player))) {
            final FieldListener listener = player == Player.FIRST ? this.opponentFieldListener : this.ownFieldListener;
            listener.accept(coordinate, this.getField(player.inverse(), coordinate));
            for (
                final Coordinate waterHit :
                    this.rules.getImpossibleCoordinatesAfterShot(player, coordinate, this.game)
            ) {
                this.opponentFieldListener.accept(waterHit, Field.WATER_HIT);
            }
            this.currentTurn = this.getNextTurn();
            return true;
        }
        return false;
    }

    public Field[][] toFieldArray(final Player player, final boolean showShips) {
        final int horizontalLength = this.rules.getHorizontalLength();
        final int verticalLength = this.rules.getVerticalLength();
        final Field[][] result = new Field[horizontalLength][verticalLength];
        for (int column = 0; column < horizontalLength; column++) {
            for (int row = 0; row < verticalLength; row++) {
                result[row][column] = Field.WATER;
            }
        }
        for (final Coordinate ship : this.game.getShipCoordinates(player)) {
            result[ship.row()][ship.column()] = Field.SHIP;
        }
        for (final Coordinate shot : this.game.getActualShotCoordinates(player.inverse())) {
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
            .isPresent() ?
                Field.WATER_HIT :
                    Field.WATER;
    }

    private Optional<Turn> getNextTurn() {
        Optional<Turn> result = this.rules.getNextTurn(this.game);
        while (result.isPresent() && result.get().player() == Player.SECOND) {
            final Turn turn = result.get();
            if (turn.toPlace().isEmpty()) {
                final Shot shot = this.opponent.getShot(this.rules, this.toFieldArray(Player.FIRST, false));
                if (turn.action().apply(shot)) {
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
                turn.action().apply(
                    this.opponent.getShipPlacement(
                        this.rules,
                        this.toFieldArray(Player.SECOND, true),
                        turn.toPlace().get()
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
