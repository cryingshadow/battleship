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
        this.setLayout(new GridLayout(fields.length + 1, fields[0].length + 1));
        this.add(new JLabel());
        for (int x = 0; x < fields[0].length; x++) {
            final JLabel label = new JLabel(String.valueOf((char)('A' + x)));
            Utility.center(label);
            this.add(label);
        }
        for (int y = 0; y < fields.length; y++) {
            final JLabel label = new JLabel(String.valueOf(y + 1));
            Utility.center(label);
            this.add(label);
            for (int x = 0; x < fields[y].length; x++) {
                final FieldDisplay display = new FieldDisplay(fields[y][x], new Coordinate(x, y), listener);
                this.fields[y][x] = display;
                Utility.center(display);
                this.add(display);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return this.getSize();
    }

    @Override
    public Dimension getSize() {
        final int size = this.fields[0][0].getSize().width;
        return new Dimension(size * (this.fields[0].length + 1), size * (this.fields.length + 1));
    }

    public void setField(final Coordinate coordinate, final Field field) {
        this.setField(coordinate.x(), coordinate.y(), field);
    }

    public void setField(final int x, final int y, final Field field) {
        this.fields[y][x].setField(field);
        this.repaint();
    }

}
