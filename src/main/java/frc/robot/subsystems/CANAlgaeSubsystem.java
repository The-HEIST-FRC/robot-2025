// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.function.DoubleSupplier;

/** Class to run the rollers over CAN */
public class CANAlgaeSubsystem extends SubsystemBase {
  private final SparkMax algaeMotor;


  public static final class AlgaeConstants {
    public static final int ALGAE_MOTOR_ID = 6;
    public static final int MOVER_MOTOR_ID = 3;
    public static final int ALGAE_MOTOR_CURRENT_LIMIT = 60;
    public static final double ALGAE_MOTOR_VOLTAGE_COMP = 10;
    public static final double ALGAE_EJECT_VALUE = 0.44;
  }

  TalonSRX mover = new TalonSRX(AlgaeConstants.MOVER_MOTOR_ID);


  public CANAlgaeSubsystem() {
    // Set up the roller motor as a brushed motor
    algaeMotor = new SparkMax(AlgaeConstants.ALGAE_MOTOR_ID, MotorType.kBrushless);

    // Set can timeout. Because this project only sets parameters once on
    // construction, the timeout can be long without blocking robot operation. Code
    // which sets or gets parameters during operation may need a shorter timeout.
    algaeMotor.setCANTimeout(250);

    // Create and apply configuration for roller motor. Voltage compensation helps
    // the roller behave the same as the battery
    // voltage dips. The current limit helps prevent breaker trips or burning out
    // the motor in the event the roller stalls.
    SparkMaxConfig algaeConfig = new SparkMaxConfig();
    algaeConfig.voltageCompensation(AlgaeConstants.ALGAE_MOTOR_VOLTAGE_COMP);
    algaeConfig.smartCurrentLimit(AlgaeConstants.ALGAE_MOTOR_CURRENT_LIMIT);
    algaeMotor.configure(algaeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {
  }


  public Command eject() {
    return Commands.runOnce(() -> algaeMotor.set(0.7), this)
            .andThen(Commands.waitSeconds(1.5))  // Adjust time for a full 180 turn
            .andThen(() -> algaeMotor.set(0));  // Stop the robot after turning
  }


  public Command lower() {
    return Commands.runOnce(() -> mover.set(ControlMode.PercentOutput, 30), this)
            .andThen(Commands.waitSeconds(1.5))  // Adjust time for a full 180 turn
            .andThen(() -> mover.set(ControlMode.PercentOutput, 0));  // Stop the robot after turning
  }

  public Command raise() {
    return Commands.runOnce(() -> mover.set(ControlMode.PercentOutput, -30), this)
            .andThen(Commands.waitSeconds(1.5))  // Adjust time for a full 180 turn
            .andThen(() -> mover.set(ControlMode.PercentOutput, 0));  // Stop the robot after turning
  }

  // Command to run the roller with joystick inputs
  public Command runAlgae(
          CANAlgaeSubsystem algaeSubsystem, DoubleSupplier forward, DoubleSupplier reverse, DoubleSupplier rollerSpeed) {
    return Commands.run(
        () -> algaeMotor.set((forward.getAsDouble() - reverse.getAsDouble()) * rollerSpeed.getAsDouble()), algaeSubsystem);
  }

}
