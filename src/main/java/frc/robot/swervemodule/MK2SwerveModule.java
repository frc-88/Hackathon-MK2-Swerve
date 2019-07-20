package frc.robot.swervemodule;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.ConfigParameter;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.robot.Constants;

/**
 * Encapsulates the hardware interface for an MK2 Swerve Module.
 */
public class MK2SwerveModule implements SwerveModule {

    /**
     * Constants relating to the wheel speed controls of the module
     */

    // Conversions from the Spark Max's native units of revolutions and rpm to feet and feet/second
    private static final double WHEEL_POSITION_CONVERSION = ((4. / 12.) * Math.PI) / 8.33;
    private static final double WHEEL_VELOCITY_CONVERSION = WHEEL_POSITION_CONVERSION / 60;


    /**
     * Constants relating to the azimuth controls of the module.
     */

    // Conversions from the Spark Max's native units of revolutions and rpm to degrees
    // and degrees/second
    private static final double AZIMUTH_POSITION_CONVERSION = 360. / 18. ;
    private static final double AZIMUTH_VELOCITY_CONVERSION = AZIMUTH_POSITION_CONVERSION / 60;

    // Conversion from the absolute encoder's voltage output to degrees
    private static final double AZIMUTE_ABSOLUTE_SENSOR_CONVERSION = 360. / 5.;



    // Hardware device objects
    private CANSparkMax wheelMotor;
    private CANEncoder wheelEncoder;
    private CANSparkMax azimuthMotor;
    private CANEncoder azimuthEncoder;
    private AnalogInput azimuthAbsoluteSensor;


    // PID
    private CANPIDController wheelSpeedPID;
    private CANPIDController azimuthPID;

    private double azimuthOffset = 0;

    public MK2SwerveModule(int wheelMotorId, int azimuthMotorId, int azimuthAbsoluteSensorId) {

        // Set up wheel motor
        wheelMotor = new CANSparkMax(wheelMotorId, MotorType.kBrushless);
        wheelMotor.setIdleMode(IdleMode.kBrake);
        wheelMotor.setParameter(ConfigParameter.kInputDeadband, 0.1);
        wheelEncoder = wheelMotor.getEncoder();
        wheelEncoder.setPositionConversionFactor(WHEEL_POSITION_CONVERSION);
        wheelEncoder.setVelocityConversionFactor(WHEEL_VELOCITY_CONVERSION);
        wheelSpeedPID = wheelMotor.getPIDController();
        wheelSpeedPID.setP(Constants.mk2WheelKP);
        wheelSpeedPID.setI(Constants.mk2WheelKI);
        wheelSpeedPID.setD(Constants.mk2WheelKD);
        wheelSpeedPID.setFF(Constants.mk2WheelKF);
        wheelSpeedPID.setIZone(Constants.mk2WheelIZone);
        wheelSpeedPID.setIMaxAccum(Constants.mk2WheelIMax, 0);

        // Set up azimute motor
        azimuthMotor = new CANSparkMax(azimuthMotorId, MotorType.kBrushless);
        azimuthMotor.setInverted(true);
        azimuthMotor.setIdleMode(IdleMode.kBrake);
        azimuthMotor.setParameter(ConfigParameter.kInputDeadband, 0.1);
        azimuthMotor.setClosedLoopRampRate(Constants.mk2AzimuthRamp);
        azimuthEncoder = azimuthMotor.getEncoder();
        azimuthEncoder.setPositionConversionFactor(AZIMUTH_POSITION_CONVERSION);
        azimuthEncoder.setVelocityConversionFactor(AZIMUTH_VELOCITY_CONVERSION);
        azimuthPID = azimuthMotor.getPIDController();
        azimuthPID.setP(Constants.mk2AzimuthKP);
        azimuthPID.setI(Constants.mk2AzimuthKI);
        azimuthPID.setD(Constants.mk2AzimuthKD);

        // Set up azimuth absolute encoder
        azimuthAbsoluteSensor = new AnalogInput(azimuthAbsoluteSensorId);

    }

    /**
     * Sets the offset of the azbsolutw azimuth sensor.
     * 
     * @param offset the ammount of degrees to add to the azimuth absolute sensor reading.
     */
    public void setAzimuthOffset(double offset) {
        this.azimuthOffset = offset;
    }

    @Override
    public void set(double wheelSpeed, double azimuth) {

        // // Set the wheel angle to the closest multiple of 180
        double currentAzimuth = azimuthEncoder.getPosition();
        double targetAzimuth = azimuth % 180;
        boolean flipSpeed = (Math.abs(azimuth - 180) < .001) || (Math.abs(azimuth + 180) < .001);
        while (Math.abs(targetAzimuth - currentAzimuth) > 90) {
            if (targetAzimuth < currentAzimuth) {
                targetAzimuth += 180;
            } else {
                targetAzimuth -= 180;
            }
            flipSpeed = !flipSpeed;
        }
        if (flipSpeed) {
            wheelSpeed = -wheelSpeed;
        }


        // Set the wheel speed
        wheelSpeedPID.setReference(wheelSpeed, ControlType.kVelocity);

        // Set the azimuth
        azimuthPID.setReference(targetAzimuth, ControlType.kPosition);
                
    }

    @Override
    public double getSpeed() {
        return wheelEncoder.getVelocity();
    }

    @Override
    public double getAzimuth() {
        double rawAngle = azimuthAbsoluteSensor.getAverageVoltage() 
                * AZIMUTE_ABSOLUTE_SENSOR_CONVERSION + azimuthOffset;

        return (rawAngle + 180) % 360 - 180;
    }

    @Override
    public double getAzimuthVelocity() {
        return azimuthEncoder.getVelocity()
;    }

    /**
     * Sets the azimuth tracked by the encoder (used for PID) using the absolute encoder.
     */
    public void calibrateAzimuth() {
        azimuthEncoder.setPosition(getAzimuth());
    }

    /**
     * Sets the azimuth to the given velocity. Mainly used for tuning the PID.
     * 
     * @param velocity The angular velocity to set in degrees per second
     */
    public void setAngularVelocity(double velocity) {
        azimuthPID.setReference(velocity, ControlType.kVelocity);
    }


}