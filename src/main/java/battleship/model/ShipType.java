package battleship.model;

public enum ShipType {

    BATTLESHIP("Schlachtschiff", 4),
    CANNON_BOAT("Kanonenboot", 1),
    CARRIER("Flugzeugträger", 5),
    CRUISER("Kreuzer", 3),
    DESTROYER("Zerstörer", 2),
    SUBMARINE("U-Boot", 3);

    public final int length;

    public final String name;

    private ShipType(final String name, final int length) {
        this.name = name;
        this.length = length;
    }

}
