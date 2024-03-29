package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.util.Robot;

public abstract class AutonomousOpMode extends LinearOpMode {

    public static int TEST = 1;
    private final int PPR = 1890;
    private final double WHL_DIAM = 4;
    private final double JUULPWR = .15;
    private final double JUULDIST = 3;
    private final double HDNG_THRESHOLD = 10;
    private final double PPI = PPR / (WHL_DIAM * Math.PI);
    public Robot r;
    public RelicRecoveryVuMark v = RelicRecoveryVuMark.UNKNOWN;
    // Vuforia Stuff
    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;
    Orientation angles;
    private ElapsedTime runtime = new ElapsedTime();
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;

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

        if (opModeIsActive()) {
            relicTrackables.activate();
            new Thread(new VisionBot()).start();
            r.LEFT_GRABBER.setPosition(r.LG_MAX);
            r.RIGHT_GRABBER.setPosition(r.RG_MAX);
            sleep(1000);
            r.LIFT_1.setPower(0.3);
            r.LIFT_2.setPower(0.3);
            sleep(750);
            r.LIFT_1.setPower(0);
            r.LIFT_2.setPower(0);

            sleep(500);

            run();
        }
    }

    public abstract void run();

    public RelicRecoveryVuMark getVision(double timeout) {
        relicTrackables.activate();
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        int count = 0;
        runtime.reset();
        while (vuMark == RelicRecoveryVuMark.UNKNOWN && runtime.seconds() < timeout) {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            count++;
            telemetry.addData("COUNT", count);
            telemetry.addData("VUMARK", vuMark);
            telemetry.update();
        }
        return vuMark;
    }

    public void drive(double l_inches, double r_inches, double pwr, double timeout) {
        if (opModeIsActive()) {

            resetEnc();

            int start, end;

            int l_target = (int) (l_inches * PPI);
            int r_target = (int) (r_inches * PPI);

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
                //dx = end - start;
                start = r.LEFT_FRONT.getCurrentPosition();

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
    }

    public void driveNew(double inches, double pwr, double timeout) {
        if (!opModeIsActive()) return;
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        int newLeftTicks = r.LEFT_FRONT.getCurrentPosition() + (int)(inches * PPI);
        int newRightTicks = r.LEFT_BACK.getCurrentPosition() + (int)(inches * PPI);
        runtime.reset();
        if (inches > 0) {
            setPwrNoAbs(pwr);
            while (r.LEFT_FRONT.getCurrentPosition() < newLeftTicks && r.RIGHT_BACK.getCurrentPosition() < newRightTicks && runtime.seconds() < timeout) {
                telemetry.addData("Pos", "%05d | %05d", r.LEFT_FRONT.getCurrentPosition(), r.RIGHT_BACK.getCurrentPosition());
                telemetry.addData("Tgt", "%05d | %05d", newLeftTicks, newRightTicks);
                telemetry.update();
                if (this.isStopRequested()) {
                    return;
                }
            }
        } else {
            setPwrNoAbs(-pwr);
            while (r.LEFT_FRONT.getCurrentPosition() > newLeftTicks && r.RIGHT_BACK.getCurrentPosition() > newRightTicks && runtime.seconds() < timeout) {
                telemetry.addData("Pos", "%05d | %05d", r.LEFT_FRONT.getCurrentPosition(), r.RIGHT_BACK.getCurrentPosition());
                telemetry.addData("Tgt", "%05d | %05d", newLeftTicks, newRightTicks);
                telemetry.update();
                if (this.isStopRequested()) {
                    return;
                }
            }
        }

        setPwr(0);
    }

    public void turnEncoder(double inches, int direction, double pwr, double timeout) {
        if (!opModeIsActive()) return;
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        int newLeftTicks = r.LEFT_FRONT.getCurrentPosition() + (int)(inches * PPI * -direction);
        int newRightTicks = r.LEFT_BACK.getCurrentPosition() + (int)(inches * PPI * direction);
        runtime.reset();
        setTurnPwr(direction * pwr);
        if (direction > 0) {
            while (r.LEFT_FRONT.getCurrentPosition() > newLeftTicks && r.RIGHT_BACK.getCurrentPosition() < newRightTicks && runtime.seconds() < timeout) {
                telemetry.addData("Pos", "%05d | %05d", r.LEFT_FRONT.getCurrentPosition(), r.RIGHT_BACK.getCurrentPosition());
                telemetry.addData("Tgt", "%05d | %05d", newLeftTicks, newRightTicks);

                telemetry.update();
            }
        } else {
            while (r.LEFT_FRONT.getCurrentPosition() < newLeftTicks && r.RIGHT_BACK.getCurrentPosition() > newRightTicks && runtime.seconds() < timeout) {
                telemetry.addData("Pos", "%05d | %05d", r.LEFT_FRONT.getCurrentPosition(), r.RIGHT_BACK.getCurrentPosition());
                telemetry.addData("Tgt", "%05d | %05d", newLeftTicks, newRightTicks);
                telemetry.update();
            }
        }

        setPwr(0);
    }

    public void resetEnc() {
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void releaseGrip() {
        r.LEFT_GRABBER.setPosition(r.LG_MIN);
        r.RIGHT_GRABBER.setPosition(r.RG_MIN);
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

    public void setTurnPwr(double pwr) {
        r.LEFT_BACK.setPower(-pwr);
        r.LEFT_FRONT.setPower(-pwr);
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

    public void senseRedTurn() {
        if (!opModeIsActive()) return;
        r.LEFT_EXT.setPosition(1);
        //r.RIGHT_EXT.setPosition(1);
        telemetry.addData("Red", r.COLOR_SENSOR_RED.red());
        telemetry.addData("Blue", r.COLOR_SENSOR_RED.blue());
        telemetry.update();
        sleep(1000);
        if (Math.abs(r.COLOR_SENSOR_RED.red() - r.COLOR_SENSOR_RED.blue()) < 5) {
            r.LEFT_EXT.setPosition(0);
            sleep(300);
            return;
        }
        if (r.COLOR_SENSOR_RED.red() < r.COLOR_SENSOR_RED.blue()) {
            driveFwd();
        } else {
            driveBack();
        }
    }

    public void senseBlueTurn() {
        if (!opModeIsActive()) return;
        r.LEFT_EXT.setPosition(1);
        //r.RIGHT_EXT.setPosition(1);
        telemetry.addData("Red", r.COLOR_SENSOR_RED.red());
        telemetry.addData("Blue", r.COLOR_SENSOR_RED.blue());
        telemetry.update();
        sleep(1000);
        if (Math.abs(r.COLOR_SENSOR_RED.red() - r.COLOR_SENSOR_RED.blue()) < 5) {
            r.LEFT_EXT.setPosition(0);
            sleep(300);
            return;
        }
        if (r.COLOR_SENSOR_RED.blue() > r.COLOR_SENSOR_RED.red()) {
            driveBack();
        } else {
            driveFwd();
        }
    }

    public void driveBack() {
        driveNew(-2, 0.1, 2);
        sleep(250);
        r.LEFT_EXT.setPosition(0);
        sleep(500);
        driveNew(3, 0.3, 2);
    }

    public void driveFwd() {
        driveNew(2, 0.1, 2);
        sleep(250);
        r.LEFT_EXT.setPosition(0);
    }

    public void turnLeft() {
        turnEncoder(JUULDIST, 1, JUULPWR, 2);
        sleep(250);
        r.LEFT_EXT.setPosition(0);
        sleep(250);
        turnEncoder(JUULDIST, -1, JUULPWR, 2);
        sleep(500);
    }

    public void turnRight() {
        turnEncoder(JUULDIST, -1, JUULPWR, 2);
        sleep(250);
        r.LEFT_EXT.setPosition(0);
        sleep(250);
        turnEncoder(JUULDIST, 1, JUULPWR, 2);
    }


    public void driveUntilFlat(double threshold, double pwr) {
        if (!opModeIsActive()) return;
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPwrNoAbs(pwr);
        double pitch = pitch();
        while (Math.abs(pitch) > threshold && opModeIsActive()) {
            telemetry.addLine("Driving until flat");
            telemetry.addData("threshold", threshold);
            telemetry.addData("pitch", pitch);
            telemetry.update();
            pitch = pitch();
            if (this.isStopRequested()) {
                return;
            }
        }
        setPwr(0);
        resetEnc();
    }

    public void turnUntilHeading(double heading, double pwr, double dir, double timeout) {
        if (!opModeIsActive()) return;
        double yaw = yaw();
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        runtime.reset();
        while (Math.abs(distance(yaw, heading)) > HDNG_THRESHOLD && opModeIsActive() && runtime.seconds() < timeout) {
            setLPwr(distance(yaw, heading) < 0? -pwr * dir:pwr * dir);
            setRPwr(distance(yaw, heading) > 0? -pwr * dir:pwr * dir);
            telemetry.addData("Turning", distance(yaw, heading) > 0? "Right":"Left");
            telemetry.addData("Power", pwr);
            telemetry.addData("", "Heading Target: %s | Actual: %s", heading, yaw);
            telemetry.addData("", "Distance: %s", distance(yaw,heading));
            telemetry.update();
            yaw = yaw();
            if (this.isStopRequested()) {
                return;
            }
        }
        resetEnc();
        setPwr(0);
    }

    public void dragLeftTurnHeading(double heading, double pwr, double dir, double timeout) {
        if (!opModeIsActive()) return;
        double yaw = yaw();
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        runtime.reset();
        while (Math.abs(distance(yaw, heading)) > HDNG_THRESHOLD && opModeIsActive() && runtime.seconds() < timeout) {
            setLPwr(distance(yaw, heading) < 0 ? -pwr * dir : pwr * dir);
            //setRPwr(distance(yaw, heading) > 0? -pwr * dir:pwr * dir);
            telemetry.addData("Turning", distance(yaw, heading) > 0 ? "Right" : "Left");
            telemetry.addData("Power", pwr);
            telemetry.addData("", "Heading Target: %s | Actual: %s", heading, yaw);
            telemetry.addData("", "Distance: %s", distance(yaw, heading));
            telemetry.update();
            yaw = yaw();
            if (this.isStopRequested()) {
                return;
            }
        }
        resetEnc();
        setPwr(0);
    }

    public void dragRightTurnHeading(double heading, double pwr, double dir, double timeout) {
        if (!opModeIsActive()) return;
        double yaw = yaw();
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        runtime.reset();
        while (Math.abs(distance(yaw, heading)) > HDNG_THRESHOLD && opModeIsActive() && runtime.seconds() < timeout) {
            //setLPwr(distance(yaw, heading) < 0? -pwr * dir:pwr * dir);
            setRPwr(distance(yaw, heading) > 0? -pwr * dir:pwr * dir);
            telemetry.addData("Turning", distance(yaw, heading) > 0? "Right":"Left");
            telemetry.addData("Power", pwr);
            telemetry.addData("", "Heading Target: %s | Actual: %s", heading, yaw);
            telemetry.addData("", "Distance: %s", distance(yaw,heading));
            telemetry.update();
            yaw = yaw();
            if (this.isStopRequested()) {
                return;
            }
        }
        resetEnc();
        setPwr(0);
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

    public void depositBlock() {
        if (!opModeIsActive()) return;
        sleep(500);
        r.LIFT_1.setPower(-0.3);
        r.LIFT_2.setPower(-0.3);
        sleep(450);
        r.LIFT_1.setPower(0);
        r.LIFT_2.setPower(0);
        sleep(250);
        driveNew(-20, 0.3, 2);
        sleep(250);
        releaseGrip();
        sleep(250);
        driveNew(5, 0.3, 2);
        sleep(250);
        driveNew(-10, 0.3, 2);
        sleep(250);
        driveNew(5, 0.3, 2);
        r.LIFT_1.setPower(0.3);
        r.LIFT_2.setPower(0.3);
        sleep(850);
        r.LIFT_1.setPower(0);
        r.LIFT_2.setPower(0);
        r.INTAKE.setPosition(0);
        sleep(250);
        r.LIFT_1.setPower(-0.3);
        r.LIFT_2.setPower(-0.3);
        sleep(850);
        r.LIFT_1.setPower(0);
        r.LIFT_2.setPower(0);
        //driveNew(-2, 0.3, 2);
    }

    public RelicRecoveryVuMark driveUntilVision(double pwr, double timeout) {
        relicTrackables.activate();
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        int count = 0;
        runtime.reset();
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPwr(pwr);
        while (vuMark == RelicRecoveryVuMark.UNKNOWN && runtime.seconds() < timeout) {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            count++;
            telemetry.addData("COUNT", count);
            telemetry.addData("VUMARK", vuMark);
            telemetry.update();
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) return vuMark;
        }
        return vuMark;
    }

    public void driveALittle(int dir) {
        if (v == RelicRecoveryVuMark.UNKNOWN) {
            sleep(250);
            driveNew(dir * 1, .3, 1.5);
            sleep(250);
            driveNew(-dir * 1, .3, 1.5);
            sleep(250);
            driveNew(-dir * 1, .3, 1.5);
            sleep(250);
        }
    }

    public class VisionBot implements Runnable {
        public RelicRecoveryVuMark vuMark;

        @Override
        public void run() {
            int count = 0;
            while (v == RelicRecoveryVuMark.UNKNOWN) {
                if (RelicRecoveryVuMark.from(relicTemplate) != RelicRecoveryVuMark.UNKNOWN) {
                    v = RelicRecoveryVuMark.from(relicTemplate);
                    break;
                }
                telemetry.addData("VuMark", v);
                telemetry.addData("count", count);
                telemetry.update();
                sleep(30);
                count++;
                idle();
            }

        }
    }
}
