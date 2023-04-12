package finalProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import tage.*;
import tage.shapes.ImportedModel;
import tage.shapes.Torus;

import org.joml.*;

// A ghost MUST be connected as a child of the root,
// so that it will be rendered, and for future removal.
// The ObjShape and TextureImage associated with the ghost
// must have already been created during loadShapes() and
// loadTextures(), before the game loop is started.

public class GhostAvatar extends GameObject {
	UUID uuid;
	private ScriptController scriptController;
	HashMap<String, Integer> playerStats = new HashMap<String, Integer>();

	public GhostAvatar(UUID id, ObjShape s, TextureImage t, Vector3f p) {
		super(GameObject.root(), s, t);
		uuid = id;
		setPosition(p);
		// initialize game variables + scripting setup
		scriptController = new ScriptController();
		playerStats.put("class", 0);
		playerStats.put("health", scriptController.getStartingHealth());
		playerStats.put("level", scriptController.getStartingLevel());
		playerStats.put("experience", scriptController.getStartingExperience());
		playerStats.put("atk", scriptController.getAtk());
		playerStats.put("skillPoint", scriptController.getStartingSkillPoint());
		playerStats.put("fireballLv", scriptController.getfireballLv());
		playerStats.put("avatarOrbiterLv", scriptController.getAvatarOrbiterLv());
		playerStats.put("circleLv", scriptController.getCircleLv());
	}

	public UUID getID() {
		return uuid;
	}

	public void setPosition(Vector3f m) {
		setLocalLocation(m);
	}

	public Vector3f getPosition() {
		return getWorldLocation();
	}

	public void setOrientation(Vector3f m) {
		Matrix4f rotation = new Matrix4f();
		rotation.rotateZYX(m);
		setLocalRotation(rotation);
	}

	public Vector3f getOrientation() {
		Vector3f orientationEuler = new Vector3f(0f, 0f, 0f);
		(getWorldRotation()).getEulerAnglesZYX(orientationEuler);
		return orientationEuler;
	}

	public void setInfo(HashMap ghostStats) {
		playerStats.replace("class", (Integer) ghostStats.get("class"));
		playerStats.replace("health", (Integer) ghostStats.get("health"));
		playerStats.replace("level", (Integer) ghostStats.get("level"));
		playerStats.replace("atk", (Integer) ghostStats.get("atk"));
		playerStats.replace("fireballLv", (Integer) ghostStats.get("fireballLv"));
		playerStats.replace("avatarOrbiterLv", (Integer) ghostStats.get("avatarOrbiterLv"));
		playerStats.replace("circleLv", (Integer) ghostStats.get("circleLv"));

	}

	public HashMap getInfo() {
		return playerStats;
	}
}
