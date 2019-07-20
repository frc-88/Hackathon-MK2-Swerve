package frc.robot.util;

public class Vector2D {

    public static Vector2D ORIGIN = createCartesianCoordinates(0, 0);

    private final double x;
    private final double y;

    private Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getMagnitude() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public double getAngle() {
        return Math.toDegrees(Math.atan2(-x, y));
    }

    public static Vector2D createCartesianCoordinates(double x, double y) {
        return new Vector2D(x, y);
    }

    public static Vector2D createPolarCoordinates(double magnitude, double angle) {
        angle = Math.toRadians(angle);
        return createCartesianCoordinates(
            magnitude * -Math.sin(angle), magnitude * Math.cos(angle));
    }

    public Vector2D rotate(double angle) {
        return createPolarCoordinates(this.getMagnitude(), this.getAngle() + angle);
    }

}