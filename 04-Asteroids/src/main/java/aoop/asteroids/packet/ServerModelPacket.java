package aoop.asteroids.packet;

import aoop.asteroids.model.game.MultiplayerGame;
import aoop.asteroids.model.game_object.Asteroid;
import aoop.asteroids.model.game_object.Bullet;
import aoop.asteroids.model.game_object.Spaceship;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Collection;

/**
 * ServerModelPacket contains the entire data of server-side game.
 */
public class ServerModelPacket extends GamePacket {
    private Collection<Asteroid> asteroids;
    private Collection<Bullet> bullets;
    private Collection<Spaceship> ships;

    /**
     * creates a new ServerModelPacket
     *
     * @param socket socket from which data will be sent
     * @param type type of packet
     * @param game server-side game object
     */
    public ServerModelPacket(DatagramSocket socket, PacketType type, MultiplayerGame game) {
        super(socket, type);
        this.asteroids = game.getAsteroids();
        this.bullets = game.getBullets();
        this.ships = game.getShips();
    }

    /**
     * The essential information, asteroids, bullets, and ships are written on ObjectOutputStream
     *
     * @throws IOException in case of error
     */
    @Override
    public void writePacket() throws IOException {
        out.writeInt(type.getId());
        out.writeObject(asteroids);
        out.writeObject(bullets);
        out.writeObject(ships);
    }
}
