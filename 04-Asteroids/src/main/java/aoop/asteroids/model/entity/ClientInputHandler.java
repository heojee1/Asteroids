package aoop.asteroids.model.entity;

import java.io.*;
import java.net.DatagramPacket;

/**
 * ClientInputHandler is an abstract class whose subclasses JoinerInputHandler and SpectatorInputHandler
 * it contains the share features and functionalities among the two classes.
 */
public class ClientInputHandler extends Thread {
    ByteArrayInputStream byteIn = null;
    ObjectInputStream in = null;

    private Client client;

    /** constructor of ClientInputHandler
     *
     * @param client for which the ClientInputHandler works for
     */
    public ClientInputHandler(Client client) {
        this.client = client;
    }

    /**
     * loop that receives game model sent from the server and call to process the model
     */
    void receiveGameModel() {
        while (!this.isInterrupted() && client.isConnected() && !client.getGame().isAborted()) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[4000], 4000);
                client.getSocket().receive(packet);
                client.processGameData(packet);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * prepare input streams
     * @param packet DatagramPacket which will be read
     */
    void prepInputStream(DatagramPacket packet) {
        try {
            byteIn = new ByteArrayInputStream(packet.getData());
            in = new ObjectInputStream(byteIn);
            System.out.println("prepInputStream - in : " + in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
