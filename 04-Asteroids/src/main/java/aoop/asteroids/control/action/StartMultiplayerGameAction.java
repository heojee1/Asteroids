package aoop.asteroids.control.action;

import aoop.asteroids.control.PlayerKeyListener;
import aoop.asteroids.model.game.MultiplayerGame;
import aoop.asteroids.model.game_object.Spaceship;
import aoop.asteroids.view.MultiplayerGameFrame;
import aoop.asteroids.view.ServerLobbyFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * StartMultiplayerGameAction is fired when a button is clicked on a ServerLobbyFrame
 * it starts a server-side MultiPlayerGame in
 */
public class StartMultiplayerGameAction extends AbstractAction {
    private MultiplayerGame game;
    private ServerLobbyFrame lobbyFrame;

    /**
     * creates a new StartMultiplayerGameAction
     * @param game game to be started
     * @param frame current server lobby frame (to be disposed)
     */
    public StartMultiplayerGameAction(MultiplayerGame game, ServerLobbyFrame frame) {
        super("Start Game");
        this.game = game;
        this.lobbyFrame = frame;
    }

    /**
     * creates a new MultiplayerGameFrame
     * @param e e event firing the action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        new MultiplayerGameFrame(this.game, new PlayerKeyListener(game.getMyShip()));
        this.game.start();
        this.lobbyFrame.dispose();
    }
}
