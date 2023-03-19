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
    private ProtocolClient protClient;

    public FwdAction(MyGame g, ProtocolClient p) {
        game = g;
        protClient = p;
    }

    @Override
    public void performAction(float time, Event e) {
        float keyValue = e.getValue();
        if (keyValue > -.2 && keyValue < .2)
            return; // deadzone

        if (MyGame.getBooster()) {
            game.getAvatar().fwdAction(game.getScriptController().getSprintSpeed() * time);
        } else {
            game.getAvatar().fwdAction(game.getScriptController().getBaseSpeed() * time);
        }

        game.callSendMoveMessage();
    }
}