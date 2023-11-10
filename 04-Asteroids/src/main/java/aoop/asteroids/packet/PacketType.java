package aoop.asteroids.packet;

/**
 * PacketType is an enumeration of possible types data sent via UDP.
 *   id from 0 to 4 is dedicated for message regarding connection
 *   id 5 and 6 are reserved for GameObjects (entire Game model and a single Spaceship)
 */
public enum PacketType {
    REQUEST_CONNECTION_JOINER(0),
    REQUEST_CONNECTION_SPECTATOR(1),
    DISCONNECT(2),
    REJECT_CONNECTION(3),
    ACCEPT_CONNECTION(4),
    SHIP(5),
    GAME_MODEL(6);

    /**
     * int value that corresponds with each PacketType
     */
    private int id;

    /**
     * creates a new PacketType object
     *
     * @param id id of the PacketType to be created
     */
    PacketType(int id) {
        this.id = id;
    }

    /**
     * getter for an id of the type
     *
     * @return int id
     */
    public int getId() {
        return this.id;
    }

    /**
     * getter for type
     *
     * @param id of a type
     * @return type of the given id
     */
    public static PacketType getType(int id) {
        switch(id) {
            case 0:
                return REQUEST_CONNECTION_JOINER;
            case 1:
                return REQUEST_CONNECTION_SPECTATOR;
            case 2:
                return DISCONNECT;
            case 3:
                return REJECT_CONNECTION;
            case 4:
                return ACCEPT_CONNECTION;
            case 5:
                return SHIP;
            case 6:
                return GAME_MODEL;
            default:
                return null;
        }
    }

}
