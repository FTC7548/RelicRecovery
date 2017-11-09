package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by dchotzen-hartzell19 on 10/21/17.
 */

@Autonomous(name="Red Corner Hello?", group="Red")
public class RedCorner extends AutonomousOpMode {

    @Override
    public void run() {
        senseRedTurn();
        driveNew(30, 0.7, 5);
        switch (getVision()) {
            case CENTER:
                driveNew(6, 0.3, 2);
            case RIGHT:
                driveNew(12, 0.3, 2);
        }
        turnUntilHeading(270, 0.4, 1);
        drive(8, 8, 0.6, 2);
        //releaseGrabber();
        drive(-2, -2, 0.6, 2);

    }
}
