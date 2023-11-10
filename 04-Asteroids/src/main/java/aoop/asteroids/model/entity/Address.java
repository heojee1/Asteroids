package aoop.asteroids.model.entity;

import java.net.*;
import java.util.Enumeration;

/**
 * Address provides encapsulation and easy access and modification of addresses for the program
 */
public class Address {
    private InetAddress ipAddress;
    private int port;
    private String hostName;

    /**
     * creates a new Address object
     * @param ipAddress ip address
     * @param port port number
     */
    public Address(InetAddress ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.hostName = ipAddress.getHostName();
    }

    /**
     * getter for ip address
     *
     * @return ip address
     */
    public InetAddress getIpAddress() {
        return this.ipAddress;
    }

    /**
     * getter for port number
     *
     * @return port
     */
    public int getPort() {
        return this.port;
    }


    public InetSocketAddress toSocketAddress() {
        return new InetSocketAddress(this.ipAddress, this.port);
    }

    /**
     * @return a valid ip address
     */
    public static InetAddress getValidIP() {
        Enumeration<NetworkInterface> netEnum;
        Enumeration<InetAddress> InetEnum;
        InetAddress inetAddress;

        try {
            netEnum = NetworkInterface.getNetworkInterfaces();
            while(netEnum.hasMoreElements()) {
                NetworkInterface n = netEnum.nextElement();
                if(n.isLoopback()) continue;
                if(n.isPointToPoint()) continue;
                InetEnum = n.getInetAddresses();
                while (InetEnum.hasMoreElements()) {
                    inetAddress =  InetEnum.nextElement();
                    if (inetAddress instanceof Inet4Address) {
                        if (!inetAddress.isLinkLocalAddress()) {
                            return inetAddress;
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("can't get network interface");
        }
        return null;
    }
}
