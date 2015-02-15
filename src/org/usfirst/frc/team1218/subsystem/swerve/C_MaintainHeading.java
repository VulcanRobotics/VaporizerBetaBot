package org.usfirst.frc.team1218.subsystem.swerve;

import org.usfirst.frc.team1218.robot.OI;
import org.usfirst.frc.team1218.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.command.Command;

/**
 * 
 * @author afiol-mahon
 *
 */
public class C_MaintainHeading extends Command implements PIDOutput, PIDSource{//FIXME Tune PID
	
	private PIDController headingController;
	
	public C_MaintainHeading() {
		requires(Robot.swerveDrive);
	}
	
	private static final double HEADING_CONTROLLER_P = 0.03;
	private static final double HEADING_CONTROLLER_I = 0.0;
	private static final double HEADING_CONTROLLER_D = 0.0;
	
	@Override
	protected void initialize() {
		headingController = new PIDController(
				HEADING_CONTROLLER_P,
				HEADING_CONTROLLER_I,
				HEADING_CONTROLLER_D,
				this,
				this);
		headingController.setOutputRange(-1.0, 1.0);
		headingController.setInputRange(-180, 180);
		headingController.setContinuous();
		headingController.enable();
		headingController.setSetpoint(Robot.swerveDrive.navModule.getYaw());
		System.out.println("Maintaining Heading of " + headingController.getSetpoint());
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		headingController.disable();
	}

	@Override
	protected void interrupted() {
		this.end();
	}
	
	@Override
	public void pidWrite(double output) {
		Robot.swerveDrive.powerDrive(
				OI.getDriverLeftJoystickVector(),
				output,
				Robot.swerveDrive.navModule.getYaw()
				);
	}

	@Override
	public double pidGet() {
		return Robot.swerveDrive.navModule.getYaw();
	}
}