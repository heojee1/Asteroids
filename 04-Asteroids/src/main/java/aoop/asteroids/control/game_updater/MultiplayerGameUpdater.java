package aoop.asteroids.control.game_updater;

import aoop.asteroids.model.entity.Server;
import aoop.asteroids.model.game.MultiplayerGame;
import aoop.asteroids.model.game_object.*;
import aoop.asteroids.util.Network;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * MultiplayerGameUpdater updates its associated MultiplayerGame
 * Note that this is only used by Server. since the clients will never call to start the thread.
 * The thread can only be started by Server when the server user clicks the "Start Game" button.
 */
public class MultiplayerGameUpdater extends GameUpdater {
    private MultiplayerGame game;
    private Server server;

    /**
     * creates a new MultiplayerGame : used to create server-side game
     *      (since all the updates are done by server, only server needs an updater)
     * @param game The game that this updater will update when it's running.
     * @param server The server containing the game
     */
    public MultiplayerGameUpdater(MultiplayerGame game, Server server) {
        super(game);
        this.game = game;
        this.server = server;
    }

    /**
     * The main game loop.
     *
     * Starts the game updater thread. This will run until the quit() method is called on this updater's game object.
     */
    @Override
    public void run() {
        this.previousTime = System.currentTimeMillis();
        this.timeSinceLastTick = 0L;
        this.timeSinceLastDisplayFrame = 0L;
        Network.sendGameModel(server);
        while (!game.isAborted() && game.isRunning()) {
            if (!game.isGameOver()) {
                updateTimeData();
                updatePhysicsData();
                updateDisplayOnScreen();
            } else {
                restartGame();
            }
        }
        this.server.notifyServerClosed();
    }

    /**
     * Called every game tick, to update all of the game's model objects.
     *
     * First, each object's movement is updated by calling nextStep() on it.
     * Then, if the player is pressing the key to fire the ship's weapon, a new bullet should spawn.
     * Then, once all objects' positions are updated, we check for any collisions between them.
     * And finally, any objects which are destroyed by collisions are removed from the game.
     *
     * Also, every 200 game ticks, if possible, a new random asteroid is added to the game.
     */
    @Override
    public void updatePhysics() {
        ArrayList<Spaceship> ships  = this.game.getShips();
        ArrayList<Bullet> bullets = this.game.getBullets();
        ArrayList<Asteroid> asteroids = this.game.getAsteroids();

        asteroids.forEach(GameObject::nextStep);
        bullets.forEach(GameObject::nextStep);
        ships.forEach(GameObject::nextStep);

        ships.forEach(this::updateBulletsFiredPerShip);
        this.checkCollisions();
        this.removeDestroyedObjects();

        // Every 200 game ticks, try and spawn a new asteroid.
        if (this.updateCounter % 200 == 0 && asteroids.size() < this.asteroidsLimit) {
            this.addRandomAsteroid();
        }
        this.updateCounter++;
    }

    /**
     * If enough time has passed to refresh the display, not only for the server itself but for the connected clients.
     * it notifies the game panel of the server
     * and notifies the clients to update its display as well
     */
    @Override
    void updateDisplayOnScreen() {
        synchronized (this.game) {
            if (timeSinceLastDisplayFrame >= millisecondsPerDisplayFrame) {
                this.game.notifyListeners(timeSinceLastTick);
                timeSinceLastDisplayFrame = 0L;
                Network.sendGameModel(server);
            }
        }
    }

    /**
     * Checks all objects for collisions and marks them as destroyed upon collision. All objects can collide with
     * objects of a different type, but not with objects of the same type. I.e. bullets cannot collide with bullets etc.
     */
    @Override
    public void checkCollisions() {
        // First check collisions between bullets and other objects.
        this.game.getBullets().forEach(bullet -> {
            this.game.getAsteroids().forEach(asteroid -> { // Check collision with any of the asteroids.
                if (asteroid.collides(bullet)) {
                    asteroid.destroy();
                    bullet.destroy();
                }
            });
            this.game.getShips().forEach(ship -> { // Check collision with any of the spaceships.
                if (ship.collides(bullet) && !ship.isDestroyed()) {
                    bullet.destroy();
                    ship.destroy();
                }
            });
        });
        // Next check for collisions between asteroids and spaceships.
        this.game.getAsteroids().forEach(asteroid -> {
            this.game.getShips().forEach(ship -> {
                if(asteroid.collides(ship) && !ship.isDestroyed())  {
                    asteroid.destroy();
                    ship.destroy();
                }
            });
            if (KESSLER_SYNDROME) { // Only check for asteroid - asteroid collisions if we allow kessler syndrome.
                this.game.getAsteroids().forEach(secondAsteroid -> {
                    if (!asteroid.equals(secondAsteroid) && asteroid.collides(secondAsteroid)) {
                        asteroid.destroy();
                        secondAsteroid.destroy();
                    }
                });
            }
        });
    }

    /**
     * finds an available location for a new asteroid
     *
     * @param rng ThreadLocalRandom of its parent method (addRandomAsteroid)
     * @return location of a new asteroid
     */
    Point.Double findNewAsteroidLocation(ThreadLocalRandom rng) {
        ArrayList<Spaceship> ships = this.game.getShips();
        Point.Double newAsteroidLocation;
        double distanceX, distanceY;
        boolean keepLook;

        do {
            newAsteroidLocation = new Point.Double(rng.nextDouble(0.0, 800.0), rng.nextDouble(0.0, 800.0));
            keepLook = false;
            for (Spaceship ship : ships) {
                Point.Double shipLocation = ship.getLocation();
                distanceX = newAsteroidLocation.x - shipLocation.x;
                distanceY = newAsteroidLocation.y - shipLocation.y;
                if (distanceX * distanceX + distanceY * distanceY < 50 * 50) {
                    keepLook = true;
                    break;
                }
            }
        } while (keepLook);
        return newAsteroidLocation;
    }

    /**
     * Removes all destroyed asteroids and bullets from the game model
     * note that it does nothing when a Spaceship is destroyed.
     * unlike SingleplayerGame, MultiplayerGame has to maintain its information on Spaceships.
     * at the restart of the game, it can't simply reset the Spaceship's data unlike SingleplayerGame.
     * removing the Spaceship would mean permanently deleting the player's Spaceship.
     * Instead, destroyed Spaceships are dealt when drawing on MultiplayerGamePanel. (the panel only draws when it's not destroyed)
     *
     * When an asteroid is destroyed, it may spawn some smaller successor asteroids, and these are added to the game's
     * list of asteroids.
     */
    @Override
    public void removeDestroyedObjects() {
        ArrayList<Asteroid> newAsteroids = new ArrayList<>(this.game.getAsteroids().size() * 2); // Avoid reallocation and assume every asteroid spawns successors.
        this.game.getAsteroids().forEach(asteroid -> {
            if (asteroid.isDestroyed()) {
                newAsteroids.addAll(asteroid.getSuccessors());
            }
        });
        this.game.getAsteroids().addAll(newAsteroids);
        // Remove all asteroids that are destroyed.
        this.game.getAsteroids().removeIf(GameObject::isDestroyed);
        // Remove any bullets that are destroyed.
        this.game.getBullets().removeIf(GameObject::isDestroyed);
    }
}
