package battleship.rules;

import java.util.*;

import battleship.ai.*;
import battleship.model.*;
import battleship.view.*;

public class RuleEngine {

    private final Stack<Coordinate> coordinates;

    private Turn currentTurn;

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
        return this.game.toFieldArray(player, this.rules.getHorizontalLength(), this.rules.getVerticalLength());
    }

    public boolean placement(final Coordinate coordinate, final Player player) {
        if (!player.equals(this.currentTurn.player()) || this.currentTurn.toPlace().isEmpty()) {
            return false;
        }
        if (this.coordinates.isEmpty()) {
            this.coordinates.push(coordinate);
            return true;
        }
        final Coordinate start = this.coordinates.pop();
        final ShipType toPlace = this.currentTurn.toPlace().get();
        final Optional<Direction> direction = ShipPlacement.toDirection(toPlace, start, coordinate);
        if (direction.isPresent()) {
            final ShipPlacement placement = new ShipPlacement(toPlace, start, direction.get(), player);
            if (this.currentTurn.action().apply(placement)) {
                placement.toCoordinates().forEach(c -> this.ownFieldListener.accept(c, Field.SHIP));
                this.currentTurn = this.getNextTurn();
                return true;
            }
        }
        return false;
    }

    public boolean shot(final Coordinate coordinate, final Player player) {
        if (!player.equals(this.currentTurn.player()) || this.currentTurn.toPlace().isPresent()) {
            return false;
        }
        if (this.currentTurn.action().apply(new Shot(coordinate, player))) {
            this.opponentFieldListener.accept(coordinate, this.game.getField(Player.SECOND, coordinate));
            this.currentTurn = this.getNextTurn();
            return true;
        }
        return false;
    }

    private Turn getNextTurn() {
        // TODO Auto-generated method stub
        final Turn result = this.rules.getNextTurn(this.game);
        while (result.player() == Player.SECOND) {
            if (result.toPlace().isEmpty() && result.action().apply(this.opponent.getShot(this.getFields(null)))) {

            }
        }
        return result;
    }

}
