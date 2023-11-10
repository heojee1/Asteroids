package aoop.asteroids.model.entity;

import aoop.asteroids.util.Network;

import static aoop.asteroids.packet.PacketType.DISCONNECT;
import static aoop.asteroids.packet.PacketType.REQUEST_CONNECTION_JOINER;

/**
 * Thread than handlers Joiner's output
 */
public class JoinerOutputHandler extends Thread {

    private Joiner joiner;

    /**
     * creates a new JoinerOutputHandler
     * @param joiner Joiner whose output has to be handled
     */
    public JoinerOutputHandler(Joiner joiner) {
        this.joiner = joiner;
    }

    /**
     * main loop
     *
     * when joiner is not connected, it attempts connection with the server by sending a REQUEST_CONNECTION_JOINER message
     * when connection is established, it sends its ship model every time the ship is moved
     * when the game is aborted or thread is interrupted, it sends DISCONNECT message to the server
     */
    @Override
    public void run() {
        if (!joiner.isConnected()) {
            Network.sendConnectionMessage(joiner.getSocket(), REQUEST_CONNECTION_JOINER, joiner.getNickname(), joiner.getServerAddress());
        }
        while (!this.isInterrupted() && !joiner.getGame().isAborted()) {
            if (joiner.isConnected() && joiner.shipMoved()) {
                Network.sendShipModel(joiner);
                joiner.shipStop();
            }
        }
        if (!joiner.getSocket().isClosed())
            Network.sendMessage(joiner.getSocket(), DISCONNECT, joiner.getServerAddress());
    }

}
