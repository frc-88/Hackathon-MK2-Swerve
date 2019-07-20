package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;

public class OI {
    Joystick controller;

    public OI() {
        controller = new Joystick(0);
    }

    public double getAzimuth() {
        // [0, 1)
        return controller.getRawAxis(0) / 2 + 0.5;
    }

    public double getSpeed() {
        // TODO: this scale is completely made up.
        return (controller.getRawAxis(5) * 300);
    }

    public boolean motorAActive() {
        return controller.getRawButton(1);
    }

    public boolean motorBActive() {
        return controller.getRawButton(2);
    }

    public boolean motorCActive() {
        return controller.getRawButton(3);
    }

    public boolean motorDActive() {
        return controller.getRawButton(4);
    }
}