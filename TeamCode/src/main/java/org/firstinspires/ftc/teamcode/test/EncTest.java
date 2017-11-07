package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.Robot;

/**
 * Created by dchotzen-hartzell19 on 10/10/17.
 */

@TeleOp(name="Encoder Test")
public class EncTest extends LinearOpMode {

    public Robot r;

    public void runOpMode() {

        r = new Robot(this);


        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("LIFT", "1: %s | 2: %s", r.LIFT_1.getCurrentPosition(), r.LIFT_2.getCurrentPosition());
            telemetry.update();
            idle();

        }
    }
}
