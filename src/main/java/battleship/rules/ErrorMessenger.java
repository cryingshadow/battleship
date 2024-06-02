package battleship.rules;

import java.util.function.*;

import javax.swing.*;

public class ErrorMessenger implements Consumer<String> {

    private final JFrame parent;

    public ErrorMessenger(final JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void accept(final String errorMessage) {
        JOptionPane.showMessageDialog(this.parent, errorMessage);
    }

}
