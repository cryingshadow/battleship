package battleship.model;

import java.util.function.*;

public record Turn(Player player, Consumer<Event> action, String prompt) {}
