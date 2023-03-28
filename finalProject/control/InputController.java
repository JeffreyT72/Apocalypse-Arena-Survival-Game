package finalProject.control;

import finalProject.*;
import finalProject.camera.*;
import finalProject.control.*;
import tage.*;
import tage.input.*;
import tage.input.action.AbstractInputAction;

import org.joml.*;

import net.java.games.input.*;

public class InputController {
    private MyGame game;
    InputManager im;

	public InputController(MyGame game, Engine e) {
        this.game = game;
        im = e.getInputManager(); 
        inputSetup();
	}

	private void inputSetup() {
		// ----------------- INPUTS SECTION -----------------------------
		//im = e.getInputManager();
		FwdAction fwdAction = new FwdAction(game);
		BwdAction bwdAction = new BwdAction(game);
		FwdBwdAction fwdbwdAction = new FwdBwdAction(game);
		TeleportAction teleportAction = new TeleportAction(game);

		TurnRightAction turnRightAction = new TurnRightAction(game);
		TurnLeftAction turnLeftAction = new TurnLeftAction(game);
		TurnAction turnAction = new TurnAction(game);

		// SpeedUpAction speedUpAction = new SpeedUpAction(this);

		// Second Camera Control
		SecCamPanUpAction secCamPanUpAction = new SecCamPanUpAction(game);
		SecCamPanDownAction secCamPanDownAction = new SecCamPanDownAction(game);
		SecCamPanLeftAction secCamPanLeftAction = new SecCamPanLeftAction(game);
		SecCamPanRightAction secCamPanRightAction = new SecCamPanRightAction(game);
		SecCamPanZoomInAction secCamPanZoomInAction = new SecCamPanZoomInAction(game);
		SecCamPanZoomOutAction secCamPanZoomOutAction = new SecCamPanZoomOutAction(game);

		ToggleXYZ toggleXYZ = new ToggleXYZ(game);

		// Gamepad
		im.associateActionWithAllGamepads(
				net.java.games.input.Component.Identifier.Axis.Y, fwdbwdAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(
				net.java.games.input.Component.Identifier.Axis.X, turnAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		// Keyboard
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.W, fwdAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.S, bwdAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.A, turnLeftAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.D, turnRightAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.LSHIFT, teleportAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		// im.associateActionWithAllKeyboards(
		// net.java.games.input.Component.Identifier.Key.LSHIFT, speedUpAction,
		// InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);

		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.UP, secCamPanUpAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.DOWN, secCamPanDownAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.LEFT, secCamPanLeftAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.RIGHT, secCamPanRightAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.PERIOD, secCamPanZoomInAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.COMMA, secCamPanZoomOutAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.X, toggleXYZ,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	}

	public void update(float time){
		im.update(time);
	}
}
