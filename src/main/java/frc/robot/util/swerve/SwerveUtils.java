package frc.robot.util.swerve;

import frc.robot.state.VelocitySwerveState;
import frc.robot.util.Vector2D;

public class SwerveUtils {

    public static VelocitySwerveState limitAccelerations(VelocitySwerveState current, 
            VelocitySwerveState desired, double maxLinearAccel, double maxTranslationAngularAccel,
            double maxHeadingAngularAccel, long dt) {

        double dt_seconds = dt / 1_000_000.;

        double limitedLinearSpeed = 
            limitChange(current.getTranslationVelocity().getMagnitude(), 
            desired.getTranslationVelocity().getMagnitude(), maxLinearAccel / dt_seconds);
        double limitedTranslationAngularSpeed =
            limitChange(current.getTranslationVelocity().getAngle(), 
            desired.getTranslationVelocity().getAngle(), maxTranslationAngularAccel / dt_seconds);
        double limitedHeadingAngularSpeed =
            limitChange(current.getRotationalVelocity(), 
            desired.getRotationalVelocity(), maxHeadingAngularAccel / dt_seconds);

        return new VelocitySwerveState(
            Vector2D.createPolarCoordinates(limitedLinearSpeed, limitedTranslationAngularSpeed), 
            limitedHeadingAngularSpeed);

    }

    public static double limitChange(double current, double desired, double maxChange) {
        if (desired > current) {
            return Math.min(desired, current + maxChange);
        } else {
            return Math.max(desired, current - maxChange);
        }
    }

}