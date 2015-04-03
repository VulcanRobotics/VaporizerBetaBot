package org.usfirst.frc.team1218.commands.auton;

import org.usfirst.frc.team1218.commands.Delay;
import org.usfirst.frc.team1218.commands.Print;
import org.usfirst.frc.team1218.commands.binIntake.SetBinIntake;
import org.usfirst.frc.team1218.commands.binIntake.SetClamp;
import org.usfirst.frc.team1218.commands.elevator.AutoStack;
import org.usfirst.frc.team1218.commands.elevator.DelayUntilToteDetected;
import org.usfirst.frc.team1218.commands.elevator.ElevatorHoldPosition;
import org.usfirst.frc.team1218.commands.fourBar.SeekPosition;
import org.usfirst.frc.team1218.commands.swerve.AutoDrive;
import org.usfirst.frc.team1218.commands.swerve.MaintainRobotHeading;
import org.usfirst.frc.team1218.commands.swerve.VisionAlign;
import org.usfirst.frc.team1218.commands.toteIntake.SetToteIntake;
import org.usfirst.frc.team1218.subsystem.binIntake.BinIntake;
import org.usfirst.frc.team1218.subsystem.elevator.Elevator;
import org.usfirst.frc.team1218.subsystem.fourBar.FourBar;
import org.usfirst.frc.team1218.subsystem.toteIntake.ToteIntake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;


/**
 * @author afiolmahon
 * @author lcook
 */
public class Auton_ThreeTote extends CommandGroup {
    	
    public  Auton_ThreeTote() {    	
    	System.out.println("Three Tote Auton Selected");
    	
    	//turn on intakes
    	addParallel(new SetClamp(false));
    	addParallel(new SetToteIntake(1.0));
    	addParallel(new SetBinIntake(BinIntake.INTAKE_POWER));
    	addSequential(new Auton_Calibrate());
    	addParallel(new SetBinIntake(BinIntake.CONTINOUS_HOLD_POWER));
    	addParallel(new SeekPosition(FourBar.PID_HIGH_POSITION));
    	
    	class FirstDrive extends CommandGroup {
    	     FirstDrive() {  
    	    	 addParallel(new AutoStack(1));
    	         addSequential(new AutoDrive(4.0, 270.0, -90, 2.2));
    	    	 }
    	}
    	addSequential( new FirstDrive());
    	addParallel(new SetToteIntake(0.7));
    	addSequential(new VisionAlign(), 1.0);
    	
    	addParallel(new Print("vision aligned"));
    	
    	class SecondDrive extends CommandGroup{
    		SecondDrive(){
    			addParallel(new AutoDrive(4.0, 270, -90.0, 0.9));
    			addSequential(new DelayUntilToteDetected(4.0));
    			addParallel(new AutoStack(1));
    			addSequential(new AutoDrive(2.5, 0, -90, 2.4));
    			addSequential(new AutoDrive(2.0, 270, -90, 2.4));
    			addSequential(new AutoDrive(3.5, 180, -90, 2.4));
    			addSequential(new AutoDrive(2, 0, -90,  2.4));
    			addParallel(new SeekPosition(FourBar.PID_HIGH_POSITION), 2.0);
     		}
    	}
    	addSequential(new SecondDrive());
    	addSequential(new VisionAlign(), 1.0);
    	
    	addParallel(new AutoDrive(6.0, 270.0, -90, 0.9));
    	addSequential(new DelayUntilToteDetected(5.0));
    	addParallel(new ElevatorHoldPosition(Elevator.BOTTOM_SOFT_LIMT));
    	addParallel(new SetToteIntake(ToteIntake.TOTE_INTAKE_POWER_HOLD));
    	
    	addSequential(new AutoDrive(9.0, 0, -90, 2.0));
    	addParallel(new SetToteIntake(-ToteIntake.TOTE_INTAKE_POWER));
    	addParallel(new Print("spitting out totes: " + Timer.getMatchTime()));
    	addSequential(new AutoDrive(2.0, 90, -90, 2.0));
    }
}
