package finalProject.control;

import tage.input.action.AbstractInputAction;
import finalProject.MyGame;
// uses JInput library
import net.java.games.input.*;

public class AttackAction extends AbstractInputAction {
    private MyGame game;

    public AttackAction(MyGame g) { game = g; }

    @Override
    public void performAction(float time, Event e)
    {
        game.attack();
    }
}