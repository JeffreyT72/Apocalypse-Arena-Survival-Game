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
        if (keyValue < -.2){
            if (MyGame.getBooster()) {
                game.getAvatar().fwdAction(game.getScriptController().getSprintSpeed() * time);
            } else {
                game.getAvatar().fwdAction(((int) game.getPlayerStats().get("spd")) * time * 0.003f);
            }

            game.callSendMoveMessage();
        }
        else if (keyValue > .2){
            if (MyGame.getBooster()) {
                game.getAvatar().bwdAction(game.getScriptController().getSprintSpeed() * time);
            } else {
                game.getAvatar().bwdAction(((int) game.getPlayerStats().get("spd")) * time * 0.003f);
            }

            game.callSendMoveMessage();
        }


        
    }
}