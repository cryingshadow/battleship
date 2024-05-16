package battleship.model;

public record Coordinate(int x, int y) {

    public Coordinate plusX(final int x) {
        return new Coordinate(this.x + x, this.y);
    }

    public Coordinate plusY(final int y) {
        return new Coordinate(this.x, this.y + y);
    }

    public Coordinate plus(final int length, final Direction direction) {
        switch (direction) {
        case NORTH:
            return this.plusY(-length);
        case SOUTH:
            return this.plusY(length);
        case WEST:
            return this.plusX(-length);
        case EAST:
            return this.plusX(length);
        default:
            throw new IllegalStateException("Unknown direction!");
        }
    }

}
