package aoop.asteroids.model.entity;

import aoop.asteroids.control.PlayerKeyListener;
import aoop.asteroids.model.game.MultiplayerGame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * Joiner is a Runnable class that represent a player in MultiplayerGame
 * it contains information about the player.
 */
public class Joiner extends Client implements Observer {
    /** PlayerKeyListener that joiner observes **/
    private PlayerKeyListener playerKeyListener;

    /** nickname of the joiner **/
    private String nickname;

    /** boolean that indicates whether joiner moved its ship **/
    private boolean shipMoved = false;


    /**
     * creates a new Joiner object
     * @param nickname nickname of the joiner
     * @param serverAddress Address of its connected server
     */
    public Joiner(String nickname, Address serverAddress) {
        super(serverAddress);
        this.nickname = nickname;
        this.game = new MultiplayerGame(nickname);
        this.playerKeyListener = new PlayerKeyListener(game.getMyShip());
        this.playerKeyListener.addObserver(this);
        this.outputThread = new JoinerOutputHandler(this);
        this.inputThread = new JoinerInputHandler(this);
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
     * @return nickname
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * @return playerKeyListener
     */
    public PlayerKeyListener getPlayerKeyListener() {
        return this.playerKeyListener;
    }

    /**
     * getter for shipMoved
     *
     * @return whether joiner has moved the ship
     */
    public boolean shipMoved() {
        return this.shipMoved;
    }

    /**
     * sets shipMoved to false
     */
    public void shipStop() {
        this.shipMoved = false;
    }

    /**
     * sets joiner's game model according to the received game models from the server and notifies its game panel
     * the Spaceship playerKeyListener is connected to is updated as well
     *
     * @param in ObjectInputStream that reads the packet
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void setGameModel(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.setGameModel(in);
        this.playerKeyListener.setLinkedShip(this.game.getMyShip());
        game.notifyListeners();
    }

    /**
     * the method is called by playerKeyListener when one of the game keys are pressed or released
     */
    @Override
    public void update(Observable o, Object arg) {
        if (!this.getGame().getMyShip().isDestroyed())
            shipMoved = true;
    }
}
