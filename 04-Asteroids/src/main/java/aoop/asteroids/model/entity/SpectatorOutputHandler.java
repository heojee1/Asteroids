package aoop.asteroids.model.entity;

import aoop.asteroids.util.Network;

import java.net.DatagramSocket;

import static aoop.asteroids.packet.PacketType.*;

/**
 * Thread than handlers Spectator's output
 */
public class SpectatorOutputHandler extends Thread {
    private Spectator spectator;
    private DatagramSocket socket;

    /**
     * creates a new SpectatorOutputHandler
     * @param spectator Spectator whose output has to be handled
     */
    public SpectatorOutputHandler(Spectator spectator) {
        this.spectator = spectator;
        this.socket = spectator.getSocket();
    }

    /**
     * main loop
     *
     * when joiner is not connected, it attempts connection with the server by sending a REQUEST_CONNECTION_SPECTATOR message
     * when connection is established, it sends its ship model every time the ship is moved
     * when the game is aborted or thread is interrupted, it sends DISCONNECT message to the server
     */
    @Override
    public void run() {
        if (!spectator.isConnected()) {
            System.out.println("try");
            Network.sendConnectionMessage(spectator.getSocket(), REQUEST_CONNECTION_SPECTATOR, null, spectator.getServerAddress());
        }
        while (!this.isInterrupted() && !spectator.getGame().isAborted());

        Network.sendMessage(spectator.getSocket(), DISCONNECT, spectator.getServerAddress());
    }
}
