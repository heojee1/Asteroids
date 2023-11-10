package aoop.asteroids.util;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Dialog contains a multiple static methods that receives input by JOptionPane dialog
 */
public class Dialog {

    /**
     * receives nickname from the user
     *
     * @param frame frame on which input dialog will be displayed on
     * @return nickname received
     */
    public static String receiveNickname(JFrame frame) {
        String nickname = JOptionPane.showInputDialog("Your nickname");
        if (nickname == null) return null;
        while (nickname.length() > 10 || nickname.length() == 0) {
            JOptionPane.showMessageDialog(frame, "Nickname should between 1 and 10 letters");
            nickname = JOptionPane.showInputDialog("Your nickname");
            if (nickname == null) return null;
        }
        return nickname;
    }

    /**
     * receives address from the user, return as InetAddress
     *
     * @param frame frame on which input dialog will be displayed on
     * @return InetAddress form of the address input
     */
    public static InetAddress receiveServerIP(JFrame frame) {
        try {
            String input = JOptionPane.showInputDialog("Sever IP");
            if (input == null) return null;
            if (input.length() == 0) return null;
            return InetAddress.getByName(input);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
