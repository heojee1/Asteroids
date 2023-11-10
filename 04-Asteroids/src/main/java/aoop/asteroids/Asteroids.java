package aoop.asteroids;

import aoop.asteroids.view.MenuFrame;

/**
 * Main class of the Asteroids program.
 */
public class Asteroids {
    public static void main(String[] args) {
        if (System.getProperty("os.name").contains("Mac")) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }

        new MenuFrame();
    }
}