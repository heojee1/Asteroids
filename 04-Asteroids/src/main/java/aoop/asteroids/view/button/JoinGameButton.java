package aoop.asteroids.view.button;

import aoop.asteroids.control.action.JoinMultiplayerGameAction;
import aoop.asteroids.view.MenuFrame;

import javax.swing.*;

public class JoinGameButton extends JButton {

    public JoinGameButton(MenuFrame frame) {
        setAction(new JoinMultiplayerGameAction(frame));
    }
}
