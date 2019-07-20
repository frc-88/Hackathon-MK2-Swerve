package frc.robot.state;

import frc.robot.util.Vector2D;

public class VelocitySwerveState implements SwerveState {

    public static final VelocitySwerveState ZERO_STATE = 
        new VelocitySwerveState(Vector2D.ORIGIN, 0);

    private final Vector2D translationVelocity;
    private final double rotationalVelocity;

    public VelocitySwerveState(Vector2D translationVelocity, double rotationalVelocity) {
        this.translationVelocity = translationVelocity;
        this.rotationalVelocity = rotationalVelocity;
    }

    @Override
    public <R> R accept(SwerveStateVisitor<R> v) {
        return v.visitVelocitySwerveState(this);
    }

    public Vector2D getTranslationVelocity() {
        return translationVelocity;
    }

    public double getRotationalVelocity() {
        return this.rotationalVelocity;
    }

}