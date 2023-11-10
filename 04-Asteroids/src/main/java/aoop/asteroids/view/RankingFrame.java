package aoop.asteroids.view;

import aoop.asteroids.database.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static aoop.asteroids.packet.PacketType.DISCONNECT;

public class RankingFrame extends JFrame {
    private static final String RANKING_TITLE = "RANKING";
    private static final Dimension RANKING_SIZE = new Dimension(400, 400);

    public RankingFrame() {
        initSwingUI();
    }

    private void initSwingUI() {
        setContentPane(new JLabel(new ImageIcon("space.gif")));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(RANKING_SIZE);
        this.setTitle(RANKING_TITLE);
        setLayout(null);
        setResizable(false);
        setVisible(true);
        setRankingTable();
    }

    private void setRankingTable() {
        JTextArea rankingList = new JTextArea(Database.getTopTenScorers());
        rankingList.setEditable(false);
        rankingList.setBounds(60, 60, 280, 280);
        rankingList.setBackground(Color.PINK);
        rankingList.setFont(new Font("Monaco", Font.PLAIN, 15));
        getContentPane().add(rankingList);
    }

}
