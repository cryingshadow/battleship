package battleship.model;

import java.util.function.*;

public record Turn(Player player, BiConsumer<Game, Event> action, Function<Game, String> prompt) {}
