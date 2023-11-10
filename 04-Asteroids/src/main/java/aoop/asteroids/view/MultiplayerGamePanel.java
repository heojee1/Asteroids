package aoop.asteroids.view;

import aoop.asteroids.model.game.MultiplayerGame;
import aoop.asteroids.view.view_model.AsteroidViewModel;
import aoop.asteroids.view.view_model.BulletViewModel;
import aoop.asteroids.view.view_model.SpaceshipViewModel;

import java.awt.*;

/**
 * Game panel of Multiplayer game
 */
public class MultiplayerGamePanel extends GamePanel {
    private static Point SCORE_TABLE_POSITION = new Point(100, 20);
    private final static int COLUMN_SPACING = 40;
    private final static int ROW_SPACING = 35;
    private MultiplayerGame game;

    /**
     * creates a new MultiplayerGamePanel object
     *
     * @param game The model which will be drawn in this panel.
     */
    public MultiplayerGamePanel(MultiplayerGame game) {
        super(game);
        this.game = game;
    }

    /**
     * the method provided by JPanel for 'painting' this component.
     *
     * @param graphics The graphics object that exposes various drawing methods to use.
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (this.game.getMyShip() != null)
            drawShipInformation(graphics);
        drawOpponentScores(graphics);
        drawGameObjects(graphics);

        if (this.game.isAborted()) drawQuitMessage(graphics);
    }

    /**
     * draw scores of opponent ships
     * @param graphics
     */
    private void drawOpponentScores(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        this.game.getShips().forEach(ship -> {
            if (ship.getNickname() != null) { // in case of spectator whose nickname is null, draw all
                graphics2D.setColor(ship.getIdColor());
                graphics2D.drawString(
                        ship.getNickname(),
                        SCORE_TABLE_POSITION.x += COLUMN_SPACING,
                        SCORE_TABLE_POSITION.y
                );
                graphics2D.drawString(
                        String.valueOf(ship.getScore()),
                        SCORE_TABLE_POSITION.x,
                        SCORE_TABLE_POSITION.y + ROW_SPACING
                );
            }

        });
        SCORE_TABLE_POSITION.setLocation(100,20);
    }

    /**
     * Draws all of the game's objects. Wraps each object in a view model, then uses that to draw the object.
     *
     * @param graphics The graphics object that provides the drawing methods.
     */
    @Override
    void drawGameObjects(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;

        synchronized (this.game) {
            this.game.getShips().forEach(ship -> {
                if (!ship.isDestroyed())
                    new SpaceshipViewModel(ship).drawObject(graphics2D, this.timeSinceLastTick);
            });
            this.game.getAsteroids().forEach(asteroid -> new AsteroidViewModel(asteroid).drawObject(graphics2D, this.timeSinceLastTick));
            this.game.getBullets().forEach(bullet -> new BulletViewModel(bullet).drawObject(graphics2D, this.timeSinceLastTick));
        }
    }

    void drawQuitMessage(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("GAME ENDED", this.getWidth() / 2-20, this.getHeight() / 2);
    }

    @Override
    public void onConnectionUpdated() {
        repaint();
    }
}
