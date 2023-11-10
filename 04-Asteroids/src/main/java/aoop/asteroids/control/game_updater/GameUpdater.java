package aoop.asteroids.control.game_updater;

import aoop.asteroids.model.game.Game;
import aoop.asteroids.model.game_object.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A runnable object which, when started in a thread, runs the main game loop and periodically updates the game's model
 * as time goes on. This class can be thought of as the 'Game Engine', because it is solely responsible for all changes
 * to the game model as a result of user input, and this class also defines the very important game loop itself.
 */
public abstract class GameUpdater implements Runnable {
    /**
     * The refresh rate of the display, in frames per second. Increasing this number makes the game look smoother, up to
     * a certain point where it's no longer noticeable.
     */
    static final int DISPLAY_FPS = 120;

    /**
     * The rate at which the game ticks (how often physics updates are applied), in frames per second. Increasing this
     * number speeds up everything in the game. Ships react faster to input, bullets fly faster, etc.
     */
    private static final int PHYSICS_FPS = 30;

    /**
     * The number of milliseconds in a game tick.
     */
    public static final double MILLISECONDS_PER_TICK = 1000.0 / PHYSICS_FPS;

    final static double millisecondsPerDisplayFrame = 1000.0 / DISPLAY_FPS;

    /**
     * previous time at start of it main loop
     */
    long previousTime;

    /**
     * time passed since last game tick
     */
    long timeSinceLastTick;

    /**
     * time passed since last display update on screen
     */
    long timeSinceLastDisplayFrame;

    /**
     * The default maximum number of asteroids that may be present in the game when starting.
     */
    static final int ASTEROIDS_LIMIT_DEFAULT = 7;

    /**
     * Set this to true to allow asteroids to collide with each other, potentially causing chain reactions of asteroid
     * collisions.
     */
    static final boolean KESSLER_SYNDROME = false;

    /**
     * The game that this updater works for.
     */
    protected Game game;

    /**
     * Counts the number of times the game has updated.
     */
    int updateCounter;

    /**
     * The limit to the number of asteroids that may be present. If the current number of asteroids exceeds this amount,
     * no new asteroids will spawn.
     */
    int asteroidsLimit;

    /**
     * constructor for GameUpdater
     * @param game The game that this updater will update when it's running.
     */
    public GameUpdater(Game game) {
        this.game = game;
        this.updateCounter = 0;
        this.asteroidsLimit = ASTEROIDS_LIMIT_DEFAULT;
    }

    /**
     * updates data regarding time
     * timeSinceLastTick, timeSinceLastDisplayFrame, previousTime are updated
     * to be used in checking for need to update physics or update display on screen
     */
    void updateTimeData() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - previousTime;
        timeSinceLastTick += elapsedTime;
        timeSinceLastDisplayFrame += elapsedTime;
        previousTime = currentTime;
    }

    /**
     * if enough time has passed, it updates physics.
     */
    void updatePhysicsData() {
        if (timeSinceLastTick >= MILLISECONDS_PER_TICK) { // Check if enough time has passed to update the physics.
            synchronized (this.game) {
                this.updatePhysics(); // Perform one 'step' in the game.
            }
            timeSinceLastTick = 0L;
        }
    }

    /**
     * restarts the game when the game is over
     */
    void restartGame() {
        game.restart();
        asteroidsLimit = ASTEROIDS_LIMIT_DEFAULT;
        updateCounter = 0;
    }

    /**
     * spawn new bullets while a player is pressing the key to fire the ship's weapon
     * Note that this is for one single player
     *
     * @param ship Spaceship of the player
     */
    void updateBulletsFiredPerShip(Spaceship ship) {
        ArrayList<Bullet> bullets = this.game.getBullets();
        if (ship.canFireWeapon() && !ship.isDestroyed()) {
            double direction = ship.getDirection();
            bullets.add(
                    new Bullet(
                            ship.getLocation().getX(),
                            ship.getLocation().getY(),
                            ship.getVelocity().x + Math.sin(direction) * 15,
                            ship.getVelocity().y - Math.cos (direction) * 15
                    )
            );
            ship.setFired();
        }
    }

    /**
     * Adds a random asteroid at least 50 pixels away from the any player's spaceship.
     */
    void addRandomAsteroid() {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        Point.Double newAsteroidLocation = findNewAsteroidLocation(rng);
        double randomChance = rng.nextDouble();
        Point.Double randomVelocity = new Point.Double(rng.nextDouble() * 6 - 3, rng.nextDouble() * 6 - 3);
        AsteroidSize randomSize;
        if (randomChance < 0.333) { // 33% chance of spawning a large asteroid.
            randomSize = AsteroidSize.LARGE;
        } else if (randomChance < 0.666) { // 33% chance of spawning a medium asteroid.
            randomSize = AsteroidSize.MEDIUM;
        } else { // And finally a 33% chance of spawning a small asteroid.
            randomSize = AsteroidSize.SMALL;
        }
        this.game.getAsteroids().add(new Asteroid(newAsteroidLocation, randomVelocity, randomSize));
    }

    /**
     * abstract method that updates physics every game tick
     */
    abstract void updatePhysics();

    /**
     * abstract method that checks all objects for collisions and marks them as destroyed upon collision
     */
    abstract void checkCollisions();

    /**
     * abstract method that removes all destroyed objects
     */
    abstract void removeDestroyedObjects();

    /**
     * abstract method that finds an available location for asteroids
     * @param rng ThreadLocalRandom to be used
     * @return Location of a new asteroid
     */
    abstract Point.Double findNewAsteroidLocation(ThreadLocalRandom rng);

    /**
     * abstract method that displays a its updated game model on screen
     */
    abstract void updateDisplayOnScreen();
}
