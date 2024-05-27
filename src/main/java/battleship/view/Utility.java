package battleship.view;

import javax.swing.*;

public abstract class Utility {

    public static void center(final AbstractButton button) {
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
    }

    public static void center(final JLabel label) {
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
    }

}
