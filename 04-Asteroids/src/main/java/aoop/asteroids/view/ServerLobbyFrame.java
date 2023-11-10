package aoop.asteroids.view;

import aoop.asteroids.model.entity.Server;
import aoop.asteroids.model.entity.ServerListener;
import aoop.asteroids.view.button.StartMultiplayerGameButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * SererLobbyFrame is a hub that displays joiner's nickname and provides a button to start multiplayer gmae
 */
public class ServerLobbyFrame extends JFrame implements ServerListener {
    private static final String LOBBY_TITLE = "Asteroids Lobby";
    private static final Dimension LOBBY_SIZE = new Dimension(400, 400);
    private Server server;
    private JTextArea playerList = new JTextArea();

    /**
     * creates a new ServerLobbyFrame
     *
     * @param server server whose list of clients to be displayed
     */
    public ServerLobbyFrame(Server server) {
        this.server = server;
        this.initSwingUI();
        server.addListener(this);
    }

    /**
     * set up UI
     */
    private void initSwingUI() {
        setContentPane(new JLabel(new ImageIcon("space.gif")));
        this.setTitle(LOBBY_TITLE);
        this.setSize(LOBBY_SIZE);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            // disconnects all clients, abort the game and close socket upon clsoing
            @Override
            public void windowClosing(WindowEvent e) {
                server.notifyServerClosed();
                server.getGame().abort();
                server.getSocket().close();
                dispose();
            }
        });
        this.setVisible(true);
        this.setResizable(false);

        setTitleText();
        setPlayListText();
        setAddressText();

        StartMultiplayerGameButton button = new StartMultiplayerGameButton(server.getGame(), this);
        button.setBounds(150, 340, 100, 30);
        getContentPane().add(button);
    }

    /**
     * displays title of the frame
     */
    private void setTitleText() {
        JTextArea titleText = new JTextArea("PLAYER LIST");
        titleText.setEditable(false);
        titleText.setForeground(Color.WHITE);
        titleText.setOpaque(false);
        titleText.setBounds(60, 60, 250, 100);
        titleText.setFont(new Font("Monaco", Font.BOLD, 25));
        getContentPane().add(titleText);
    }

    /**
     * displays list of joiners
     */
    private void setPlayListText() {
        playerList.setEditable(false);
        playerList.setBounds(60, 120, 280, 200);
        playerList.setBackground(Color.PINK);
        playerList.setFont(new Font("Monaco", Font.PLAIN, 15));
        playerList.append(" " + server.getNickname());
        getContentPane().add(playerList);
    }

    /**
     * displays servers ip address and port number
     */
    private void setAddressText() {
        String ipAddress = null;
        try {
            ipAddress = String.valueOf(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String port = String.valueOf(server.getSocket().getLocalPort());
        JTextArea addressText = new JTextArea("ip : " + ipAddress + "\n" +  "port : " + port);
        addressText.setEditable(false);
        addressText.setForeground(Color.WHITE);
        addressText.setOpaque(false);
        addressText.setBounds(10, 330, 150, 50);
        getContentPane().add(addressText);
    }

    /**
     * when joiner joins the game, the method is called to update the list of joiners displayed
     */
    @Override
    public void onClientListUpdate() {
        StringBuilder sb = new StringBuilder();
        server.getGame().getShips().forEach(ship -> {
            if (ship.getNickname() != null)
                sb.append(" ").append(ship.getNickname()).append("\n");
        });
        playerList.setFont(new Font("Monaco", Font.PLAIN, 15));
        playerList.setText(sb.toString());
    }
}
