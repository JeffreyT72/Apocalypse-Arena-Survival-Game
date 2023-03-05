package finalProject.camera;
import java.lang.Math;
import java.awt.event.*;

import tage.*;
import tage.input.*;
import tage.input.action.AbstractInputAction;

import org.joml.*;

import net.java.games.input.*;

public class CameraOrbit3D {
    private Engine engine; 
    private Camera camera; // the camera being controlled 
    private GameObject avatar; // the target avatar the camera looks at 
    private float cameraAzimuth; // rotation around target Y axis 
    private float cameraElevation; // elevation of camera above target 
    private float cameraRadius; // distance between camera and target

    public CameraOrbit3D(Camera cam, GameObject av, 
       String gpName, Engine e) { 
        engine = e; 
        camera = cam; 
        avatar = av; 
        cameraAzimuth = 180.0f; // start BEHIND and ABOVE the target 
        cameraElevation = 20.0f; // elevation is in degrees 
        cameraRadius = 4.0f; // distance from camera to avatar 
        setupInputs(gpName); 
        updateCameraPosition(); 
    }

    private void setupInputs(String gp) { 
        InputManager im = engine.getInputManager(); 
        OrbitAzimuthAction azmAction = new OrbitAzimuthAction(); 
        OrbitElevationAction elevAction = new OrbitElevationAction();
        OrbiteLeftAction leftACtion = new OrbiteLeftAction();
        OrbiteRightAction rightACtion = new OrbiteRightAction();
        OrbiteUpAction upAction = new OrbiteUpAction();
        OrbiteDownAction downAction = new OrbiteDownAction();
        ZoomOutAction zoomOutAction = new ZoomOutAction();
        ZoomInAction zoomInAction = new ZoomInAction();

        im.associateActionWithAllKeyboards(
			net.java.games.input.Component.Identifier.Key.J, leftACtion,
			InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(
            net.java.games.input.Component.Identifier.Key.L, rightACtion,
            InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(
            net.java.games.input.Component.Identifier.Key.I, upAction,
            InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(
            net.java.games.input.Component.Identifier.Key.K, downAction,
            InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(
            net.java.games.input.Component.Identifier.Key.Q, zoomOutAction,
            InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(
            net.java.games.input.Component.Identifier.Key.E, zoomInAction,
            InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        im.associateActionWithAllGamepads(
            net.java.games.input.Component.Identifier.Axis.RX, azmAction,
            InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllGamepads(
            net.java.games.input.Component.Identifier.Axis.RY, elevAction,
            InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    }
    // Compute the camera azimuth, elevation, and distance, relative to 
    // the target in spherical coordinates, then convert to world Cartesian  
    // coordinates and set the camera position from that. 
    public void updateCameraPosition() { 
        // zoom restricted from cameraRadius 2-8
        if (cameraRadius < 2 ) {
            cameraRadius = 2;
            return;
        } else if (cameraRadius > 8) {
            cameraRadius = 8;
            return;
        }

        // if camera rotate with avatar rotation, 
        Vector3f avatarRot = avatar.getWorldForwardVector(); 
        double avatarAngle = Math.toDegrees((double) avatarRot.angleSigned(new Vector3f(0,0,-1), new Vector3f(0,1,0))); 
        float totalAz = cameraAzimuth - (float)avatarAngle - 180;
        double theta = Math.toRadians(totalAz);
        //double theta = Math.toRadians(cameraAzimuth);

        double phi = Math.toRadians(cameraElevation); 
        float x = cameraRadius * (float)(Math.cos(phi)* Math.sin(theta)); 
        float y = cameraRadius * (float)(Math.sin(phi)); 
        float z = cameraRadius * (float)(Math.cos(phi) * Math.cos(theta));

        if (cameraElevation < 2) {
            cameraElevation = 2;
            return;
        }
        
        camera.setLocation(new 
            Vector3f(x,y,z).add(avatar.getWorldLocation())); 
        camera.lookAt(avatar); 
    }

    // Gamepad
    private class OrbitAzimuthAction extends AbstractInputAction { 
        public void performAction(float time, Event event) { 
            float rotAmount; 
            if (event.getValue() < -0.2) { 
                rotAmount=-0.2f;  
            } else { 
                if (event.getValue() > 0.2) { 
                    rotAmount=0.2f;  
                } else { 
                    rotAmount=0.0f;  
                } 
            } 
            cameraAzimuth += rotAmount; 
            cameraAzimuth = cameraAzimuth % 360; 
            updateCameraPosition(); 
        }
    }

    private class OrbitElevationAction extends AbstractInputAction { 
        public void performAction(float time, Event event) { 
            float rotAmount; 
            if (event.getValue() < -0.2) { 
                rotAmount=-0.2f;  
            } else { 
                if (event.getValue() > 0.2) { 
                    rotAmount=0.2f;  
                } else { 
                    rotAmount=0.0f;  
                } 
            } 
            cameraElevation += rotAmount; 
            cameraElevation = cameraElevation % 360; 
            updateCameraPosition(); 
        }
    }

    // Keyboard
    private class OrbiteLeftAction extends AbstractInputAction {
        public void performAction(float time, Event event) { 
            cameraAzimuth += -0.2f; 
            cameraAzimuth = cameraAzimuth % 360; 
            updateCameraPosition(); 
        }
    }

    private class OrbiteRightAction extends AbstractInputAction {
        public void performAction(float time, Event event) { 
            cameraAzimuth += 0.2f;
            cameraAzimuth = cameraAzimuth % 360; 
            updateCameraPosition(); 
        }
    }

    private class OrbiteUpAction extends AbstractInputAction {
        public void performAction(float time, Event event) { 
            cameraElevation += 0.2f;
            cameraElevation = cameraElevation % 360; 
            updateCameraPosition(); 
        }
    }

    private class OrbiteDownAction extends AbstractInputAction {
        public void performAction(float time, Event event) { 
            cameraElevation += -0.2f;
            cameraElevation = cameraElevation % 360; 
            updateCameraPosition(); 
        }
    }

    private class ZoomOutAction extends AbstractInputAction {
        public void performAction(float time, Event event) { 
            cameraRadius += 0.05;
            updateCameraPosition(); 
        }
    }

    private class ZoomInAction extends AbstractInputAction {
        public void performAction(float time, Event event) { 
            cameraRadius -= 0.05;
            updateCameraPosition(); 
        }
    }

    // Mouse
    public void yaw(float mouseDeltaX) { 
		float yawV;

		if (mouseDeltaX < 0.0f) 
            yawV = -1.0f;
		else if (mouseDeltaX > 0.0f) 
            yawV = 1.0f;
		else 
            yawV = 0.0f;

		cameraAzimuth += 1.6 * yawV;
		cameraAzimuth = cameraAzimuth % 360; 
		updateCameraPosition();
	}

    public void pitch(float mouseDeltaY) {
        float pitchV;

        if (mouseDeltaY < 0.0f)
            pitchV = 1.0f;
        else if (mouseDeltaY > 0.0f) 
            pitchV = -1.0f;
		else 
            pitchV = 0.0f;

        cameraElevation += 1.4 * pitchV; 
        cameraElevation = cameraElevation % 360; 
        updateCameraPosition();
    }

    public void zoom(float rotate) {
        float rotateV;
        
        if (rotate < 0)
            rotateV = -1.0f;
        else if (rotate > 0)
            rotateV = 1.0f;
        else
            rotateV = 0.0f;

        cameraRadius += rotateV;
        updateCameraPosition();
    }
}
