package aoop.asteroids.packet;

import aoop.asteroids.model.game_object.Spaceship;

import java.io.IOException;
import java.net.DatagramSocket;

/**
 * PlayerModelPacket contains a player's Spaceship object
 */
public class PlayerModelPacket extends GamePacket {
    private Spaceship ship;

    /**
     * creates a new PlayerModelPacket object
     *
     * @param socket socket from which data will be sent
     * @param type type of packet
     * @param ship Spaceship controlled by the player
     */
    public PlayerModelPacket(DatagramSocket socket, PacketType type, Spaceship ship) {
        super(socket, type);
        this.ship = ship;
    }

    /**
     * writes data onto ObjectOutputStream
     *
     * @throws IOException in case of error
     */
    @Override
    public void writePacket() throws IOException {
        out.writeInt(type.getId());
        out.writeObject(ship);
    }
}
