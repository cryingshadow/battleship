package battleship.ai;

import battleship.model.*;
import battleship.rules.*;

public interface AI {

    ShipPlacement getShipPlacement(final Rules rules, final Field[][] ownFields, final ShipType type);

    Shot getShot(final Rules rules, final Field[][] opponentField);

}
