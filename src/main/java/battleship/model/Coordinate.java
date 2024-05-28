package battleship.model;

public record Coordinate(int column, int row) {

    public Coordinate plusColumn(final int column) {
        return new Coordinate(this.column + column, this.row);
    }

    public Coordinate plusRow(final int row) {
        return new Coordinate(this.column, this.row + row);
    }

    public Coordinate plus(final int length, final Direction direction) {
        switch (direction) {
        case NORTH:
            return this.plusRow(-length);
        case SOUTH:
            return this.plusRow(length);
        case WEST:
            return this.plusColumn(-length);
        case EAST:
            return this.plusColumn(length);
        default:
            throw new IllegalStateException("Unknown direction!");
        }
    }

}
