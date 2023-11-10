package aoop.asteroids.control.action;

import aoop.asteroids.model.entity.Address;
import aoop.asteroids.model.entity.Joiner;
import aoop.asteroids.util.Dialog;
import aoop.asteroids.view.MenuFrame;
import aoop.asteroids.view.MultiplayerGameFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.InetAddress;

/**
 * JoinMultiplayerGameAction creates a new Joiner, start its thread and its MultiplayerGamFrame
 */
public class JoinMultiplayerGameAction extends AbstractAction {
    private MenuFrame frame;

    /**
     * creates a new JoinMultiplayerGameActioln object
     * @param frame frame on which input dialog will be displayed on
     */
    public JoinMultiplayerGameAction(MenuFrame frame) {
        super("Join Multiplayer Game");
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
        InetAddress serverIP = Dialog.receiveServerIP(frame);
        if (serverIP == null) {
            JOptionPane.showMessageDialog(frame, "invalid server ip");
            return;
        }

        Address serverAddress = new Address(serverIP, 55555);
        Joiner joiner = new Joiner(nickname, serverAddress);
        joiner.safeRun();
        MultiplayerGameFrame f = new MultiplayerGameFrame(joiner.getGame(), joiner.getPlayerKeyListener());
        f.revalidate();
    }

}
