package aoop.asteroids.view;

import javax.swing.*;
import java.awt.*;

/**
 * abstract class of the frame displaying the game.
 */
public abstract class GameFrame extends JFrame {
    private static final String WINDOW_TITLE = "Asteroids";
    public static final Dimension WINDOW_SIZE = new Dimension(800, 800);

    /**
     * constructor of GameFrame
     */
    public GameFrame() {
        initSwingUI();
    }

    /**
     * basic UI set up (added more features by subclasses)
     */
    void initSwingUI() {
        this.setTitle(WINDOW_TITLE);
        this.setSize(WINDOW_SIZE);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
    }
}
