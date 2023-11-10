package aoop.asteroids.view.button;

import aoop.asteroids.control.action.HostGameAction;
import aoop.asteroids.view.MenuFrame;

import javax.swing.*;

public class HostGameButton extends JButton {

    public HostGameButton(MenuFrame frame) {
        setAction(new HostGameAction(frame));
    }
}
