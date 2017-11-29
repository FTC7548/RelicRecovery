package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by dchotzen-hartzell19 on 10/30/17.
 */

public class Intake {

    private Robot r;

    /**
     * First number is left
     * Second number is right
     */
    public double[][] positions = {
            {0, 1},
            {0.50, 0.50}
    };

    public Intake(Robot r) {
        this.r = r;
    }

    public void setPosition(int pos) {
        r.INTAKE_LS.setPosition(positions[pos][0]);
        r.INTAKE_RS.setPosition(positions[pos][1]);
    }

    public void setSpeed(double pwr) {
        if (pwr > 0) {
            r.INTAKE_L.setPosition(0);
            r.INTAKE_R.setPosition(0);
            r.INTAKE_L.setDirection(Servo.Direction.FORWARD);
            r.INTAKE_R.setDirection(Servo.Direction.REVERSE);
        } else if (pwr < 0){
            r.INTAKE_L.setPosition(0);
            r.INTAKE_R.setPosition(0);
            r.INTAKE_L.setDirection(Servo.Direction.REVERSE);
            r.INTAKE_R.setDirection(Servo.Direction.FORWARD);
        } else {
            r.INTAKE_L.setPosition(.5);
            r.INTAKE_L.setPosition(.5);
        }
    }

}
