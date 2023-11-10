package aoop.asteroids.model.entity;

import aoop.asteroids.model.game.MultiplayerGame;
import aoop.asteroids.model.game_object.Spaceship;
import aoop.asteroids.packet.PacketType;
import aoop.asteroids.util.Network;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import static aoop.asteroids.packet.PacketType.ACCEPT_CONNECTION;
import static aoop.asteroids.packet.PacketType.DISCONNECT;

/**
 * Server is a Runnable class that hosts a multiplayer game.
 * in its main loop, it receives data from the clients and process it.
 * note that sending the game model is done by game updater thread in server's game
 */
public class Server extends UDPEntity implements Runnable {
    private ArrayList<Address> clientAddresses;
    private String nickname;
    private ArrayList<ServerListener> listeners;

    /**
     * creates a new Server object
     * @param nickname nickname of the server user
     */
    public Server(String nickname) {
        this.nickname = nickname;
        this.game = new MultiplayerGame(this);
        this.clientAddresses = new ArrayList<>();
        this.listeners = new ArrayList<>();
        openDatagramSocket();
    }

    /**
     * main loop that receives connection requests and game data from clients
     */
    @Override
    public void run() {
        receiveConnectionRequest();
        receiveDataFromClients();
    }

    /**
     * @return client addresses list
     */
    public ArrayList<Address> getClientAddresses() {
        return this.clientAddresses;
    }

    /**
     * @return nickname of the server
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * @return the game of the server
     */
    public MultiplayerGame getGame() {
        return this.game;
    }

    /**
     * @return DatagramSocket of the server
     */
    public DatagramSocket getSocket() {
        return this.socket;
    }

    /**
     * opens the server's DatagramSocket. the port is set to its default 55555
     */
    @Override
    void openDatagramSocket() {
        try {
            this.socket = new DatagramSocket(55555, Address.getValidIP());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * receives connection requests from clients
     * when game has aborted or already started running, server does not receive any more requests
     */
    private void receiveConnectionRequest() {
        while (!this.game.isAborted() && !this.game.isRunning()) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[200], 200);
                socket.receive(packet);
                prepareInputStream(packet);
                processConnection(packet);
            } catch (IOException | ClassNotFoundException e) {
                this.game.abort();
            }
        }
    }

    /**
     * receives data from clients
     *  1. Spaceship model from players
     *  2. disconnection notice from clients
     *  server only receives these data when the game is not aborted and game is running
     */
    private void receiveDataFromClients() {
        while (!this.game.isAborted() && this.game.isRunning()) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[1400], 1400);
                socket.receive(packet);
                prepareInputStream(packet);
                processGameData(packet);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("invalid type cast : game data process");
            }
        }
    }

    /**
     * notifies the clients that server has closed
     */
    public synchronized void notifyServerClosed() {
        this.clientAddresses.forEach(address -> Network.sendMessage(this.socket, DISCONNECT, address));
    }

    /**
     * processes game data sent from clients. it processes :
     *  1. SHIP model
     *  2. DISCONNECT message
     *
     * @param packet DatagramPacket sent from client
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void processGameData(DatagramPacket packet) throws IOException, ClassNotFoundException {
        PacketType type = PacketType.getType(in.readInt());
        if (type == null) return;
        switch(type) {
            case SHIP:
                Spaceship newShip = (Spaceship) in.readObject();
                this.overWriteShip(newShip);
                break;
            case DISCONNECT:
                processDisconnectionNotice(packet);
                break;
        }
    }

    /**
     * processes initial connection request
     *
     * @param packet DatagramPacket sent from client
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void processConnection(DatagramPacket packet) throws IOException, ClassNotFoundException {
        PacketType type = PacketType.getType(in.readInt());
        if (type == null) return;
        switch(type) {
            case REQUEST_CONNECTION_JOINER:
                String joinerName = (String) in.readObject();
                this.createNewJoinerShip(joinerName, packet.getPort());
                Address joinerAddress = new Address(packet.getAddress(), packet.getPort());
                this.clientAddresses.add(joinerAddress);
                Network.sendAcceptanceMessage(this.socket, ACCEPT_CONNECTION, packet.getPort(), joinerAddress);
                notifyObservers();
                break;
            case REQUEST_CONNECTION_SPECTATOR:
                Address specAddress = new Address(packet.getAddress(), packet.getPort());
                this.clientAddresses.add(specAddress);
                Network.sendAcceptanceMessage(this.socket, ACCEPT_CONNECTION, -1, specAddress);
                break;
            case DISCONNECT:
                processDisconnectionNotice(packet);
                notifyObservers();
                break;
        }
    }

    /**
     * processes DISCONNECT messages from clients
     *
     * @param packet DatagramPacket sent from client
     */
    private void processDisconnectionNotice(DatagramPacket packet) {
        Address disconnAddress = new Address(packet.getAddress(), packet.getPort());
        Spaceship ship = this.game.getShipByIdColor(new Color(packet.getPort()));
        if (ship != null) this.game.getShips().remove(ship);
        this.clientAddresses.remove(disconnAddress);
    }

    /**
     * overwrites a Spaceship with its newer model sent from the client
     * @param newShip new Spaceship model
     */
    public void overWriteShip(Spaceship newShip) {
        int idx = 0;
        for (Spaceship ship : this.game.getShips()) {
            if (ship.equals(newShip)) {
                break;
            }
            idx++;
        }
        this.game.getShips().set(idx, newShip);
    }

    /**
     * creates a new Spaceship according to the joiner's nickname and port number.
     * port numbers are unique, we use this as identifying color
     *
     * @param joinerName nickname of joiner
     * @param id port number of joiner
     */
    private void createNewJoinerShip(String joinerName, int id) {
        Spaceship ship = new Spaceship(joinerName, new Color(id));
        this.game.getShips().add(ship);
    }

    /**
     * prepares input streams
     * @param packet DatagramPacket to be read
     */
    protected void prepareInputStream(DatagramPacket packet) {
        try {
            byteIn = new ByteArrayInputStream(packet.getData());
            in = new ObjectInputStream(byteIn);
        } catch (IOException e) {
            System.out.println("exception when prepare input stream");
        }
    }

    /**
     * Adds the given listener to the list of listeners that will get notified when the connected client list is changed.
     * It updates ServerLobbyFrame which displays currently connected players - before starting the game.
     *
     * @param listener The listener to add.
     */
    public void addListener(ServerListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Tells all the registered listeners that their list of players should be updated
     */
    public void notifyObservers() {
        listeners.forEach(ServerListener::onClientListUpdate);
    }
}
