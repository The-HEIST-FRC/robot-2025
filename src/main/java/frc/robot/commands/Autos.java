// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.CANDriveSubsystem;
import frc.robot.subsystems.CANRollerSubsystem;

public final class Autos {
  // Example autonomous command which drives forward for 1 second.
  public static final Command exampleAuto(CANDriveSubsystem driveSubsystem, CANRollerSubsystem rollerSubsystem) {
    return new SequentialCommandGroup(
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.5).withTimeout(4),
            rollerSubsystem.runRoller(rollerSubsystem, () -> 0.8, () -> 0, () -> 0.5).withTimeout(1.5)
    );
  }

  public static final Command goForwardNoScore(CANDriveSubsystem driveSubsystem, CANRollerSubsystem rollerSubsystem) {
    return new SequentialCommandGroup(
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.5).withTimeout(4)
    );
  }

  public static final Command rightScore(CANDriveSubsystem driveSubsystem, CANRollerSubsystem rollerSubsystem) {
    return new SequentialCommandGroup(
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.5).withTimeout(7),
            rollerSubsystem.runRoller(rollerSubsystem, () -> 0.9, () -> 0, () -> 0.5).withTimeout(1.5)
    );
  }

  public static final Command scoreProcessorIntake(CANDriveSubsystem driveSubsystem, CANRollerSubsystem rollerSubsystem) {
    return new SequentialCommandGroup(
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.5).withTimeout(4),
            rollerSubsystem.runRoller(rollerSubsystem, () -> 0.8, () -> 0, () -> 0.5).withTimeout(1),
            rollerSubsystem.runRoller(rollerSubsystem, () -> 0, () -> 0, () -> 0.5).withTimeout(0.3),
            driveSubsystem.driveArcade(driveSubsystem, () -> -1, () -> 0.0, () -> 0.5).withTimeout(1),
            driveSubsystem.driveArcade(driveSubsystem, () -> 0, () -> 1, () -> 0.5).withTimeout(1.3),
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.5).withTimeout(3),
            driveSubsystem.driveArcade(driveSubsystem, () -> 0, () -> -1, () -> 0.5).withTimeout(1),
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.7).withTimeout(4),
            driveSubsystem.driveArcade(driveSubsystem, () -> 0, () -> -1, () -> 1).withTimeout(1),
            driveSubsystem.driveArcade(driveSubsystem, () -> -1, () -> 0.0, () -> 0.7).withTimeout(4)
            );
  }

  public static final Command scoreNoProcessorIntake(CANDriveSubsystem driveSubsystem, CANRollerSubsystem rollerSubsystem) {
    return new SequentialCommandGroup(
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.5).withTimeout(4),
            rollerSubsystem.runRoller(rollerSubsystem, () -> 0.8, () -> 0, () -> 0.5).withTimeout(1),
            rollerSubsystem.runRoller(rollerSubsystem, () -> 0, () -> 0, () -> 0.5).withTimeout(0.3),
            driveSubsystem.driveArcade(driveSubsystem, () -> -1, () -> 0.0, () -> 0.5).withTimeout(1),
            driveSubsystem.driveArcade(driveSubsystem, () -> 0, () -> -1, () -> 0.5).withTimeout(1.3),
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.5).withTimeout(3),
            driveSubsystem.driveArcade(driveSubsystem, () -> 0, () -> 1, () -> 0.5).withTimeout(1),
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.7).withTimeout(4),
            driveSubsystem.driveArcade(driveSubsystem, () -> 0, () -> -1, () -> 1).withTimeout(1),
            driveSubsystem.driveArcade(driveSubsystem, () -> -1, () -> 0.0, () -> 0.7).withTimeout(4)
    );
  }

  public static final Command processorScoreProcessorIntake(CANDriveSubsystem driveSubsystem, CANRollerSubsystem rollerSubsystem) {
    return new SequentialCommandGroup(
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.7).withTimeout(5),
            rollerSubsystem.runRoller(rollerSubsystem, () -> 0.9, () -> 0, () -> 0.5).withTimeout(1),
            rollerSubsystem.runRoller(rollerSubsystem, () -> 0, () -> 0, () -> 0.5).withTimeout(0.1),
            driveSubsystem.driveArcade(driveSubsystem, () -> -1, () -> 0.0, () -> 0.5).withTimeout(1),
            driveSubsystem.driveArcade(driveSubsystem, () -> 0, () -> 1, () -> 0.5).withTimeout(0.9),
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.7).withTimeout(4),
            driveSubsystem.driveArcade(driveSubsystem, () -> 0, () -> -1, () -> 1).withTimeout(1),
            driveSubsystem.driveArcade(driveSubsystem, () -> -1, () -> 0.0, () -> 0.7).withTimeout(4)
    );
  }

  public static final Command noProcessorScoreNoProcessorIntake(CANDriveSubsystem driveSubsystem, CANRollerSubsystem rollerSubsystem) {
    return new SequentialCommandGroup(
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.7).withTimeout(5),
            rollerSubsystem.runRoller(rollerSubsystem, () -> 0.9, () -> 0, () -> 0.5).withTimeout(1),
            rollerSubsystem.runRoller(rollerSubsystem, () -> 0, () -> 0, () -> 0.5).withTimeout(0.1),
            driveSubsystem.driveArcade(driveSubsystem, () -> -1, () -> 0.0, () -> 0.5).withTimeout(1),
            driveSubsystem.driveArcade(driveSubsystem, () -> 0, () -> 1, () -> 0.5).withTimeout(0.9),
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.7).withTimeout(4),
            driveSubsystem.driveArcade(driveSubsystem, () -> 0, () -> -1, () -> 1).withTimeout(1),
            driveSubsystem.driveArcade(driveSubsystem, () -> -1, () -> 0.0, () -> 0.7).withTimeout(4)
    );
  }
}
