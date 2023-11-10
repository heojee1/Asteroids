package aoop.asteroids.view.button;

import aoop.asteroids.control.action.SeeRankingAction;

import javax.swing.*;

public class SeeRankingButton extends JButton {

    public SeeRankingButton() {
        setAction(new SeeRankingAction());
    }
}
