package org.firstinspires.ftc.teamcode.util;

import android.hardware.fingerprint.FingerprintManager;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * Hello, World!
 */


public class Robot {

    public DcMotor  LEFT_FRONT,
                    LEFT_BACK,
                    RIGHT_FRONT,
                    RIGHT_BACK;

    public DcMotor  LIFT_1,
                    LIFT_2;

    public DcMotor  INTAKE_L,
                    INTAKE_R;

    public Servo    LEFT_GRABBER,
                    RIGHT_GRABBER,
                    LEFT_EXT,
                    RIGHT_EXT,
                    INTAKE_LS,
                    INTAKE_RS,
                    BACK,
                    GRIP,
                    RAIL,
                    RELICCC_TOP,
                    RELICCC_BOTTOM;
    ;

    public Intake INTAKE;

    public DigitalChannel LIFT_TOUCH_SENSOR;


    public double   LG_MIN = 0,
                    LG_MAX = 0.3,
                    RG_MIN = 0.4,
                    RG_MAX = 0.0;

    public BNO055IMU IMU;

    public ColorSensor  COLOR_SENSOR_RED,
                        COLOR_SENSOR_BLU;

    public double   RT_EXT_MIN = 0,
                    RT_EXT_MAX = 1;

    public int[][]  LIFT_ENC_VALUES = {
            {0, 0},
            {500, 500},
            {1000, 1000},
            {1500, 1500},
            {2000, 2000}
    };


    //public Telemetry t = opMode.telemetry;

    public Robot(LinearOpMode opMode) {
        HardwareMap hardwareMap = opMode.hardwareMap;
        LEFT_FRONT = hardwareMap.dcMotor.get("left_front");
        LEFT_BACK = hardwareMap.dcMotor.get("left_back");
        RIGHT_FRONT = hardwareMap.dcMotor.get("right_front");
        RIGHT_BACK = hardwareMap.dcMotor.get("right_back");
        RIGHT_FRONT.setDirection(DcMotor.Direction.REVERSE);
        RIGHT_BACK.setDirection(DcMotor.Direction.REVERSE);

        LIFT_1 = hardwareMap.dcMotor.get("lift_1");
        LIFT_2 = hardwareMap.dcMotor.get("lift_2");

        LIFT_1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LIFT_2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        INTAKE_R = hardwareMap.dcMotor.get("intake_r");
        INTAKE_L = hardwareMap.dcMotor.get("intake_l");

        LEFT_GRABBER = hardwareMap.servo.get("left_grabber");
        RIGHT_GRABBER = hardwareMap.servo.get("right_grabber");
        LEFT_EXT = hardwareMap.servo.get("left_ext");
        RIGHT_EXT = hardwareMap.servo.get("right_ext");
        INTAKE_LS = hardwareMap.servo.get("intake_ls");
        INTAKE_RS = hardwareMap.servo.get("intake_rs");

        RAIL = hardwareMap.servo.get("rail");
        RELICCC_TOP = hardwareMap.servo.get("relic_top");
        RELICCC_BOTTOM = hardwareMap.servo.get("relic_bottom");

        LIFT_TOUCH_SENSOR = hardwareMap.digitalChannel.get("lift_touch_sensor");

        COLOR_SENSOR_RED = hardwareMap.colorSensor.get("color_blue");

        BACK = hardwareMap.servo.get("back");
        GRIP = hardwareMap.servo.get("GriprSrvo");

        INTAKE = new Intake(this);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        IMU = hardwareMap.get(BNO055IMU.class, "imu");
        IMU.initialize(parameters);
    }

}
