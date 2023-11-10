package aoop.asteroids.control.game_updater;

import aoop.asteroids.database.Database;
import aoop.asteroids.model.game.Game;
import aoop.asteroids.model.game_object.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * SingleplayerGameUpdater updates its associated SingleplayerGame
 */
public class SingleplayerGameUpdater extends GameUpdater {

    /**
     * creates a new SingleplayerGameUpdater
     * @param game game of played by the user
     */
    public SingleplayerGameUpdater (Game game) {
        super(game);
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

        while (this.game.isRunning()) {
            if (!this.game.isGameOver()) {
                updateTimeData();
                updatePhysicsData();
                updateDisplayOnScreen();
            } else {
                Database.insert(this.game.getMyShip().getNickname(), this.game.getMyShip().getScore());
                restartGame();
            }
        }
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
    void updatePhysics() {
        Spaceship ship = this.game.getMyShip();
        ArrayList<Bullet> bullets = this.game.getBullets();
        ArrayList<Asteroid> asteroids = this.game.getAsteroids();

        asteroids.forEach(GameObject::nextStep);
        bullets.forEach(GameObject::nextStep);
        ship.nextStep();

        this.updateBulletsFiredPerShip(ship);
        this.checkCollisions();
        this.removeDestroyedObjects();

        // Every 200 game ticks, try and spawn a new asteroid.
        if (this.updateCounter % 200 == 0 && asteroids.size() < this.asteroidsLimit) {
            this.addRandomAsteroid();
        }
        this.updateCounter++;
    }

    @Override
    void updateDisplayOnScreen() {
        if (timeSinceLastDisplayFrame >= millisecondsPerDisplayFrame) { // Check if enough time has passed to refresh the display.
            this.game.notifyListeners(timeSinceLastTick); // Tell the asteroids panel that it should refresh.
            timeSinceLastDisplayFrame = 0L;
        }
    }

    /**
     * finds an available location for a new asteroid
     *
     * @param rng ThreadLocalRandom of its parent method (addRandomAsteroid)
     * @return location of a new asteroid
     */
    Point.Double findNewAsteroidLocation(ThreadLocalRandom rng) {
        Point.Double newAsteroidLocation;
        Point.Double shipLocation = this.game.getMyShip().getLocation();
        double distanceX, distanceY;
        do {
            newAsteroidLocation = new Point.Double(rng.nextDouble(0.0, 800.0), rng.nextDouble(0.0, 800.0));
            distanceX = newAsteroidLocation.x - shipLocation.x;
            distanceY = newAsteroidLocation.y - shipLocation.y;
        } while (distanceX * distanceX + distanceY * distanceY < 50 * 50);
        return newAsteroidLocation;
    }

    /**
     * Checks all objects for collisions and marks them as destroyed upon collision. All objects can collide with
     * objects of a different type, but not with objects of the same type. I.e. bullets cannot collide with bullets etc.
     */
    @Override
    void checkCollisions() {
        // First check collisions between bullets and other objects.
        this.game.getBullets().forEach(bullet -> {
            this.game.getAsteroids().forEach(asteroid -> { // Check collision with any of the asteroids.
                if (asteroid.collides(bullet)) {
                    asteroid.destroy();
                    bullet.destroy();
                }
            });
            if (this.game.getMyShip().collides(bullet)) { // Check collision with ship.
                bullet.destroy();
                this.game.getMyShip().destroy();
            }
        });
        // Next check for collisions between asteroids and the spaceship.
        this.game.getAsteroids().forEach(asteroid -> {
            if (asteroid.collides(this.game.getMyShip())) {
                asteroid.destroy();
                this.game.getMyShip().destroy();
            }
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
     * Increment the player's score, and for every five score points, the asteroids limit is incremented.
     */
    void increaseScore() {
        this.game.getMyShip().increaseScore();
        if (this.game.getMyShip().getScore() % 5 == 0) {
            this.asteroidsLimit++;
        }
    }

    /**
     * Removes all destroyed objects (those which have collided with another object).
     *
     * When an asteroid is destroyed, it may spawn some smaller successor asteroids, and these are added to the game's
     * list of asteroids.
     */
    @Override
    void removeDestroyedObjects ()
    {
        ArrayList<Asteroid> newAsteroids = new ArrayList<>(this.game.getAsteroids().size() * 2); // Avoid reallocation and assume every asteroid spawns successors.
        this.game.getAsteroids().forEach(asteroid -> {
            if (asteroid.isDestroyed()) {
                this.increaseScore();
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
