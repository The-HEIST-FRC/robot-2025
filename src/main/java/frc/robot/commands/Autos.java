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
            rollerSubsystem.runRoller(rollerSubsystem, () -> 0.5, () -> 0, () -> 0.5).withTimeout(1.5)
    );
  }

  public static final Command rightScore(CANDriveSubsystem driveSubsystem, CANRollerSubsystem rollerSubsystem) {
    return new SequentialCommandGroup(
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.5).withTimeout(2.5),
            driveSubsystem.driveArcade(driveSubsystem, () -> 0, () -> 1, () -> 0.5).withTimeout(1),
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.5).withTimeout(2),
            rollerSubsystem.runRoller(rollerSubsystem, () -> 0.5, () -> 0, () -> 0.5).withTimeout(1.5)
    );
  }

  public static final Command leftScore(CANDriveSubsystem driveSubsystem, CANRollerSubsystem rollerSubsystem) {
    return new SequentialCommandGroup(
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.5).withTimeout(2.5),
            driveSubsystem.driveArcade(driveSubsystem, () -> 0, () -> -1, () -> 0.5).withTimeout(1),
            driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.5).withTimeout(2),
            rollerSubsystem.runRoller(rollerSubsystem, () -> 0.5, () -> 0, () -> 0.5).withTimeout(1.5)
    );
  }

  public static final Command rightScoreCoral(CANDriveSubsystem driveSubsystem, CANRollerSubsystem rollerSubsystem) {
    return new SequentialCommandGroup(
      driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.5).withTimeout(3.5),
      driveSubsystem.driveArcade(driveSubsystem, () -> 0, () -> 1, () -> 0.5).withTimeout(1),
      driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.5).withTimeout(2),
      rollerSubsystem.runRoller(rollerSubsystem, () -> 0.5, () -> 0, () -> 0.5).withTimeout(1.5),
      driveSubsystem.driveArcade(driveSubsystem, () -> -1, () -> 0.0, () -> 0.5).withTimeout(1),
      driveSubsystem.driveArcade(driveSubsystem, () -> 0, () -> -1, () -> 0.5).withTimeout(1)
            //driveSubsystem.driveArcade(driveSubsystem, () -> 1, () -> 0.0, () -> 0.5).withTimeout(3),
            //driveSubsystem.driveArcade(driveSubsystem, () -> 0, () -> 1, () -> 0.5).withTimeout(1.5),
            //driveSubsystem.driveArcade(driveSubsystem, () -> -1, () -> 0.0, () -> 0.5).withTimeout(5)
            );
  }
}
