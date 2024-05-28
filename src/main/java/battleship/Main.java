package battleship;

import battleship.ai.*;
import battleship.model.*;
import battleship.rules.*;
import battleship.view.*;

public class Main {

    public static void main(final String[] args) {
        final FieldGrid grid1 = new FieldGrid();
        final FieldGrid grid2 = new FieldGrid();
        final FieldListener listener1 = new FieldListener(grid1);
        final FieldListener listener2 = new FieldListener(grid2);
        final RuleEngine engine = new RuleEngine(new StandardRules(), new SimpleAI(), listener1, listener2);
        grid1.setFields(engine.getFields(Player.FIRST));
        grid2.setFields(engine.getFields(Player.SECOND));
        grid1.addListener(new PlacementListener(engine, Player.FIRST));
        grid2.addListener(new ShotListener(engine, Player.FIRST));
        new MainFrame(grid1, grid2).setVisible(true);
    }

    public boolean someLibraryMethod() {
        return true;
    }
}
