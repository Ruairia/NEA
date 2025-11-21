package ruairi.nea.gameClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

import java.util.ArrayList;

import static java.lang.Math.abs;


public class InputHandler {

    public Float horizontalAxisStrength = null; //Coefficient between 0 and 1

    private Controller gamepad;
    public static final float DEADZONE = 0.3f;

    public InputHandler() {
        if (Controllers.getControllers().size > 0) {
            gamepad = Controllers.getControllers().first();
        }
    }

    public ArrayList<String> getInputs() {
        ArrayList<String> inputs = new ArrayList<>();

        if (gamepad==null) horizontalAxisStrength =1f;

        if (isJumpPressed()) inputs.add("JUMP");
        if (checkMoveLeft()) inputs.add("LEFT");
        if (checkMoveRight()) inputs.add("RIGHT");
        if (isAttackPressed()) inputs.add("ATTACK");

        if (horizontalAxisStrength==null) horizontalAxisStrength=1f;

        return inputs;
    }

    private boolean isJumpPressed() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) return true;
        //A button (Xbox)
        return gamepad != null && gamepad.getButton(0);
    }

    private boolean checkMoveLeft() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) return true;

        if (gamepad != null) {
            float leftStickX = gamepad.getAxis(0);
            if (leftStickX < -DEADZONE) {
                horizontalAxisStrength = abs(leftStickX);
                return true;
            }
        }

        return false;
    }

    private boolean checkMoveRight() {
        if (Gdx.input.isKeyPressed(Input.Keys.D)) return true;
        if (gamepad != null) {
            float leftStickX = gamepad.getAxis(0);
            if (leftStickX > DEADZONE) {
                horizontalAxisStrength = abs(leftStickX);
                return true;
            }
        }

        return false;
    }

    private boolean isAttackPressed() {
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) return true;

        //X button (Xbox)
        if (gamepad != null && gamepad.getButton(2)) return true;

        return false;
    }
}