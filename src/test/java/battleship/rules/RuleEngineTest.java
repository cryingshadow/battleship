package battleship.rules;

import java.util.*;

import javax.swing.*;

import org.testng.*;
import org.testng.annotations.*;

import battleship.ai.*;
import battleship.model.*;
import battleship.view.*;

public class RuleEngineTest {

    private static class DummyAI implements AI {

        int ship = 0;

        int shot = 0;

        @Override
        public ShipPlacement getShipPlacement(final Rules rules, final Field[][] ownFields, final ShipType type) {
            final int ship = this.ship;
            final int row = this.ship * 2;
            this.ship++;
            return new ShipPlacement(
                this.getType(ship),
                new Coordinate(0, row),
                Direction.EAST,
                Player.SECOND
            );
        }

        @Override
        public Shot getShot(final Rules rules, final Field[][] opponentField) {
            int count = 0;
            for (int row = 0; row < 10; row++) {
                for (int column = 0; column < 10; column++) {
                    if (count == this.shot) {
                        this.shot++;
                        return new Shot(new Coordinate(column, row), Player.SECOND);
                    }
                    count++;
                }
            }
            return new Shot(new Coordinate(0, 0), Player.SECOND);
        }

        ShipType getType(final int ship) {
            switch (ship) {
            case 0:
                return ShipType.CARRIER;
            case 1:
                return ShipType.BATTLESHIP;
            case 2:
                return ShipType.CRUISER;
            case 3:
                return ShipType.DESTROYER;
            default:
                return ShipType.CANNON_BOAT;
            }
        }

    }

    @DataProvider
    public Object[][] toFieldArrayData() {
        final FieldListener dummyListener = new FieldListener(null) {
            @Override
            public void accept(final Coordinate coordinate, final Field field) {}
        };
        final JLabel status = new JLabel();
        final ErrorMessenger dummyMessenger = new ErrorMessenger(null) {
            @Override
            public void accept(final String text) {}
        };
        final RuleEngine engine1 =
            new RuleEngine(
                StandardRules.INSTANCE,
                new DummyAI(),
                dummyListener,
                dummyListener,
                status,
                dummyMessenger
            );
        final RuleEngine engine2 =
            new RuleEngine(
                StandardRules.INSTANCE,
                new DummyAI(),
                dummyListener,
                dummyListener,
                status,
                dummyMessenger
            );
        engine2.placement(new Coordinate(0, 0), Player.FIRST);
        engine2.placement(new Coordinate(0, 4), Player.FIRST);
        engine2.placement(new Coordinate(9, 9), Player.FIRST);
        engine2.placement(new Coordinate(6, 9), Player.FIRST);
        engine2.placement(new Coordinate(7, 0), Player.FIRST);
        engine2.placement(new Coordinate(9, 0), Player.FIRST);
        engine2.placement(new Coordinate(5, 0), Player.FIRST);
        engine2.placement(new Coordinate(5, 1), Player.FIRST);
        engine2.placement(new Coordinate(5, 5), Player.FIRST);
        final RuleEngine engine3 =
            new RuleEngine(
                StandardRules.INSTANCE,
                new DummyAI(),
                dummyListener,
                dummyListener,
                status,
                dummyMessenger
            );
        engine3.placement(new Coordinate(0, 0), Player.FIRST);
        engine3.placement(new Coordinate(0, 4), Player.FIRST);
        engine3.placement(new Coordinate(9, 9), Player.FIRST);
        engine3.placement(new Coordinate(6, 9), Player.FIRST);
        engine3.placement(new Coordinate(7, 0), Player.FIRST);
        engine3.placement(new Coordinate(9, 0), Player.FIRST);
        engine3.placement(new Coordinate(5, 0), Player.FIRST);
        engine3.placement(new Coordinate(5, 1), Player.FIRST);
        engine3.placement(new Coordinate(5, 5), Player.FIRST);
        engine3.shot(new Coordinate(5, 5), Player.FIRST);
        engine3.shot(new Coordinate(0, 8), Player.FIRST);
        return new Object[][] {
            {
                engine1,
                Player.FIRST,
                true,
                new Field[][] {
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER}
                }
            },
            {
                engine2,
                Player.FIRST,
                true,
                new Field[][] {
                    {Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.SHIP, Field.WATER, Field.SHIP, Field.SHIP, Field.SHIP},
                    {Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.SHIP, Field.SHIP, Field.SHIP, Field.SHIP}
                }
            },
            {
                engine2,
                Player.SECOND,
                true,
                new Field[][] {
                    {Field.SHIP, Field.SHIP, Field.SHIP, Field.SHIP, Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.SHIP, Field.SHIP, Field.SHIP, Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.SHIP, Field.SHIP, Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.SHIP, Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER}
                }
            },
            {
                engine3,
                Player.FIRST,
                true,
                new Field[][] {
                    {Field.SHIP_HIT, Field.WATER_HIT, Field.WATER, Field.WATER, Field.WATER, Field.SHIP, Field.WATER, Field.SHIP, Field.SHIP, Field.SHIP},
                    {Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.SHIP, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.SHIP, Field.SHIP, Field.SHIP, Field.SHIP}
                }
            },
            {
                engine3,
                Player.SECOND,
                false,
                new Field[][] {
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER_HIT, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER_HIT, Field.WATER_HIT, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.SHIP_HIT, Field.WATER_HIT, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER},
                    {Field.WATER_HIT, Field.WATER_HIT, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER, Field.WATER}
                }
            }
        };
    }

    @Test(dataProvider="toFieldArrayData")
    public void toFieldArrayTest(
        final RuleEngine engine,
        final Player player,
        final boolean showShips,
        final Field[][] expected
    ) {
        Assert.assertTrue(Arrays.deepEquals(engine.toFieldArray(player, showShips), expected));
    }

}
