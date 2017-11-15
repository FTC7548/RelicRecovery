package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by dchotzen-hartzell19 on 11/12/17.
 */

@Autonomous(name="Vision Code Test")
public class TestVision extends AutonomousOpMode {

    @Override
    public void run() {
        getVision();
        sleep(10000);
    }
}
