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
        g.fillOval(width / 2, height / 2, width / 2, height / 2);
    }

    private Field field;

    public FieldDisplay(final Field field, final Coordinate coordinate, final Consumer<Coordinate> listener) {
        this.field = field;
        this.addActionListener(
            new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    listener.accept(coordinate);
                }

            }
        );
    }

    @Override
    public void paint(final Graphics g) {
        final Dimension size = this.getSize();
        final int width = size.width;
        final int height = size.height;
        g.setColor(new Color(50, 50, 200));
        g.fillRect(0, 0, width, height);
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
            g.fillOval(width / 2, height / 2, width / 2 - 2, height / 2 - 2);
            break;
        default:
            // do nothing
        }
    }

    public void setField(final Field field) {
        this.field = field;
        this.repaint();
    }

}
