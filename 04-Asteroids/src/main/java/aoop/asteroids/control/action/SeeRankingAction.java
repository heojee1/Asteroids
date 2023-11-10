package aoop.asteroids.control.action;

import aoop.asteroids.view.RankingFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * SeeRankingAction creates a new RankingFrame
 */
public class SeeRankingAction extends AbstractAction {

    /**
     * creates a new SeeRankingAction object
     */
    public SeeRankingAction() {
        super("See Ranking");
    }

    /**
     * creates a new RankingFrame
     * @param e event firing the action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        new RankingFrame();
    }
}
