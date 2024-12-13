// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  // Objeto de motor Spark Max
  CANSparkMax motor = new CANSparkMax(0, null);

  // Encoder interno do NEO
  RelativeEncoder encoder;

  // Diâmetro da nossa roda
  private static final double DIAMETRO_RODA = 1;

  // Objeto que conta o tempo decorrido
  Timer timer = new Timer();
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // Atribuimos o objeto do motor integrado
    encoder = motor.getEncoder();

    // Resetamos sua posição para 0
    encoder.setPosition(0);

    // Definimos um fator de conversão que será multiplicado pela unidade nativa do encoder
    encoder.setPositionConversionFactor(Math.PI * DIAMETRO_RODA / encoder.getCountsPerRevolution());
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    timer.start();
  }

  /** This function is called periodically during autonomous. */
  // Contador que serve para saber em que parte da trajetória estamos
  int contador = 0;
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
      // Aqui retornamos a posição atual do encoder
        double posicao = encoder.getPosition();
        // Colocamos seu valor na shuffleboard
        SmartDashboard.putNumber("Posição encoder", posicao);
        // Se for o primeiro movimento acessamos o primeiro if
        if(contador == 0) {
          // acionamos o motor até a posição ser 20
          acionaMotor(20, posicao, 0.4);
        }
        // Se for o segundo movimento queremos que o contador seja 1
        if(contador == 1) {
          acionaMotor(40, posicao, 0.5);
        } 
        break;
      case kDefaultAuto:
        // Retornamos o valor do tempo
        double tempo = timer.get();
        // Seguimos a mesma lógica para fazermos vários movimentos
        if(contador == 0) {
          acionaMotor(20, tempo, 0.4);
        }
        if(contador == 1) {
          acionaMotor(40, tempo, 0.5);
        }
        break;
    }
  }

  // Função criada para simplificar o código de acionamento dos motores
  private void acionaMotor(double setPoint, double variavelProcesso, double speed) {
    if(variavelProcesso <= setPoint) {
      motor.set(speed);
    } else if(variavelProcesso > setPoint) {
      motor.set(-speed);
    } else {
      motor.set(0);
      contador++;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
