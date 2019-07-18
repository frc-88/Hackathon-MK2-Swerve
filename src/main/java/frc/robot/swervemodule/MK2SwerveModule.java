package frc.robot.swervemodule;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.ConfigParameter;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogInput;

public class MK2SwerveModule implements SwerveModule {

    private static final double WHEEL_POSITION_CONVERSION = ((4. / 12.) * Math.PI) / 8.33;
    private static final double WHEEL_VELOCITY_CONVERSION = WHEEL_POSITION_CONVERSION / 60;

    private static final double WHEEL_KP = 0; 
    private static final double WHEEL_KI = 0.0001;
    private static final double WHEEL_KD = 0;
    private static final double WHEEL_KF = .08;
    private static final double WHEEL_IZONE = 2;
    private static final double WHEEL_IMAX = .2;

    private static final double AZIMUTH_POSITION_CONVERSION = 360. / 18. ;
    private static final double AZIMUTH_VELOCITY_CONVERSION = WHEEL_POSITION_CONVERSION / 60;

    private static final double AZIMUTH_KP = 0.0002; 
    private static final double AZIMUTH_KI = 0;
    private static final double AZIMUTH_KD = 0;

    private static final double AZIMUTE_ABSOLUTE_SENSOR_CONVERSION = 360. / 5.;

    private CANSparkMax wheelMotor;
    private CANEncoder wheelEncoder;
    private CANSparkMax azimuthMotor;
    private CANEncoder azimuthEncoder;
    private AnalogInput azimuthAbsoluteSensor;

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
        wheelSpeedPID.setP(WHEEL_KP);
        wheelSpeedPID.setI(WHEEL_KI);
        wheelSpeedPID.setD(WHEEL_KD);
        wheelSpeedPID.setFF(WHEEL_KF);
        wheelSpeedPID.setIZone(WHEEL_IZONE);
        wheelSpeedPID.setIAccum(WHEEL_IMAX);

        // Set up azimute motor
        azimuthMotor = new CANSparkMax(azimuthMotorId, MotorType.kBrushless);
        azimuthMotor.setIdleMode(IdleMode.kBrake);
        azimuthMotor.setParameter(ConfigParameter.kInputDeadband, 0.1);
        azimuthEncoder = azimuthMotor.getEncoder();
        azimuthEncoder.setPositionConversionFactor(AZIMUTH_POSITION_CONVERSION);
        azimuthEncoder.setVelocityConversionFactor(AZIMUTH_VELOCITY_CONVERSION);
        azimuthPID = azimuthMotor.getPIDController();
        azimuthPID.setP(AZIMUTH_KP);
        azimuthPID.setI(AZIMUTH_KI);
        azimuthPID.setD(AZIMUTH_KD);

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

        // Set the wheel speed
        wheelSpeedPID.setReference(wheelSpeed, ControlType.kVelocity);
        
        // Calibrate the azimuth encoder
        azimuthEncoder.setPosition(getAzimuth());

        // Set the azimuth
        azimuthPID.setReference(azimuth, ControlType.kPosition);
                
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


}