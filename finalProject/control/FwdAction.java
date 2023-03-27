package finalProject.control;

import java.util.Vector;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

import finalProject.*;
// uses JInput library
import net.java.games.input.*;

public class FwdAction extends AbstractInputAction {
    private MyGame game;

    public FwdAction(MyGame g) {
        game = g;
    }

    @Override
    public void performAction(float time, Event e) {
        float keyValue = e.getValue();
        if (keyValue > -.2 && keyValue < .2)
            return; // deadzone

        if (MyGame.getBooster()) {
            game.getAvatar().fwdAction(game.getScriptController().getSprintSpeed() * time);
        } else {
            game.getAvatar().fwdAction(((int) game.getPlayerStats().get("spd")) * time * 0.003f);
        }

        game.callSendMoveMessage();
    }
}