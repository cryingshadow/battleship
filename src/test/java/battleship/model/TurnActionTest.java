package battleship.model;

import org.testng.*;
import org.testng.annotations.*;

public class TurnActionTest {

    @Test(dataProvider="turnActionData")
    public void equalsTest(final TurnAction action1, final TurnAction action2, final boolean expected) {
        Assert.assertEquals(action1.equals(action2), expected);
    }

    @Test(dataProvider="turnActionData")
    public void hashCodeTest(final TurnAction action1, final TurnAction action2, final boolean expected) {
        Assert.assertEquals(action1.hashCode() == action2.hashCode(), expected);
    }

    @DataProvider
    public Object[][] turnActionData() {
        return new Object[][] {
            {new ShotAction(Player.FIRST), new ShotAction(Player.FIRST), true},
            {new ShotAction(Player.SECOND), new ShotAction(Player.SECOND), true},
            {new ShotAction(Player.FIRST), new ShotAction(Player.SECOND), false},
            {
                new ShipPlacementAction(Player.FIRST, ShipType.BATTLESHIP),
                new ShipPlacementAction(Player.FIRST, ShipType.BATTLESHIP),
                true
            },
            {
                new ShipPlacementAction(Player.SECOND, ShipType.BATTLESHIP),
                new ShipPlacementAction(Player.SECOND, ShipType.BATTLESHIP),
                true
            },
            {
                new ShipPlacementAction(Player.FIRST, ShipType.CRUISER),
                new ShipPlacementAction(Player.FIRST, ShipType.CRUISER),
                true
            },
            {
                new ShipPlacementAction(Player.FIRST, ShipType.BATTLESHIP),
                new ShipPlacementAction(Player.SECOND, ShipType.BATTLESHIP),
                false
            },
            {
                new ShipPlacementAction(Player.FIRST, ShipType.BATTLESHIP),
                new ShipPlacementAction(Player.FIRST, ShipType.CRUISER),
                false
            },
            {new ShipPlacementAction(Player.FIRST, ShipType.BATTLESHIP), new ShotAction(Player.FIRST), false}
        };
    }

}
