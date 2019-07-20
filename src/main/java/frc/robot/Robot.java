/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifier.PWMChannel;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.swervemodule.JoeSwerveModule;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends IterativeRobot {
  private static final int FRONT_RIGHT_MOTOR_A_ID = 1;
  private static final int FRONT_RIGHT_MOTOR_B_ID = 16;
  private static final PWMChannel FRONT_RIGHT_AZIMUTH_ABSOLUTE_SENSOR_ID = PWMChannel.PWMChannel0;

  private static final int FRONT_LEFT_MOTOR_A_ID = 3;
  private static final int FRONT_LEFT_MOTOR_B_ID = 2;
  private static final PWMChannel FRONT_LEFT_AZIMUTH_ABSOLUTE_SENSOR_ID = PWMChannel.PWMChannel1;

  private static final int BACK_LEFT_MOTOR_A_ID = 13;
  private static final int BACK_LEFT_MOTOR_B_ID = 12;
  private static final PWMChannel BACK_LEFT_AZIMUTH_ABSOLUTE_SENSOR_ID = PWMChannel.PWMChannel2;

  private static final int BACK_RIGHT_MOTOR_A_ID = 15;
  private static final int BACK_RIGHT_MOTOR_B_ID = 14;
  private static final PWMChannel BACK_RIGHT_AZIMUTH_ABSOLUTE_SENSOR_ID = PWMChannel.PWMChannel3;

  private static final double FRONT_RIGHT_ZERO_AZIMUTH = 0.63;
  private static final double FRONT_LEFT_ZERO_AZIMUTH = 0.50;
  private static final double BACK_RIGHT_ZERO_AZIMUTH = 0.55;
  private static final double BACK_LEFT_ZERO_AZIMUTH = 0.95;

  public static final int CANIFIER_ID = 22;

  JoeSwerveModule frontRightModule;
  JoeSwerveModule frontLeftModule;
  JoeSwerveModule backLeftModule;
  JoeSwerveModule backRightModule;

  AHRS navX;

  CANifier canifier;

  public static OI oi;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    Constants.init();
    Constants.updatePreferences();

    canifier = new CANifier(CANIFIER_ID);

    frontRightModule = new JoeSwerveModule(FRONT_RIGHT_MOTOR_A_ID, FRONT_RIGHT_MOTOR_B_ID, canifier,
        FRONT_RIGHT_AZIMUTH_ABSOLUTE_SENSOR_ID);
    frontRightModule.setAzimuthOffset(FRONT_RIGHT_ZERO_AZIMUTH);
    frontLeftModule = new JoeSwerveModule(FRONT_LEFT_MOTOR_A_ID, FRONT_LEFT_MOTOR_B_ID, canifier,
        FRONT_LEFT_AZIMUTH_ABSOLUTE_SENSOR_ID);
    frontLeftModule.setAzimuthOffset(FRONT_LEFT_ZERO_AZIMUTH);
    backLeftModule = new JoeSwerveModule(BACK_LEFT_MOTOR_A_ID, BACK_LEFT_MOTOR_B_ID, canifier,
        BACK_LEFT_AZIMUTH_ABSOLUTE_SENSOR_ID);
    backLeftModule.setAzimuthOffset(BACK_LEFT_ZERO_AZIMUTH);
    backRightModule = new JoeSwerveModule(BACK_RIGHT_MOTOR_A_ID, BACK_RIGHT_MOTOR_B_ID, canifier,
        BACK_RIGHT_AZIMUTH_ABSOLUTE_SENSOR_ID);
    backRightModule.setAzimuthOffset(BACK_RIGHT_ZERO_AZIMUTH);

    navX = new AHRS(Port.kOnboard);
    navX.zeroYaw();

    oi = new OI();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("FrontRightAzimuthVel", frontRightModule.getAzimuthVelocity());
    SmartDashboard.putNumber("FrontLeftAzimuthVel", frontLeftModule.getAzimuthVelocity());
    SmartDashboard.putNumber("BackLeftAzimuthVel", backLeftModule.getAzimuthVelocity());
    SmartDashboard.putNumber("BackRightAzimuthVel", backRightModule.getAzimuthVelocity());

    SmartDashboard.putNumber("Front Right Speed", frontRightModule.getSpeed());
    SmartDashboard.putNumber("Front Right Azimuth", frontRightModule.getAzimuth());
    SmartDashboard.putNumber("Front Left Speed", frontLeftModule.getSpeed());
    SmartDashboard.putNumber("Front Left Azimuth", frontLeftModule.getAzimuth());
    SmartDashboard.putNumber("Back Left Speed", backLeftModule.getSpeed());
    SmartDashboard.putNumber("Back Left Azimuth", backLeftModule.getAzimuth());
    SmartDashboard.putNumber("Back Right Speed", backRightModule.getSpeed());
    SmartDashboard.putNumber("Back Right Azimuth", backRightModule.getAzimuth());

    SmartDashboard.putNumber("FrontRightMotorAVel", frontRightModule.getMotorAVelocity());
    SmartDashboard.putNumber("FrontRightMotorBVel", frontRightModule.getMotorBVelocity());

    SmartDashboard.putNumber("NavX Yaw", -navX.getYaw());
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    navX.zeroYaw();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    Constants.updatePreferences();

    frontRightModule.configureFromPreferences();
    frontLeftModule.configureFromPreferences();
    backLeftModule.configureFromPreferences();
    backRightModule.configureFromPreferences();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    var speed = oi.getSpeed();
    var azimuth = oi.getAzimuth();

    frontRightModule.set(speed, azimuth);
    frontLeftModule.set(speed, azimuth);
    backRightModule.set(speed, azimuth);
    backLeftModule.set(speed, azimuth);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
