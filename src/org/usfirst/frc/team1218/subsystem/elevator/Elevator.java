package org.usfirst.frc.team1218.subsystem.elevator;

import org.usfirst.frc.team1218.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *@author afiol-mahon
 */
public class Elevator extends Subsystem {
	
	private DigitalInput toteDetector;
	private DigitalOutput toteIndicator;
	
	private final CANTalon liftMaster;
	
	private boolean liftHasReferenced = false;
	
	ElevatorSafety elevatorSafety;
	
	private static final double ELEVATOR_P = 0.1;
	private static final double ELEVATOR_I = 0.0001;
	private static final double ELEVATOR_D = 0.0;
	 
	public static final int ELEVATOR_DROP_POSITION = 0; 
	public static final int ELEVATOR_STEP_POSITION = 2232;
	public static final int ELEVATOR_RAISE_POSITION = 5087;
	public static final int ELEVATOR_DEFAULT_POSITION = ELEVATOR_STEP_POSITION;
	
	public static final double ELEVATOR_MANUAL_POSITIONING_POWER = 1.0;
	public static final double ELEVATOR_REFERENCING_POWER = 0.3;
	
    public void initDefaultCommand() {
    	setDefaultCommand(new C_ElevatorDefault());
    }
    
    public Elevator() {
    	liftMaster = new CANTalon(RobotMap.ELEVATOR_LIFT_MASTER);
    	liftMaster.enableBrakeMode(true);
    	liftMaster.enableLimitSwitch(true, true);
    	liftMaster.ConfigFwdLimitSwitchNormallyOpen(false);
    	liftMaster.ConfigRevLimitSwitchNormallyOpen(false);
    	liftMaster.changeControlMode(ControlMode.PercentVbus);
    	//liftMaster.setPID(ELEVATOR_P, ELEVATOR_I, ELEVATOR_D);
    	//liftMaster.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	
    	toteDetector = new DigitalInput(RobotMap.ELEVATOR_TOTE_DETECTOR);
    	toteIndicator = new DigitalOutput(RobotMap.ELEVATOR_TOTE_INDICATOR);
    	
    	elevatorSafety = new ElevatorSafety();
    }
    
    /**
     * @return Current position of tote elevator
     */
    public double getPosition() {
	   return liftMaster.getPosition();
    }
    
    public void setPosition(double position) {
    	if(liftHasReferenced) {
    		liftMaster.changeControlMode(ControlMode.Position);
        	liftMaster.set(position);
    	} else {
    		System.out.println("Cannot PID Elevator: Must Reference Limit First");
    		liftMaster.changeControlMode(ControlMode.PercentVbus);
    		liftMaster.set(0.0);
    	}
    }
    
    public void setPower(double power) {
    	liftMaster.changeControlMode(ControlMode.PercentVbus);
    	liftMaster.set(power);
    }
     
    public double getPower() {
    	return liftMaster.get();
    }
    
    public boolean getHasTote() {
    	return toteDetector.get();
    }
    
   protected boolean getTopLimit() {
    	return !liftMaster.isFwdLimitSwitchClosed();
    }
    
    protected boolean getBottomLimit() {
    	return !liftMaster.isRevLimitSwitchClosed();
    }
    
    public double getCurrent() {
    	return liftMaster.getOutputCurrent();
    }
    
    public void syncDashboard() {
    	SmartDashboard.putNumber("Elevator_Position", getPosition());
    	SmartDashboard.putBoolean("Elevator_Upper_Limit", liftMaster.isFwdLimitSwitchClosed());
    	SmartDashboard.putBoolean("Elevator_Lower_Limit", liftMaster.isRevLimitSwitchClosed());
    	SmartDashboard.putNumber("Elevator_Position_Error", liftMaster.getClosedLoopError());
    	SmartDashboard.putBoolean("Elevator_Has_Tote", getHasTote());
    	
    	SmartDashboard.putNumber("Elevator_Current", liftMaster.getOutputCurrent());
    	
    	checkForZero();
    	
    	toteIndicator.set(getHasTote());
    }
    
    public void checkForZero() {
    	if (getBottomLimit()) {
    		zeroPosition();
    	}
    }
    
    public boolean atReference() {
    	return !liftMaster.isRevLimitSwitchClosed();
    }
    
    public void zeroPosition() {
    	liftMaster.setPosition(0);
    	liftHasReferenced = true;
    }
}