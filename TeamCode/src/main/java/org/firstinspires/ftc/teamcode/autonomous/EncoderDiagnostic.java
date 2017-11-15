package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Robot;

/**
 * Created by dchotzen-hartzell19 on 11/7/17
 */

@Autonomous(name="Encoder Diagnostic")
@Disabled
public class EncoderDiagnostic extends LinearOpMode {

    Robot r;
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        r = new Robot(this);

        r.LEFT_BACK.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        r.RIGHT_FRONT.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        runtime.reset();
        setPwr(0.75);
        while (r.LEFT_FRONT.getCurrentPosition() < 5000 && r.RIGHT_BACK.getCurrentPosition() < 5000 && opModeIsActive()) {
            //telemetry.addData("Enc", "%05d | %05d | %05d | %05d", r.LEFT_FRONT.getCurrentPosition(), r.LEFT_BACK.getCurrentPosition(),
            // r.RIGHT_FRONT.getCurrentPosition(), r.RIGHT_BACK.getCurrentPosition());
            telemetry.addData("Enc", "%05d | %05d", r.LEFT_FRONT.getCurrentPosition(), r.RIGHT_BACK.getCurrentPosition());
            telemetry.update();
        }
        setPwr(0);

        sleep(2000);

    }

    public void setMode(DcMotor.RunMode mode) {
        r.LEFT_FRONT.setMode(mode);
        //r.LEFT_BACK.setMode(mode);
        //r.RIGHT_FRONT.setMode(mode);
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
