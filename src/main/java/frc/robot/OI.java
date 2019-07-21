package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;

public class OI {
    Joystick controller;

    public OI() {
        controller = new Joystick(0);
    }

    public double getAzimuth() {
        return controller.getDirectionDegrees();
    }

    public double getSpeed() {
        double ret =controller.getMagnitude() * 4.;
        if (ret < 0.25) {
            return 0;
        } else {
            return ret;
        }
    }

    public double getRotation() {
        return Math.pow(controller.getRawAxis(4), 3) * 4;
    }
}