package finalProject.camera;

import tage.Camera;
import tage.input.action.AbstractInputAction;
import org.joml.Vector3f;
import finalProject.MyGame;
// uses JInput library
import net.java.games.input.*;

public class SecCamPanZoomInAction extends AbstractInputAction {
    private MyGame game;
    private Camera c;
    private Vector3f camLoc;

    public SecCamPanZoomInAction(MyGame g) { 
        game = g; 
        c = (game.getEngine().getRenderSystem()).getViewport("TOPLEFT").getCamera();
    }

    @Override
    public void performAction(float time, Event e) {
        camLoc = c.getLocation();
        camLoc.add(0f, -.01f, 0f);
        // prevent too close/go below 0
        if (camLoc.y < 0.5f) { 
            camLoc.y = 0.5f;
            return;
        }
        c.setLocation(camLoc);
    }
}
