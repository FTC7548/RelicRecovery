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
        senseBlueTurn();
        sleep(500);
        driveNew(-8, 0.3, 3);
        driveUntilFlat(3, -0.3);
        driveNew(-2, 0.3, 3);
                /*switch (v) {
+            case CENTER:
+                driveNew(-6, 0.3, 2);
+            case RIGHT:
+                driveNew(-12, 0.3, 2);*/
        sleep(500);
        turnUntilHeading(80, 0.3, 1, 3);
        driveNew(10, 0.3, 3);
        sleep(250);
        turnUntilHeading(0, 0.3, 1, 3);
        sleep(500);
        depositBlock();


    }

}
