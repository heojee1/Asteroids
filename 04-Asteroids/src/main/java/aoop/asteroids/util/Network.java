package aoop.asteroids.util;

import aoop.asteroids.model.entity.Address;
import aoop.asteroids.model.entity.Joiner;
import aoop.asteroids.model.entity.Server;
import aoop.asteroids.packet.*;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static aoop.asteroids.packet.PacketType.GAME_MODEL;
import static aoop.asteroids.packet.PacketType.SHIP;

/**
 * Network class provides multiple static methods that sends wanted data over to the destination address.
 */
public class Network {
    /**
     * Sends server's game model to every clients
     *
     * @param server server which will send the game model
     */
    public static void sendGameModel(Server server) {
        ServerModelPacket gameModelPacket = new ServerModelPacket(server.getSocket(), GAME_MODEL, server.getGame());
        server.getClientAddresses().forEach(address -> {
            try {
                gameModelPacket.sendPacket(new Address(InetAddress.getLocalHost(), address.getPort()));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Sends joiner's ship model to its server
     *
     * @param joiner joiner which will send the game model
     */
    public static void sendShipModel(Joiner joiner) {
        PlayerModelPacket playerModelPacket = new PlayerModelPacket(joiner.getSocket(), SHIP, joiner.getGame().getMyShip());
        playerModelPacket.sendPacket(joiner.getServerAddress());
    }

    /**
     * Sends message to the destination address
     *
     * @param socket socket from which the message will be sent
     * @param type type of Data
     * @param address destination address
     */
    public static void sendMessage(DatagramSocket socket, PacketType type, Address address) {
        MessagePacket message = new MessagePacket(socket, type);
        message.sendPacket(address);
    }

    /**
     * Sends connection request message to the destination address
     *
     * @param socket socket from which the message will be sent
     * @param type type of Data
     * @param address destination address
     */
    public static void sendConnectionMessage(DatagramSocket socket, PacketType type, String info, Address address) {
        MessagePacket message = new MessagePacket(socket, type, info);
        message.sendPacket(address);
    }

    /**
     * Sends connection acceptance message to the destination address
     *
     * @param socket socket from which the message will be sent
     * @param type type of Data
     * @param address destination address
     */
    public static void sendAcceptanceMessage(DatagramSocket socket, PacketType type, int idColor, Address address) {
        MessagePacket message = new MessagePacket(socket, type, idColor);
        message.sendPacket(address);
    }

}
