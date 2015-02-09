package org.usfirst.frc.team1218.subsystem.escalator;

import org.usfirst.frc.team1218.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *@author afiol-mahon
 */
public class Escalator extends Subsystem {

	protected final CANTalon dartL;
	protected final CANTalon dartR;
	
	protected final DartSafety dartSafety;
	
	private static final double DART_P = 1.0;
	private static final double DART_I = 0.0;
	private static final double DART_D = 0.0;
	
	private static final double DART_FAILSAFE_DISTANCE = 70;
	private static final double DART_REALIGN_DISTANCE = 50;
	protected static final double DART_REALIGN_POWER = 0.2;
	
	private static final int DART_SOFT_LIMIT_FORWARD = 1024; //TODO Tune Dart soft limits
	private static final int DART_SOFT_LIMIT_REVERSE = 0;
	
	public static final int ESCALATOR_HIGH_POSITION = 600;
	public static final int ESCALATOR_MIDDLE_POSITION = 500;
	public static final int ESCALATOR_LOW_POSITION = 400;
	
	public Escalator() {
		dartL = new CANTalon(RobotMap.ESCALATOR_LEFT_DART);
		initDart(dartL);
		dartR = new CANTalon(RobotMap.ESCALATOR_RIGHT_DART);
		initDart(dartR);

		dartSafety = new DartSafety(dartL, dartR, DART_FAILSAFE_DISTANCE, DART_REALIGN_DISTANCE);
		
		System.out.println("Escalator Initialized");
	}
	
    public void initDefaultCommand() {
       setDefaultCommand(new C_EscalatorDefault());
    }
    
    /**
     * Set position of escalator
     * @param set value from 0-1023 if position, -1.0 to 1.0 if power.
     */
    public void setDarts(double set) {
    	dartL.set(set);
    	dartR.set(set);
    }
    
    /**
     * Configures motor controller for use with dart linear actuator
     * @param dartController dart to be configured
     */
    private static void initDart(CANTalon dartController) {
    	dartController.enableLimitSwitch(true, true);
    	dartController.ConfigFwdLimitSwitchNormallyOpen(false);
    	dartController.ConfigRevLimitSwitchNormallyOpen(false);
    	dartController.setForwardSoftLimit(DART_SOFT_LIMIT_FORWARD);
    	dartController.setReverseSoftLimit(DART_SOFT_LIMIT_REVERSE);
    	dartController.enableBrakeMode(true);
    	dartController.setPID(DART_P, DART_I, DART_D);
    	dartController.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);
    }
    
    protected void dartManualMode() {
    	dartL.changeControlMode(CANTalon.ControlMode.PercentVbus);
    	dartR.changeControlMode(CANTalon.ControlMode.PercentVbus);
    }
    
    protected void dartPositionMode() {
    	dartL.changeControlMode(CANTalon.ControlMode.Position);
    	dartL.set(dartL.get());
    	dartR.changeControlMode(CANTalon.ControlMode.Position);
    	dartR.set(dartR.get());
    }
    
    protected void disableDarts() {
    	dartL.disableControl();
    	dartR.disableControl();
    }
    
    protected void enableDarts() {
    	dartL.enableControl();
    	dartR.enableControl();
    }
    
    public void syncDashboard() {
    	SmartDashboard.putNumber("Escalator_Left_Dart_Setpoint", dartL.getSetpoint());
    	SmartDashboard.putNumber("Escalator_Right_Dart_Setpoint", dartR.getSetpoint());
    	SmartDashboard.putNumber("Escalator_Left_Dart_Power", dartL.get());
    	SmartDashboard.putNumber("Escalator_Right_Dart_Power", dartR.get());
    	SmartDashboard.putNumber("Escalator_Left_Dart_Position", dartL.getPosition());
    	SmartDashboard.putNumber("Escalator_Right_Dart_Position", dartR.getPosition());
    	SmartDashboard.putBoolean("Escalator_Left_Dart_Manual_Control", (dartL.getControlMode() == CANTalon.ControlMode.PercentVbus));
    	SmartDashboard.putBoolean("Escalator_Right_Dart_Manual_Control", (dartR.getControlMode() == CANTalon.ControlMode.PercentVbus));
    	SmartDashboard.putBoolean("Escalator_Left_Dart_Position_Control", (dartL.getControlMode() == CANTalon.ControlMode.Position));
    	SmartDashboard.putBoolean("Escalator_Right_Dart_Position_Control", (dartR.getControlMode() == CANTalon.ControlMode.Position));
    	
    	SmartDashboard.putNumber("Escalator_Left_Dart_P_Value", dartL.getP());
    	SmartDashboard.putNumber("Escalator_Right_Dart_P_Value", dartR.getP());
    	
    	SmartDashboard.putNumber("Escalator_Left_Dart_I_Value", dartL.getI());
    	SmartDashboard.putNumber("Escalator_Right_Dart_I_Value", dartR.getI());
    	
    	SmartDashboard.putNumber("Escalator_Left_Dart_D_Value", dartL.getD());
    	SmartDashboard.putNumber("Escalator_Right_Dart_D_Value", dartR.getD());
    }
}

