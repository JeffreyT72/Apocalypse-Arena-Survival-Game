package finalProject.control;

import tage.input.action.AbstractInputAction;
import finalProject.MyGame;
// uses JInput library
import net.java.games.input.*;

public class ToggleLightAction extends AbstractInputAction {
    private MyGame game;
    public ToggleLightAction(MyGame g)
    {
        game = g;
    }

    @Override
    public void performAction(float time, Event e)
    {
        game.toggleLight();
    }
}