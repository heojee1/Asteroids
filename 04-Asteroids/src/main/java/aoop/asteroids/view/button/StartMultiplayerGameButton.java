package aoop.asteroids.view.button;

import aoop.asteroids.control.action.StartMultiplayerGameAction;
import aoop.asteroids.model.game.MultiplayerGame;
import aoop.asteroids.view.ServerLobbyFrame;

import javax.swing.*;

public class StartMultiplayerGameButton extends JButton {

    public StartMultiplayerGameButton(MultiplayerGame game, ServerLobbyFrame frame) {
        setAction(new StartMultiplayerGameAction(game, frame));
    }
}
