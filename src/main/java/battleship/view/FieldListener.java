package battleship.view;

import java.util.function.*;

import battleship.model.*;

public class FieldListener implements BiConsumer<Coordinate, Field> {

    private final FieldGrid grid;

    public FieldListener(final FieldGrid grid) {
        this.grid = grid;
    }

    @Override
    public void accept(final Coordinate coordinate, final Field field) {
        this.grid.setField(coordinate, field);
    }

}
