package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by dchotzen-hartzell19 on 10/21/17.
 */

@Autonomous(name="Blue Corner", group="Blue")
public class BlueCorner extends AutonomousOpMode {

    @Override
    public void run() {
        //senseBlueTurn();
        drive(-24, -24, 0.7, 2);
        //turnUntilHeading(270, 0.2);
    }
}
