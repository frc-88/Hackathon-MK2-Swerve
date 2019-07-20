package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI {
    Joystick controller;

    public OI() {
        controller = new Joystick(0);
    }

    public double getTranslationX() {
        return controller.getRawAxis(0);
    }

    public double getTranslationY() {
        return -controller.getRawAxis(1);
    }

    public double getRotation() {
        return controller.getRawAxis(4);
    }
}