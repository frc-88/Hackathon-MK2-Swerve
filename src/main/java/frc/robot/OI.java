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
        return Math.pow(controller.getMagnitude(), 3) * 12;
    }

    public double getRotation() {
        return Math.pow(controller.getRawAxis(4), 3) * 800;
    }
}