package aoop.asteroids.view;

import aoop.asteroids.model.game.SingleplayerGame;
import aoop.asteroids.view.view_model.AsteroidViewModel;
import aoop.asteroids.view.view_model.BulletViewModel;
import aoop.asteroids.view.view_model.SpaceshipViewModel;

import java.awt.*;

/**
 * Game panel of Singleplayer game
 */
public class SingleplayerGamePanel extends GamePanel {

    /**
     * creates a new SingleplayerGamePanel object
     *
     * @param game The model which will be drawn in this panel.
     */
    public SingleplayerGamePanel(SingleplayerGame game) {
        super(game);
    }

    /**
     * the method provided by JPanel for 'painting' this component.
     *
     * @param graphics The graphics object that exposes various drawing methods to use.
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawShipInformation(graphics);
        drawGameObjects(graphics);
    }

    /**
     * Draws all of the game's objects. Wraps each object in a view model, then uses that to draw the object.
     *
     * @param graphics The graphics object that provides the drawing methods.
     */
    @Override
    void drawGameObjects(Graphics graphics) {
        /*
         * Because the game engine is running concurrently in its own thread, we must obtain a lock for the game model
         * while drawing to ensure that we don't encounter a concurrentModificationException, which would happen if we
         * were in the middle of drawing while the game engine starts a new physics update.
         */
        Graphics2D graphics2D = (Graphics2D) graphics;
        synchronized (this.game) {
            new SpaceshipViewModel(this.game.getMyShip()).drawObject(graphics2D, this.timeSinceLastTick);
            this.game.getAsteroids().forEach(asteroid -> new AsteroidViewModel(asteroid).drawObject(graphics2D, this.timeSinceLastTick));
            this.game.getBullets().forEach(bullet -> new BulletViewModel(bullet).drawObject(graphics2D, this.timeSinceLastTick));
        }
    }

    /**
     * necessary empty method since its parent class implements GameUpdateListener
     */
    @Override
    public void onConnectionUpdated() {
    }
}
