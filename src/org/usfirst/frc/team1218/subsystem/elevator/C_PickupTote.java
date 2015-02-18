package org.usfirst.frc.team1218.subsystem.elevator;

import org.usfirst.frc.team1218.auton.C_Wait;
import org.usfirst.frc.team1218.subsystem.swerve.C_AutoDrive;
import org.usfirst.frc.team1218.subsystem.toteIntake.C_AutorunToteIntake;
import org.usfirst.frc.team1218.subsystem.toteIntake.ToteIntake;

import edu.wpi.first.wpilibj.command.CommandGroup;
/**
 *
 */
public class C_PickupTote extends CommandGroup {
    
    public  C_PickupTote() {
    	addSequential(new C_GoToTop());
    	addParallel(new C_AutoDrive(5, 0));
    	addSequential(new C_AutorunToteIntake(ToteIntake.TOTE_INTAKE_POWER));
    	addSequential(new C_Wait(2));
    	addSequential(new C_GoToBottom());
    	addSequential(new C_AutorunToteIntake(0.0));
    	addSequential(new C_GoToTop());
    }
}
