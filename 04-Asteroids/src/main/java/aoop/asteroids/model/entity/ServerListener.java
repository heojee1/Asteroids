package aoop.asteroids.model.entity;

/**
 * Classes which implement this interface indicate that they would like to be notified when server's client list is updated, and must
 * implement the onClientListUpdate() to reflect the change.
 */
public interface ServerListener {
    /**
     * This method is called when the server that this listener is listening to announces that it should update.
     */
    void onClientListUpdate();
}
