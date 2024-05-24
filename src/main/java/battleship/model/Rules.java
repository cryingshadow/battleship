package battleship.model;

import java.util.stream.*;

public interface Rules {

    int getMaxX();

    int getMaxY();

    Turn getNextTurn();

    Stream<ShipType> getShipsToPlace();

    boolean placementConflict(Coordinate first, Coordinate second);

}
