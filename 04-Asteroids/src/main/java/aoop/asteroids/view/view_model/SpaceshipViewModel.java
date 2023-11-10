package aoop.asteroids.view.view_model;

import aoop.asteroids.model.game_object.Spaceship;
import aoop.asteroids.util.PolarCoordinate;

import java.awt.*;
import java.awt.geom.Path2D;

import static java.lang.Math.PI;

public class SpaceshipViewModel extends GameObjectViewModel<Spaceship> {
    /**
     * Constructs a new view model with the given game object.
     *
     * @param gameObject The object that will be displayed when this view model is drawn.
     */
    public SpaceshipViewModel(Spaceship gameObject) {
        super(gameObject);
    }

    @Override
    public void draw(Graphics2D graphics2D, Point.Double location) {
        Spaceship spaceship = this.getGameObject();
        this.drawMainBody(spaceship, graphics2D, location);
        if (spaceship.isAccelerating()) {
            this.drawExhaust(spaceship, graphics2D, location);
        }
    }

    private void drawMainBody(Spaceship spaceship, Graphics2D graphics2D, Point.Double location) {
        Path2D.Double spaceshipMainBody = this.buildTriangle(
                location,
                spaceship.getDirection(),
                new PolarCoordinate(0.0 * PI, 20),
                new PolarCoordinate(0.8 * PI, 20),
                new PolarCoordinate(1.2 * PI, 20)
        );
        // The area where the spaceship's body goes is first cleared by filling it with black, then the path is drawn.
        graphics2D.setColor(spaceship.getIdColor());
        graphics2D.fill(spaceshipMainBody);
        graphics2D.draw(spaceshipMainBody);
    }

    private void drawExhaust(Spaceship spaceship, Graphics2D graphics2D, Point.Double location) {
        Path2D.Double exhaustFlame = this.buildTriangle(
                location,
                spaceship.getDirection(),
                new PolarCoordinate(1.0 * PI, 25),
                new PolarCoordinate(0.9 * PI, 15),
                new PolarCoordinate(1.1 * PI, 15)
        );
        graphics2D.setColor(Color.YELLOW);
        graphics2D.fill(exhaustFlame);
    }

    private Path2D.Double buildTriangle(
            Point.Double location,
            double facingDirection,
            PolarCoordinate a,
            PolarCoordinate b,
            PolarCoordinate c
    ) {
        Path2D.Double path = new Path2D.Double();
        path.moveTo(
                location.getX() + Math.sin(facingDirection + a.getAngle()) * a.getRadius(),
                location.getY() - Math.cos(facingDirection + a.getAngle()) * a.getRadius()
        );
        path.lineTo(
                location.getX() + Math.sin(facingDirection + b.getAngle()) * b.getRadius(),
                location.getY() - Math.cos(facingDirection + b.getAngle()) * b.getRadius()
        );
        path.lineTo(
                location.getX() + Math.sin(facingDirection + c.getAngle()) * c.getRadius(),
                location.getY() - Math.cos(facingDirection + c.getAngle()) * c.getRadius()
        );
        path.closePath();
        return path;
    }
}
