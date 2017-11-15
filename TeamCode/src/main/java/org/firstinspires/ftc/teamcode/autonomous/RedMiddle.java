package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by dchotzen-hartzell19 on 10/21/17.
 */


@Autonomous(name="Red Middle", group="Red")
public class RedMiddle extends AutonomousOpMode {

	@Override public void run() {
		RelicRecoveryVuMark v = getVision();
		senseRedTurn();
		sleep(1000);
		driveNew(8, 0.3, 3);
		driveUntilFlat(3, 0.3);
		driveNew(2, 0.3, 3);
		sleep(1000);
		turnUntilHeading(90, 0.3, 1, 4);
        sleep(250);
        driveNew(10, 0.3, 3);
        sleep(250);
		switch(v) {
			case LEFT:
				turnUntilHeading(165, 0.3, 1, 3);
			case RIGHT:
				turnUntilHeading(195, 0.3, 1, 3);
            case CENTER:
                turnUntilHeading(180, 0.3, 1, 3);
            case UNKNOWN:
                turnUntilHeading(180, 0.3, 1, 3);
		}
        depositBlock();
	}
}
