package aoop.asteroids.view;

import aoop.asteroids.control.PlayerKeyListener;
import aoop.asteroids.model.game.MultiplayerGame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Game frame for the Multiplayer game
 */
public class MultiplayerGameFrame extends GameFrame {
    private MultiplayerGame game;

    /**
     * creates a new MultiplayerGameFrame for the server and joiner who requires PlayerKeyListener
     * @param game game to be displayed
     * @param playerKeyListener PlayerKeyListener to be added to the frame
     */
    public MultiplayerGameFrame(MultiplayerGame game, PlayerKeyListener playerKeyListener) {
        this.game = game;
        this.addKeyListener(playerKeyListener);
        this.add(new MultiplayerGamePanel(this.game));
    }

    /**
     * creates a new MultiplayerGameFrame for spectator
     * @param game game to be displayed
     */
    public MultiplayerGameFrame(MultiplayerGame game) {
        this.game = game;
        this.add(new MultiplayerGamePanel(this.game));
    }

    /**
     * set up UI
     */
    @Override
    void initSwingUI() {
        super.initSwingUI();
        this.addWindowListener(new WindowAdapter() {
            // abort and quit game upon closing
            @Override
            public void windowClosing(WindowEvent e) {
                game.abort();
                game.quit();
                dispose();
            }
        });
    }
}
