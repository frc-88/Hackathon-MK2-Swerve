package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI {
    Joystick controller;

    public OI() {
        controller = new Joystick(0);
    }

    public double getAzimuth() {
        return controller.getDirectionDegrees();
    }

    public double getSpeed() {
        // TODO: this scale is completely made up.
        return -1 * controller.getMagnitude() * 250;
    }
}