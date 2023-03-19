package finalProject.control;

import tage.input.action.AbstractInputAction;
import finalProject.MyGame;
// uses JInput library
import net.java.games.input.*;

public class FwdBwdAction extends AbstractInputAction {
    private MyGame game;
    public FwdBwdAction(MyGame g)
    {
        game = g;
    }
    
    @Override
    public void performAction(float time, Event e)
    {
        float keyValue = e.getValue();
        if (keyValue > -.2 && keyValue < .2) return;  // deadzone

        if (keyValue <= 0) {
            if (MyGame.getBooster()) {
                game.getAvatar().fwdAction(0.06f);
            } else {
                game.getAvatar().fwdAction(.003f * time);
            }
        } else {
            if (MyGame.getBooster()) {
                game.getAvatar().bwdAction(game.getScriptController().getSprintSpeed() * time);
            } else {
                game.getAvatar().bwdAction(game.getScriptController().getBaseSpeed() * time);
            }
        }
    }
}