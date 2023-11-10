package aoop.asteroids.model.game;

import aoop.asteroids.control.game_updater.SingleplayerGameUpdater;
import aoop.asteroids.model.game_object.Spaceship;

import java.awt.*;

/**
 * MultiplayerGame model contains all game objects of the game
 */
public class SingleplayerGame extends Game {
    private String nickname;

    /**
     * creates a new Singleplayer object
     * @param nickname nickname of the player
     */
    public SingleplayerGame(String nickname) {
        this.nickname = nickname;
        this.myShip = new Spaceship();
        this.myShip.setNickname(nickname);
        initializeGameData();
    }

    /**
     * Initializes all of the model objects used by the game. Can also be used to reset the game's state back to a
     * default starting state before beginning a new game.
     */
    @Override
    public void initializeGameData() {
        super.initializeGameData();
        this.myShip.reset();
    }

    /**
     * @return True if the player's ship has been destroyed, or false otherwise.
     */
    @Override
    public boolean isGameOver() {
        return this.getMyShip().isDestroyed();
    }

    /**
     * restarts the game by initialize game data
     */
    @Override
    public void restart() {
        initializeGameData();
    }

    /**
     * Using this game's current model, spools up a new SingleplayerGameUpdater thread to begin a game loop and start processing
     * user input and physics updates. Only if the game isn't currently running, that is.
     */
    @Override
    public void start() {
        if (!running) {
            this.running = true;
            this.gameUpdaterThread = new Thread(new SingleplayerGameUpdater(this));
            this.gameUpdaterThread.start();
        }
    }

}
