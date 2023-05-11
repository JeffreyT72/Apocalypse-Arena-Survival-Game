package finalProject.camera;

import tage.Camera;
import tage.input.action.AbstractInputAction;
import org.joml.Vector3f;
import finalProject.MyGame;
// uses JInput library
import net.java.games.input.*;

public class GamepadCamPan extends AbstractInputAction {
    private MyGame game;
    private Camera c;
    private Vector3f camLoc;

    public GamepadCamPan(MyGame g) { 
        game = g; 
        c = (game.getEngine().getRenderSystem()).getViewport("TOPLEFT").getCamera();
    }

    @Override
    public void performAction(float time, Event e) {
        float keyValue = e.getValue();
        if ((keyValue >= .375 && keyValue <=.625)){
            //Pan right
            camLoc = c.getLocation();
            camLoc.add(0f, -.05f, 0f);
            // prevent too close/go below 0
            if (camLoc.y < 5f) { 
                camLoc.y = 5f;
                return;
            }
            c.setLocation(camLoc);
        }
        if ((keyValue >= .875 || (keyValue <=.125 && keyValue > 0))){
            //Pan left
            camLoc = c.getLocation();
            camLoc.add(0f, .05f, 0f);
            // prevent too far/go above 8
            if (camLoc.y > 30f) { 
                camLoc.y = 30f;
                return;
            }
            c.setLocation(camLoc);
        }    


    }
}
