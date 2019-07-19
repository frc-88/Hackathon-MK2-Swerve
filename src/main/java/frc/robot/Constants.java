package frc.robot;

import edu.wpi.first.wpilibj.Preferences;

/**
 * This class contains many of the constants for the robot. Mainly, those values which should go
 * here are either ones which are relevant in a global scope or ones that will be registered as a
 * WPILIB preference.
 */
public class Constants {

    // PID constants for the MK2 wheel speed controller running on the Spark Max
    // Configured through Preferences
    private static String name_mk2WheelKP = "MK2 Wheel kP";
    public static double mk2WheelKP = 0; 
    private static String name_mk2WheelKI = "MK2 Wheel kI";
    public static double mk2WheelKI = 0.0001;
    private static String name_mk2WheelKD = "MK2 Wheel kD";
    public static double mk2WheelKD = 0;
    private static String name_mk2WheelKF = "MK2 Wheel kF";
    public static double mk2WheelKF = .08;
    private static String name_mk2WheelIZone = "MK2 Wheel IZone";
    public static double mk2WheelIZone = 2;
    private static String name_mk2WheelIMax = "MK2 Wheel IMax";
    public static double mk2WheelIMax = .2;

    // PID constants for the MK2 azimuth controller using the Spark Max's "Smart Motion" 
    // trapezoidal motion profiling
    // Configured through Preferences
    private static String name_mk2AzimuthKP = "MK2 Azimuth kP";
    public static double mk2AzimuthKP = 0; 
    private static String name_mk2AzimuthKI = "MK2 Azimuth kI";
    public static double mk2AzimuthKI = 0;
    private static String name_mk2AzimuthKD = "MK2 Azimuth kD";
    public static double mk2AzimuthKD = 0;
    private static String name_mk2AzimuthKF = "MK2 Azimuth kF";
    public static double mk2AzimuthKF = 0;
    private static String name_mk2AzimuthIZone = "MK2 Azimuth IZone";
    public static double mk2AzimuthIZone = 0;
    private static String name_mk2AzimuthIMax = "MK2 Azimuth IMax";
    public static double mk2AzimuthIMax = 0;
    private static String name_mk2AzimuthMaxVelocity = "MK2 Azimuth Max Vel";
    public static double mk2AzimuthMaxVelocity = 0;
    private static String name_mk2AzimuthMinVelocity = "MK2 Azimuth Min Vel";
    public static double mk2AzimuthMinVelocity = 0;
    private static String name_mk2AzimuthMaxAcceleration = "MK2 Azimuth Max Accel";
    public static double mk2AzimuthMaxAcceleration = 0;

    // Initialize all of the preferences
    public static void init() {
        initializeDoublePreference(name_mk2WheelKP, mk2WheelKP);
        initializeDoublePreference(name_mk2WheelKI, mk2WheelKI);
        initializeDoublePreference(name_mk2WheelKD, mk2WheelKD);
        initializeDoublePreference(name_mk2WheelKF, mk2WheelKF);
        initializeDoublePreference(name_mk2WheelIZone, mk2WheelIZone);
        initializeDoublePreference(name_mk2WheelIMax, mk2WheelIMax);

        initializeDoublePreference(name_mk2AzimuthKP, mk2AzimuthKP);
        initializeDoublePreference(name_mk2AzimuthKI, mk2AzimuthKI);
        initializeDoublePreference(name_mk2AzimuthKD, mk2AzimuthKD);
        initializeDoublePreference(name_mk2AzimuthKF, mk2AzimuthKF);
        initializeDoublePreference(name_mk2AzimuthIZone, mk2AzimuthIZone);
        initializeDoublePreference(name_mk2AzimuthIMax, mk2AzimuthIMax);
        initializeDoublePreference(name_mk2AzimuthMaxVelocity, mk2AzimuthMaxVelocity);
        initializeDoublePreference(name_mk2AzimuthMinVelocity, mk2AzimuthMinVelocity);
        initializeDoublePreference(name_mk2AzimuthMaxAcceleration, mk2AzimuthMaxAcceleration);
    }

    public void updatePreferences() {
        updateDoublePreference(name_mk2WheelKP, mk2WheelKP);
        updateDoublePreference(name_mk2WheelKI, mk2WheelKI);
        updateDoublePreference(name_mk2WheelKD, mk2WheelKD);
        updateDoublePreference(name_mk2WheelKF, mk2WheelKF);
        updateDoublePreference(name_mk2WheelIZone, mk2WheelIZone);
        updateDoublePreference(name_mk2WheelIMax, mk2WheelIMax);

        updateDoublePreference(name_mk2AzimuthKP, mk2AzimuthKP);
        updateDoublePreference(name_mk2AzimuthKI, mk2AzimuthKI);
        updateDoublePreference(name_mk2AzimuthKD, mk2AzimuthKD);
        updateDoublePreference(name_mk2AzimuthKF, mk2AzimuthKF);
        updateDoublePreference(name_mk2AzimuthIZone, mk2AzimuthIZone);
        updateDoublePreference(name_mk2AzimuthIMax, mk2AzimuthIMax);
        updateDoublePreference(name_mk2AzimuthMaxVelocity, mk2AzimuthMaxVelocity);
        updateDoublePreference(name_mk2AzimuthMinVelocity, mk2AzimuthMinVelocity);
        updateDoublePreference(name_mk2AzimuthMaxAcceleration, mk2AzimuthMaxAcceleration);
    }

    private static void initializeDoublePreference(String name, double defaultValue) {
        Preferences prefs = Preferences.getInstance();
        if (!prefs.containsKey(name)) {
            prefs.putDouble(name, defaultValue);
        }
    }

    private static double updateDoublePreference(String name, double currentValue) {
        return Preferences.getInstance().getDouble(name, currentValue);
    }

}