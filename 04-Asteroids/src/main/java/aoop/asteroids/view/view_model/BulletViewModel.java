package aoop.asteroids.view.view_model;

import aoop.asteroids.model.game_object.Bullet;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class BulletViewModel extends GameObjectViewModel<Bullet> {
    /**
     * Constructs a new view model with the given game object.
     *
     * @param gameObject The object that will be displayed when this view model is drawn.
     */
    public BulletViewModel(Bullet gameObject) {
        super(gameObject);
    }

    @Override
    public void draw(Graphics2D graphics2D, Point.Double location) {
        graphics2D.setColor(Color.YELLOW);
        Ellipse2D.Double bulletEllipse = new Ellipse2D.Double(
                location.getX() - 2.0,
                location.getY() - 2.0,
                5.0,
                5.0
        );
        graphics2D.draw(bulletEllipse);
    }
}
