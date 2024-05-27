package battleship.view;

import java.awt.*;
import java.util.function.*;

import javax.swing.*;

import battleship.model.*;

public class FieldGrid extends JPanel {

    private static final long serialVersionUID = -2485007638406874995L;

    private final FieldDisplay[][] fields;

    public FieldGrid(final Field[][] fields, final Consumer<Coordinate> listener) {
        this.fields = new FieldDisplay[fields.length][fields[0].length];
        this.setLayout(new GridLayout(fields.length, fields[0].length));
        for (int y = 0; y < fields.length; y++) {
            for (int x = 0; x < fields[y].length; x++) {
                final FieldDisplay display = new FieldDisplay(fields[y][x], new Coordinate(x, y), listener);
                this.fields[y][x] = display;
                this.add(display);
            }
        }
    }

    public void setField(final int x, final int y, final Field field) {
        this.fields[y][x].setField(field);
        this.repaint();
    }

}
