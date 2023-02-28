package finalProject.control;

import tage.input.action.AbstractInputAction;
import finalProject.MyGame;
// uses JInput library
import net.java.games.input.*;

public class FwdAction extends AbstractInputAction {
    private MyGame game;
    public FwdAction(MyGame g)
    {
        game = g;
    }

    @Override
    public void performAction(float time, Event e)
    {
        float keyValue = e.getValue();
        if (keyValue > -.2 && keyValue < .2) return;  // deadzone

        if (MyGame.getBooster()) {
            game.getAvatar().fwdAction(0.06f);
        } else {
            game.getAvatar().fwdAction(.003f * time);
        }
    }
}