package battleship.model;

public class ShotAction extends TurnAction {

    public ShotAction(final Player player) {
        super(player);
    }

    @Override
    public Boolean apply(final EventAndState eventAndState) {
        return eventAndState.rules().shot(eventAndState.game(), this.player, eventAndState.event());
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof ShotAction) {
            final ShotAction other = (ShotAction)o;
            return this.player == other.player;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.player.hashCode() * 5;
    }

}
