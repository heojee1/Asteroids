package aoop.asteroids.control;

import aoop.asteroids.model.game_object.Spaceship;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

/**
 * This class is responsible for handling keyboard input for a single player that is bound to a ship.
 */
public class PlayerKeyListener extends Observable implements KeyListener {
	/**
	 * The Key that, when pressed, causes the ship to accelerate.
	 */
	private static final int ACCELERATION_KEY = KeyEvent.VK_W;

	/**
	 * The Key that turns the ship left, or counter-clockwise.
	 */
	private static final int LEFT_KEY = KeyEvent.VK_A;

	/**
	 * The Key that turns the ship right, or clockwise.
	 */
	private static final int RIGHT_KEY = KeyEvent.VK_D;

	/**
	 * The Key that causes the ship to fire its weapon.
	 */
	private static final int FIRE_WEAPON_KEY = KeyEvent.VK_SPACE;

	/**
	 * The spaceship that will respond to Key events caught by this listener.
	 */
	private Spaceship ship;

	/**
	 * Constructs a new player Key listener to control the given ship.
	 * @param ship The ship that this Key listener will control.
	 */
	public PlayerKeyListener(Spaceship ship) {
		this.ship = ship;
	}

	public void setLinkedShip(Spaceship ship) {
		 this.ship = ship;
	}

	/**
	 * This method is invoked when a Key is pressed and sets the corresponding fields in the spaceship to true.
	 *
	 * @param event Key event that triggered the method.
	 */
	@Override
	public void keyPressed(KeyEvent event) {
		switch (event.getKeyCode()) {
			case ACCELERATION_KEY:
				this.ship.setAccelerateKeyPressed(true);
				updateObservers();
				break;
			case LEFT_KEY:
				this.ship.setTurnLeftKeyPressed(true);
				updateObservers();
				break;
			case RIGHT_KEY:
				this.ship.setTurnRightKeyPressed(true);
				updateObservers();
				break;
			case FIRE_WEAPON_KEY:
				this.ship.setIsFiring(true);
				updateObservers();
		}
	}

	/**
	 * This method is invoked when a Key is released and sets the corresponding fields in the spaceship to false.
	 *
	 * @param event Key event that triggered the method.
	 */
	@Override
	public void keyReleased(KeyEvent event) {
		switch (event.getKeyCode()) {
			case ACCELERATION_KEY:
				this.ship.setAccelerateKeyPressed(false);
				updateObservers();
				break;
			case LEFT_KEY:
				this.ship.setTurnLeftKeyPressed(false);
				updateObservers();
				break;
			case RIGHT_KEY:
				this.ship.setTurnRightKeyPressed(false);
				updateObservers();
				break;
			case FIRE_WEAPON_KEY:
				this.ship.setIsFiring(false);
				updateObservers();
		}
	}

	/**
	 * This method doesn't do anything, but we must provide an empty implementation to satisfy the contract of the
	 * KeyListener interface.
	 *	
	 * @param event Key event that triggered the method.
	 */
	@Override
	public void keyTyped(KeyEvent event) {}

	private void updateObservers() {
		setChanged();
		notifyObservers();
	}
}
