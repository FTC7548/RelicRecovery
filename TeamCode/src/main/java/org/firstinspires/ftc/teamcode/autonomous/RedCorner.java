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
        RelicRecoveryVuMark v = getVision();
        senseRedTurn();
        sleep(1000);
        driveNew(8, 0.3, 3);
        driveUntilFlat(3, 0.3);
        driveNew(10, 0.3, 3);
        /*switch (v) {
            case CENTER:
                driveNew(6, 0.2, 2);
            case RIGHT:
                driveNew(12, 0.2, 2);
        }*/
        sleep(1000);
        turnUntilHeading(90, 0.2, 1, 3);
        sleep(1000);
        driveNew(-5, 0.3, 3);
        sleep(250);
        releaseGrip();
        sleep(250);
        driveNew(5, 0.3, 3);
        sleep(250);
        driveNew(-10, 0.3, 2);
        sleep(250);
        driveNew(3, 0.3, 2);

    }
}
