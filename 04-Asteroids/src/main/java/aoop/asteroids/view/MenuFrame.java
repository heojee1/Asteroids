package aoop.asteroids.view;

import aoop.asteroids.view.button.*;

import javax.swing.*;
import java.awt.*;

/**
 * Main menu frame that contains buttons of options
 */
public class MenuFrame extends JFrame {
    private static final String MENU_TITLE = "Asteroids";
    private static final Dimension MENU_SIZE = new Dimension(650, 500);
    private static final int BUTTON_WIDTH = 300;
    private static final int BUTTON_HEIGHT = 40;
    private static final int BUTTON_X_LOCATION = 180;
    private static final int BUTTON_SPACING = 55;
    private int buttonYLocation = 100;

    /**
     * creates a new MenuFrame object
     */
    public MenuFrame() {
        initSwingUI();
        addButtons();
    }

    /**
     * set up UI
     */
    private void initSwingUI() {
        setContentPane(new JLabel(new ImageIcon("space.gif")));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(MENU_SIZE);
        this.setTitle(MENU_TITLE);
        setLayout(null);
        setResizable(false);
        setVisible(true);
    }

    /**
     * adds the buttons
     */
    private void addButtons() {
        SingleplayerButton singleplayerButton = new SingleplayerButton(this);
        singleplayerButton.setBounds(BUTTON_X_LOCATION, buttonYLocation, BUTTON_WIDTH, BUTTON_HEIGHT);
        getContentPane().add(singleplayerButton);

        HostGameButton hostGameButton = new HostGameButton(this);
        hostGameButton.setBounds(BUTTON_X_LOCATION, buttonYLocation+=BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT);
        getContentPane().add(hostGameButton);

        JoinGameButton joinGameButton = new JoinGameButton(this);
        joinGameButton.setBounds(BUTTON_X_LOCATION, buttonYLocation+=BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT);
        getContentPane().add(joinGameButton);

        SpectateGameButton spectateGameButton = new SpectateGameButton(this);
        spectateGameButton.setBounds(BUTTON_X_LOCATION, buttonYLocation+=BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT);
        getContentPane().add(spectateGameButton);

        SeeRankingButton seeRankingButton = new SeeRankingButton();
        seeRankingButton.setBounds(BUTTON_X_LOCATION, buttonYLocation+=BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT);
        getContentPane().add(seeRankingButton);
    }
}
