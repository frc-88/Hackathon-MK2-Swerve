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

public class JoeSwerveModule implements SwerveModule {
    // Hardware device objects
    private CANSparkMax motorA;
    private CANEncoder encoderA;
    private CANSparkMax motorB;
    private CANEncoder encoderB;
    private AnalogInput azimuthAbsoluteSensor;
    private CANPIDController speedPidA;
    private CANPIDController speedPidB;

    public JoeSwerveModule(int motorAId, int motorBId, int azimuthAbsoluteSensorId) {
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

    @Override
    public void set(double wheelSpeed, double azimuth) {
        // TODO: right now this interprets azimuth as azimuth velocity!!
        double speedB = 3.33 * wheelSpeed - 33.3 * azimuth;
        double speedA = 66.6 * azimuth + speedB;

        speedPidA.setReference(speedA, ControlType.kVelocity);
        speedPidB.setReference(speedB, ControlType.kVelocity);
    }

    @Override
    public double getSpeed() {
        // TODO: haha
        return 0;
    }

    @Override
    public double getAzimuth() {
        // TODO: haha
        return 0;
    }

    @Override
    public double getAzimuthVelocity() {
        // TODO: haha
        return 0;
    }

    public double getMotorAVelocity() {
        return encoderA.getVelocity();
    }

    public double getMotorBVelocity() {
        return encoderB.getVelocity();
    }

    public void setMotorA(double speed) {
        speedPidA.setReference(speed, ControlType.kVelocity);
    }

    public void setMotorB(double speed) {
        speedPidB.setReference(speed, ControlType.kVelocity);
    }
}