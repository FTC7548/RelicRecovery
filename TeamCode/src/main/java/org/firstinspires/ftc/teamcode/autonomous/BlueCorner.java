package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by dchotzen-hartzell19 on 10/21/17.
 */

@Autonomous(name="Blue Cornerk", group="Blue")
public class BlueCorner extends AutonomousOpMode {

    @Override
    public void run() {
        senseBlueTurn();
        sleep(500);
        RelicRecoveryVuMark v = driveUntilVision(.1, 1);
        driveNew(-10, 0.3, 3);
        driveUntilFlat(3, -0.3);
        sleep(500);
        if (v == RelicRecoveryVuMark.LEFT) { // SHOULD BE LEFT ACTUALLY
            driveNew(-7.5, 0.3, 3);
            sleep(500);
            dragLeftTurnHeading(45, 0.4, 1, 3);
            sleep(500);
            dragRightTurnHeading(105, 0.4, 1, 2);
        } else if (v == RelicRecoveryVuMark.CENTER) {
            driveNew(-1.5, 0.3, 3);
            sleep(500);
            dragRightTurnHeading(85, 0.4, 1, 3);
        } else if (v == RelicRecoveryVuMark.RIGHT || v == RelicRecoveryVuMark.UNKNOWN)  { // SHOULD BE RIGHT ACTUALLY
            driveNew(-7, 0.3, 3);
            sleep(500);
            dragRightTurnHeading(85, 0.4, 1, 3);
        }
        depositBlock();
    }
}
