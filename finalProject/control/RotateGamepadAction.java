package finalProject.control;

import tage.input.action.AbstractInputAction;
import finalProject.MyGame;
// uses JInput library
import net.java.games.input.*;

public class RotateGamepadAction extends AbstractInputAction {
    private MyGame game;

    public RotateGamepadAction(MyGame g) { game = g; }

    @Override
    public void performAction(float time, Event e)
    {
        float keyValue = e.getValue();
        if (keyValue < -.2){
            game.getAvatar().yaw(.001f * time);
        }
        else if (keyValue > .2){
            game.getAvatar().yaw(-.001f * time);

            
        }
        game.callSendMoveMessage();
    }
}