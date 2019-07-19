package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;

public class OI {
    Joystick controller;

    public OI() {
        controller = new Joystick(0);
    }

    public double getAzimuth() {
        double ret = controller.getRawAxis(0);
        return 180 + (ret * 180);
    }

    public double getSpeed() {
        double ret = controller.getRawAxis(5);
        if (Math.abs(ret) < 0.05) {
            return 0.0;
        } else {
            return ret * 5.0;
        }
    }
}