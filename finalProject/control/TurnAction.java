package finalProject.control;

import tage.input.action.AbstractInputAction;
import finalProject.MyGame;
// uses JInput library
import net.java.games.input.*;

public class TurnAction extends AbstractInputAction {
    private MyGame game;

    public TurnAction(MyGame g) { game = g; }

    @Override
    public void performAction(float time, Event e)
    {
        float keyValue = e.getValue();
        if (keyValue > -.2 && keyValue < .2) return;  // deadzone

        if (keyValue <= 0) {
            if (MyGame.getBooster()) {
                game.getAvatar().turnLeftAction(.013f);
            } else {
                game.getAvatar().turnLeftAction(.006f);
            }
        } else {
            if (MyGame.getBooster()) {
                game.getAvatar().turnRightAction(.013f);
            } else {
                game.getAvatar().turnRightAction(.006f);
            }
        }
    }
}