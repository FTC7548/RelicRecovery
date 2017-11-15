package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Robot;

/**
 * Created by dchotzen-hartzell19 on 11/6/17.
 */

@Autonomous(name="MEGA TEST")
@Disabled
public class MegaTest extends LinearOpMode {

    public Robot r;

    static final double     COUNTS_PER_MOTOR_REV    = 280 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    private ElapsedTime runtime = new ElapsedTime();

    @Override public void runOpMode() {
        r = new Robot(this);
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        encoderDrive(1, 64, 64, 3);

    }

    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            r.LEFT_BACK.setTargetPosition(r.LEFT_BACK.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH));
            r.RIGHT_BACK.setTargetPosition(r.RIGHT_BACK.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH));


            // Turn On RUN_TO_POSITION
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            r.RIGHT_FRONT.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            r.LEFT_FRONT.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            // reset the timeout time and start motion.
            runtime.reset();
            setPwr(speed);

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (r.RIGHT_BACK.isBusy() && r.LEFT_BACK.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", r.LEFT_FRONT.getTargetPosition(),  r.RIGHT_FRONT.getTargetPosition());
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        r.LEFT_FRONT.getCurrentPosition(),
                        r.RIGHT_FRONT.getCurrentPosition());
                r.LEFT_FRONT.setPower(r.LEFT_BACK.getPower());
                r.RIGHT_FRONT.setPower(r.RIGHT_BACK.getPower());
                telemetry.update();
            }

            // Stop all motion;
            setPwr(0);
            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // Turn off RUN_TO_POSITION
            setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }


    public void setMode(DcMotor.RunMode mode) {
        r.LEFT_FRONT.setMode(mode);
        r.LEFT_BACK.setMode(mode);
        r.RIGHT_FRONT.setMode(mode);
        r.RIGHT_BACK.setMode(mode);
    }
    public void setPwr(double pwr) {
        pwr = Math.abs(pwr);
        r.LEFT_BACK.setPower(pwr);
        r.LEFT_FRONT.setPower(pwr);
        r.RIGHT_FRONT.setPower(pwr);
        r.RIGHT_BACK.setPower(pwr);
    }

}
