package frc.robot;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.CANRollerSubsystem.RollerConstants;;

public class Controls {
    public static final class ControlConstants {
      public static final int DRIVER_CONTROLLER_PORT = 0;
      public static final int OPERATOR_CONTROLLER_PORT = 1;
    }

    // The driver's controller
    private static final CommandXboxController driverController = new CommandXboxController(
        ControlConstants.DRIVER_CONTROLLER_PORT);
  
    // The operator's controller
    private static final CommandXboxController operatorController = new CommandXboxController(
        ControlConstants.OPERATOR_CONTROLLER_PORT);

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
   * an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link
   * CommandXboxController
   * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  public static void configureBindings(RobotContainer bot) {
    // Set the A button to run the "runRoller" command from the factory with a fixed
    // value ejecting the gamepiece while the button is held
    driverController.rightBumper()
        .whileTrue(bot.rollerSubsystem.runRoller(bot.rollerSubsystem, () -> RollerConstants.ROLLER_EJECT_VALUE, () -> 0, () -> 1));

    // Set the default command for the drive subsystem to the command provided by
    // factory with the values provided by the joystick axes on the driver
    // controller. The Y axis of the controller is inverted so that pushing the
    // stick away from you (a negative value) drives the robot forwards (a positive
    // value)
    bot.driveSubsystem.setDefaultCommand(
        bot.driveSubsystem.driveArcade(
            bot.driveSubsystem, () -> -driverController.getLeftY(), () -> -driverController.getRightX(), () -> (driverController.leftBumper().getAsBoolean() ? 1 : 0.7)));
    // Set the default command for the roller subsystem to the command from the
    // factory with the values provided by the triggers on the operator controller
    bot.rollerSubsystem.setDefaultCommand(
        bot.rollerSubsystem.runRoller(
            bot.rollerSubsystem,
            () -> driverController.getRightTriggerAxis(),
            () -> driverController.getLeftTriggerAxis(),
            () -> (driverController.leftBumper().getAsBoolean() ? 0.7 : 0.4)));
  }
    
}
