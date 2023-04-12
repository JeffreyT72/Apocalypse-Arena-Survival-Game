package finalProject;

import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;
import org.joml.*;

import tage.*;

public class GhostManager {
	private MyGame game;
	private Vector<GhostAvatar> ghostAvatars = new Vector<GhostAvatar>();
	private GameObject gmage, garcher;
	private GhostAvatar newAvatar;
	private Matrix4f initialScale, initialTranslation;

	public GhostManager(VariableFrameRateGame vfrg) {
		game = (MyGame) vfrg;
	}

	public void createGhostAvatar(UUID id, Vector3f position, Integer ghostClass) throws IOException {
		System.out.println("adding ghost with ID --> " + id);
		
		ObjShape ds = game.getXPOrbShape();
		ObjShape ms = game.getGhostMageShape();
		ObjShape as = game.getGhostArcherShape();

		TextureImage dt = game.getXPOrbTexture();
		TextureImage mt = game.getGhostMageTexture();
		TextureImage at = game.getGhostArcherTexture();

		if (ghostClass == 1) {
			newAvatar = new GhostAvatar(id, ms, mt, position);
		} else if (ghostClass == 2) {
			newAvatar = new GhostAvatar(id, as, at, position);
		} else {
			newAvatar = new GhostAvatar(id, ds, dt, position);
		}

		initialScale = (new Matrix4f()).scaling(0.2f);
		newAvatar.setLocalScale(initialScale);
		newAvatar.getRenderStates().setModelOrientationCorrection(
				(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(-90.0f)));

		gmage = new GameObject(GameObject.root(), ms, mt);
		gmage.getRenderStates().setModelOrientationCorrection(
				(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(-90.0f)));
		initialScale = (new Matrix4f()).scaling(0.2f);
		gmage.setLocalScale(initialScale);
		gmage.setParent(newAvatar);
		gmage.propagateTranslation(true);
		gmage.propagateRotation(true);
		gmage.propagateScale(false);
		gmage.applyParentRotationToPosition(true);
		gmage.getRenderStates().disableRendering();

		garcher = new GameObject(GameObject.root(), as, at);
		garcher.getRenderStates().setModelOrientationCorrection(
				(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(-90.0f)));
		initialScale = (new Matrix4f()).scaling(0.2f);
		garcher.setLocalScale(initialScale);
		garcher.setParent(newAvatar);
		garcher.propagateTranslation(true);
		garcher.propagateRotation(true);
		garcher.propagateScale(false);
		garcher.applyParentRotationToPosition(true);
		garcher.getRenderStates().disableRendering();

		game.getgavatarOrbiter1().setParent(newAvatar);
		initialTranslation = (new Matrix4f()).translation(0, 0.3f, 0);
		game.getgavatarOrbiter1().setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scale(2.5f, 0.3f, 2.5f);
		game.getgavatarOrbiter1().setLocalScale(initialScale);
		game.getgavatarOrbiter1().propagateTranslation(true);
		game.getgavatarOrbiter1().propagateRotation(false);
		game.getgavatarOrbiter1().getRenderStates().disableRendering();

		game.getgavatarOrbiter2().setParent(newAvatar);
		initialTranslation = (new Matrix4f()).translation(0, 0.3f, 0);
		game.getgavatarOrbiter2().setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scale(2.5f, 0.3f, 2.5f);
		game.getgavatarOrbiter2().setLocalScale(initialScale);
		game.getgavatarOrbiter2().propagateTranslation(true);
		game.getgavatarOrbiter2().propagateRotation(false);
		game.getgavatarOrbiter2().getRenderStates().disableRendering();
		game.getgavatarOrbiter2().setParent(newAvatar);

		game.getgavatarOrbiter3().setParent(newAvatar);
		initialTranslation = (new Matrix4f()).translation(0, 0.3f, 0);
		game.getgavatarOrbiter3().setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scale(2.5f, 0.3f, 2.5f);
		game.getgavatarOrbiter3().setLocalScale(initialScale);
		game.getgavatarOrbiter3().propagateTranslation(true);
		game.getgavatarOrbiter3().propagateRotation(false);
		game.getgavatarOrbiter3().getRenderStates().disableRendering();

		game.getgcircle().setParent(newAvatar);
		initialTranslation = (new Matrix4f()).translation(0, -.58f, 0);
		game.getgcircle().setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scale(10f);
		game.getgcircle().setLocalScale(initialScale);
		game.getgcircle().propagateTranslation(true);
		game.getgcircle().propagateRotation(false);
		game.getgcircle().getRenderStates().disableRendering();

		ghostAvatars.add(newAvatar);
	}

	public void removeGhostAvatar(UUID id) {
		GhostAvatar ghostAvatar = findAvatar(id);
		if (ghostAvatar != null) {
			game.getEngine().getSceneGraph().removeGameObject(game.getgavatarOrbiter1());
			game.getEngine().getSceneGraph().removeGameObject(game.getgavatarOrbiter2());
			game.getEngine().getSceneGraph().removeGameObject(game.getgavatarOrbiter3());
			game.getEngine().getSceneGraph().removeGameObject(ghostAvatar);
			ghostAvatars.remove(ghostAvatar);
		} else {
			System.out.println("tried to remove, but unable to find ghost in list");
		}
	}

	private GhostAvatar findAvatar(UUID id) {
		GhostAvatar ghostAvatar;
		Iterator<GhostAvatar> it = ghostAvatars.iterator();
		while (it.hasNext()) {
			ghostAvatar = it.next();
			if (ghostAvatar.getID().compareTo(id) == 0) {
				return ghostAvatar;
			}
		}
		return null;
	}

	public void updateGhostAvatar(UUID id, Vector3f position, Vector3f orientation) {
		GhostAvatar ghostAvatar = findAvatar(id);
		if (ghostAvatar != null) {
			ghostAvatar.setPosition(position);
			ghostAvatar.setOrientation(orientation);
		} else {
			System.out.println("tried to update ghost avatar position, but unable to find ghost in list");
		}
	}

	public void updateGhostAvatarInfo(UUID id, HashMap ghostStats) {
		GhostAvatar ghostAvatar = findAvatar(id);

		if (ghostAvatar != null) {
			ghostAvatar.setInfo(ghostStats);
			avatarUpdate(ghostStats);
			skillUpdate(ghostStats);
		} else {
			System.out.println("tried to update ghost avatar position, but unable to find ghost in list");
		}
	}

	private void avatarUpdate(HashMap ghostStats) {
		int currClass = (Integer) ghostStats.get("class");
		if (currClass == 1) {
			gmage.getRenderStates().enableRendering();
			garcher.getRenderStates().disableRendering();
			newAvatar.getRenderStates().disableRendering();
		} else if (currClass == 2) {
			garcher.getRenderStates().enableRendering();
			gmage.getRenderStates().disableRendering();
			newAvatar.getRenderStates().disableRendering();
		}
	}

	private void skillUpdate(HashMap ghostStats) {
		int currAvatarOrbiterLv = (Integer) ghostStats.get("avatarOrbiterLv");
		int currCircleLv = (Integer) ghostStats.get("circleLv");
		// AvatarOrbiter
		if (currAvatarOrbiterLv == 1) {
			game.getgavatarOrbiter1().getRenderStates().enableRendering();
			game.getgavatarOrbiter2().getRenderStates().enableRendering();
		} else if (currAvatarOrbiterLv == 2) {
			game.getgavatarOrbiter1().setLocalScale(new Matrix4f().scale(3.5f, 0.3f, 3.5f));
			game.getgavatarOrbiter2().setLocalScale(new Matrix4f().scale(3.5f, 0.3f, 3.5f));
			game.getgavatarOrbiter3().setLocalScale(new Matrix4f().scale(3.5f, 0.3f, 3.5f));
		} else if (currAvatarOrbiterLv == 3) {
			game.getgavatarOrbiter3().getRenderStates().enableRendering();
		} else if (currAvatarOrbiterLv == 4) {
			game.getgavatarOrbiter1().setLocalScale(new Matrix4f().scale(4f, 0.3f, 4f));
			game.getgavatarOrbiter2().setLocalScale(new Matrix4f().scale(4f, 0.3f, 4f));
			game.getgavatarOrbiter3().setLocalScale(new Matrix4f().scale(4f, 0.3f, 4f));
		}
		// Circle
		if (currCircleLv == 1) {
			game.getgcircle().getRenderStates().enableRendering();
		} else if (currCircleLv == 2) {
			game.getgcircle().setLocalScale(new Matrix4f().scale(15f));
		} else if (currCircleLv == 3) {
			game.getgcircle().setLocalScale(new Matrix4f().scale(20f));
		}
	}
}
