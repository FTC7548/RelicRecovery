package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by dchotzen-hartzell19 on 10/21/17.
 */

@Autonomous(name="Blue Middle", group="Blue")
public class BlueMiddle extends AutonomousOpMode {

    @Override
    public void run() {
        RelicRecoveryVuMark v = getVision();
        senseBlueTurn();
        sleep(500);
        driveNew(-8, 0.3, 3);
        driveUntilFlat(3, -0.3);
        driveNew(-2, 0.3, 3);
        sleep(500);
        turnUntilHeading(80, 0.3, 1, 3);
        driveNew(10, 0.3, 3);
        sleep(250);
        switch(v) {
            case LEFT:
                turnUntilHeading(15, 0.3, 1, 3);
            case RIGHT:
                turnUntilHeading(-15, 0.3, 1, 3);
            case CENTER:
                turnUntilHeading(0, 0.3, 1, 3);
            case UNKNOWN:
                turnUntilHeading(0, 0.3, 1, 3);
        }


    }

}
