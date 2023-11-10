package aoop.asteroids.view;

import aoop.asteroids.game_observer.GameUpdateListener;
import aoop.asteroids.model.game.Game;

import javax.swing.*;
import java.awt.*;

/**
 * abstract panel at the center of the game's window which is responsible for the custom drawing of game objects.
 */
public abstract class GamePanel extends JPanel implements GameUpdateListener {
    /**
     * The x- and y-coordinates of the score indicator.
     */
    private static Point SCORE_INDICATOR_POSITION = new Point(20, 20);

    /**
     * The game model that this panel will draw to the screen.
     */
    Game game;

    /**
     * Number of milliseconds since the last time the game's physics were updated. This is used to continue drawing all
     * game objects as if they have kept moving, even in between game ticks.
     */
    long timeSinceLastTick = 0L;

    /**
     * constructor of a new GamePanel object
     * @param game The model which will be drawn in this panel.
     */
    public GamePanel(Game game) {
        this.game = game;
        this.game.addListener(this);
    }

    /**
     * The method provided by JPanel for 'painting' this component. It is overridden here so that this panel can define
     * some custom drawing. By default, a JPanel is just an empty rectangle.
     *
     * @param graphics The graphics object that exposes various drawing methods to use.
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.setBackground(Color.BLACK);
    }

    /**
     * Draws the ship's score and energy.
     *
     * @param graphics The graphics object that provides the drawing methods.
     */
    void drawShipInformation(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(
                String.valueOf(this.game.getMyShip().getScore()),
                SCORE_INDICATOR_POSITION.x,
                SCORE_INDICATOR_POSITION.y
        );
        graphics2D.setColor(Color.GREEN);
        graphics2D.drawRect(SCORE_INDICATOR_POSITION.x, SCORE_INDICATOR_POSITION.y + 20, 100, 15);
        graphics2D.fillRect(SCORE_INDICATOR_POSITION.x, SCORE_INDICATOR_POSITION.y + 20, (int) this.game.getMyShip().getEnergyPercentage(), 15);
    }

    /**
     * abstract method that draws all game objects
     * @param graphics The graphics object that provides the drawing methods.
     */
    abstract void drawGameObjects(Graphics graphics);

    /**
     * Do something when the game has indicated that it is updated. For this panel, that means redrawing.
     *
     * @param timeSinceLastTick The number of milliseconds since the game's physics were updated. This is used to allow
     *                          objects to continue to appear animated between each game tick.
     *
     * Note for your information: when repaint() is called, Swing does some internal stuff, and then paintComponent()
     * is called.
     */
    @Override
    public void onGameUpdated(long timeSinceLastTick) {
        this.timeSinceLastTick = timeSinceLastTick;
        repaint();
    }
}
