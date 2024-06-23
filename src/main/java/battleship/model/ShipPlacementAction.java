package battleship.model;

public class ShipPlacementAction extends TurnAction {

    public final ShipType shipType;

    public ShipPlacementAction(final Player player, final ShipType shipType) {
        super(player);
        this.shipType = shipType;
    }

    @Override
    public Boolean apply(final EventAndState eventAndState) {
        return eventAndState.rules().shipPlacement(
            eventAndState.game(),
            this.shipType,
            this.player,
            eventAndState.event()
        );
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof ShipPlacementAction) {
            final ShipPlacementAction other = (ShipPlacementAction)o;
            return this.player == other.player && this.shipType == other.shipType;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.player.hashCode() * 3 + this.shipType.hashCode() * 7;
    }

}
