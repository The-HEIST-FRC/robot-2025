// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

//packages for camera vision
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.apriltag.AprilTagDetection;
import edu.wpi.first.apriltag.AprilTagDetector;
import edu.wpi.first.apriltag.AprilTagPoseEstimator;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.networktables.IntegerArrayPublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  double startTime;
  double currentTime;
  double actualTime;

  private Command autoCommand;

  private final Spark leftMotor1 = new Spark(0);
  private final Spark leftMotor2 = new Spark(1);
  private final Spark rightMotor1 = new Spark(2);
  private final Spark rightMotor2 = new Spark(3);

  public void BothLeftMotors(double speed) {
    // Ensure speed is between 0 and 1
    speed = Math.max(0, Math.min(1, speed));

    // Set speed to both motors
    leftMotor1.set(speed);
    leftMotor2.set(speed);
  }
  public void BothRightMotors(double speed) {
    // Ensure speed is between 0 and 1
    speed = Math.max(0, Math.min(1, speed));

    // Set speed to both motors
    rightMotor1.set(speed);
    rightMotor2.set(speed);
  }

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer. This will perform all our button bindings,
    // and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    

    // Used to track usage of Kitbot code, please do not remove.
    HAL.report(tResourceType.kResourceType_Framework, 10);
  
    //camera
    var visionThread = new Thread(this::apriltagVisionThreadProc);
        visionThread.setDaemon(true);
        visionThread.start();
  
  }

  void apriltagVisionThreadProc()
  {
      var detector = new AprilTagDetector();
      // look for tag36h11, correct 1 error bit (hamming distance 1)
      // hamming 1 allocates 781KB, 2 allocates 27.4 MB, 3 allocates 932 MB
      // max of 1 recommended for RoboRIO 1, while hamming 2 is feasible on the RoboRIO 2
      detector.addFamily("tag36h11",1); //tag16h5
      
      // Set up Pose Estimator - parameters are for a Microsoft Lifecam HD-3000
      // (https://www.chiefdelphi.com/t/wpilib-apriltagdetector-sample-code/421411/21)
      var poseEstConfig =
              new AprilTagPoseEstimator.Config(
                      0.1651, 699.3778103158814, 677.7161226393544, 345.6059345433618, 207.12741326228522);
      var estimator = new AprilTagPoseEstimator(poseEstConfig);
      
      // Get the UsbCamera from CameraServer
      UsbCamera camera = CameraServer.startAutomaticCapture();

      Integer resolutionMultyplier = 1;
      // Set the resolution
      camera.setResolution(640 *resolutionMultyplier, 480*resolutionMultyplier);
      
      // Get a CvSink. This will capture Mats from the camera
      CvSink cvSink = CameraServer.getVideo();
      // Setup a CvSource. This will send images back to the Dashboard
      CvSource outputStream = CameraServer.putVideo("Camera1", 640*resolutionMultyplier, 480*resolutionMultyplier);
      
      // Mats are very memory expensive. Lets reuse these.
      var mat = new Mat();
      var grayMat = new Mat();
      
      // Instantiate once
      ArrayList<Long> tags = new ArrayList<>();
      var outlineColor = new Scalar(0, 255, 0);
      var crossColor = new Scalar(0, 0, 255);
      
      // We'll output to NT
      NetworkTable tagsTable = NetworkTableInstance.getDefault().getTable("apriltags");
      IntegerArrayPublisher pubTags = tagsTable.getIntegerArrayTopic("tags").publish();
      
      // This cannot be 'true'. The program will never exit if it is. This
      // lets the robot stop this thread when restarting robot code or
      // deploying.
      while (!Thread.interrupted())
      {
          // Tell the CvSink to grab a frame from the camera and put it
          // in the source mat.  If there is an error notify the output.
          if (cvSink.grabFrame(mat) == 0)
          {
              // Send the output the error.
              outputStream.notifyError(cvSink.getError());
              // skip the rest of the current iteration
              continue;
          }
          
          Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_RGB2GRAY);
          
          CvSource outputStreamBW = CameraServer.putVideo("CameraBW", 640*resolutionMultyplier, 480*resolutionMultyplier);

          AprilTagDetection[] detections = detector.detect(grayMat);
          
          // have not seen any tags yet
          tags.clear();
          
          for (AprilTagDetection detection : detections)
          {
              // remember we saw this tag
              tags.add((long) detection.getId());
              
              // draw lines around the tag
              for (var i = 0; i <= 3; i++)
              {
                  var j = (i + 1) % 4;
                  var pt1 = new Point(detection.getCornerX(i), detection.getCornerY(i));
                  var pt2 = new Point(detection.getCornerX(j), detection.getCornerY(j));
                  Imgproc.line(mat, pt1, pt2, outlineColor, 2);
              }
              
              // mark the center of the tag
              var cx = detection.getCenterX();
              var cy = detection.getCenterY();
              var ll = 10;
              Imgproc.line(mat, new Point(cx - ll, cy), new Point(cx + ll, cy), crossColor, 2);
              Imgproc.line(mat, new Point(cx, cy - ll), new Point(cx, cy + ll), crossColor, 2);
              
              // identify the tag
              Imgproc.putText(
                      mat,
                      Integer.toString(detection.getId()),
                      new Point(cx + ll, cy),
                      Imgproc.FONT_HERSHEY_SIMPLEX,
                      1,
                      crossColor,
                      3);
              
              // determine pose
              Transform3d pose = estimator.estimate(detection);
              if (pose.getX() == 0 && pose.getY() == 0 && pose.getZ() == 0) {
                  System.out.println("Skipping ambiguous tag pose.");
                  continue;
              }
              
              // put pose into dashboard
              Rotation3d rot = pose.getRotation();
              tagsTable
                      .getEntry("pose_" + detection.getId())
                      .setDoubleArray(
                              new double[] {
                                      pose.getX(), pose.getY(), pose.getZ(), rot.getX(), rot.getY(), rot.getZ()
                              });

            System.out.println("id: " + detection.getId() + ", cx: " + cx + ", cy: " + cy);
          }
          
          // put list of tags onto dashboard
          pubTags.set(tags.stream().mapToLong(Long::longValue).toArray());
          
          // Give the output stream a new image to display
          outputStream.putFrame(mat);
          outputStreamBW.putFrame(grayMat);
      }
      
      pubTags.close();
      detector.close();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items
   * like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler. This is responsible for polling buttons, adding
    // newly-scheduled
    // commands, running already-scheduled commands, removing finished or
    // interrupted commands,
    // and running subsystem periodic() methods. This must be called from the
    // robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your
   * {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
    startTime = Timer.getFPGATimestamp();
  }


  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {
  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {
  }
}
