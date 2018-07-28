package org.usfirst.frc.team3390.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;

import com.ctre.phoenix.motorcontrol.can.*;
import com.kauailabs.navx.frc.AHRS;

public class Robot extends IterativeRobot {
		
	WPI_TalonSRX frontLeftMotor = new WPI_TalonSRX(0);
	WPI_TalonSRX frontRightMotor = new WPI_TalonSRX(1);
	WPI_TalonSRX rearLeftMotor = new WPI_TalonSRX(2);
	WPI_TalonSRX rearRightMotor = new WPI_TalonSRX(3);
	
	WPI_TalonSRX elevatorMotor1 = new WPI_TalonSRX(4);
	WPI_TalonSRX elevatorMotor2 = new WPI_TalonSRX(5);
	SpeedControllerGroup elevator = new SpeedControllerGroup(elevatorMotor1, elevatorMotor2);
	
	WPI_TalonSRX armMotor1 = new WPI_TalonSRX(7);
	WPI_TalonSRX armMotor2 = new WPI_TalonSRX(6);
	SpeedControllerGroup arm = new SpeedControllerGroup(armMotor1, armMotor2);
	
	WPI_TalonSRX baseMotor1 = new WPI_TalonSRX(8);
	WPI_TalonSRX baseMotor2 = new WPI_TalonSRX(9);
	SpeedControllerGroup base = new SpeedControllerGroup(baseMotor1, baseMotor2);
	
	MecanumDrive robotDrive;
	
	Joystick rightStick;
	Joystick gamepad;
	
	JoystickButton gameButton1;
	JoystickButton gameButton2;
	JoystickButton gameButton3;
	JoystickButton gameButton4;
	JoystickButton gameButton5;
	JoystickButton gameButton6;
	JoystickButton gameButton7;
	JoystickButton gameButton8;
	JoystickButton gameButton9;
	JoystickButton gameButton10;
	JoystickButton gameButton12;
	
	
	JoystickButton rightButton1;
	JoystickButton rightButton2;
	JoystickButton rightButton3;
	JoystickButton rightButton4;
	JoystickButton rightButton5;
	JoystickButton rightButton6;
	JoystickButton rightButton7;
	JoystickButton rightButton8;
	
	
	DigitalInput baseSwitch;
		
	Solenoid fren = new Solenoid(0);
	Solenoid kiskac = new Solenoid(1);
	Solenoid indirici = new Solenoid(2);
	
	Compressor c = new Compressor(0);
	
	Encoder enc = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
	
	AHRS ahrs;
	
	String gameData;
	
	final String Center = "Center";
 	final String Left = "Left";
 	final String Right = "Right";
 	final String LeftF = "LeftF";
 	final String RightF = "RightF";
 	final String Test = "Test";
 	SendableChooser<String> chooser = new SendableChooser<>();
	int autonomousMode;

	@Override
	public void robotInit() {
		
		armMotor1.setInverted(true);
		baseMotor1.setInverted(true);

		frontLeftMotor.setInverted(true);
		rearLeftMotor.setInverted(true);

		robotDrive = new MecanumDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);

		rightStick = new Joystick(1);
		gamepad = new Joystick(0);
		
        gameButton1 = new JoystickButton(gamepad, 1);
        gameButton2 = new JoystickButton(gamepad, 2);
        gameButton3 = new JoystickButton(gamepad, 3);
        gameButton4 = new JoystickButton(gamepad, 4);        
        gameButton5 = new JoystickButton(gamepad, 5);
        gameButton6 = new JoystickButton(gamepad, 6);
        gameButton7 = new JoystickButton(gamepad, 7);
        gameButton8 = new JoystickButton(gamepad, 8);
        gameButton9 = new JoystickButton(gamepad, 9);
        gameButton10 = new JoystickButton(gamepad, 10);
        gameButton12 = new JoystickButton(gamepad, 12);
        
        
		rightButton1 = new JoystickButton(rightStick, 1);
        rightButton2 = new JoystickButton(rightStick, 2);
        rightButton3 = new JoystickButton(rightStick, 3);
        rightButton4 = new JoystickButton(rightStick, 4);
        rightButton5 = new JoystickButton(rightStick, 5);
        rightButton6 = new JoystickButton(rightStick, 6);
        rightButton7 = new JoystickButton(rightStick, 7);
        rightButton8 = new JoystickButton(rightStick, 8);
        
        baseSwitch = new DigitalInput(2);
        
        kiskac.set(false);
        indirici.set(false);
                        
        enc.setDistancePerPulse(100);
        //enc.reset();
        
        CameraServer.getInstance().startAutomaticCapture();
        
        try {
            ahrs = new AHRS(SPI.Port.kMXP); 
        } catch (RuntimeException ex ) {
            DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
        }
        
		chooser.addDefault("Center", Center);
	    chooser.addObject("Right", Right);
	    chooser.addObject("Left", Left);
	    chooser.addObject("LeftF", LeftF);
	    chooser.addObject("RightF", RightF);
	    chooser.addObject("Test", Test);
		SmartDashboard.putData("Auto Modes", chooser);
		
		ahrs.reset();
		ahrs.resetDisplacement();
			
	}
	
	@Override
	public void autonomousInit() {
		
		String autoSelected = chooser.getSelected();
		
		ahrs.reset();
		ahrs.resetDisplacement();
    	
    	if(autoSelected == Center)
    		autonomousMode = 0;
    	else if(autoSelected == Right)
    		autonomousMode = 1;
    	else if(autoSelected == Left)
    		autonomousMode = 2;
    	else if(autoSelected == RightF)
    		autonomousMode = 3;
    	else if(autoSelected == LeftF)
    		autonomousMode = 4;
    	else if(autoSelected == Test)
    		autonomousMode = 5;
    	
    	gameData = DriverStation.getInstance().getGameSpecificMessage();
    	
	}
	
	@Override
	public void autonomousPeriodic() {
		
	
		double timeElapsed = DriverStation.getInstance().getMatchTime();
		
		robotDrive.setSafetyEnabled(false);
		
		
    	if(gameData.length() > 0) {
    		
    		if(autonomousMode == 0) { //Center
    			    			
    			//Exchange
    			SmartDashboard.putString("Aim ", "Exchange");
    			
    			kiskac.set(true);
    			
    			if(gameData.charAt(0) == 'L') {
	    			
	    			if(timeElapsed >= 12) {
	    				elevator.set(0.5);
	    				kiskac.set(true);
	    				
	    				
	    			} else {
	    				elevator.set(0);
	    				kiskac.set(false);
	    				
	    			}
	    					
    			} else {
    				
    				if(timeElapsed >= 12) {
	    				robotDrive.driveCartesian(-0.2, 0, -0.6, 0);
	    				
	    				arm.set(0);
	    			} else {
	    				robotDrive.driveCartesian(0, 0, 0, 0);
	    				arm.set(0);
	    			}
    				
    			}
    			
    		} else if(autonomousMode == 1) { //Right
    			
        		if(gameData.charAt(0) == 'L') {
        			
        			//Cizgi
        			SmartDashboard.putString("Aim ", "Line");
        			
        			kiskac.set(true);
        			
        			if(timeElapsed >= 11) {
        				robotDrive.driveCartesian(-0.5, 0, 0.6, 0);
        			} else {
        				robotDrive.driveCartesian(0, 0, 0, 0);
        			}       			
        			
        		} else {
        			
        			//Switch ve Cizgi
        			SmartDashboard.putString("Aim ", "Switch and Line");
        			
    				kiskac.set(true);
        			
        			if(timeElapsed > 13) {
        				elevator.set(-0.5);
        				fren.set(true);
        			} else {
        				elevator.set(0);
        				fren.set(false);
        			}
        			
        			if(timeElapsed >= 12) {
        				robotDrive.driveCartesian(-0.3, 0, 0, 0);
        			} else {
        				robotDrive.driveCartesian(0, 0, 0, 0);
        			}
        			
        			if(timeElapsed <= 11 && timeElapsed > 10) {
        				arm.set(-0.4);
        			} else {
        				arm.set(0);
        			}
        			
        		}
    			
    		} else if(autonomousMode == 2) { //Left
    			
        		if(gameData.charAt(0) == 'L') {
        			
        			//Switch ve Cizgi
        			SmartDashboard.putString("Aim ", "Switch and Line");
        			
        			kiskac.set(true);
        			
        			if(timeElapsed > 13) {
        				elevator.set(-0.5);
        				fren.set(true);
        			} else {
        				elevator.set(0);
        				fren.set(false);
        			}
        			
        			if(timeElapsed >= 14) {
        				robotDrive.driveCartesian(0.2, -0.24, 0, 0);
        			} else if(timeElapsed < 14 && timeElapsed >= 11) {
        				robotDrive.driveCartesian(-0.5, 0, 0, 0);
        			} else {
        				robotDrive.driveCartesian(0, 0, 0, 0);
        			}
        			
        			if(timeElapsed <= 11 && timeElapsed > 10) {
        				arm.set(-0.4);
        			} else {
        				arm.set(0);
        			}
        			
        		} else {
        			
        			//Cizgi
        			SmartDashboard.putString("Aim ", "Line");
        			
        			kiskac.set(true);
        			
        			if(timeElapsed >= 11) {
        				robotDrive.driveCartesian(-0.5, 0, -0.6, 0);
        			} else {
        				robotDrive.driveCartesian(0, 0, 0, 0);
        			}
        			        			
        		}
    			
    		} else if(autonomousMode == 3) { //RightForward
    			
    			//Cizgi
    			SmartDashboard.putString("Aim ", "Line");
    			
    			kiskac.set(true);
    			
    			if(timeElapsed >= 11) {
    				robotDrive.driveCartesian(-0.5, 0, 0.6, 0);
    			} else {
    				robotDrive.driveCartesian(0, 0, 0, 0);
    			}
    			
    		} else if(autonomousMode == 4) { //RightForward
    			
    			//Cizgi
    			SmartDashboard.putString("Aim ", "Line");
    			
    			kiskac.set(true);
    			
    			if(timeElapsed >= 11) {
    				robotDrive.driveCartesian(-0.5, 0, -0.6, 0);
    			} else {
    				robotDrive.driveCartesian(0, 0, 0, 0);
    			}
    			
    		} else if(autonomousMode == 5) {
    				
				robotDrive.driveCartesian(-0.35, 0, 0, ahrs.getYaw());	
    		}
    			
    		
    	}  else {
    		
    		gameData = DriverStation.getInstance().getGameSpecificMessage();
			SmartDashboard.putString("Aim ", "Null");
			SmartDashboard.putString("Game Data ", "Null");
    	
    	}
		
		
		SmartDashboard.putNumber("Voltage", RobotController.getBatteryVoltage());
		SmartDashboard.putBoolean(" Switch", baseSwitch.get());
		SmartDashboard.putBoolean(" Fren", fren.get());
		SmartDashboard.putNumber("EncDistance ", enc.getDistance());
		SmartDashboard.putBoolean(" EncDireciton", enc.getDirection());
		SmartDashboard.putString("Game Data ", gameData);
		SmartDashboard.putNumber("navx(Z)", ahrs.getYaw());
	}
	
	@Override
	public void teleopInit() {
		
		enc.reset();
		ahrs.reset();
		ahrs.resetDisplacement();
	
	}

	@Override
	public void teleopPeriodic() {
				
		robotDrive.driveCartesian(rightStick.getY(), rightStick.getZ()*-1, rightStick.getX());
		
		double axisY = gamepad.getRawAxis(1);
		
		if(axisY > -0.1 && axisY < 0.1)
			fren.set(false);
		else
			fren.set(true);
		
		elevator.set(gamepad.getY()*0.7);
		
		/*if(rightStick.getPOV() == -1.0) {
			elevator.set(0);
		} else if(rightStick.getPOV() == 0.0) {
			if (enc.getDistance() > -12000) {
				elevator.set(-0.5);
			}
				else {
				elevator.set(0);
			}
		} else if(rightStick.getPOV() == 180.0) {
			elevator.set(0.5);
		}*/
				
		if(rightButton2.get() || gameButton6.get())
			arm.set(1);
		else if(rightButton5.get() || gameButton8.get())
			arm.set(-1);
		else
			arm.set(0);
		
		if(gameButton5.get() || rightButton2.get())
			base.set(-1);
		
		else if(gameButton7.get() || rightButton3.get())
			base.set(1);
		else
			base.set(0);
			
		if(gameButton10.get() || rightButton7.get())
			
			indirici.set(true);	
		
		if(gameButton9.get() || rightButton8.get())
			indirici.set(false);
		
		if(gameButton2.get() || rightButton6.get())
			kiskac.set(true);
		
		if(gameButton1.get())
			kiskac.set(false);
		
		if(baseSwitch.get())
			enc.reset();
		
		if(gameButton3.get())
			c.stop();
		
		if(gameButton4.get())
			c.start();
		
		if(gameButton12.get()) {
			fren.set(true);
			elevator.set(-0.7);
			Timer.delay(1);
		} 
		
		SmartDashboard.putBoolean(" Switch", baseSwitch.get());
		SmartDashboard.putNumber("Voltage", RobotController.getBatteryVoltage());
		SmartDashboard.putNumber("EncDistance ", enc.getDistance());
		SmartDashboard.putBoolean(" EncDireciton", enc.getDirection());
		SmartDashboard.putBoolean(" Fren", fren.get());
		SmartDashboard.putBoolean(" Compressor", c.enabled());
		SmartDashboard.putBoolean(" PSI", c.getPressureSwitchValue());
		SmartDashboard.putNumber("navx(X)", ahrs.getPitch());
		SmartDashboard.putNumber("navx(Y)", ahrs.getRoll());
		SmartDashboard.putNumber("navx(Z)", ahrs.getYaw());
		
	}
}
 