/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.swervemodule.MK2SwerveModule;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends IterativeRobot {
  private static final int FRONT_RIGHT_WHEEL_MOTOR_ID = 1;
  private static final int FRONT_RIGHT_AZIMUTH_MOTOR_ID = 16;
  private static final int FRONT_RIGHT_AZIMUTH_ABSOLUTE_SENSOR_ID = 0;

  private static final int FRONT_LEFT_WHEEL_MOTOR_ID = 3;
  private static final int FRONT_LEFT_AZIMUTH_MOTOR_ID = 2;
  private static final int FRONT_LEFT_AZIMUTH_ABSOLUTE_SENSOR_ID = 1;

  private static final int BACK_LEFT_WHEEL_MOTOR_ID = 13;
  private static final int BACK_LEFT_AZIMUTH_MOTOR_ID = 12;
  private static final int BACK_LEFT_AZIMUTH_ABSOLUTE_SENSOR_ID = 2;

  private static final int BACK_RIGHT_WHEEL_MOTOR_ID = 15;
  private static final int BACK_RIGHT_AZIMUTH_MOTOR_ID = 14;
  private static final int BACK_RIGHT_AZIMUTH_ABSOLUTE_SENSOR_ID = 3;

  private static final double FRONT_RIGHT_AZIMUTH_OFFSET = -178.3;
  private static final double FRONT_LEFT_AZIMUTH_OFFSET = -179.6;
  private static final double BACK_LEFT_AZIMUTH_OFFSET = 174.8;
  private static final double BACK_RIGHT_AZIMUTH_OFFSET = -178.0;

  MK2SwerveModule frontRightModule;
  MK2SwerveModule frontLeftModule;
  MK2SwerveModule backLeftModule;
  MK2SwerveModule backRightModule;

  AHRS navX;

  public static OI oi;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {

    Constants.init();
    Constants.updatePreferences();

    frontRightModule = new MK2SwerveModule(FRONT_RIGHT_WHEEL_MOTOR_ID, FRONT_RIGHT_AZIMUTH_MOTOR_ID,
        FRONT_RIGHT_AZIMUTH_ABSOLUTE_SENSOR_ID);
    frontRightModule.setAzimuthOffset(FRONT_RIGHT_AZIMUTH_OFFSET);
    frontLeftModule = new MK2SwerveModule(FRONT_LEFT_WHEEL_MOTOR_ID, FRONT_LEFT_AZIMUTH_MOTOR_ID,
        FRONT_LEFT_AZIMUTH_ABSOLUTE_SENSOR_ID);
    frontLeftModule.setAzimuthOffset(FRONT_LEFT_AZIMUTH_OFFSET);
    backLeftModule = new MK2SwerveModule(BACK_LEFT_WHEEL_MOTOR_ID, BACK_LEFT_AZIMUTH_MOTOR_ID,
        BACK_LEFT_AZIMUTH_ABSOLUTE_SENSOR_ID);
    backLeftModule.setAzimuthOffset(BACK_LEFT_AZIMUTH_OFFSET);
    backRightModule = new MK2SwerveModule(BACK_RIGHT_WHEEL_MOTOR_ID, BACK_RIGHT_AZIMUTH_MOTOR_ID,
        BACK_RIGHT_AZIMUTH_ABSOLUTE_SENSOR_ID);
    backRightModule.setAzimuthOffset(BACK_RIGHT_AZIMUTH_OFFSET);

    navX = new AHRS(Port.kOnboard);
    navX.zeroYaw();

    SmartDashboard.putBoolean("EnableAzimuthVelocityMode", false);
    SmartDashboard.putNumber("CommandFrontRightAzimuthVel", 0);
    SmartDashboard.putNumber("CommandFrontLeftAzimuthVel", 0);
    SmartDashboard.putNumber("CommandBackLeftAzimuthVel", 0);
    SmartDashboard.putNumber("CommandBackRightAzimuthVel", 0);

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

    Constants.updatePreferences();

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
    frontRightModule.calibrateAzimuth();
    frontLeftModule.calibrateAzimuth();
    backLeftModule.calibrateAzimuth();
    backRightModule.calibrateAzimuth();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    if (SmartDashboard.getBoolean("EnableAzimuthVelocityMode", false)) {

      frontRightModule.setAngularVelocity(SmartDashboard.getNumber("CommandFrontRightAzimuthVel", 0));
      frontLeftModule.setAngularVelocity(SmartDashboard.getNumber("CommandFrontLeftAzimuthVel", 0));
      backLeftModule.setAngularVelocity(SmartDashboard.getNumber("CommandBackLeftAzimuthVel", 0));
      backRightModule.setAngularVelocity(SmartDashboard.getNumber("CommandBackRightAzimuthVel", 0));

    } else {

      

    }
  }

  public static double[] calculateSwerveModuleAngles(double forward, double strafe, double rotation) {

    double WHEELBASE = 28;
    double TRACKWIDTH = 26;

    rotation = Math.toRadians(rotation);

    double a = strafe - rotation * (WHEELBASE / TRACKWIDTH);
    double b = strafe + rotation * (WHEELBASE / TRACKWIDTH);
    double c = forward - rotation * (TRACKWIDTH / WHEELBASE);
    double d = forward + rotation * (TRACKWIDTH / WHEELBASE);

    return new double[]{
            Math.atan2(b, c) * 180 / Math.PI,
            Math.atan2(b, d) * 180 / Math.PI,
            Math.atan2(a, d) * 180 / Math.PI,
            Math.atan2(a, c) * 180 / Math.PI
    };
}

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
