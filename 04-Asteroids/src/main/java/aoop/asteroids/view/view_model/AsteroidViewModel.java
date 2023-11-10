package aoop.asteroids.view.view_model;

import aoop.asteroids.model.game_object.Asteroid;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class AsteroidViewModel extends GameObjectViewModel<Asteroid> {

    public AsteroidViewModel(Asteroid gameObject) {
        super(gameObject);
    }

    @Override
    public void draw(Graphics2D graphics2D, Point.Double location) {
        double radius = this.getGameObject().getRadius();
        graphics2D.setColor(Color.GRAY);
        Ellipse2D.Double asteroidEllipse = new Ellipse2D.Double(
                location.getX() - radius,
                location.getY() - radius,
                2 * radius,
                2 * radius
        );
        graphics2D.fill(asteroidEllipse);
    }
}
