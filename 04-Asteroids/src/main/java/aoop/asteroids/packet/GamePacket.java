package aoop.asteroids.packet;

import aoop.asteroids.model.entity.Address;
import aoop.asteroids.util.Network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static aoop.asteroids.packet.PacketType.DISCONNECT;

/**
 * GamePacket is an abstract class that hold the share features among the packets used for multiplayer game.
 *
 */
public abstract class GamePacket {
    private DatagramSocket socket;
    PacketType type;
    ByteArrayOutputStream byteOut;
    ObjectOutputStream out;

    /**
     * Constructor of GamePacket objects
     *
     * @param socket DatagramSocket that will send data
     * @param type PacketType of the data
     */
    public GamePacket(DatagramSocket socket, PacketType type) {
        this.socket = socket;
        this.type = type;
    }

    /**
     * writes data onto ObjectOutputStream
     *
     * @throws IOException in case of error
     */
    public abstract void writePacket() throws IOException;

    /**
     * Creates a new DatagramPacket and send it to the given address
     *
     * @param address destination Address
     */
    public synchronized void sendPacket(Address address) {
        try {
            prepareOutputStream();
            writePacket();
            out.flush();
            DatagramPacket packet = new DatagramPacket(byteOut.toByteArray(), byteOut.toByteArray().length, address.toSocketAddress());
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prepares new output streams
     */
    private void prepareOutputStream() {
        try {
            byteOut = new ByteArrayOutputStream();
            out = new ObjectOutputStream(byteOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
