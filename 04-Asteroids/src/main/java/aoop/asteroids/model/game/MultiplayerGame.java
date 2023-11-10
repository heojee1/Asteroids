package aoop.asteroids.model.game;

import aoop.asteroids.control.game_updater.MultiplayerGameUpdater;
import aoop.asteroids.model.entity.Server;
import aoop.asteroids.model.game_object.Asteroid;
import aoop.asteroids.model.game_object.Bullet;
import aoop.asteroids.model.game_object.Spaceship;

import java.awt.*;
import java.util.ArrayList;

/**
 * MultiplayerGame model contains all game objects of the game
 */
public class MultiplayerGame extends Game {
    private Server server;
    private ArrayList<Spaceship> ships = new ArrayList<>();
    private boolean aborted = false;

    /**
     * creates a new server-side MultiplayerGame object
     * @param server server who has the game
     */
    public MultiplayerGame(Server server) {
        this.myShip = new Spaceship(server.getNickname(), Color.WHITE);
        this.ships.add(myShip);
        this.server = server;
        initializeGameData();
    }

    /**
     * creates a new joiner-side MultiplayerGame object
     * @param nickname nickname of the joiner
     */
    public MultiplayerGame(String nickname) {
        this.myShip = new Spaceship(nickname, Color.WHITE);
        this.ships.add(myShip);
        initializeGameData();
    }

    /**
     * creates a new spectator-side MultiplayerGame object
     */
    public MultiplayerGame() {
        this.myShip = null;
        super.initializeGameData();
    }

    /**
     * Initializes all of the model objects used by the game.
     * The ships are set to the random location
     */
    @Override
    public void initializeGameData() {
        super.initializeGameData();
        this.ships.forEach(Spaceship::resetAtRandomLocation);
    }

    /**
     * @return spaceships list of the game
     */
    public ArrayList<Spaceship> getShips() {
        return this.ships;
    }

    /**
     * sets the game's asteroids list to a newer one
     *
     * @param newAsteroids new asteroids list
     */
    public void setAsteroids(ArrayList<Asteroid> newAsteroids) {
        this.asteroids = newAsteroids;
    }

    /**
     * sets the game's bullets list to a newer one
     *
     * @param newBullets new bullets list
     */
    public void setBullets(ArrayList<Bullet> newBullets) {
        this.bullets = newBullets;
    }

    /**
     * sets the game's ships list to a newer one.
     * note that it sets myShip is modified to point to a newer model of previous self
     *
     * @param newShips new ships list
     */
    public void setShips(ArrayList<Spaceship> newShips) {
        this.ships = newShips;
        if (this.ships.contains(this.myShip)) {
            int idx = this.ships.indexOf(this.myShip);
            this.myShip = this.ships.get(idx);
        }
    }

    /**
     * returns a spaceship with the given idColor
     * @param idColor color id to be looked up
     * @return Spaceship with the same color id
     */
    public Spaceship getShipByIdColor(Color idColor) {
        for (Spaceship ship : this.ships) {
            if (ship.getIdColor().equals(idColor)) {
                return ship;
            }
        }
        return null;
    }

    /**
     * Game is over when only one spaceship is alive
     *
     * @return whether game is over
     */
    @Override
    public boolean isGameOver() {
        int survivor = 0;
        for (Spaceship ship : this.ships) {
            if (!ship.isDestroyed()) {
                survivor++;
            }
        }
        return survivor == 1;
    }

    /**
     * @return whether or not game has been aborted
     */
    public boolean isAborted() {
        return this.aborted;
    }

    /**
     * abort the game. this could stop the input and output thread of the clients as well as server's main loop.
     */
    public void abort() {
        this.aborted = true;
        notifyListeners();
    }

    /**
     * awards a point to the last standing player
     */
    private void givePointToWinner() {
        for (Spaceship ship : ships) {
            if (!ship.isDestroyed()) {
                ship.increaseScore();
            }
        }
    }

    /**
     * awards point to the winner and initialized game data
     */
    @Override
    public void restart() {
        givePointToWinner();
        initializeGameData();
    }

    /**
     * Using this game's current model, spools up a new MultiplayerGameUpdater thread to begin a game loop and start processing
     * user input and physics updates. Only if the game isn't currently running, that is.
     * (note that this is only called by server)
     */
    @Override
    public void start() {
        if (!running) {
            running = true;
            gameUpdaterThread = new Thread(new MultiplayerGameUpdater(this, server));
            this.gameUpdaterThread.start();
        }
    }
}
