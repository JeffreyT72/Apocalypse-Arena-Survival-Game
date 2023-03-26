package finalProject.control;

import tage.input.action.AbstractInputAction;
import finalProject.MyGame;
// uses JInput library
import net.java.games.input.*;

public class BwdAction extends AbstractInputAction {
    private MyGame game;
    public BwdAction(MyGame g)
    {
        game = g;
    }

    @Override
    public void performAction(float time, Event e)
    {
        float keyValue = e.getValue();
        if (keyValue > -.2 && keyValue < .2) return;  // deadzone

        if (MyGame.getBooster()) {
            game.getAvatar().bwdAction(game.getScriptController().getSprintSpeed() * time);
        } else {
            game.getAvatar().bwdAction(((int) game.getPlayerStats().get("spd")) * time * 0.003f);
        }
        game.callSendMoveMessage();
    }
}