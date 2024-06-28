package battleship;

import javax.swing.*;

import battleship.ai.*;
import battleship.model.*;
import battleship.rules.*;
import battleship.view.*;

public class Main {

    public static void main(final String[] args) {
        final FieldGrid grid1 = new FieldGrid();
        final FieldGrid grid2 = new FieldGrid();
        final JLabel status = new JLabel();
        final FieldListener listener1 = new FieldListener(grid1);
        final FieldListener listener2 = new FieldListener(grid2);
        final MainFrame frame = new MainFrame(grid1, grid2, status);
        final ErrorMessenger errorMessenger = new ErrorMessenger(frame);
        final RuleEngine engine =
            new RuleEngine(StandardRules.INSTANCE, new SimpleAI(), listener1, listener2, status, errorMessenger);
        grid1.setFields(engine.toFieldArray(Player.FIRST, true));
        grid2.setFields(engine.toFieldArray(Player.SECOND, false));
        grid1.addListener(new PlacementListener(engine, Player.FIRST));
        grid2.addListener(new ShotListener(engine, Player.FIRST));
        frame.pack();
        frame.setVisible(true);
    }

}
