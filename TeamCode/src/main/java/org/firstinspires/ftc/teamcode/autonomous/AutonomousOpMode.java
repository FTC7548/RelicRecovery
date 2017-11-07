package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.util.Robot;

public abstract class AutonomousOpMode extends LinearOpMode {

    public Robot r;

    private final int PPR = 2830;
    private final double WHL_DIAM = 4;

    private final double HDNG_THRESHOLD = 7;

    private final double PPI = PPR / (WHL_DIAM * Math.PI);

    private ElapsedTime runtime = new ElapsedTime();


    // Vuforia Stuff

    VuforiaTrackables relicTrackables;
    VuforiaTrackable relicTemplate;
    VuforiaLocalizer vuforia;

    @Override
    public void runOpMode() {
        r = new Robot(this);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AfAbEIf/////AAAAGcuoYXidf0JOqPaKWgm8WkA1zDYGkoLnlPknk8vq7RH0" +
                "naH/HM6ewoB3oq7pbgiM2sNyKG1wEcWsnmR3NhOXCFpXFylxkILdAua00dD5Cq+6pvZTlsb756HP3XJa3ajT" +
                "/3kjS0ZBaKFT3FaV3ZZCTALJWCRIfkg0YOay9FTneZMgt0r8n7ybZ8lhfN2VqAyqOGQZHOFENunfyA54DKvSZ" +
                "aJn4fHqBAAvE//7p62dfcqtGxm/4JBYoeTwGXcjJO9S3tW9UyttI7Hu3n4HnfCyS9TFu2vo/uMD96hKYtdAu+" +
                "VMlycqdU8ggE5ndbAG7WC9XdLRzRlm3mwou3gFWrtyuZD5eSqS+oGxpo+nSlX7afnk";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        resetEnc();

        waitForStart();
        /*relicTrackables.activate();
        sleep(1000);

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        int count = 0;
        while (vuMark == RelicRecoveryVuMark.UNKNOWN && count < 15) {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            count++;
            telemetry.addData("COUNT", count);
            telemetry.addData("VUMARK", vuMark);
            telemetry.update();
        }*/

        // grab cube and lift up before going off platform
        r.LEFT_GRABBER.setPosition(r.LG_MAX);
        r.RIGHT_GRABBER.setPosition(r.RG_MAX);
        r.LIFT_1.setPower(0.5);
        r.LIFT_2.setPower(0.5);
        sleep(400);
        r.LIFT_1.setPower(0);
        r.LIFT_2.setPower(0);

        run();
    }

    public RelicRecoveryVuMark getVision() {
        relicTrackables.activate();
        sleep(500);
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        int count = 0;
        while (vuMark == RelicRecoveryVuMark.UNKNOWN && count < 15) {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            count++;
            telemetry.addData("COUNT", count);
            telemetry.addData("VUMARK", vuMark);
            telemetry.update();
        }
        return vuMark;
    }

    public abstract void run();

    public void drive(double l_inches, double r_inches, double pwr, double timeout) {
        if (opModeIsActive()) {

            resetEnc();

            int l_target = (int) (l_inches / (Math.PI * WHL_DIAM)) * PPR;
            int r_target = (int) (r_inches / (Math.PI * WHL_DIAM)) * PPR;

            setTarget(l_target, r_target);

            setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            //setPwr(pwr);

            int phase = 0;
            ElapsedTime et = new ElapsedTime();
            setPwr(pwr);

            while (opModeIsActive() &&
                    runtime.seconds() < timeout &&
                    busy()) {

                et.reset();

                int diff_l = l_target - r.LEFT_FRONT.getCurrentPosition();
                int diff_r = r_target - r.RIGHT_FRONT.getCurrentPosition();

                telemetry.addData("PWR", "APP: %.2f | DES: %.2f");

                telemetry.addData("ENC", "LEFT: %d (%d/%d) | RIGHT: %d (%d/%d) | TIME: %2.2f/%2.2f",
                        diff_l, r.LEFT_FRONT.getCurrentPosition(), l_target,
                        diff_r, r.RIGHT_FRONT.getCurrentPosition(), r_target,
                        runtime.seconds(), timeout);
                telemetry.addData("CYCLE", et.milliseconds());
                telemetry.update();
                if (et.milliseconds() < 100) sleep((long) (100 - et.milliseconds()));

            }

            setPwr(0);
            setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);
        }
        telemetry.clearAll();
    }

    public void resetEnc() {
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    public void setMode(DcMotor.RunMode mode) {
        r.LEFT_FRONT.setMode(mode);
        r.LEFT_BACK.setMode(mode);
        r.RIGHT_FRONT.setMode(mode);
        r.RIGHT_BACK.setMode(mode);
    }

    public void setTarget(int l, int rt) {
        r.LEFT_BACK.setTargetPosition(l);
        r.LEFT_FRONT.setTargetPosition(l);
        r.RIGHT_FRONT.setTargetPosition(rt);
        r.RIGHT_BACK.setTargetPosition(rt);
    }

    public void setPwr(double pwr) {
        pwr = Math.abs(pwr);
        r.LEFT_BACK.setPower(pwr);
        r.LEFT_FRONT.setPower(pwr);
        r.RIGHT_FRONT.setPower(pwr);
        r.RIGHT_BACK.setPower(pwr);
    }

    public void setPwrNoAbs(double pwr) {
        r.LEFT_BACK.setPower(pwr);
        r.LEFT_FRONT.setPower(pwr);
        r.RIGHT_FRONT.setPower(pwr);
        r.RIGHT_BACK.setPower(pwr);
    }

    public void setLPwr(double pwr) {
        r.LEFT_BACK.setPower(pwr);
        r.LEFT_FRONT.setPower(pwr);
    }

    public void setRPwr(double pwr) {
        r.RIGHT_BACK.setPower(pwr);
        r.RIGHT_FRONT.setPower(pwr);
    }

    public void senseBlueTurn() {
        r.LEFT_EXT.setPosition(1);
        r.RIGHT_EXT.setPosition(1);
        sleep(1000);
        if (r.COLOR_SENSOR_RED.blue() > r.COLOR_SENSOR_RED.red()) {
            drive(-2, 2, 0.3, 2);
            sleep(1000);
            r.LEFT_EXT.setPosition(0);
            drive(2, -2, 0.3, 2);
        } else {
            drive(2, -2, 0.3, 2);
            sleep(1000);
            r.LEFT_EXT.setPosition(0);
            drive(-2, 2, 0.3, 2);
        }
        telemetry.clearAll();
    }


    public void senseRedTurn() {
        r.LEFT_EXT.setPosition(1);
        r.RIGHT_EXT.setPosition(1);
        sleep(1000);
        if (r.COLOR_SENSOR_RED.blue() < r.COLOR_SENSOR_RED.red()) {
            drive(-8, 8, 0.4, 2);
            sleep(1000);
            r.LEFT_EXT.setPosition(0);
            drive(8, -8, 0.4, 2);
        } else {
            drive(8, -8, 0.4, 2);
            sleep(1000);
            r.LEFT_EXT.setPosition(0);
            drive(-8, 8, 0.4, 2);
        }
        telemetry.clearAll();
    }

    public void driveUntilFlat(double threshold, double pwr) {
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPwrNoAbs(pwr);
        double pitch = pitch();
        while (Math.abs(pitch) < threshold && opModeIsActive()) {
            telemetry.addLine("Driving until flat");
            telemetry.addData("threshold", threshold);
            telemetry.addData("pitch", pitch);
            telemetry.update();
            pitch = pitch();
        }
        setPwr(0);
        resetEnc();
        telemetry.clearAll();

    }

    public void turnUntilHeading(double heading, double pwr) {
        double yaw = yaw();
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        while (Math.abs(distance(yaw, heading)) > HDNG_THRESHOLD && opModeIsActive()) {
            setLPwr(distance(yaw, heading) > 0? -pwr:pwr);
            setRPwr(distance(yaw, heading) < 0? -pwr:pwr);
            telemetry.addData("Turning", distance(yaw, heading) > 0? "Right":"Left");
            telemetry.addData("Power", pwr);
            telemetry.addData("", "Heading Target: %s | Actual: %s", heading, yaw);
            telemetry.update();
            yaw = yaw();
        }
        resetEnc();
        setPwr(0);
        telemetry.clearAll();
    }

    public boolean busy() {
        return r.RIGHT_BACK.isBusy() && r.LEFT_BACK.isBusy() && r.LEFT_FRONT.isBusy() && r.RIGHT_FRONT.isBusy();
    }

    public double pitch() {
        return r.IMU.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).thirdAngle - 3;
    }

    public double yaw() {
        return r.IMU.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }

    public double distance(double angle1, double angle2) {
        return ((angle2 - angle1 + 180) % 360) - 180;
    }

    public void releaseGrabber() {
        r.LEFT_GRABBER.setPosition(r.LG_MAX);
        r.RIGHT_GRABBER.setPosition(r.RG_MAX);
    }

}
