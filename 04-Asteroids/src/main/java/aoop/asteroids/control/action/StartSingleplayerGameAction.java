package aoop.asteroids.control.action;

import aoop.asteroids.model.game.SingleplayerGame;
import aoop.asteroids.util.Dialog;
import aoop.asteroids.view.MenuFrame;
import aoop.asteroids.view.SingleplayerGameFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * StartSingleplayerGameAction starts a new SingleplayerGame
 */
public class StartSingleplayerGameAction extends AbstractAction {
    private MenuFrame frame;

    /**
     * creates a new StartSingleplayerGameAction object
     */
    public StartSingleplayerGameAction(MenuFrame frame) {
        super("Start Single Player Game");
        this.frame = frame;
    }

    /**
     * creates a new SingleplayerGame and corresponding SingleplayerGameFrame
     *
     * @param e e event firing the action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String nickname = Dialog.receiveNickname(frame);
        if (nickname == null) return;

        SingleplayerGame game = new SingleplayerGame(nickname);
        new SingleplayerGameFrame(game);
        game.start();
    }
}
