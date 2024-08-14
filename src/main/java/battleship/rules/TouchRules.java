package battleship.rules;

import java.util.*;

import battleship.model.*;

public class TouchRules implements Rules {

    public static final TouchRules INSTANCE = new TouchRules();

    private TouchRules() {}

    @Override
    public int getHorizontalLength() {
        return StandardRules.INSTANCE.getHorizontalLength();
    }

    @Override
    public Set<Coordinate> getImpossibleCoordinatesAfterShot(
        final Player playerWhoShot,
        final Coordinate shot,
        final Game game
    ) {
        return Collections.emptySet();
    }

    @Override
    public Optional<Turn> getNextTurn(final Game game) {
        return StandardRules.INSTANCE.getNextTurn(game);
    }

    @Override
    public int getVerticalLength() {
        return StandardRules.INSTANCE.getVerticalLength();
    }

    @Override
    public Optional<Player> getWinner(final Game game) {
        return StandardRules.INSTANCE.getWinner(game);
    }

    @Override
    public boolean placementConflict(final Coordinate first, final Coordinate second) {
        return first.equals(second);
    }

}
