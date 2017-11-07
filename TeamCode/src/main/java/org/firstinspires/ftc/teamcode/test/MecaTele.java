package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.util.Robot;

/**
 * Created by dchotzen-hartzell19 on 9/16/17.
 */

@TeleOp(name="Test MecaTele")
@Disabled
public class MecaTele extends OpMode {

    private Robot r;

    // lf, lb, rf, rb
    public float[][] mecValues = {
            {1, -1, -1, 1}, // east
            {1, 0, 0, 1}, // north east
            {1, 1, 1, 1}, // north
            {0, 1, 1, 0}, // north west
            {-1, 1, 1, -1}, // west
            {-1, 0, 0, -1}, // south west
            {-1, -1, -1, -1}, // south
            {0, -1, -1 , 0} // south east
    };

    public void init() {
        r = new Robot(new LinearOpMode() {
            @Override
            public void runOpMode() throws InterruptedException {

            }
        });
    }

    public void loop() {
        mecanumDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
    }


    public void mecanumDrive(float x, float y, float rot) {

        double mag = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

        double angle = Math.atan(y/x);

        if (x < 0) {
            angle += Math.PI;
        } else if (y < 0) {
            angle += 2*Math.PI;
        }


        int index = (int)Math.floor((angle + Math.PI / 8) / (Math.PI / 4));
        index = index % 8;
        float[] pwr = mecValues[index];

        double motorValues[] = {
                Range.clip(pwr[0] * mag + rot, -1, 1),
                Range.clip(pwr[1] * mag + rot, -1, 1),
                Range.clip(pwr[2] * mag - rot, -1, 1),
                Range.clip(pwr[3] * mag - rot, -1, 1)
        };



        setDrivePow(motorValues[0], motorValues[1], motorValues[2], motorValues[3]);
        telemetry.addData("PWR", "DIR %d | MAG %.2f | ANGLE %.2f | LF %.2f | LR %.2f | RF %.2f | RR %.2f",
                index, mag, angle, motorValues[0], motorValues[1], motorValues[2], motorValues[3]);
    }




    public void setDrivePow(double lf, double lr, double rf, double rr) {
        r.LEFT_FRONT.setPower(lf);
        r.LEFT_BACK.setPower(lr);
        r.RIGHT_FRONT.setPower(rf);
        r.RIGHT_BACK.setPower(rr);
        telemetry.update();
    }

    public void setMode(DcMotor.RunMode mode) {
        r.LEFT_FRONT.setMode(mode);
        r.LEFT_BACK.setMode(mode);
        r.RIGHT_FRONT.setMode(mode);
        r.RIGHT_BACK.setMode(mode);
    }
}
