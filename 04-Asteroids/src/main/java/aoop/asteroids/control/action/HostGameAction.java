package aoop.asteroids.control.action;

import aoop.asteroids.model.entity.Server;
import aoop.asteroids.util.Dialog;
import aoop.asteroids.view.MenuFrame;
import aoop.asteroids.view.ServerLobbyFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * HostGameAction creates a new Server and its ServerLobbyFrame
 */
public class HostGameAction extends AbstractAction {
    private MenuFrame frame;

    /**
     * createsa new HostGameAction object
     *
     * @param frame frame on which input dialog will be displayed on
     */
    public HostGameAction(MenuFrame frame) {
        super("Host Multiplayer Game");
        this.frame = frame;
    }

    /**
     * receives a String of nickname from the user, creates a Server and a ServerLobbyFrame
     *
     * @param e event firing the action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String nickname = Dialog.receiveNickname(frame);
        if (nickname == null) return;
        Server server = new Server(nickname);
        new ServerLobbyFrame(server);
        new Thread(server).start();
    }
}
