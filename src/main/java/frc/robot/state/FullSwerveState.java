package frc.robot.state;

import frc.robot.util.Vector2D;

public class FullSwerveState extends AbsoluteHeadingSwerveState {

    public static final FullSwerveState ZERO_STATE = 
        new FullSwerveState(Vector2D.ORIGIN, 0, Vector2D.ORIGIN, 0);

    private final Vector2D position;

    public FullSwerveState(Vector2D position, double heading, 
            Vector2D translationVelocity, double rotationalVelocity) {

        super(heading, translationVelocity, rotationalVelocity);
        this.position = position;
    }

    public Vector2D getPosition() {
        return this.position;
    }

}