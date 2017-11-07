package org.firstinspires.ftc.teamcode.util;

import java.util.ArrayList;

/**
 * Created by dchotzen-hartzell19 on 10/31/17.
 */

public abstract class ToggleServo {

    private static ArrayList<ToggleServo> servoList = new ArrayList<ToggleServo>();

    public static void add(ToggleServo t) {
        servoList.add(t);
    }

    public static void toggleStuff() {
        for (ToggleServo t: servoList) {
            t.toggle();
        }
    }

    public boolean alreadyPressed;
    public boolean state;

    public abstract void toggleTrue();
    public abstract void toggleFalse();
    public abstract boolean toggleCondition();

    public void toggle() {
        if (toggleCondition()) {
            if (!alreadyPressed) {
                execute();
                alreadyPressed = true;
            }
        } else
            alreadyPressed = false;
    }

    public void execute() {
        if (state) {
            toggleFalse();
        } else {
            toggleTrue();
        }
        state = !state;
    }
}
