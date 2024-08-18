package battleship.view;

import java.awt.*;

import javax.swing.*;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = -7810253204514052620L;

    public MainFrame(final FieldGrid grid1, final FieldGrid grid2, final JLabel status) {
        super("Schiffe versenken");
        final Container content = this.getContentPane();
        content.setLayout(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        content.add(grid1, constraints);
        constraints.gridx = 1;
        content.add(grid2, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        content.add(status, constraints);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.pack();
    }

}
