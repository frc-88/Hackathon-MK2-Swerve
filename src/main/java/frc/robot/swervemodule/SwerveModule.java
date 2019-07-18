package frc.robot.swervemodule;

public interface SwerveModule {

    /**
     * Sets the commanded speed and azimuith of the swerve module.
     * 
     * @param wheelSpeed The wheel speed commanded, in feet/second
     * @param azimuth The azimuth commanded, in degrees (0 being forwards)
     */
    public void set(double wheelSpeed, double azimuth);

    /**
     * Gets the speed of the wheel.
     * 
     * @return The speed in feet/second
     */
    public double getSpeed();

    /**
     * Gets the azimuth of the module.
     */
    public double getAzimuth();

}