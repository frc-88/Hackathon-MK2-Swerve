package frc.robot.subsystems;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.I2C.Port;
import frc.robot.Constants;
import frc.robot.state.AbsoluteHeadingSwerveState;
import frc.robot.state.FullSwerveState;
import frc.robot.state.SwerveState;
import frc.robot.state.SwerveStateVisitor;
import frc.robot.state.VelocitySwerveState;
import frc.robot.telemetry.SwerveTelemetry;
import frc.robot.util.Vector2D;
import frc.robot.util.swerve.SwerveUtils;

public abstract class SwerveChassis {

    // Robot centric
    private SwerveState desiredState;

    private MotionController motionController = new MotionController();

    private long lastUpdateTime;

    protected SwerveChassis() {
        desiredState = VelocitySwerveState.ZERO_STATE;
        lastUpdateTime = RobotController.getFPGATime();
    }

    public void setRobotCentricVelocityTargets(double xVelocity, 
            double yVelocity, double rotationalVelocity) {

        desiredState = new VelocitySwerveState(
            Vector2D.createCartesianCoordinates(xVelocity, yVelocity), rotationalVelocity);

    }

    public void setFieldCentricVelocityTargets(double xVelocity, 
            double yVelocity, double rotationalVelocity) {

        desiredState = new VelocitySwerveState(
            Vector2D.createCartesianCoordinates(xVelocity, yVelocity), rotationalVelocity)
            .rotateFrame(-SwerveTelemetry.getInstance().getIMUHeading());

    }

    public void update() {
        this.desiredState.accept(this.motionController);
        lastUpdateTime = RobotController.getFPGATime();
    }

    private class MotionController implements SwerveStateVisitor<Void> {

        @Override
        public Void visitVelocitySwerveState(VelocitySwerveState desired) {

            VelocitySwerveState commanded = desired;
            FullSwerveState current = SwerveTelemetry.getInstance().getRobotCentricState();
            
            SwerveUtils.limitAccelerations(current, desired, Constants.linearAccelLimit, 
                Constants.translationAngularAccelLimit, Constants.headingAngularAccelLimit, 
                RobotController.getFPGATime() - lastUpdateTime);

            // TODO Convert to module commands

            return null;
        }

        @Override
        public Void visitAbsoluteHeadingSwerveState(AbsoluteHeadingSwerveState vss) {
            return null;
        }

        @Override
        public Void visitFullSwerveState(FullSwerveState vss) {
            return null;
		}

    }

}