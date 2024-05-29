package battleship.model;

public enum Player {

    FIRST, SECOND;

    public Player inverse() {
        switch (this) {
        case FIRST:
            return Player.SECOND;
        case SECOND:
            return Player.FIRST;
        default:
            throw new IllegalStateException("Unknown player detected!");
        }
    }

}
