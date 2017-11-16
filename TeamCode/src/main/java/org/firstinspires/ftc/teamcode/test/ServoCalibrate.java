package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.Robot;

/**
 * Created by dchotzen-hartzell19 on 10/24/17.
 */

@TeleOp(name="Calibrate Ext Servos")
@Disabled
public class ServoCalibrate extends LinearOpMode {

    @Override
    public void runOpMode() {
        Robot r = new Robot(this);
        waitForStart();
        while(opModeIsActive()) {
            if (gamepad1.a) {
                r.LEFT_EXT.setPosition(0);
            } else {
                r.LEFT_EXT.setPosition(1);
            }
            idle();
        }
    }
}
