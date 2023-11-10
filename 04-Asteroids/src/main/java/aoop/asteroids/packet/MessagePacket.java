package aoop.asteroids.packet;

import java.io.IOException;
import java.net.DatagramSocket;

import static aoop.asteroids.packet.PacketType.ACCEPT_CONNECTION;
import static aoop.asteroids.packet.PacketType.REQUEST_CONNECTION_JOINER;

/**
 * MessagePacket contains simple data regarding connection among server and client.
 */
public class MessagePacket extends GamePacket {
    private String nickname;
    private int idColor;

    /**
     * creates a new MessagePacket object
     *
     * @param socket socket from which data will be sent
     * @param type type of packet
     */
    public MessagePacket(DatagramSocket socket, PacketType type) {
        super(socket, type);
    }

    /**
     * creates a new MessagePacket object, specifically, a packet that requests connection to server
     * the additional field String nickname is sent
     *
     * @param socket socket from which data will be sent
     * @param type type of packet
     * @param nickname nickname of the player requesting connection
     */
    public MessagePacket(DatagramSocket socket, PacketType type, String nickname) {
        super(socket, type);
        this.nickname = nickname;
    }

    /**
     * creates a new MessagePacket object, specifically, a packet that accepts connection from client
     * the additional field int idColor is sent
     *
     * @param socket socket from which data will be sent
     * @param type type of packet
     * @param idColor assigned color to the client's Spaceship
     */
    public MessagePacket(DatagramSocket socket, PacketType type, int idColor) {
        super(socket, type);
        this.idColor = idColor;
    }

    /**
     * writes message onto ObjectOutputStream
     *
     * @throws IOException in case of error
     */
    @Override
    public void writePacket() throws IOException {
        out.writeInt(type.getId());
        if (type == REQUEST_CONNECTION_JOINER) {
            out.writeObject(this.nickname);
        } else if (type == ACCEPT_CONNECTION) {
            out.writeInt(this.idColor);
        }
    }
}
