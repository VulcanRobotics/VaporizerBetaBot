package org.usfirst.frc.team1218.subsystem.fourBar;

import org.usfirst.frc.team1218.commands.fourBar.FourBarDefaultCommand;
import org.usfirst.frc.team1218.robot.Robot;
import org.usfirst.frc.team1218.robot.RobotMap;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 *@author afiol-mahon
 */
public class FourBar extends Subsystem implements PIDOutput, PIDSource{
		
	private final DartController dartLeft;
	private final DartController dartRight;
	private final PIDController positionController;
	
	private static final double POSITION_CONTROLLER_P = 6.0;
	private static final double POSITION_CONTROLLER_I = 0.05;
	private static final double POSITION_CONTROLLER_D = 0.01;
	private static final double POSITION_CONTROLLER_MAX_OUTPUT = 0.8;

	private static final double DART_POSITION_SYNC_P = 2.0;
	
	private static final double DART_ON_TARGET_DISTANCE = 0.01;
	private static final double DART_FAILSAFE_DISTANCE = 0.1;
	
	public static final double PID_HIGH_POSITION = 0.7; //TODO change to something useful
	public static final double PID_AUTON_START_POSITION = 0.16;
	public static final double PID_GET_BIN_FROM_STEP_POSITION = 0.2;
	public static final double PID_GET_NOODLE_POSITION = 0.42; //TODO: find this
	
	public static final double SLOWDOWN_NEAR_LIMIT_DISTANCE = 0.2;
	public static final double MIN_POWER = 0.2;
	
	public FourBar() {
		dartLeft = new DartController(RobotMap.FOUR_BAR_LEFT_DART, RobotMap.FOUR_BAR_LEFT_DART_POTENTIOMETER);
		dartRight = new DartController(RobotMap.FOUR_BAR_RIGHT_DART, RobotMap.FOUR_BAR_RIGHT_DART_POTENTIOMETER);
		positionController = new PIDController(
				POSITION_CONTROLLER_P,
				POSITION_CONTROLLER_I,
				POSITION_CONTROLLER_D,
				this,
				this);
		positionController.setAbsoluteTolerance(DART_ON_TARGET_DISTANCE);
		positionController.setOutputRange(-POSITION_CONTROLLER_MAX_OUTPUT, POSITION_CONTROLLER_MAX_OUTPUT);
		System.out.println("Four Bar Initialized");
	}
	
    public void initDefaultCommand() {
       setDefaultCommand(new FourBarDefaultCommand());
    }
    
    public void leftDartManual(double power) {
    	dartLeft.talon.set(power);
    }
    
    public void rightDartManual(double power) {
    	dartRight.talon.set(power);
    }
    
	public boolean isAlignmentSafe() {
		return Robot.fourBar.getDartPositionDifference() < FourBar.DART_FAILSAFE_DISTANCE;
	}
	
    public boolean isOnTarget() {
    	return positionController.onTarget();
    }
    
    public void setDartPosition(double setpoint) {
    	positionController.setSetpoint(setpoint);
    }
    
    protected double getDartPositionDifference() {
    	return Math.abs(dartLeft.getPosition() - dartRight.getPosition());
    }
    
    public double getDistanceToTopLimit() {
    	return DartController.TOP_SOFT_LIMIT - getPosition();
    }
    
    public double getDistanceToBottomLimit() {
    	return getPosition() - DartController.BOTTOM_SOFT_LIMIT ;
    }
    
    public double getPosition() {
		return (dartLeft.getPosition() + dartRight.getPosition())/2;
	}
    
    public void setDartPower(double power) {
   		if (getDistanceToTopLimit() < SLOWDOWN_NEAR_LIMIT_DISTANCE && power > 0) {
   			if (getDistanceToTopLimit() > 0){ 
   				power *= getDistanceToTopLimit() / SLOWDOWN_NEAR_LIMIT_DISTANCE;
   			}
   			else {
   				power = 0; //prevents oscilating around limit
   			}
       		
   		}
   		
   		if (getDistanceToBottomLimit() < SLOWDOWN_NEAR_LIMIT_DISTANCE && power < 0) {
   			if (getDistanceToBottomLimit() > 0) { 
   				power *= getDistanceToBottomLimit() / SLOWDOWN_NEAR_LIMIT_DISTANCE;
   			}
   			else {
   				power = 0; //prevents oscilating around limit
   			}
   		}
   		
   		if (Math.abs(power) < MIN_POWER) {
   			double sign = Math.signum(power);
   			power = MIN_POWER * sign;
   		}
   		
   		//Power Gain combats the left and right dart position drift
      	double leftPowerGain = (dartRight.getPosition() - dartLeft.getPosition()) * DART_POSITION_SYNC_P;
        double rightPowerGain =  (dartLeft.getPosition() - dartRight.getPosition()) * DART_POSITION_SYNC_P;
       	dartLeft.setPower(power + leftPowerGain);
       	dartRight.setPower(power + rightPowerGain);
    }
    
    public void dartEnablePID(boolean enabled) {
    	if (enabled) {
    		positionController.enable();
    	} else {
    		positionController.disable();
    	}
    }
    
    public void enableDartHardLimits(boolean enable) {
    	dartLeft.enableHardLimits(enable);
    	dartRight.enableHardLimits(enable);
    }
    
    protected void enableDarts() {
   		dartLeft.enable();
       	dartRight.enable();
    }
    
    protected void disableDarts() {
    	dartEnablePID(false);
    	dartLeft.disable();
    	dartRight.disable();
    	positionController.disable();
    }
    
    public void periodicTasks() {
    	SmartDashboard.putBoolean("FourBar_isDartSafe", isAlignmentSafe());
    	
    	SmartDashboard.putNumber("FourBar_Dart_PID_Controller_Setpoint", positionController.getSetpoint());
    	SmartDashboard.putBoolean("FourBar_Dart_PID_Enabled", positionController.isEnable());

    	SmartDashboard.putNumber("FourBar_Left_Dart_Power", dartLeft.getPower());
    	SmartDashboard.putNumber("FourBar_Right_Dart_Power", dartRight.getPower());
    	
    	SmartDashboard.putNumber("FourBar_Left_Dart_Position", dartLeft.getPosition());
    	SmartDashboard.putNumber("FourBar_Right_Dart_Position", dartRight.getPosition());
    	
    	SmartDashboard.putNumber("FourBar_Left_Current", dartLeft.getCurrent());
    	SmartDashboard.putNumber("FourBar_Right_Current", dartRight.getCurrent());
    	
    	SmartDashboard.putNumber("FourBar_Dart_Position_Difference", getDartPositionDifference());
    	    	
    	SmartDashboard.putBoolean("FourBar_Left_Top_Hard_Limit", !dartLeft.isFwdLimitSwitchClosed());
    	SmartDashboard.putBoolean("FourBar_Right_Top_Hard_Limit", !dartRight.isFwdLimitSwitchClosed());
    	SmartDashboard.putBoolean("FourBar_Left_Bottom_Hard_Limit", !dartLeft.isRevLimitSwitchClosed());
    	SmartDashboard.putBoolean("FourBar_Right_Bottom_Hard_Limit", !dartRight.isRevLimitSwitchClosed());
    	
    	SmartDashboard.putBoolean("FourBar_Left_Top_Soft_Limit", dartLeft.getTopSoftLimit());
    	SmartDashboard.putBoolean("FourBar_Left_Bottom_Soft_Limit", dartLeft.getBottomSoftLimit());
    	SmartDashboard.putBoolean("FourBar_Right_Top_Soft_Limit", dartRight.getTopSoftLimit());
    	SmartDashboard.putBoolean("FourBar_Right_Bottom_Soft_Limit", dartRight.getBottomSoftLimit());
    	
    	enableDartHardLimits(SmartDashboard.getBoolean("DartHardLimitsEnabled", false));
    }

	@Override
	public void pidWrite(double output) {
		setDartPower(output);
	}
	
	@Override
	public double pidGet() {
		return getPosition();
	}
}