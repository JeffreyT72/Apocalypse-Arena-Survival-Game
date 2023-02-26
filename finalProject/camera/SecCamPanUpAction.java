package finalProject.camera;

import tage.Camera;
import tage.input.action.AbstractInputAction;
import org.joml.Vector3f;
import finalProject.MyGame;
// uses JInput library
import net.java.games.input.*;

public class SecCamPanUpAction extends AbstractInputAction {
    private MyGame game;
    private Camera c;
    private Vector3f camLoc;

    public SecCamPanUpAction(MyGame g) { 
        game = g; 
        c = (game.getEngine().getRenderSystem()).getViewport("TOPLEFT").getCamera();
    }

    @Override
    public void performAction(float time, Event e) {
        camLoc = c.getLocation();
        camLoc.add(0f, 0, -.01f);
        c.setLocation(camLoc);
    }
}
