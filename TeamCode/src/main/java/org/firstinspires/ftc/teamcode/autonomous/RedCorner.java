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
        sleep(500);
        if (v == RelicRecoveryVuMark.RIGHT) { // SHOULD BE LEF/T ACTUALLY
            driveNew(4, 0.3, 3);
            sleep(1000);
            dragLeftTurnHeading(115, 0.4, 1, 3);
        } else if (v == RelicRecoveryVuMark.CENTER || v == RelicRecoveryVuMark.UNKNOWN) {
            driveNew(10, 0.3, 3);
            sleep(1000);
            dragLeftTurnHeading(70, 0.4, 1, 3);
        } else if (v == RelicRecoveryVuMark.LEFT)  { // SHOULD BE RIGHT ACTUALLY
            driveNew(5, 0.3, 3);
            sleep(1000);
            dragLeftTurnHeading(65, 0.4, 1, 3);
        }



        depositBlock();

    }
}
