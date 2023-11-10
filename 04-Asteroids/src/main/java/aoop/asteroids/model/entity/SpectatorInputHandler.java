package aoop.asteroids.model.entity;

import aoop.asteroids.packet.PacketType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static aoop.asteroids.packet.PacketType.ACCEPT_CONNECTION;

/**
 * Thread than handlers Joiner's input
 */
public class SpectatorInputHandler extends ClientInputHandler {
    private Spectator spectator;
    private DatagramSocket socket;

    /**
     * creates a new SpectatorInputHandler object
     * @param spectator Spectator whose input has to be handled
     */
    public SpectatorInputHandler(Spectator spectator) {
        super(spectator);
        this.spectator = spectator;
        this.socket = spectator.getSocket();
    }

    /**
     * main loop
     *
     * First, it attempts receives a packet from server regarding connection.
     * if the connection is attempted, it goes on to receive game models
     */
    @Override
    public void run() {
        try {
            DatagramPacket packet = new DatagramPacket(new byte[300], 300);
            socket.receive(packet);
            prepInputStream(packet);
            PacketType type = PacketType.getType(in.readInt());
            System.out.println(type);
            if (type == ACCEPT_CONNECTION) {
                spectator.connect();
            } else {
                spectator.getGame().abort();
            }
        } catch (IOException e) {
            spectator.getGame().abort();
        }
        receiveGameModel();
    }
}
