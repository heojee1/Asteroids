package aoop.asteroids.view.button;

import aoop.asteroids.control.action.StartSingleplayerGameAction;
import aoop.asteroids.view.MenuFrame;

import javax.swing.*;

public class SingleplayerButton extends JButton {

    public SingleplayerButton(MenuFrame frame) {
        setAction(new StartSingleplayerGameAction(frame));
    }
}
