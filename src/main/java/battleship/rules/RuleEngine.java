package battleship.rules;

import java.util.*;

import battleship.ai.*;
import battleship.model.*;
import battleship.view.*;

public class RuleEngine {

    private final Stack<Coordinate> coordinates;

    private Optional<Turn> currentTurn;

    private final Game game;

    private final AI opponent;

    private final FieldListener opponentFieldListener;

    private final FieldListener ownFieldListener;

    private final Rules rules;

    public RuleEngine(
        final Rules rules,
        final AI opponent,
        final FieldListener ownFieldListener,
        final FieldListener opponentFieldListener
    ) {
        this.rules = rules;
        this.opponent = opponent;
        this.ownFieldListener = ownFieldListener;
        this.opponentFieldListener = opponentFieldListener;
        this.coordinates = new Stack<Coordinate>();
        this.game = new Game();
        this.currentTurn = this.rules.getNextTurn(this.game);
    }

    public Field[][] getFields(final Player player) {
        return this.getFields(player, true);
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
            return true;
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
            }
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
            this.opponentFieldListener.accept(coordinate, this.game.getField(Player.SECOND, coordinate));
            this.currentTurn = this.getNextTurn();
            return true;
        }
        return false;
    }

    private Field[][] getFields(final Player player, final boolean showShips) {
        return this.game.toFieldArray(
            player,
            this.rules.getHorizontalLength(),
            this.rules.getVerticalLength(),
            showShips
        );
    }

    private Optional<Turn> getNextTurn() {
        Optional<Turn> result = this.rules.getNextTurn(this.game);
        while (result.isPresent() && result.get().player() == Player.SECOND) {
            final Turn turn = result.get();
            if (
                (
                    turn.toPlace().isEmpty()
                    && turn.action().apply(this.opponent.getShot(this.rules, this.getFields(Player.FIRST, false)))
                ) || (
                    turn.toPlace().isPresent()
                    && turn.action().apply(
                        this.opponent.getShipPlacement(
                            this.rules,
                            this.getFields(Player.SECOND),
                            turn.toPlace().get()
                        )
                    )
                )
            ) {
                result = this.rules.getNextTurn(this.game);
            }
        }
        return result;
    }

}
