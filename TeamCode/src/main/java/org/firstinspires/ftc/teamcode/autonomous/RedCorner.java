package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by dchotzen-hartzell19 on 10/21/17.
 */

@Autonomous(name="Red Corner", group="Red")
public class RedCorner extends AutonomousOpMode {

    @Override
    public void run() {
        senseRedTurn();
        drive(-27, -27, 0.3, 2);
        switch (getVision()) {
            case CENTER:
                drive(6, 6, 0.4, 2);
            case RIGHT:
                drive(12, 12, 0.4, 2);

        }
        turnUntilHeading(90, 0.3);
        drive(8, 8, 0.6, 2);
        releaseGrabber();
        drive(-2, -2, 0.6, 2);

    }
}
