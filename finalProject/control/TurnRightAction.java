package finalProject.control;

import tage.input.action.AbstractInputAction;
import finalProject.MyGame;
// uses JInput library
import net.java.games.input.*;

public class TurnRightAction extends AbstractInputAction {
    private MyGame game;
    public TurnRightAction(MyGame g) { game = g; }

    @Override
    public void performAction(float time, Event e)
    {
        float keyValue = e.getValue();
        if (keyValue > -.2 && keyValue < .2) return;  // deadzone

        if (MyGame.getBooster()) {
            game.getAvatar().rightAction(.013f);
        } else {
            game.getAvatar().rightAction(.003f * time);
        }
    }
}