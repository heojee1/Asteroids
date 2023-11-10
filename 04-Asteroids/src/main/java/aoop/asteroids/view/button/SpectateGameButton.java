package aoop.asteroids.view.button;

import aoop.asteroids.control.action.SpectateMultiplayerGameAction;
import aoop.asteroids.view.MenuFrame;

import javax.swing.*;

public class SpectateGameButton extends JButton {
    public SpectateGameButton(MenuFrame frame) {
        setAction(new SpectateMultiplayerGameAction(frame));
    }
}
