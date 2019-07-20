package frc.robot;

import edu.wpi.first.wpilibj.Preferences;

/**
 * This class contains many of the constants for the robot. Mainly, those values
 * which should go here are either ones which are relevant in a global scope or
 * ones that will be registered as a WPILIB preference.
 */
public class Constants {
    private static String name_motorAKP = "Motor A kP";
    public static double motorAKP = 0;
    private static String name_motorAKI = "Motor A kI";
    public static double motorAKI = 0;
    private static String name_motorAKD = "Motor A kD";
    public static double motorAKD = 0;
    private static String name_motorAKF = "Motor A kF";
    public static double motorAKF = 0;
    private static String name_motorAIZone = "Motor A IZone";
    public static double motorAIZone = 0;
    private static String name_motorAIMax = "Motor A IMax";
    public static double motorAIMax = 0;
    private static String name_motorARamp = "Motor A Ramp";
    public static double motorARamp = 0;

    private static String name_motorBKP = "Motor B kP";
    public static double motorBKP = 0;
    private static String name_motorBKI = "Motor B kI";
    public static double motorBKI = 0;
    private static String name_motorBKD = "Motor B kD";
    public static double motorBKD = 0;
    private static String name_motorBKF = "Motor B kF";
    public static double motorBKF = 0;
    private static String name_motorBIZone = "Motor B IZone";
    public static double motorBIZone = 0;
    private static String name_motorBIMax = "Motor B IMax";
    public static double motorBIMax = 0;
    private static String name_motorBRamp = "Motor B Ramp";
    public static double motorBRamp = 0;

    // Initialize all of the preferences
    public static void init() {
        initializeDoublePreference(name_motorAKP, motorAKP);
        initializeDoublePreference(name_motorAKI, motorAKI);
        initializeDoublePreference(name_motorAKD, motorAKD);
        initializeDoublePreference(name_motorAKF, motorAKF);
        initializeDoublePreference(name_motorAIZone, motorAIZone);
        initializeDoublePreference(name_motorAIMax, motorAIMax);
        initializeDoublePreference(name_motorARamp, motorARamp);

        initializeDoublePreference(name_motorBKP, motorBKP);
        initializeDoublePreference(name_motorBKI, motorBKI);
        initializeDoublePreference(name_motorBKD, motorBKD);
        initializeDoublePreference(name_motorBKF, motorBKF);
        initializeDoublePreference(name_motorBIZone, motorBIZone);
        initializeDoublePreference(name_motorBIMax, motorBIMax);
        initializeDoublePreference(name_motorBRamp, motorBRamp);
    }

    public static void updatePreferences() {
        motorAKP = updateDoublePreference(name_motorAKP, motorAKP);
        motorAKI = updateDoublePreference(name_motorAKI, motorAKI);
        motorAKD = updateDoublePreference(name_motorAKD, motorAKD);
        motorAKF = updateDoublePreference(name_motorAKF, motorAKF);
        motorAIZone = updateDoublePreference(name_motorAIZone, motorAIZone);
        motorAIMax = updateDoublePreference(name_motorAIMax, motorAIMax);
        motorARamp = updateDoublePreference(name_motorARamp, motorARamp);

        motorBKP = updateDoublePreference(name_motorBKP, motorBKP);
        motorBKI = updateDoublePreference(name_motorBKI, motorBKI);
        motorBKD = updateDoublePreference(name_motorBKD, motorBKD);
        motorBKF = updateDoublePreference(name_motorBKF, motorBKF);
        motorBIZone = updateDoublePreference(name_motorBIZone, motorBIZone);
        motorBIMax = updateDoublePreference(name_motorBIMax, motorBIMax);
        motorBRamp = updateDoublePreference(name_motorBRamp, motorBRamp);
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