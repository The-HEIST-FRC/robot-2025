// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.Autos;
import frc.robot.subsystems.CANDriveSubsystem;
import frc.robot.subsystems.CANRollerSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  public final CANDriveSubsystem driveSubsystem = new CANDriveSubsystem();
  public final CANRollerSubsystem rollerSubsystem = new CANRollerSubsystem();

  // The autonomous chooser
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    Controls.configureBindings(this);

    // Set the options to show up in the Dashboard for selecting auto modes. If you
    // add additional auto modes you can add additional lines here with
    // autoChooser.addOption
    autoChooser.setDefaultOption("Sides", Autos.rightScore(driveSubsystem, rollerSubsystem));
    autoChooser.addOption("Middle", Autos.exampleAuto(driveSubsystem, rollerSubsystem));
    autoChooser.addOption("ScoreProcessorIntake", Autos.scoreProcessorIntake(driveSubsystem, rollerSubsystem));
    autoChooser.addOption("ScoreNoProcessorIntake", Autos.scoreNoProcessorIntake(driveSubsystem, rollerSubsystem));
    autoChooser.addOption("ProcessorScoreProcessorIntake", Autos.processorScoreProcessorIntake(driveSubsystem, rollerSubsystem));
    autoChooser.addOption("NoProcessorScoreNoProcessorIntake", Autos.noProcessorScoreNoProcessorIntake(driveSubsystem, rollerSubsystem));
    autoChooser.addOption("ForwardNoScore", Autos.goForwardNoScore(driveSubsystem, rollerSubsystem));

    //autoChooser.addOption("Rightscorecoral", Autos.rightScoreCoral(driveSubsystem, rollerSubsystem));
  
    SmartDashboard.putData("Auto Position", autoChooser);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return autoChooser.getSelected();
  }
}
