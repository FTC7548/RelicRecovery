package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.ToggleServo;

/**
 * Created by dchotzen-hartzell19 on 10/10/17
 */

@TeleOp(name="Tank Drive")
public class TeleOP extends LinearOpMode {

    public Robot r;

    public void runOpMode() {

        r = new Robot(this);
        ElapsedTime time = new ElapsedTime();
        waitForStart();

        time.reset();
        // intake
        ToggleServo.add(new ToggleServo() {
            @Override public void toggleTrue() {
                r.INTAKE.setPosition(0);
            }

            @Override public void toggleFalse() {
                r.INTAKE.setPosition(1);
            }

            @Override public boolean toggleCondition() {
                return gamepad1.y;
            }
        });


        // back thing
        ToggleServo.add(new ToggleServo() {
            @Override public void toggleTrue() {
                r.BACK.setPosition(1);
            }

            @Override public void toggleFalse() {
                r.BACK.setPosition(0);
            }

            @Override public boolean toggleCondition() {
                return gamepad1.x;
            }
        });

        // grabberino
        ToggleServo.add(new ToggleServo() {
            @Override public void toggleTrue() {
                r.LEFT_GRABBER.setPosition(r.LG_MIN);
                r.RIGHT_GRABBER.setPosition(r.RG_MIN);
            }

            @Override public void toggleFalse() {
                r.LEFT_GRABBER.setPosition(r.LG_MAX);
                r.RIGHT_GRABBER.setPosition(r.RG_MAX);
            }

            @Override public boolean toggleCondition() {
                return gamepad1.a;
            }
        });


        while (opModeIsActive()) {
            drive();
            lift();
            intake();
            ToggleServo.toggleStuff();

            telemetry.update();
            idle();
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
        telemetry.addData("LIFT", "1: %d | 2: %d", r.LIFT_1.getCurrentPosition(), r.LIFT_2.getCurrentPosition());
        telemetry.addData("GRAB", "L: %.1f | R: %.1f", r.LEFT_GRABBER.getPosition(), r.RIGHT_GRABBER.getPosition());

    }

    public void intake() {
        if (gamepad1.right_bumper) {
            r.INTAKE.setSpeed(1);
        } else if (gamepad1.right_trigger > 0.5) {
            r.INTAKE.setSpeed(-1);
        } else {
            r.INTAKE.setSpeed(0);
        }
    }

    /*

    public void intake() {
        if (gamepad1.b) {
            if (!b_pressed) {
                toggleIntake();
                b_pressed = true;
            }
        } else {
            b_pressed = false;
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
        } else {
            r.LEFT_GRABBER.setPosition(r.LG_MAX);
            r.RIGHT_GRABBER.setPosition(r.RG_MAX);
        }
        grabber_toggled = !grabber_toggled;
    }

    public void back() {
        if (gamepad1.y) {
            if (!y_pressed) {
                toggleBack();
                y_pressed = true;
            }
        } else {
            y_pressed = false;
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

    */

}