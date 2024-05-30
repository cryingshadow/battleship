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

    private final Stack<Coordinate> coordinates;

    private Optional<Turn> currentTurn;

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
        final JLabel status
    ) {
        this.rules = rules;
        this.opponent = opponent;
        this.ownFieldListener = ownFieldListener;
        this.opponentFieldListener = opponentFieldListener;
        this.status = status;
        this.coordinates = new Stack<Coordinate>();
        this.game = new Game();
        this.currentTurn = this.getNextTurn();
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
            for (
                final Coordinate waterHit :
                    this.rules.getImpossibleCoordinatesAfterHit(Player.SECOND, coordinate, this.game)
            ) {
                this.opponentFieldListener.accept(waterHit, Field.WATER_HIT);
            }
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
            if (turn.toPlace().isEmpty()) {
                final Shot shot = this.opponent.getShot(this.rules, this.getFields(Player.FIRST, false));
                if (turn.action().apply(shot)) {
                    this.ownFieldListener.accept(shot.coordinate, this.game.getField(Player.FIRST, shot.coordinate));
                    for (
                        final Coordinate waterHit :
                            this.rules.getImpossibleCoordinatesAfterHit(Player.FIRST, shot.coordinate, this.game)
                    ) {
                        this.ownFieldListener.accept(waterHit, Field.WATER_HIT);
                    }
                    result = this.rules.getNextTurn(this.game);
                }
            } else if (
                turn.action().apply(
                    this.opponent.getShipPlacement(
                        this.rules,
                        this.getFields(Player.SECOND),
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
