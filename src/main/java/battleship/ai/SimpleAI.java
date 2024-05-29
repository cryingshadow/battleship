package battleship.ai;

import java.util.*;
import java.util.stream.*;

import battleship.model.*;
import battleship.rules.*;

public class SimpleAI implements AI {

    private final Random random = new Random();

    @Override
    public ShipPlacement getShipPlacement(final Rules rules, final Field[][] fields, final ShipType type) {
        final Stream.Builder<ShipPlacement> placementCandidates = Stream.builder();
        final List<Coordinate> shipCoordinates = new LinkedList<Coordinate>();
        for (int row = 0; row < fields.length; row++) {
            for (int column = 0; column < fields[row].length; column++) {
                final Coordinate start = new Coordinate(column, row);
                if (fields[row][column] == Field.SHIP) {
                    shipCoordinates.add(start);
                } else {
                    placementCandidates.accept(new ShipPlacement(type, start, Direction.NORTH, Player.SECOND));
                    placementCandidates.accept(new ShipPlacement(type, start, Direction.EAST, Player.SECOND));
                    placementCandidates.accept(new ShipPlacement(type, start, Direction.SOUTH, Player.SECOND));
                    placementCandidates.accept(new ShipPlacement(type, start, Direction.WEST, Player.SECOND));
                }
            }
        }
        final List<ShipPlacement> possiblePlacements =
            placementCandidates
            .build()
            .filter(placement -> rules.validShipPlacement(placement, shipCoordinates))
            .toList();
        if (possiblePlacements.isEmpty()) {
            throw new IllegalStateException("No valid ship placement is possible!");
        }
        return possiblePlacements.get(this.random.nextInt(possiblePlacements.size()));
    }

    @Override
    public Shot getShot(final Rules rules, final Field[][] fields) {
        final List<Coordinate> unshotCoordinates = new ArrayList<Coordinate>();
        for (int row = 0; row < fields.length; row++) {
            for (int column = 0; column < fields[row].length; column++) {
                if (fields[row][column] == Field.WATER || fields[row][column] == Field.SHIP) {
                    unshotCoordinates.add(new Coordinate(column, row));
                }
            }
        }
        if (unshotCoordinates.isEmpty()) {
            throw new IllegalStateException("No unshot field found and not won!");
        }
        return new Shot(unshotCoordinates.get(this.random.nextInt(unshotCoordinates.size())), Player.SECOND);
    }

}
