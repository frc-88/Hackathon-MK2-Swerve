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

    TJPIDController azimuthPid;

    /** Azimuth encoder value when the module is pointing forwards, [0-1) */
    private double azimuthOffset = 0.0;
    private PWMChannel azimuthAbsoluteEncoder;

    public JoeSwerveModule(int motorAId, int motorBId, CANifier canifier, PWMChannel azimuthAbsoluteEncoder) {
        this.canifier = canifier;
        this.azimuthAbsoluteEncoder = azimuthAbsoluteEncoder;
        azimuthPid = new TJPIDController(200, 0, 0);

        // Set up motor A
        motorA = new CANSparkMax(motorAId, MotorType.kBrushless);
        motorA.setIdleMode(IdleMode.kBrake);
        motorA.setParameter(ConfigParameter.kInputDeadband, 0.1);
        encoderA = motorA.getEncoder();
        speedPidA = motorA.getPIDController();

        // Set up motor B
        motorB = new CANSparkMax(motorBId, MotorType.kBrushless);
        motorB.setIdleMode(IdleMode.kBrake);
        motorB.setParameter(ConfigParameter.kInputDeadband, 0.1);
        encoderB = motorB.getEncoder();
        speedPidB = motorB.getPIDController();
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
        double azimuthVel = azimuthPid.calculateOutput(getAzimuth(), azimuth);
        double velocityB = 3.33 * wheelSpeed - 33.3 * azimuthVel;
        double velocityA = 66.6 * azimuthVel + velocityB;
        setMotors(velocityA, velocityB);
    }

    @Override
    public double getSpeed() {
        return wheelVelocityFromMotors(getMotorAVelocity(), getMotorBVelocity());
    }

    @Override
    public double getAzimuth() {
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
        return 1 - azimuth;
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