package battleship.model;

import java.util.function.*;

public abstract class TurnAction implements Function<EventAndState, Boolean> {

    public final Player player;

    public TurnAction(final Player player) {
        this.player = player;
    }

}
