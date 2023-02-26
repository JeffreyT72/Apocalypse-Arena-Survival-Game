package finalProject.control;

import tage.input.action.AbstractInputAction;
import finalProject.MyGame;
// uses JInput library
import net.java.games.input.*;

public class SpeedUpAction extends AbstractInputAction {
    private MyGame game;
    public SpeedUpAction(MyGame g)
    {
        game = g;
    }

    @Override
    public void performAction(float time, Event e)
    {
        if (e.getValue() == 1)
            MyGame.setBooster(true);
        else
            MyGame.setBooster(false);
    }
}
