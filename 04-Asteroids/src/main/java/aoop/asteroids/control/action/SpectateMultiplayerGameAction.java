package aoop.asteroids.control.action;

import aoop.asteroids.model.entity.Address;
import aoop.asteroids.model.entity.Spectator;
import aoop.asteroids.util.Dialog;
import aoop.asteroids.view.MenuFrame;
import aoop.asteroids.view.MultiplayerGameFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.InetAddress;

/**
 * SpectateMultiplayerGameAction creates a new Spectator, start its thread and its MultiplayerGameFrame
 */
public class SpectateMultiplayerGameAction extends AbstractAction {
    private MenuFrame frame;

    /**
     * creates a new SpectateMultiplayerGameAction object
     * @param frame frame on which input dialog will be displayed on
     */
    public SpectateMultiplayerGameAction(MenuFrame frame) {
        super("Spectate Game");
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        InetAddress serverIP = Dialog.receiveServerIP(frame);
        if (serverIP == null) {
            JOptionPane.showMessageDialog(frame, "invalid server ip");
            return;
        }
        Address serverAddress = new Address(serverIP, 55555);
        Spectator spectator = new Spectator(serverAddress);
        spectator.safeRun();
        MultiplayerGameFrame f = new MultiplayerGameFrame(spectator.getGame());
        f.revalidate();
    }
}
