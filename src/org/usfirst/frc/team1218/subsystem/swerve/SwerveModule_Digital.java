package org.usfirst.frc.team1218.subsystem.swerve;

import org.usfirst.frc.team1218.robot.RobotMap;
import org.usfirst.frc.team1218.subsystem.swerve.math.Angle;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;

/**
 * @author afiol-mahon
 */
public class SwerveModule_Digital extends SwerveModule {
	
	public AngleEncoder angleEncoder;
	private final PIDController anglePIDController;
	
	private static final double ANGLE_CONTROLLER_P = -0.1;
	private static final double ANGLE_CONTROLLER_I = 0.0;
	private static final double ANGLE_CONTROLLER_D = 0.01;
	
	public SwerveModule_Digital(int moduleNumber) {
		super(moduleNumber);
		this.angleEncoder = new AngleEncoder(moduleNumber);
		//Initialize PID
		this.anglePIDController = new PIDController(
				ANGLE_CONTROLLER_P,
				ANGLE_CONTROLLER_I,
				ANGLE_CONTROLLER_D,
				angleEncoder,
				angleController);
		this.anglePIDController.setInputRange(0.0, 360.0);
		this.anglePIDController.setOutputRange(-MAX_ANGLE_CONTROLLER_POWER, MAX_ANGLE_CONTROLLER_POWER);;
		this.anglePIDController.setContinuous();
		//Begin Module
		this.angleEncoder.reset();
		this.anglePIDController.enable();
	}
	
	@Override
	public double getEncoderAngle() {
		anglePIDController.setPID(-0.01, 0.0001, 0.4);
		anglePIDController.setPercentTolerance(1);
		return angleEncoder.pidGet();
	}
	
	@Override
	public void setRealAngle(double angle) {
		this.anglePIDController.setSetpoint(angle); //applies module specific direction preferences
	}
	
	public class AngleEncoder extends Encoder {
		
		public AngleEncoder(int moduleNumber) {
			super(RobotMap.SM_ANGLE_ENCODER_A[moduleNumber],
					RobotMap.SM_ANGLE_ENCODER_B[moduleNumber],
					RobotMap.SM_ANGLE_ENCODER_I[moduleNumber]
					);
		}
		
		@Override
		public double pidGet() {
			double angle = get();
			angle *= ENCODER_CLICK_TO_DEGREE;//TODO Try removing this and using getDistance()
			angle = Angle.get360Angle(angle);
			return angle;
		}
	}
}