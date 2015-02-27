package org.usfirst.frc.team1218.subsystem.swerve;

import org.usfirst.frc.team1218.robot.Robot;
import org.usfirst.frc.team1218.subsystem.swerve.math.Vector;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class C_GoToHeading extends Command {

	double heading;
	
    public C_GoToHeading(double heading) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.swerveDrive);
    	this.heading = heading;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.swerveDrive.enableHeadingController(heading);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.swerveDrive.powerDrive(new Vector(0, 0), 0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.swerveDrive.isHeadingOnTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}