package battleship.rules;

import java.util.function.*;

import battleship.model.*;

public class ShotListener implements Consumer<Coordinate> {

    private final RuleEngine engine;

    private final Player player;

    public ShotListener(final RuleEngine engine, final Player player) {
        this.engine = engine;
        this.player = player;
    }

    @Override
    public void accept(final Coordinate coordinate) {
        this.engine.shot(coordinate, this.player);
    }

}
