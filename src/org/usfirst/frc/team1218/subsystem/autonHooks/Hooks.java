package org.usfirst.frc.team1218.subsystem.autonHooks;

import org.usfirst.frc.team1218.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *@author afiol-mahon
 */
public class Hooks extends Subsystem {
    private final Solenoid hookDeploy;
    
    public void initDefaultCommand() {
        setDefaultCommand(new C_HooksDefault());
    }
    
    public Hooks() {
    	hookDeploy = new Solenoid(RobotMap.HOOK_DEPLOY_SOLENOID);
    	System.out.println("Hooks Initialized");
    }
    
    public void deployHooks(boolean state) {
    	hookDeploy.set(state);
    }
    
    public boolean isDeployed() {
    	return hookDeploy.get();
    }
    
	public void syncDashboard() {
		SmartDashboard.putBoolean("Hooks_Deployed", hookDeploy.get());
	}
}
