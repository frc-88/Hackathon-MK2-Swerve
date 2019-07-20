package frc.robot.telemetry;

import java.util.Objects;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SerialPort.Port;
import frc.robot.state.FullSwerveState;
import frc.robot.util.Vector2D;

public class SwerveTelemetry {

    private static SwerveTelemetry instance;
    private AHRS navX;
    private FullSwerveState currentState;

    public static void init(AHRS navX) {
        if (Objects.isNull(instance)) {
            SwerveTelemetry.instance = new SwerveTelemetry(navX);
        }
    }

    private SwerveTelemetry(AHRS navX) {
        this.navX = navX;
        this.currentState = FullSwerveState.ZERO_STATE;
        this.setHeading(0);
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

    public void setHeading(double heading) {
        this.navX.setAngleAdjustment(heading);
        this.navX.zeroYaw();
    }

    public void setPosition(double x, double y) {
        // TODO: Shift currentState
    }

    public void updateState(Vector2D frontRightVelocity, Vector2D frontLeftVelocity,
            Vector2D backLeftVelocity, Vector2D backRightVelocity) {

        // TODO: Odometry calculation

    }

    public FullSwerveState getRobotCentricState() {
        return this.currentState;
    }

    public FullSwerveState getFieldCentricState() {
        return this.currentState.rotate(-getIMUHeading());
    }
}