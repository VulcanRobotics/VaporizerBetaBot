package org.usfirst.frc.team1218.robot;

import org.usfirst.frc.team1218.commands.auton.Auton_Calibrate;
import org.usfirst.frc.team1218.commands.auton.Auton_JustDrive;
import org.usfirst.frc.team1218.commands.auton.Auton_Step;
import org.usfirst.frc.team1218.commands.auton.Auton_ThreeTote;
import org.usfirst.frc.team1218.commands.auton.Auton_TwoTote;
import org.usfirst.frc.team1218.subsystem.binGrabber.BinGrabber;
import org.usfirst.frc.team1218.subsystem.binIntake.BinIntake;
import org.usfirst.frc.team1218.subsystem.elevator.Elevator;
import org.usfirst.frc.team1218.subsystem.fourBar.FourBar;
import org.usfirst.frc.team1218.subsystem.swerve.SwerveDrive;
import org.usfirst.frc.team1218.subsystem.toteIntake.ToteIntake;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 * 
 * @author afiol-mahon
 */
public class Robot extends IterativeRobot {

	public static SwerveDrive swerveDrive;
	public static FourBar fourBar;
	public static Elevator elevator;
	public static ToteIntake toteIntake;
	public static BinIntake binIntake;
	public static OI oi;
	private static String autonName;
	public static BinGrabber binGrabber;
	public Command robotAuton;

	Command autonomousCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		swerveDrive = new SwerveDrive();
		fourBar = new FourBar();
		elevator = new Elevator();
		toteIntake = new ToteIntake();
		binIntake = new BinIntake();
		binGrabber = new BinGrabber();
		oi = new OI();
		System.out.println("Robot Initialized");
	}

	public void disabledPeriodic() {
		autonName = SmartDashboard.getString("Auton_Select", "Not Set");
		SmartDashboard.putString("Current_Auton_Selected", autonName);
		Scheduler.getInstance().run();
		periodicTasks();
	}
	
	/**
	 * Selects an auton based on dashboard input
	 */
	public void autonomousInit() {
		autonName = SmartDashboard.getString("Auton_Select", "Not Set");
		Robot.swerveDrive.setInitalOffset(SmartDashboard.getNumber("Swerve_Module_Initial_Position", 0));
		System.out.println("initial swerve module position was: " + SmartDashboard.getNumber("Swerve_Module_Initial_Position", 0));
		SmartDashboard.putString("Current_Auton", autonName);
		System.out.println("Auton " + autonName + " selected.");
		switch (autonName) {
		default:
			autonomousCommand = new Auton_Calibrate(false);
		case "No Auton":
			autonomousCommand = new Auton_Calibrate(false);
			break;
		case "TwoToteAuton":
			autonomousCommand = new Auton_TwoTote();
			break;
		case "ThreeToteAuton":
			autonomousCommand = new Auton_ThreeTote();
			break;
		case "JustDrive":
			autonomousCommand = new Auton_JustDrive();
			break;
		case "StepAuton":
			autonomousCommand = new Auton_Step();
			break;
		}
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		periodicTasks();
	}

	public void teleopInit() {
		Robot.binIntake.setClamp(false);
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		Robot.toteIntake.setPower(0);
		Robot.binIntake.setBinIntake(BinIntake.CONTINOUS_HOLD_POWER);
		System.out.println("Teleop Initialized");
	}

	
	/**
	 * This function is called when the disabled button is hit. You can use it
	 * to reset subsystems before shutting down.
	 */
	public void disabledInit() {

	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		periodicTasks();
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
	}
    
	/**
	 * Called periodically to update values displayed on dashboard
	 */
    public void periodicTasks() {
    	Robot.swerveDrive.syncDashboard();
    	Robot.elevator.periodicTasks();
    	Robot.fourBar.periodicTasks();
    	Robot.toteIntake.syncDashboard();
    	Robot.binIntake.periodicTasks();
    	SmartDashboard.putBoolean("isBeta", Preferences.getInstance().getBoolean("isBeta", false));
    }
}
