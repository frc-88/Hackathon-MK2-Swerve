package frc.robot.util;

public class MathUtils {

    public static double getReferenceAngle(double angle) {
        return (angle + 180) % 360 - 180;
    }
}