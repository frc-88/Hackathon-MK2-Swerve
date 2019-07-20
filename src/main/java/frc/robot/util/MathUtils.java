package frc.robot.util;

public class MathUtils {
    public static double getReferenceAngle(double angle) {
        angle = (angle + 180) % 360;
        if (angle < 0)
            angle += 360;
        angle -= 180;
        return angle;
    }
}