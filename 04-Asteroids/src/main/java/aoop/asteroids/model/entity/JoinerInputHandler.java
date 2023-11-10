package aoop.asteroids.model.entity;

import aoop.asteroids.packet.PacketType;

import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static aoop.asteroids.packet.PacketType.ACCEPT_CONNECTION;

/**
 * Thread than handlers Joiner's input
 */
public class JoinerInputHandler extends ClientInputHandler {
    private Joiner joiner;
    private DatagramSocket socket;

    /**
     * creates a new JoinerInputHandler object
     * @param joiner Joiner whose input has to be handled
     */
    public JoinerInputHandler(Joiner joiner) {
        super(joiner);
        this.joiner = joiner;
        this.socket = joiner.getSocket();
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
                Color color = new Color(in.readInt());
                joiner.getGame().getMyShip().setIdColor(color);
                joiner.connect();
            } else {
                joiner.getGame().abort();
            }
        } catch (IOException e) {
            joiner.getGame().abort();
        }
        receiveGameModel();
    }

}
