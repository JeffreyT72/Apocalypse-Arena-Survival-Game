package finalProject.control;

import tage.input.action.AbstractInputAction;
import finalProject.MyGame;
// uses JInput library
import net.java.games.input.*;

public class TeleportAction extends AbstractInputAction {
    private MyGame game;
    public TeleportAction(MyGame g)
    {
        game = g;
    }

    @Override
    public void performAction(float time, Event e)
    {
        float keyValue = e.getValue();
        if (keyValue > -.2 && keyValue < .2) return;  // deadzone

        
        if(!game.getInTeleportCooldown()){
            game.getAvatar().fwdAction(game.getScriptController().getTeleportDistance() * time);
            game.startTeleportCooldown();
        }
    }
}