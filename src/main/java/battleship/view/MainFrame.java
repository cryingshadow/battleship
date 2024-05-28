package battleship.view;

import java.awt.*;

import javax.swing.*;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = -7810253204514052620L;

    public MainFrame(final FieldGrid grid1, final FieldGrid grid2) {
        final Container content = this.getContentPane();
        content.setLayout(new GridBagLayout());
        content.add(grid1);
        content.add(grid2);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.pack();
    }

}
