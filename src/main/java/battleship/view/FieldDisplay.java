package battleship.view;

import java.awt.*;
import java.awt.event.*;
import java.util.function.*;

import javax.swing.*;

import battleship.model.*;

public class FieldDisplay extends JButton {

    private static final long serialVersionUID = -714726189546548708L;

    private static void drawShip(final Graphics g, final int width, final int height) {
        g.setColor(Color.BLACK);
        g.fillOval(1, 1, width - 2, height - 2);
    }

    private final Coordinate coordinate;

    private Field field;

    private int size;

    public FieldDisplay(final Field field, final Coordinate coordinate) {
        this.size = 20;
        this.field = field;
        this.coordinate = coordinate;
    }

    public void addListener(final Consumer<Coordinate> listener) {
        this.addActionListener(
            new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    listener.accept(FieldDisplay.this.coordinate);
                }

            }
        );
    }

    @Override
    public Dimension getPreferredSize() {
        return this.getSize();
    }

    @Override
    public Dimension getSize() {
        return new Dimension(this.size, this.size);
    }

    @Override
    public void paint(final Graphics g) {
        final Dimension size = this.getSize();
        final int width = size.width;
        final int height = size.height;
        g.setColor(new Color(100, 100, 200));
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(200, 200, 200));
        g.drawRect(0, 0, width, height);
        switch (this.field) {
        case WATER_HIT:
            g.setColor(Color.WHITE);
            g.drawLine(1, 1, width - 1, height - 1);
            g.drawLine(1, height - 1, width - 1, 1);
            break;
        case SHIP:
            FieldDisplay.drawShip(g, width, height);
            break;
        case SHIP_HIT:
            FieldDisplay.drawShip(g, width, height);
            g.setColor(Color.RED);
            g.fillOval(5, 5, width - 10, height - 10);
            break;
        default:
            // do nothing
        }
    }

    public void setField(final Field field) {
        this.field = field;
        this.repaint();
    }

    public void setSize(final int size) {
        this.size = size;
        this.invalidate();
        this.repaint();
    }

}
