package org.usfirst.frc.team1218.robot;

import org.usfirst.frc.team1218.commands.binGrabber.SetGrabber;
import org.usfirst.frc.team1218.commands.binIntake.SetBinIntake;
import org.usfirst.frc.team1218.commands.binIntake.SetClamp;
import org.usfirst.frc.team1218.commands.elevator.AutoStack;
import org.usfirst.frc.team1218.commands.elevator.ReferenceElevator;
import org.usfirst.frc.team1218.commands.fourBar.LeftDartManualControl;
import org.usfirst.frc.team1218.commands.fourBar.RightDartManualControl;
import org.usfirst.frc.team1218.commands.swerve.AlignStrait;
import org.usfirst.frc.team1218.commands.swerve.CalibrateModules;
import org.usfirst.frc.team1218.commands.swerve.MaintainRobotHeading;
import org.usfirst.frc.team1218.commands.swerve.TankDrive;
import org.usfirst.frc.team1218.commands.swerve.ToggleFieldCentricDrive;
import org.usfirst.frc.team1218.commands.swerve.ZeroRobotHeading;
import org.usfirst.frc.team1218.commands.toteIntake.SetToteIntake;
import org.usfirst.frc.team1218.subsystem.binGrabber.BinGrabber;
import org.usfirst.frc.team1218.subsystem.binIntake.BinIntake;
import org.usfirst.frc.team1218.subsystem.swerve.math.Vector;
import org.usfirst.frc.team1218.subsystem.toteIntake.ToteIntake;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 * @author afiolmahon
 * @author liamcook
 */
public class OI {
	
	public static final int CENTER = 1218;
	public static final int RIGHT= 193;
	public static final int LEFT = 219;
	
	static final float RUMBLE_POWER = (float) 0.7;
    //Driver
	public static Joystick driver;
	public static Button resetGyro;
	public static Button maintainHeading;
	public static Button calibrateSwerve;
	public static Button fieldCentricToggle;
	public static Button tankDrive;

	//Operator
	public static Joystick operator;

	//Elevator
	public static Button elevatorDropStack;
	public static Button elevatorRaiseStack;
	public static Button elevatorToStepPosition;
	public static Button elevatorManualLower;
	public static Button elevatorManualRaise;
	public static Button elevatorZeroPosition;
	public static Button elevatorAutomatic;
	public static Button elevatorDisableSoftLimits;

	//Tote Intake
	public static Button elevatorRunToteIntake;
	public static Button elevatorReverseToteIntake;

	public static Button toteIntakeFromLeft;
	public static Button toteIntakeFromRight;

	//Four Bar
	public static Button fourBarHighPosition;
	public static Button fourBarAutonPosition;

	public static DashboardButton leftDartUp;
	public static DashboardButton leftDartDown;
	public static DashboardButton rightDartUp;
	public static DashboardButton rightDartDown;

	//Bin Intake
	public static Button runBinIntake;
	public static Button openBinGrabber;
	public static Button reverseBinIntake;

	public static Button referenceElevator;
	//test
	public static Button test;

	public static Button alignSwerves;
	
	public static Button upDPad;
	public static Button leftDPad;
	public static Button rightDPad;
	public static Button downDPad;
	public OI() {
    	//Driver
    	driver = new Joystick(RobotMap.DRIVER_JOYSTICK);

        resetGyro = new JoystickButton(driver, RobotMap.BUTTON_RESET_GYRO);
        resetGyro.whenPressed(new ZeroRobotHeading());

        maintainHeading = new JoystickButton(driver, RobotMap.BUTTON_MAINTAIN_HEADING);
        maintainHeading.whileHeld(new MaintainRobotHeading());

        calibrateSwerve = new JoystickButton(driver, RobotMap.BUTTON_INDEX_SWERVE);
        calibrateSwerve.whenPressed(new CalibrateModules());

        fieldCentricToggle = new JoystickButton(driver, RobotMap.BUTTON_FIELD_CENTRIC_TOGGLE);
        fieldCentricToggle.whenPressed(new ToggleFieldCentricDrive());

        tankDrive = new JoystickButton(driver, RobotMap.BUTTON_TANK_DRIVE);
        tankDrive.whileHeld(new TankDrive());

         //Operator
        operator = new Joystick(RobotMap.OPERATOR_JOYSTICK);

        //Swerve
        alignSwerves = new JoystickButton(operator, RobotMap.BUTTON_PREPARE_FOR_AUTON);
        alignSwerves.whileHeld(new AlignStrait());
        
        //Elevator
        elevatorManualRaise = new JoystickButton(operator, RobotMap.BUTTON_ELEVATOR_MANUAL_RAISE);
        elevatorManualLower = new JoystickButton(operator, RobotMap.BUTTON_ELEVATOR_MANUAL_LOWER);

        elevatorAutomatic = new JoystickButton(operator, RobotMap.BUTTON_ELEVATOR_AUTOMATIC);
        elevatorAutomatic.whileHeld(new AutoStack());

        referenceElevator = new JoystickButton(operator, RobotMap.BUTTON_ELEVATOR_REFERENCE);
        referenceElevator.whileHeld(new ReferenceElevator());

        //Tote Intake
        elevatorRunToteIntake = new JoystickButton(operator, RobotMap.BUTTON_ELEVATOR_RUN_TOTE_INTAKE);
        elevatorRunToteIntake.whenPressed(new SetToteIntake(true, true, ToteIntake.TOTE_INTAKE_POWER));
        elevatorRunToteIntake.whenReleased(new SetToteIntake(true, true, 0.0));

        elevatorReverseToteIntake = new JoystickButton(operator, RobotMap.BUTTON_ELEVATOR_REVERSE_TOTE_INTAKE);
        elevatorReverseToteIntake.whenPressed(new SetToteIntake(true, true, -ToteIntake.TOTE_INTAKE_POWER));
        elevatorReverseToteIntake.whenReleased(new SetToteIntake(true, true, 0.0));

        toteIntakeFromLeft = new JoystickButton(operator, RobotMap.BUTTON_TOTE_INTAKE_FROM_LEFT);
        toteIntakeFromLeft.whenPressed(new SetToteIntake(true, false, ToteIntake.TOTE_INTAKE_POWER));
        toteIntakeFromLeft.whenReleased(new SetToteIntake(true, false, 0.0));

        toteIntakeFromRight = new JoystickButton(operator, RobotMap.BUTTON_TOTE_INTAKE_FROM_RIGHT);
        toteIntakeFromRight.whenPressed(new SetToteIntake(false, true, ToteIntake.TOTE_INTAKE_POWER));
        toteIntakeFromRight.whenReleased(new SetToteIntake(false, true, 0.0));

        //Four Bar
     /*
        fourBarAutonPosition = new JoystickButton(operator, RobotMap.BUTTON_FOUR_BAR_AUTON_START_POSITION);
        fourBarAutonPosition.whileHeld(new SeekPosition(FourBar.PID_AUTON_START_POSITION));
*/
        leftDartUp = new DashboardButton("leftDartManualUp", false);
        leftDartUp.whileActive(new LeftDartManualControl(0.3));

        leftDartDown = new DashboardButton("leftDartManualDown", false);
    	leftDartDown.whileActive(new LeftDartManualControl(-0.3));

    	rightDartUp = new DashboardButton("rightDartManualUp", false);
    	rightDartUp.whileActive(new RightDartManualControl(0.3));

    	rightDartDown = new DashboardButton("rightDartManualDown", false);
    	rightDartDown.whileActive(new RightDartManualControl(-0.3));

        //Bin Intake
        runBinIntake = new JoystickButton(operator, RobotMap.BUTTON_FOUR_BAR_RUN_BIN_INTAKE);
        runBinIntake.whenPressed(new SetBinIntake(BinIntake.INTAKE_POWER));
        runBinIntake.whenInactive(new SetBinIntake(BinIntake.CONTINOUS_HOLD_POWER));

        reverseBinIntake = new JoystickButton(operator, RobotMap.BUTTON_REVERSE_BIN_INTAKE);
        reverseBinIntake.whenPressed(new SetBinIntake(BinIntake.OUTPUT_POWER));
        reverseBinIntake.whenInactive(new SetBinIntake(BinIntake.CONTINOUS_HOLD_POWER));

        openBinGrabber = new JoystickButton(operator, RobotMap.BUTTON_FOUR_BAR_OPEN_GRABBER);
        openBinGrabber.whenPressed(new SetClamp(BinIntake.OPEN));
        openBinGrabber.whenInactive(new SetClamp(BinIntake.CLOSED));

        //test button
        test = new JoystickButton(driver, ButtonType.X);
        test.whenPressed(new SetGrabber(BinGrabber.REALEASED));
        test.whenInactive(new SetGrabber(BinGrabber.HELD));
        //test.whenPressed(new Auton_TwoTote());
        //test.whenPressed(new VisionAlign());
        //test.whenPressed(new AutoStack(1));
        //test.whenPressed(new AutoDrive(6.8, 0, 0, 1.5));
        //test.whenPressed(new MaintainRobotHeading(0));
        
        
	}
	
	public static void setRumble(int position, boolean shouldRumble){
		switch (position){
		case CENTER:
			driver.setRumble(RumbleType.kLeftRumble, shouldRumble ? RUMBLE_POWER : 0);
			driver.setRumble(RumbleType.kRightRumble, shouldRumble ? RUMBLE_POWER : 0);
			break;
		case LEFT:
			driver.setRumble(RumbleType.kLeftRumble, shouldRumble ? RUMBLE_POWER : 0);
			driver.setRumble(RumbleType.kRightRumble, 0);
			break;
		case RIGHT:
			driver.setRumble(RumbleType.kLeftRumble, 0);
			driver.setRumble(RumbleType.kRightRumble, shouldRumble ? RUMBLE_POWER : 0);
			break;
		}
		
	}
	
    public static Vector getDriverLeftJoystickVector() {
    	return new Vector(driver.getRawAxis(Axis.LEFT_X), -driver.getRawAxis(Axis.LEFT_Y));
    }

    public static double getSwerveRotationAxis() {
    	return Math.pow(driver.getRawAxis(Axis.RIGHT_X), 3);
    }

    public static double getFourBarControlAxis() {
		return operator.getRawAxis(RobotMap.AXIS_FOUR_BAR_CONTROL);
    }

	public static class Axis {
	    public final static int LEFT_X = 0;
	    public final static int LEFT_Y = 1;
	    public final static int TRIGGER_L = 2;
	    public final static int TRIGGER_R = 3;
	    public final static int RIGHT_X = 4;
	    public final static int RIGHT_Y = 5;
	}

	public static class ButtonType {
		 public final static int A = 1;
		 public final static int B = 2;
		 public final static int X = 3;
		 public final static int Y = 4;
		 public final static int L1 = 5;
		 public final static int R1 = 6;
		 public final static int LEFT_THUMB = 9;
		 public final static int RIGHT_THUMB = 10;
	}

	public static double getTurboPower() {
		return driver.getRawAxis(RobotMap.TRIGGER_TURBO_DRIVE);
	}
}
