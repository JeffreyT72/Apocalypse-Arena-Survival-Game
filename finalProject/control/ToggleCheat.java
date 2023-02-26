package finalProject.control;

import tage.input.action.AbstractInputAction;

import finalProject.MyGame;
// uses JInput library
import net.java.games.input.*;

public class ToggleCheat extends AbstractInputAction {
    private MyGame game;
    public ToggleCheat(MyGame g)
    {
        game = g;
    }

    @Override
    public void performAction(float time, Event e)
    {
        if (MyGame.getCheat() == false) {
            enableRender();
            MyGame.setCheat(true);
        } else {
            disableRender();
            MyGame.setCheat(false);
        }
    }

    private void enableRender() {
        game.getp1Line().getRenderStates().enableRendering();
        game.getp2Line().getRenderStates().enableRendering();
        game.getp3Line().getRenderStates().enableRendering();
    }

    private void disableRender() {
        game.getp1Line().getRenderStates().disableRendering();
        game.getp2Line().getRenderStates().disableRendering();
        game.getp3Line().getRenderStates().disableRendering();
    }
}
