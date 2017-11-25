package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by dchotzen-hartzell19 on 11/22/17.
 */

public class VexMotor extends OpMode {

    Servo motor;

    @Override
    public void init() {
        motor = hardwareMap.servo.get("vex");
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            motor.setPosition(0);
            motor.setDirection(Servo.Direction.FORWARD);
        } else if (gamepad1.dpad_down) {
            motor.setPosition(0);
            motor.setDirection(Servo.Direction.REVERSE);
        } else {
            motor.setPosition(.5);
        }
    }

}
