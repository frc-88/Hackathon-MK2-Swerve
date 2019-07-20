package frc.robot.state;

import frc.robot.util.Vector2D;

public class AbsoluteHeadingSwerveState extends VelocitySwerveState {

    public static final AbsoluteHeadingSwerveState ZERO_STATE = 
        new AbsoluteHeadingSwerveState(0, Vector2D.ORIGIN, 0);

    private final double heading;

    public AbsoluteHeadingSwerveState(double heading, Vector2D translationVelocity, 
            double rotationalVelocity) {

        super(translationVelocity, rotationalVelocity);
        this.heading = heading;
    }

    public double getHeading() {
        return this.heading;
    }

}