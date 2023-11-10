package aoop.asteroids.model.game;

import aoop.asteroids.game_observer.ObservableGame;
import aoop.asteroids.model.game_object.*;

import java.util.ArrayList;

/**
 * Game is an abstract class whose subclasses are MultiplayerGame SingleplayerGame
 * this class is the main model for the Asteroids game. It contains all game object and has methods to start and stop the game.
 *
 * This is strictly a model class, containing only the state of the game. Updates to the game are done in
 * GameUpdater class, which runs in its own thread, and manages the main game loop and physics updates.
 */
public abstract class Game extends ObservableGame {
    /**
     * The spaceship object that the player is in control of.
     */
    Spaceship myShip;

    /**
     * The list of all bullets currently active in the game.
     */
    ArrayList<Bullet> bullets;

    /**
     * The list of all asteroids in the game.
     */
    ArrayList<Asteroid> asteroids;

    /**
     * Indicates whether or not the game is running. Setting this to false causes the game to exit its loop and quit.
     */
    volatile boolean running = false;

    /**
     * The game updater thread, which is responsible for updating the game's state as time goes on.
     */
    Thread gameUpdaterThread;

    /**
     * Initializes all of the model objects used by the game. Can also be used to reset the game's state back to a
     * default starting state before beginning a new game.
     */
    public void initializeGameData() {
        this.bullets = new ArrayList<>();
        this.asteroids = new ArrayList<>();
    }

    /**
     * @return The game's spaceship.
     */
    public Spaceship getMyShip() {
        return this.myShip;
    }

    /**
     * @return The collection of asteroids in the game.
     */
    public ArrayList<Asteroid> getAsteroids() {
        return this.asteroids;
    }

    /**
     * @return The collection of bullets in the game.
     */
    public ArrayList<Bullet> getBullets ()
    {
        return this.bullets;
    }

    /**
     * an abstract method that returns whether or not game is over
     */
    public abstract boolean isGameOver();

    /**
     * @return Whether or not the game is running.
     */
    public synchronized boolean isRunning() {
        return this.running;
    }

    /**
     * abstract method that restarts the game when game is over
     */
    public abstract void restart();

    /**
     * abstract method that creates and runs a gameUpdaterThread
     */
    public abstract void start();

    /**
     * Tries to quit the game, if it is running.
     */
    public void quit() {
        if (this.running) {
            try { // Attempt to wait for the game updater to exit its game loop.
                this.gameUpdaterThread.join(100);
            } catch (InterruptedException exception) {
                System.err.println("Interrupted while waiting for the game updater thread to finish execution.");
            } finally {
                this.running = false;
                this.gameUpdaterThread = null; // Throw away the game updater thread and let the GC remove it.
            }
        }
    }

}
