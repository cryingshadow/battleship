package battleship;

import battleship.rules.*;
import battleship.view.*;

public class Main {

    public static void main(final String[] args) {
        new MainFrame(new RuleEngine(new StandardRules())).setVisible(true);
    }

    public boolean someLibraryMethod() {
        return true;
    }
}
