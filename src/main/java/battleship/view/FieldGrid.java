package battleship.view;

import java.awt.*;
import java.util.*;
import java.util.function.*;

import javax.swing.*;

import battleship.model.*;

public class FieldGrid extends JPanel {

    private static final long serialVersionUID = -2485007638406874995L;

    private static void center(final AbstractButton button) {
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
    }

    private static void center(final JLabel label) {
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
    }

    private final java.util.List<Consumer<Coordinate>> coordinateListeners;

    private FieldDisplay[][] fields;

    public FieldGrid() {
        this.fields = new FieldDisplay[0][0];
        this.coordinateListeners = new LinkedList<Consumer<Coordinate>>();
    }

    public void addListener(final Consumer<Coordinate> listener) {
        this.coordinateListeners.add(listener);
        for (final FieldDisplay[] displayArray : this.fields) {
            for (final FieldDisplay display : displayArray) {
                display.addListener(listener);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return this.getSize();
    }

    @Override
    public Dimension getSize() {
        if (this.fields.length == 0) {
            return new Dimension(0, 0);
        }
        final int size = this.fields[0][0].getSize().width;
        return new Dimension(size * (this.fields[0].length + 1), size * (this.fields.length + 1));
    }

    public void setField(final Coordinate coordinate, final Field field) {
        this.setField(coordinate.column(), coordinate.row(), field);
    }

    public void setField(final int column, final int row, final Field field) {
        this.fields[row][column].setField(field);
        this.repaint();
    }

    public void setFields(final Field[][] fields) {
        this.removeAll();
        this.fields = new FieldDisplay[fields.length][fields[0].length];
        this.setLayout(new GridLayout(fields.length + 1, fields[0].length + 1));
        this.add(new JLabel());
        for (int column = 0; column < fields[0].length; column++) {
            final JLabel label = new JLabel(String.valueOf((char)('A' + column)));
            FieldGrid.center(label);
            this.add(label);
        }
        for (int row = 0; row < fields.length; row++) {
            final JLabel label = new JLabel(String.valueOf(row + 1));
            FieldGrid.center(label);
            this.add(label);
            for (int column = 0; column < fields[row].length; column++) {
                final FieldDisplay display = new FieldDisplay(fields[row][column], new Coordinate(column, row));
                for (final Consumer<Coordinate> listener : this.coordinateListeners) {
                    display.addListener(listener);
                }
                this.fields[row][column] = display;
                FieldGrid.center(display);
                this.add(display);
            }
        }
    }

}
