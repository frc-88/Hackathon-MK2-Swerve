package frc.robot.state;

public interface SwerveState {

    public <R> R accept(SwerveStateVisitor<R> v);

}