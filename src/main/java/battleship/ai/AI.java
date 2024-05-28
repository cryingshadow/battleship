package battleship.ai;

import battleship.model.*;

public interface AI {

    ShipPlacement getShipPlacement(Field[][] ownFields, ShipType type);

    Shot getShot(Field[][] opponentField);

}
