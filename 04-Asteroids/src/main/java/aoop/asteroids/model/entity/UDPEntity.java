package aoop.asteroids.model.entity;

import aoop.asteroids.model.game.MultiplayerGame;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramSocket;

/**
 * UDPEntity is an abstract parent class for all entities that uses UDP
 * This includes Joiner, Spectator, and Server
 */
public abstract class UDPEntity {
    /** DatagramSocket of entity **/
    DatagramSocket socket;

    /** Multiplayer game model **/
    MultiplayerGame game;

    /** Input streams used to read the data **/
    ByteArrayInputStream byteIn;
    ObjectInputStream in;

    /**
     * abstract method that opens a DatagramSocket for the UDPEntity
     */
    abstract void openDatagramSocket();

}
