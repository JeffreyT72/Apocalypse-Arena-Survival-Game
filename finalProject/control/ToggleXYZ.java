package finalProject.control;

import tage.input.action.AbstractInputAction;

import finalProject.MyGame;
// uses JInput library
import net.java.games.input.*;

public class ToggleXYZ extends AbstractInputAction {
    private MyGame game;
    public ToggleXYZ(MyGame g)
    {
        game = g;
        disableRender();
    }

    @Override
    public void performAction(float time, Event e)
    {
        if (MyGame.getXYZ() == false) {
            enableRender();
            //MyGame.showXYZ = true;
            game.setXYZ(true);
        } else {
            disableRender();
            //MyGame.showXYZ = false;
            game.setXYZ(false);
        }
    }

    private void enableRender() {
        game.getXLine().getRenderStates().enableRendering();
        game.getYLine().getRenderStates().enableRendering();
        game.getZLine().getRenderStates().enableRendering();
    }

    private void disableRender() {
        game.getXLine().getRenderStates().disableRendering();
        game.getYLine().getRenderStates().disableRendering();
        game.getZLine().getRenderStates().disableRendering();
    }
}
