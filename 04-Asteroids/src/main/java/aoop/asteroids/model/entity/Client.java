package aoop.asteroids.model.entity;

import aoop.asteroids.model.game.MultiplayerGame;
import aoop.asteroids.model.game_object.Asteroid;
import aoop.asteroids.model.game_object.Bullet;
import aoop.asteroids.model.game_object.Spaceship;
import aoop.asteroids.packet.PacketType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Client class is an abstract Runnable class whose subclasses are Joiner and Spectator.
 * Client holds a shared features of the both subclasses.
 */
public abstract class Client extends UDPEntity implements Runnable {
    private Address serverAddress;
    private boolean connected = false;
    DatagramSocket socket;
    /**  thread that handles all inputs to the client side (eg. regarding connection) **/
    Thread inputThread;

    /**  thread that handles all output to the server side (eg. regarding connection, receiving game model) **/
    Thread outputThread;

    /**
     * Client object constructor
     *
     * @param serverAddress Address of its server
     */
    public Client(Address serverAddress) {
        this.serverAddress = serverAddress;
        openDatagramSocket();
    }

    /**
     * opens a DatagramSocket for the client with random port number between 8000 and 9000
     */
    @Override
    void openDatagramSocket() {
        Random rand = new Random();
        try {
            int randomPort = rand.nextInt(10000) + 8000;
            this.socket = new DatagramSocket(randomPort, serverAddress.getIpAddress());
        } catch (SocketException e) {
            System.out.println("Can't open a client socket");
        }
    }

    public void safeRun() {
        if (this.socket != null) new Thread(this).start();
        else this.game.abort();
    }

    /**
     * getter for the Client's game
     *
     * @return client's MultiplayerGame
     */
    public MultiplayerGame getGame() {
        return this.game;
    }

    /**
     * getter for the socket
     *
     * @return DatagramSocket of the client
     */
    public DatagramSocket getSocket() {
        return this.socket;
    }

    /**
     * getter for the server's address
     *
     * @return Address of server
     */
    public Address getServerAddress() {
        return this.serverAddress;
    }

    /**
     * @return connected
     */
    public boolean isConnected() {
        return this.connected;
    }

    /**
     * sets connected to true
     */
    public void connect() {
        this.connected = true;
    }

    /**
     * processes received game models sent from the server
     * @param packet DatagramPacket of game model server sent
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void processGameData(DatagramPacket packet) throws IOException, ClassNotFoundException {
        byteIn = new ByteArrayInputStream(packet.getData());
        in = new ObjectInputStream(byteIn);

        PacketType type = PacketType.getType(in.readInt());
        if (type == null) return;
        switch (type) {
            case GAME_MODEL:
                setGameModel(in);
                break;
            case DISCONNECT:
                synchronized (this.socket) {
                    this.socket.close();
                    this.game.abort();
                }
        }
    }

    /**
     * sets client's game model according to the received game models from the server
     * @param in ObjectInputStream that reads the packet
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void setGameModel(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ArrayList<Asteroid> asteroids = (ArrayList<Asteroid>) in.readObject();
        ArrayList<Bullet> bullets = (ArrayList<Bullet>) in.readObject();
        ArrayList<Spaceship> ships = (ArrayList<Spaceship>) in.readObject();

        this.game.setAsteroids(asteroids);
        this.game.setBullets(bullets);
        this.game.setShips(ships);
    }


}
