package aoop.asteroids.model.entity;

import aoop.asteroids.model.game.MultiplayerGame;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Joiner is a Runnable class that represent a spectator in MultiplayerGame
 * it contains information about the spectator.
 */
public class Spectator extends Client {
    public Spectator(Address serverAddress) {
        super(serverAddress);
        this.game = new MultiplayerGame();
        this.inputThread = new SpectatorInputHandler(this);
        this.outputThread = new SpectatorOutputHandler(this);
    }

    /**
     * Main loop that starts its input and output threads
     */
    @Override
    public void run() {
        outputThread.start();
        inputThread.start();
    }

    /**
     * sets spectator's game model according to the received game models from the server and notifies its game panel
     *
     * @param in ObjectInputStream that reads the packet
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void setGameModel(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.setGameModel(in);
        game.notifyListeners();
        try { Thread.sleep(0); } catch (InterruptedException e) {}
    }
}
