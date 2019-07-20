package frc.robot.telemetry;

import java.util.Objects;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.SerialPort.Port;
import frc.robot.Constants;
import frc.robot.state.FullSwerveState;
import frc.robot.util.Vector2D;

public class SwerveTelemetry {

    private static SwerveTelemetry instance;

    private AHRS navX;

    private FullSwerveState currentState;

    private long lastTime;

    public static void init(AHRS navX) {
        if (Objects.isNull(instance)) {
            instance = new SwerveTelemetry(navX);
        }
    }

    private SwerveTelemetry(AHRS navX) {
        this.navX = navX;
        this.currentState = FullSwerveState.ZERO_STATE;
        this.setHeading(0);
        lastTime = RobotController.getFPGATime();
    }

    public static SwerveTelemetry getInstance() {

        if (Objects.isNull(instance)) {
             throw new IllegalStateException("SwerveTelemetry not initialized!");
        }

        return instance;
    }

    public double getIMUHeading() {
        return -this.navX.getAngle();
    }

    public double getIMUAngularVelocity() {
        return -this.navX.getRate();
    }

    public void setHeading(double heading) {
        this.navX.setAngleAdjustment(heading);
        this.navX.zeroYaw();
    }

    public void setPosition(double x, double y) {
        this.currentState = new FullSwerveState(Vector2D.createCartesianCoordinates(x, y), 
            this.currentState.getHeading(), this.currentState.getTranslationVelocity(), 
            this.currentState.getRotationalVelocity());
    }

    public void updateState(Vector2D frontRightVelocity, Vector2D frontLeftVelocity,
            Vector2D backLeftVelocity, Vector2D backRightVelocity) {

        Vector2D frontRightModuleLocation = Vector2D.createCartesianCoordinates(
            Constants.drivebaseWidth / 2, Constants.drivebaseLength / 2);
        Vector2D frontLeftModuleLocation = Vector2D.createCartesianCoordinates(
            -Constants.drivebaseWidth / 2, Constants.drivebaseLength / 2);
        Vector2D backLeftModuleLocation = Vector2D.createCartesianCoordinates(
            -Constants.drivebaseWidth / 2, -Constants.drivebaseLength / 2);
        Vector2D backRightModuleLocation = Vector2D.createCartesianCoordinates(
            Constants.drivebaseWidth / 2, -Constants.drivebaseLength / 2);

        frontRightModuleLocation.rotate(-getIMUHeading());
        frontLeftModuleLocation.rotate(-getIMUHeading());
        backLeftModuleLocation.rotate(-getIMUHeading());
        backRightModuleLocation.rotate(-getIMUHeading());


        Vector2D[] intersections = {
            Vector2D.findIntersection(frontRightModuleLocation, frontRightVelocity.normal(), 
                frontLeftModuleLocation, frontLeftVelocity.normal()),
            Vector2D.findIntersection(frontRightModuleLocation, frontRightVelocity.normal(), 
                backRightModuleLocation, backRightVelocity.normal()),
            Vector2D.findIntersection(frontRightModuleLocation, frontRightVelocity.normal(), 
                backLeftModuleLocation, backLeftVelocity.normal()),
            Vector2D.findIntersection(frontLeftModuleLocation, frontLeftVelocity.normal(), 
                backLeftModuleLocation, backLeftVelocity.normal()),
            Vector2D.findIntersection(frontLeftModuleLocation, frontLeftVelocity.normal(), 
                backRightModuleLocation, backRightVelocity.normal()),
            Vector2D.findIntersection(backLeftModuleLocation, backLeftVelocity.normal(), 
                backRightModuleLocation, backRightVelocity.normal())
        };

        Vector2D averageIntersection = Vector2D.average(intersections);

        double heading = getIMUHeading();
        double angularVelocity = getIMUAngularVelocity();
        Vector2D positionChange;
        Vector2D velocity;
        if (averageIntersection.getMagnitude() > 50) {
            velocity = Vector2D.average(new Vector2D[]{
                frontRightVelocity,
                frontLeftVelocity,
                backLeftVelocity,
                backRightVelocity
            });
            positionChange = velocity.times((RobotController.getFPGATime() - lastTime) / 1_000_000);
        } else {
            
        }
        Vector2D position = getFieldCentricState().getPosition().plus(positionChange);

    }

    public FullSwerveState getRobotCentricState() {
        return this.currentState;
    }

    public FullSwerveState getFieldCentricState() {
        return this.currentState.rotate(-getIMUHeading());
    }
}