package frc.robot.state;

public interface SwerveStateVisitor<R> {

    public R visitVelocitySwerveState(VelocitySwerveState vss);

}