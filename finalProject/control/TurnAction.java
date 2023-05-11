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
        if (keyValue < -.2){
            if (MyGame.getBooster()) {
                game.getAvatar().leftAction(game.getScriptController().getSprintSpeed() * time);
            } else {
                game.getAvatar().leftAction(((int) game.getPlayerStats().get("spd")) * time * 0.003f);
            }
        }
        else if (keyValue > .2){
            if (MyGame.getBooster()) {
                game.getAvatar().rightAction(game.getScriptController().getSprintSpeed() * time);
            } else {
                game.getAvatar().rightAction(((int) game.getPlayerStats().get("spd")) * time * 0.003f);
            }

            
        }
        game.callSendMoveMessage();
    }
}