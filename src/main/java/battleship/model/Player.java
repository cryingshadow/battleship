package battleship.model;

public enum Player {

    FIRST, NONE, SECOND;

    public Player inverse() {
        switch (this) {
        case FIRST:
            return Player.SECOND;
        case SECOND:
            return Player.FIRST;
        default:
            return Player.NONE;
        }
    }

}
