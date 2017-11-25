package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.util.Robot;

/**
 * Created by dchotzen-hartzell19 on 10/10/17.
 */

@TeleOp(name="Tank Drive Split Controls")
public class TeleOpSplit extends LinearOpMode {

    private final double RELIC_INC = 0.001;
    public Robot r;
    public boolean a_pressed = false;
    public boolean grabber_toggled = false;
    public boolean b_pressed = false;
    public boolean intake_toggled = false;
    public boolean y_pressed = false;
    public boolean back_toggled = false;
    public boolean a2_pressed = false;
    public boolean relic_toggled = false;
    public boolean y2_pressed = false;
    public boolean extendo_toggled = false;

    public void runOpMode() {

        r = new Robot(this, true);
        ElapsedTime time = new ElapsedTime();
        waitForStart();
        //r.LEFT_EXT.setPosition(.2);

        time.reset();

        while (opModeIsActive()) {
            drive();
            lift();
            grabber();
            intake();
            back();
            rail();
            relic();

            if (gamepad2.right_bumper) {
                r.INTAKE.setSpeed(1);
            } else if (gamepad2.right_trigger > 0.5) {
                r.INTAKE.setSpeed(-1);
            } else {
                r.INTAKE.setSpeed(0);
            }

            if (gamepad2.y && gamepad2.dpad_up && !y2_pressed) {
                y2_pressed = true;
                r.RAIL.setPosition(Range.clip(r.RAIL.getPosition() - .15, 0, 0.6));
            } else if (gamepad2.y && gamepad2.dpad_down && !y2_pressed) {
                y2_pressed = true;
                r.RAIL.setPosition(Range.clip(r.RAIL.getPosition() + .15, 0, 0.7));
            } else if (y2_pressed && !gamepad2.y) {
                y2_pressed = false;
            }

            telemetry.update();

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
        if (gamepad1.right_bumper) {
            l_pwr = l_pwr / 2;
            r_pwr = r_pwr / 2;
        }
        r.LEFT_BACK.setPower(l_pwr);
        r.RIGHT_BACK.setPower(r_pwr);
        r.LEFT_FRONT.setPower(l_pwr);
        r.RIGHT_FRONT.setPower(r_pwr);
        telemetry.addData("PWR", "L %.2f | R %.2f", l_pwr, r_pwr);
        telemetry.addData("LIFT", "1: %s | 2: %s", r.LIFT_1.getCurrentPosition(), r.LIFT_2.getCurrentPosition());
        telemetry.addData("GRAB", "L: %s | R: %s", r.LEFT_GRABBER.getPosition(), r.RIGHT_GRABBER.getPosition());

    }

    public void intake() {
        if (gamepad2.x) {
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
            r.GRIP.setPosition(0.3);
        } else {
            r.LEFT_GRABBER.setPosition(r.LG_MAX);
            r.RIGHT_GRABBER.setPosition(r.RG_MAX);
            r.GRIP.setPosition(0.7);
        }
        grabber_toggled = !grabber_toggled;
    }

    public void back() {
        if (gamepad2.b) {
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

    public void rail() {
        if (gamepad2.dpad_up) {
            r.RAIL.setPosition(Range.clip(r.RAIL.getPosition() - RELIC_INC, 0.01, 0.99));
            telemetry.addData("Rail Pos", r.RAIL.getPosition());
        } else if (gamepad2.dpad_down){
            r.RAIL.setPosition(Range.clip(r.RAIL.getPosition() + RELIC_INC, 0.01, 0.99));
        }
        telemetry.addData("Rail Pos", r.RAIL.getPosition());
    }

    public void relic() {
        if (gamepad2.left_trigger > 0.5) {
            r.RELICCC_BOTTOM.setPosition(.9);
        } else {
            r.RELICCC_BOTTOM.setPosition(0);
        }
        if (gamepad2.a) {
            if (!a2_pressed) {
                a2_pressed = true;
                toggleRelic();
            }
        } else {
            a2_pressed = false;
        }
    }

    public void toggleRelic() {
        if (relic_toggled) {
            r.RELICCC_TOP.setPosition(0.75);
        } else {
            r.RELICCC_TOP.setPosition(0.35);
        }
        relic_toggled = !relic_toggled;
    }


}
