package org.firstinspires.ftc.teamcode.util;

/**
 * Created by dchotzen-hartzell19 on 10/30/17.
 */

public class Intake {

    /**
     * First number is left
     * Second number is right
     */
    public double[][] positions = {
            {0, 1},
            {0.525, 0.475}
    };
    private Robot r;

    public Intake(Robot r) {
        this.r = r;
    }

    public void setPosition(int pos) {
        r.INTAKE_LS.setPosition(positions[pos][0]);
        r.INTAKE_RS.setPosition(positions[pos][1]);
    }

    public void setSpeed(double pwr) {
        /*if (pwr > 0) {
            r.INTAKE_L.setPower(0);
            r.INTAKE_R.setPower(0);
            r.INTAKE_L.setDirection(Servo.Direction.FORWARD);
            r.INTAKE_R.setDirection(Servo.Direction.REVERSE);
        } else if (pwr < 0){
            r.INTAKE_L.setPower(0);
            r.INTAKE_R.setPower(0);
            r.INTAKE_L.setDirection(Servo.Direction.REVERSE);
            r.INTAKE_R.setDirection(Servo.Direction.FORWARD);
        } else {
            r.INTAKE_L.setPower(.5);
            r.INTAKE_L.setPower(.5);
        }*/

        if (pwr > 0) {
            r.INTAKE_L.setPower(-.7);
            r.INTAKE_R.setPower(.7);
        } else if (pwr < 0) {
            r.INTAKE_L.setPower(.7);
            r.INTAKE_R.setPower(-.7);
        } else {
            r.INTAKE_L.setPower(0);
            r.INTAKE_R.setPower(0);
        }
    }


}
