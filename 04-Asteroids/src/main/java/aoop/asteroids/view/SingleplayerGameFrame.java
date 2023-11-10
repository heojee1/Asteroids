package aoop.asteroids.view;

import aoop.asteroids.control.PlayerKeyListener;
import aoop.asteroids.model.game.SingleplayerGame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Game frame for the Multiplayer game
 */
public class SingleplayerGameFrame extends GameFrame {
    private SingleplayerGame game;

    /**
     * creates a new SingleplayerGameFrame
     * @param game game to be displayed
     */
    public SingleplayerGameFrame(SingleplayerGame game) {
        super();
        this.game = game;
        this.addKeyListener(new PlayerKeyListener(game.getMyShip()));
        this.add(new SingleplayerGamePanel(this.game));
    }

    /**
     * set up UI
     */
    @Override
    void initSwingUI() {
        super.initSwingUI();
        this.addWindowListener(new WindowAdapter() {
            // quit game upon closing
            @Override
            public void windowClosing(WindowEvent e) {
                game.quit();
                dispose();
            }

        });
    }
}
