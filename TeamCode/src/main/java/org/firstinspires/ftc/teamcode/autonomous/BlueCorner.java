package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by dchotzen-hartzell19 on 10/21/17.
 */

@Autonomous(name="Blue Corner", group="Blue")
public class BlueCorner extends AutonomousOpMode {

    @Override
    public void run() {
        RelicRecoveryVuMark v = getVision();
        senseBlueTurn();
        sleep(500);
        driveNew(-8, 0.3, 3);
        driveUntilFlat(3, -0.3);
        driveNew(-10, 0.3, 3);
        sleep(1000);
        switch(v) {
            case LEFT:
                turnUntilHeading(105, 0.3, 1, 3);
            case RIGHT:
                turnUntilHeading(75, 0.3, 1, 3);
            case CENTER:
                turnUntilHeading(90, 0.3, 1, 3);
            case UNKNOWN:
                turnUntilHeading(90, 0.3, 1, 3);
        }
        depositBlock();
    }
}
