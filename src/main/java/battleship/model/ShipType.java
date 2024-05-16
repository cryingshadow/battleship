package battleship.model;

public enum ShipType {

    BATTLESHIP("Schlachtschiff", 4),
    CARRIER("Flugzeugträger", 5),
    CRUISER("Kreuzer", 3),
    DESTROYER("Zerstörer", 2);

    public int length;

    public String name;

    private ShipType(final String name, final int length) {
        this.name = name;
        this.length = length;
    }

}
