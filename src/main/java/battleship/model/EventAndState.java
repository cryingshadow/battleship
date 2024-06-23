package battleship.model;

import battleship.rules.*;

public record EventAndState(Rules rules, Game game, Event event) {}
