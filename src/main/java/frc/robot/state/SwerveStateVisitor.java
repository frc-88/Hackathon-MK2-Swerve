package frc.robot.state;

public interface SwerveStateVisitor<R> {

    public R visitVelocitySwerveState(VelocitySwerveState vss);

    public R visitAbsoluteHeadingSwerveState(AbsoluteHeadingSwerveState vss);

    public R visitFullSwerveState(FullSwerveState vss);

}