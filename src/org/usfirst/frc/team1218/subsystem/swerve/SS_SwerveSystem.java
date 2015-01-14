package org.usfirst.frc.team1218.subsystem.swerve;

import org.usfirst.frc.team1218.math.Angle;
import org.usfirst.frc.team1218.math.Vector;
import org.usfirst.frc.team1218.robot.OI;
import org.usfirst.frc.team1218.robot.RobotMap;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * @author afiolmahon
 */

public class SS_SwerveSystem extends Subsystem {
    
    public SwerveModule[] module;

    private final Gyro gyro;
    private static double GYRO_SENSITIVITY = 0.00738888;
    public String[] test = {"Test", "hi"};
    
    public SS_SwerveSystem() {
    	module = new SwerveModule[4];
    	for (int i = 0; i < 4; i++) module[i] = new SwerveModule(i);
    	
    	gyro =  new Gyro(RobotMap.GYRO);
    	gyro.setSensitivity(GYRO_SENSITIVITY);
        System.out.println("Swerve System Initialized");
    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new C_Swerve());   
    }
    
    /**
     * Gyroscope reset accessor
     */
    public void resetGyro() {
    	this.gyro.reset();
    }
    
    /**
     * Write module set and sensor values to dashboard
     */
    public void publishModuleValues() {
		for (int i = 0; i < 4; i++) {
			module[i].publishValues();
		}
	}
    
    /**
     * Creates angle and power for all swerve modules
     */
    public void swerveDrive() {
    	double rX = (1 / Math.sqrt(2)) * OI.getRightX();
    	Vector joystickVector = new Vector(OI.getLeftX(), OI.getLeftY());
    	System.out.println(joystickVector.getAngle());
    	joystickVector.pushAngle( - Angle.get360Angle(gyro.getAngle()));
    	
    	
    	Vector vector[] = {
    			new Vector(joystickVector.getX() + rX, joystickVector.getY()  - rX),
    			new Vector(joystickVector.getX() - rX, joystickVector.getY()  - rX),
    			new Vector(joystickVector.getX() - rX, joystickVector.getY()  + rX),
    			new Vector(joystickVector.getX()  + rX, joystickVector.getY()  + rX)
    	};
    	
    	double maxMagnitude = 0;
    	
    	for(int i = 0; i < 4; i++) {
    		if (vector[i].getMagnitude() > maxMagnitude) maxMagnitude = vector[i].getMagnitude();
    	}
    	
    	double scaleFactor = ((maxMagnitude > 1.0) ? 1.0 / maxMagnitude : 1.0);
    	
    	double power[] = {
    		vector[0].getMagnitude() * scaleFactor,
    		vector[1].getMagnitude() * scaleFactor,
    		vector[2].getMagnitude() * scaleFactor,
    		vector[3].getMagnitude() * scaleFactor
    	};
    	
    	for(int i = 0; i < 4; i++) module[i].setValues(vector[i].getAngle(), power[i]);
    }
}