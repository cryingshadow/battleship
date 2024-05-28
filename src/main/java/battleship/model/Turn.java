package battleship.model;

import java.util.*;
import java.util.function.*;

public record Turn(Player player, Function<Event, Boolean> action, Optional<ShipType> toPlace, String prompt) {}
