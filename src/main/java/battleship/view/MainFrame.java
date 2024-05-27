package battleship.view;

import java.awt.*;

import javax.swing.*;

import battleship.model.*;
import battleship.rules.*;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = -7810253204514052620L;

    public MainFrame(final RuleEngine engine) {
        final Container content = this.getContentPane();
        content.setLayout(new GridBagLayout());
        final FieldGrid grid =
            new FieldGrid(new Game().toFieldArray(Player.FIRST, 10, 10), new PlacementListener(engine, Player.FIRST));
        final FieldGrid grid2 =
            new FieldGrid(new Game().toFieldArray(Player.SECOND, 10, 10), new ShotListener(engine, Player.FIRST));
        content.add(grid);
        content.add(grid2);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.pack();
    }

}
