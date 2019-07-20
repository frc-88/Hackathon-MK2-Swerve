package frc.robot.swervemodule;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifier.PWMChannel;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.ConfigParameter;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants;
import frc.robot.util.MathUtils;
import frc.robot.util.TJPIDController;

public class JoeSwerveModule implements SwerveModule {
    // Hardware device objects
    private CANSparkMax motorA;
    private CANEncoder encoderA;
    private CANSparkMax motorB;
    private CANEncoder encoderB;
    private CANPIDController speedPidA;
    private CANPIDController speedPidB;
    CANifier canifier;

    private double previousRawAzimuth;
    private double fullRotations = 0;

    TJPIDController azimuthPid;

    /** Azimuth encoder value when the module is pointing forwards, [0-1) */
    private double azimuthOffset = 0.0;
    private PWMChannel azimuthAbsoluteEncoder;

    public JoeSwerveModule(int motorAId, int motorBId, CANifier canifier, PWMChannel azimuthAbsoluteEncoder) {
        this.canifier = canifier;
        this.azimuthAbsoluteEncoder = azimuthAbsoluteEncoder;
        previousRawAzimuth = getRawAzimuth();
        azimuthPid = new TJPIDController(0.5, 0, 0);

        // Set up motor A
        motorA = new CANSparkMax(motorAId, MotorType.kBrushless);
        motorA.setIdleMode(IdleMode.kBrake);
        motorA.setParameter(ConfigParameter.kInputDeadband, 0.1);
        encoderA = motorA.getEncoder();
        speedPidA = motorA.getPIDController();
        motorA.setParameter(ConfigParameter.kClosedLoopRampRate, 0);

        // Set up motor B
        motorB = new CANSparkMax(motorBId, MotorType.kBrushless);
        motorB.setIdleMode(IdleMode.kBrake);
        motorB.setParameter(ConfigParameter.kInputDeadband, 0.1);
        encoderB = motorB.getEncoder();
        speedPidB = motorB.getPIDController();
        motorB.setParameter(ConfigParameter.kClosedLoopRampRate, 0);
    }

    public void configureFromPreferences() {
        speedPidA.setP(Constants.motorAKP);
        speedPidA.setI(Constants.motorAKI);
        speedPidA.setD(Constants.motorAKD);
        speedPidA.setFF(Constants.motorAKF);
        speedPidA.setIZone(Constants.motorAIZone);

        speedPidB.setP(Constants.motorBKP);
        speedPidB.setI(Constants.motorBKI);
        speedPidB.setD(Constants.motorBKD);
        speedPidB.setFF(Constants.motorBKF);
        speedPidB.setIZone(Constants.motorBIZone);
    }

    public void setAzimuthOffset(double azimuthOffset) {
        this.azimuthOffset = azimuthOffset;
    }

    @Override
    public void set(double wheelSpeed, double azimuth) {

        double currentAzimuth = getAzimuth();
        double targetAzimuth = azimuth;

        while (targetAzimuth - currentAzimuth > 180) {
            targetAzimuth -= 360;
        }

        while (targetAzimuth - currentAzimuth < -180) {
            targetAzimuth += 360;
        }

        double azimuthVel = azimuthPid.calculateOutput(currentAzimuth, targetAzimuth);

        double velocityB = 3.33 * wheelSpeed - 33.3 * azimuthVel;
        double velocityA = 66.6 * azimuthVel + velocityB;

        setMotors(velocityA, velocityB);
    }

    @Override
    public double getSpeed() {
        return wheelVelocityFromMotors(getMotorAVelocity(), getMotorBVelocity());
    }

    /**
     * @return Absolute azimuth angle in degrees in [-180, 180)
     */
    private double getRawAzimuth() {
        double[] pwmIn = new double[2];
        canifier.getPWMInput(azimuthAbsoluteEncoder, pwmIn);
        double azimuth = pwmIn[0] / pwmIn[1];
        azimuth -= azimuthOffset;
        while (azimuth < 0) {
            azimuth += 1;
        }
        while (azimuth >= 1.0) {
            azimuth -= 1;
        }
        return (1 - azimuth) * 360 - 180;
    }

    /**
     * @return Absolute azimuth angle in degrees without bounds
     */
    @Override
    public double getAzimuth() {
        double rawAzimuth = getRawAzimuth();

        // Detect a full rotation based on wraparound
        double rawAzimuthDelta = (previousRawAzimuth + 180) - (rawAzimuth + 180);
        if (rawAzimuthDelta > 270) {
            fullRotations += 1;
        } else if (rawAzimuthDelta < -270) {
            fullRotations -= 1;
        }

        previousRawAzimuth = rawAzimuth;
        return fullRotations * 360 + rawAzimuth;
    }

    @Override
    public double getAzimuthVelocity() {
        return azimuthVelocityFromMotors(getMotorAVelocity(), getMotorBVelocity());
    }

    public static double wheelVelocityFromMotors(double velocityA, double velocityB) {
        return (velocityA + velocityB) / 6.66;
    }

    public static double azimuthVelocityFromMotors(double velocityA, double velocityB) {
        return (velocityA - velocityB) / 66.6;
    }

    public double getMotorAVelocity() {
        return encoderA.getVelocity();
    }

    public double getMotorBVelocity() {
        return encoderB.getVelocity();
    }

    public void setMotors(double velocityA, double velocityB) {
        speedPidA.setReference(velocityA, ControlType.kVelocity);
        speedPidB.setReference(velocityB, ControlType.kVelocity);
    }
}