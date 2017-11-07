package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Robot;

/**
 * Created by dchotzen-hartzell19 on 10/10/17.
 */

@TeleOp(name="New Old Tank Drive")
public class OldTeleOP2 extends LinearOpMode {

    public Robot r;

    public boolean a_pressed = false;
    public boolean grabber_toggled = false;

    public boolean b_pressed = false;
    public boolean intake_toggled = false;

    public boolean y_pressed = false;
    public boolean back_toggled = false;

    public void runOpMode() {

        r = new Robot(this);
        ElapsedTime time = new ElapsedTime();
        waitForStart();

        time.reset();

        while (opModeIsActive()) {
            drive();
            lift();
            grabber();
            intake();
            back();

            if (gamepad1.right_bumper) {
                r.INTAKE.setSpeed(1);
            } else if (gamepad1.right_trigger > 0.5) {
                r.INTAKE.setSpeed(-1);
            } else {
                r.INTAKE.setSpeed(0);
            }


           /* if (gamepad1.dpad_up) {
                r.INTAKE.setSpeed(1);
            } else if (gamepad1.dpad_down){
                r.INTAKE.setSpeed(-1);
            } else {
                r.INTAKE.setSpeed(0);
            }

            telemetry.update();
            idle();*/
        }

    }

    public void lift() {
        if (gamepad1.left_bumper) {
            r.LIFT_1.setPower(1);
            r.LIFT_2.setPower(1);
        } else if (gamepad1.left_trigger > 0.5) {
            if (r.LIFT_TOUCH_SENSOR.getState()) r.LIFT_2.setPower(-1);
            r.LIFT_1.setPower(-1);
        } else {
            r.LIFT_1.setPower(0);
            r.LIFT_2.setPower(0);
        }
    }

    public void drive() {
        double l_pwr = Math.pow(gamepad1.left_stick_y, 3);
        double r_pwr = Math.pow(gamepad1.right_stick_y, 3);
        r.LEFT_BACK.setPower(l_pwr);
        r.RIGHT_BACK.setPower(r_pwr);
        r.LEFT_FRONT.setPower(l_pwr);
        r.RIGHT_FRONT.setPower(r_pwr);
        telemetry.addData("PWR", "L %.2f | R %.2f", l_pwr, r_pwr);
        telemetry.addData("LIFT", "1: %s | 2: %s", r.LIFT_1.getCurrentPosition(), r.LIFT_2.getCurrentPosition());
        telemetry.addData("GRAB", "L: %s | R: %s", r.LEFT_GRABBER.getPosition(), r.RIGHT_GRABBER.getPosition());

    }

    public void intake() {
        if (gamepad1.y) {
            if (!y_pressed) {
                toggleIntake();
                y_pressed = true;
            }
        } else {
            y_pressed = false;
        }
    }

    public void toggleIntake() {
        if (intake_toggled) {
            r.INTAKE.setPosition(0);
        } else {
            r.INTAKE.setPosition(1);
        }
        intake_toggled = !intake_toggled;
     }

    public void grabber() {
        if (gamepad1.a) {
            if (!a_pressed) {
                toggleGrabber();
                a_pressed = true;
            }
        } else {
            a_pressed = false;
        }
    }

    public void toggleGrabber() {
        if (grabber_toggled) {
            r.LEFT_GRABBER.setPosition(r.LG_MIN);
            r.RIGHT_GRABBER.setPosition(r.RG_MIN);
            r.GRIP.setPosition(0.7);
        } else {
            r.LEFT_GRABBER.setPosition(r.LG_MAX);
            r.RIGHT_GRABBER.setPosition(r.RG_MAX);
            r.GRIP.setPosition(0.3);
        }
        grabber_toggled = !grabber_toggled;
    }

    public void back() {
        if (gamepad1.b) {
            if (!b_pressed) {
                toggleBack();
                b_pressed = true;
            }
        } else {
            b_pressed = false;
        }
    }

    public void toggleBack() {
        if (back_toggled) {
            r.BACK.setPosition(1);
        } else {
            r.BACK.setPosition(0);
        }
        back_toggled = !back_toggled;
    }

}
