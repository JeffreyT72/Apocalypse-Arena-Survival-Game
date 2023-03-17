package finalProject;

import finalProject.*;
import finalProject.camera.*;
import finalProject.control.*;
import finalProject.manualObj.*;

import tage.*;
import tage.shapes.*;
import tage.input.*;
import tage.nodeControllers.BounceController;
import tage.nodeControllers.RotationController;

import org.joml.*;

import java.io.*; 
import java.util.*; 
import java.util.Random;
import java.lang.Math;
import java.text.DecimalFormat;
import java.awt.event.*;
import java.awt.Robot;
import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.util.HashMap;

import javax.script.ScriptEngine; 
import javax.script.ScriptEngineFactory; 
import javax.script.ScriptEngineManager; 
import javax.script.ScriptException; 

import javax.script.Invocable; 

public class MyGame extends VariableFrameRateGame {
	// Engine
	private static Engine engine;

	public static Engine getEngine() {
		return engine;
	}

	// Input variables
	private InputManager im;
	private Robot robot;
	private float curMouseX, curMouseY, centerX, centerY;
	private float prevMouseX, prevMouseY; // loc of mouse prior to move
	private boolean isRecentering; // indicates the Robot is in action
	private boolean isRightClick = false;

	// Camera and Viewport variables
	private CameraOrbit3D orbitController;
	final private String mainVpName = "MAIN";
	final private String subVpName = "TOPLEFT";

	// Time variables
	private double lastFrameTime, currFrameTime, elapsTime, displayTime;
	private double tenSec;
	private boolean skyboxCycleTime = true;

	private Random rand = new Random();
	private GameObject avatar, x, y, z, rocket;
	private GameObject soup;
	private GameObject mage;

	private ObjShape dolS, prizeS, linxS, linyS, linzS, rocketS;
	private ObjShape soupS;
	private AnimatedShape myRobAS;
	//private ObjShape mageS;
	private AnimatedShape mageAS;

	private TextureImage doltx, prizeT, rocketT;
	private TextureImage planeT;
	private TextureImage soupT;
	private TextureImage mageT;

	private int darkSky, daySky;

	private Light light1;

	private Plane planeS;

	private NodeController rc, mc;

	private File scriptFile1;
	private ScriptEngine jsEngine;
	
	// ---------- Game Variables ----------
	private static boolean showXYZ;
	private static boolean cheat;
	private static boolean booster;
	private boolean isCarryPShown, isBooster, isConsumed;
	private boolean winFlag;
	private int scoreCounter;
	private HashMap<String, Integer> playerStats;

	private ScriptController scriptController;

	public MyGame() {
		super();
	}

	public static void main(String[] args) {
		MyGame game = new MyGame();
		engine = new Engine(game);
		game.initializeSystem();
		game.game_loop();
	}

	@Override
	public void loadShapes() {
		dolS = new ImportedModel("dolphinHighPoly.obj");
		prizeS = new Sphere();
		rocketS = new Rocket();
		planeS = new Plane();

		linxS = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(3f, 0f, 0f));
		linyS = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 3f, 0f));
		linzS = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, 3f));

		soupS = new ImportedModel("soup.obj");
		//mageS = new ImportedModel("mage.obj");
		
		myRobAS = new AnimatedShape("myRobot.rkm", "myRobot.rks");
		myRobAS.loadAnimation("WAVE", "myRobot_wave.rka");

		mageAS = new AnimatedShape("mage.rkm", "mage.rks");
		mageAS.loadAnimation("MOVE", "mage_move.rka");
		mageAS.loadAnimation("ATTACK", "mage_attack.rka");
		mageAS.loadAnimation("MOVEATTACK", "mage_moveattack.rka");
	}

	@Override
	public void loadTextures() {
		doltx = new TextureImage("Dolphin_HighPolyUV.png");
		//prizeT = new TextureImage("ballTextures.png");
		rocketT = new TextureImage("myTextures.png");
		planeT = new TextureImage("sea.png");
		soupT = new TextureImage("soup.jpg");
		//myRoboT = new TextureImage("myRobot.jpg");
		mageT = new TextureImage("mage.png");
	}

	@Override
	public void buildObjects() {
		Matrix4f initialTranslation, initialScale;

		GameObject plane = new GameObject(GameObject.root(), planeS, planeT);
		initialTranslation = (new Matrix4f()).translation(0f, 0f, 0f);
		plane.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(50f);
		plane.setLocalScale(initialScale);

		// build manual object - rocket
		rocket = new GameObject(GameObject.root(), rocketS, rocketT);
		initialTranslation = (new Matrix4f()).translation(31, 3, 31);
		rocket.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(0.5f);
		rocket.setLocalScale(initialScale);

		// add X,Y,-Z axes
		x = new GameObject(GameObject.root(), linxS);
		y = new GameObject(GameObject.root(), linyS);
		z = new GameObject(GameObject.root(), linzS);
		(x.getRenderStates()).setColor(new Vector3f(1f, 0f, 0f));
		(y.getRenderStates()).setColor(new Vector3f(0f, 1f, 0f));
		(z.getRenderStates()).setColor(new Vector3f(0f, 0f, 1f));

		// build soup
		soup = new GameObject(GameObject.root(), soupS, soupT);
		initialTranslation = (new Matrix4f()).translation(randNum(), 1f, randNum());
		soup.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(0.2f);
		soup.setLocalScale(initialScale);

		mage = new GameObject(GameObject.root(), mageAS, mageT);
		initialTranslation = (new Matrix4f()).translation(0f, 1f, 0f);
		mage.setLocalTranslation(initialTranslation);
		mage.getRenderStates().setModelOrientationCorrection(
			(new Matrix4f()).rotationY((float)java.lang.Math.toRadians(-90.0f)));
		initialScale = (new Matrix4f()).scaling(0.2f);
		mage.setLocalScale(initialScale);

		//Sets the current playable character to mage
		avatar = mage;
	}

	@Override
	public void initializeLights() {
		Light.setGlobalAmbient(0.5f, 0.5f, 0.5f);
		light1 = new Light();
		light1.setLocation(new Vector3f(5.0f, 4.0f, 2.0f));
		(engine.getSceneGraph()).addLight(light1);
	}

	@Override
	public void createViewports() {
		(engine.getRenderSystem()).addViewport(mainVpName, 0, 0, 1f, 1f);
		(engine.getRenderSystem()).addViewport(subVpName, 0.75f, 0.75f, .25f, .25f);
		(engine.getRenderSystem()).addViewport("PLAYERVP", 0f, 0.75f, .25f, .25f);

		Viewport mainVp = (engine.getRenderSystem()).getViewport(mainVpName);
		Viewport topleftVp = (engine.getRenderSystem()).getViewport(subVpName);
		Viewport playerVp = (engine.getRenderSystem()).getViewport("PLAYERVP");

		Camera mainCamera = mainVp.getCamera();
		Camera topleftCamera = topleftVp.getCamera();


		topleftVp.setHasBorder(true);
		topleftVp.setBorderWidth(4);
		topleftVp.setBorderColor(0.431f, 0.149f, 0.054f);

		playerVp.setHasBorder(true);
		playerVp.setBorderWidth(4);
		playerVp.setBorderColor(0.431f, 0.149f, 0.054f);

		mainCamera.setLocation(new Vector3f(-2, 0, 2));
		mainCamera.setU(new Vector3f(1, 0, 0));
		mainCamera.setV(new Vector3f(0, 1, 0));
		mainCamera.setN(new Vector3f(0, 0, -1));

		topleftCamera.setLocation(new Vector3f(0, 10, 0));
		topleftCamera.setU(new Vector3f(1, 0, 0));
		topleftCamera.setV(new Vector3f(0, 0, -1));
		topleftCamera.setN(new Vector3f(0, -1, 0));
	}

	@Override
	public void loadSkyBoxes() { 
		darkSky = engine.getSceneGraph().loadCubeMap("darkSky");
		daySky = engine.getSceneGraph().loadCubeMap("fluffyClouds");
		(engine.getSceneGraph()).setActiveSkyBoxTexture(daySky);
		engine.getSceneGraph().setSkyBoxEnabled(true);
	}

	@Override
	public void initializeGame() {
		(engine.getRenderSystem()).setWindowDimensions(1920, 1080);
		// initialize time
		initTime();

		initNodeControl();
		// initialize game variables + scripting setup
		scriptController = new ScriptController();
		initGameVar();
		// initialize objects
		initObj();

		initMouseMode();
		// ----------------- initialize camera ----------------
		initCamera();

		// keyboard and gamepad input manager setup
		inputSetup();

		
	}

	@Override
	public void update() {
		updateTime();

		updateGameLogic();
		// update inputs
		im.update((float) elapsTime);

		myRobAS.updateAnimation();
		mageAS.updateAnimation();

		// update camera
		orbitController.updateCameraPosition();
		updateMapCamPosition();
		updatePlayerCamPosition();

		updateHUD();
	}

	private void updateMapCamPosition() {
		Camera c = (engine.getRenderSystem())
		.getViewport(subVpName).getCamera();
		c.setLocation(new Vector3f(avatar.getWorldLocation().x(), c.getLocation().y(), avatar.getWorldLocation().z()));
	}

	private void updatePlayerCamPosition() {
		Camera c = (engine.getRenderSystem()).getViewport("PLAYERVP").getCamera();
		c.setLocation(new Vector3f(avatar.getWorldLocation().x()+1, avatar.getWorldLocation().y()+1, avatar.getWorldLocation().z()+1));
		c.lookAt(avatar);
	}

	private void initCamera() {
		im = engine.getInputManager();
		String gpName = im.getFirstGamepadName();
		Camera c = (engine.getRenderSystem())
				.getViewport(mainVpName).getCamera();
		orbitController = new CameraOrbit3D(
				c, avatar, gpName, engine);
	}

	private void initTime() {
		lastFrameTime = System.currentTimeMillis();
		currFrameTime = System.currentTimeMillis();
		elapsTime = 0.0;
	}

	private void initNodeControl() {
		float min = 0.003f;
		float max = 0.008f;
		rc = new RotationController(engine, new Vector3f(0, 1, 0), rand.nextFloat() * (max - min) + min);
		rc.addTarget(soup);
		(engine.getSceneGraph()).addNodeController(rc);
		rc.enable();

		mc = new BounceController(engine);
		mc.addTarget(rocket);
		mc.addTarget(soup);
		(engine.getSceneGraph()).addNodeController(mc);
		mc.enable();
	}

	private void initGameVar() {
		playerStats = new HashMap<String, Integer>();
		playerStats.put("health", scriptController.getStartingHealth());
		playerStats.put("level", scriptController.getStartingLevel());
		playerStats.put("experience", scriptController.getStartingExperience());
		playerStats.put("atk", scriptController.getAtk());
		//Older Variables. May or may not be needed
		scoreCounter = 0;
		cheat = false;
		showXYZ = true;
		booster = false;
		winFlag = false;
		isCarryPShown = false;
		isConsumed = false;
		isBooster = false;
	}

	private void initObj() {
		x.getRenderStates().enableRendering();
		y.getRenderStates().enableRendering();
		z.getRenderStates().enableRendering();
		rocket.getRenderStates().setRenderHiddenFaces(true);
	}

	private void initMouseMode() {
		RenderSystem rs = engine.getRenderSystem();
		Viewport vw = rs.getViewport(mainVpName);
		float left = vw.getActualLeft();
		float bottom = vw.getActualBottom();
		float width = vw.getActualWidth();
		float height = vw.getActualHeight();
		centerX = (int) (left + width / 2);
		centerY = (int) (bottom - height / 2);
		isRecentering = false;

		// note that some platforms may not support the Robot class
		try {
			robot = new Robot();
		} catch (AWTException ex) {
			throw new RuntimeException("Couldn't create Robot!");
		}

		recenterMouse();
		prevMouseX = centerX; // 'prevMouse' defines the initial
		prevMouseY = centerY; // mouse position
		// also change the cursor
		Toolkit tk = Toolkit.getDefaultToolkit();
		Cursor invisibleCursor = tk.createCustomCursor(tk.getImage(""),
				new Point(0, 0), "InvisibleCursor");
		rs.getGLCanvas().setCursor(invisibleCursor);
	}

	private void recenterMouse() {
		// use the robot to move the mouse to the center point.
		// Note that this generates one MouseEvent.
		RenderSystem rs = engine.getRenderSystem();
		Viewport vw = rs.getViewport(mainVpName);
		float left = vw.getActualLeft();
		float bottom = vw.getActualBottom();
		float width = vw.getActualWidth();
		float height = vw.getActualHeight();
		int centerX = (int) (left + width / 2.0f);
		int centerY = (int) (bottom - height / 2.0f);
		isRecentering = true;
		robot.mouseMove((int) centerX, (int) centerY);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// if robot is recentering and the MouseEvent location is in the center,
		// then this event was generated by the robot
		if (isRecentering && centerX == e.getXOnScreen() && centerY == e.getYOnScreen()) {
			// mouse recentered, recentering complete
			isRecentering = false;
		} else { // event was due to a user mouse-move, and must be processed
			curMouseX = e.getXOnScreen();
			curMouseY = e.getYOnScreen();
			float mouseDeltaX = prevMouseX - curMouseX;
			float mouseDeltaY = prevMouseY - curMouseY;

			//if (isRightClick) {
				avatar.yaw(mouseDeltaX/50);
				orbitController.pitch(mouseDeltaY);
			//}

			prevMouseX = curMouseX;
			prevMouseY = curMouseY;
			// tell robot to put the cursor to the center (since user just moved it)
			recenterMouse();
			RenderSystem rs = engine.getRenderSystem();
			Viewport vw = rs.getViewport(mainVpName);
			float left = vw.getActualLeft();
			float bottom = vw.getActualBottom();
			float width = vw.getActualWidth();
			float height = vw.getActualHeight();
			int centerX = (int) (left + width / 2.0f);
			int centerY = (int) (bottom - height / 2.0f);
			prevMouseX = centerX; // reset prev to center
			prevMouseY = centerY;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		orbitController.zoom(e.getWheelRotation());
	}

	private void updateTime() {
		lastFrameTime = currFrameTime;
		currFrameTime = System.currentTimeMillis();
		elapsTime = currFrameTime - lastFrameTime;
		displayTime += (currFrameTime - lastFrameTime) / 1000;
	}

	private void updateGameLogic() {
		updateSkyboxes();
		checkTouchSoup();
	}

	private void updateSkyboxes() {
		if (skyboxCycleTime) {
			(engine.getSceneGraph()).setActiveSkyBoxTexture(daySky);
			engine.getSceneGraph().setSkyBoxEnabled(true);
		} else {
			(engine.getSceneGraph()).setActiveSkyBoxTexture(darkSky);
			engine.getSceneGraph().setSkyBoxEnabled(true);
		}
		// Update the flag every 30 seconds
		if (displayTime % 60 < 30) {
			skyboxCycleTime = true;
		} else {
			skyboxCycleTime = false;
		}
	}

	private void checkTouchSoup() {
		Vector3f avloc, souploc;
		float avrocDis;
		float avsize;
		float soupsize;
		int elapsTimeSec = Math.round((float)displayTime);

		avloc = avatar.getWorldLocation();
		avsize = (avatar.getWorldScale()).m00();

		souploc = soup.getWorldLocation();
		soupsize = (soup.getWorldScale()).m00();
		avrocDis = avloc.distance(souploc);

		if (avrocDis - avsize - soupsize <= 0 && !isConsumed) {
			isBooster = true;
			//rc.enable(); //If this is enabled, it crashed the game for some reason if the avatar collides with it.
			soup.getRenderStates().disableRendering();
			soup.setLocalTranslation((new Matrix4f()).translation(randNum(), 1f, randNum()));
			isConsumed = true;
		}

		if (isBooster) {
			tenSec = elapsTimeSec+10;
		}

		if (elapsTimeSec <= tenSec) {
			booster = true;
			isBooster = false;
		} else {
			booster = false;
			isConsumed = false;
			//rc.disable();
			soup.getRenderStates().enableRendering();
		}
	}

	private void updateHUD() {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(3);
		RenderSystem rs = engine.getRenderSystem();
		Viewport mainVp = rs.getViewport(mainVpName);
		Viewport playerVP = rs.getViewport("PLAYERVP");
		Viewport smallVp = rs.getViewport(subVpName);

		Vector3f avLoc = getAvatar().getWorldLocation();
		float avLocX = avLoc.x();
		float avLocY = avLoc.y();
		float avLocZ = avLoc.z();

		// build and set HUD
		int elapsTimeSec = Math.round((float) displayTime);
		String elapsTimeStr = Integer.toString(elapsTimeSec);
		String counterStr = Integer.toString(scoreCounter);
		String dispStr1 = "Time: " + elapsTimeStr;
		String dispStr2 = "Score: " + counterStr;
		String dispStr3 = "HP: " + playerStats.get("health");
		String dispStr4 = "Level: " + playerStats.get("level");
		String dispStr5 = "XP: " + playerStats.get("experience");
		// String win = "You Win!";
		// String lose = "You Lose!";
		Vector3f hud1Color = new Vector3f(1, 0, 1);
		Vector3f hud2Color = new Vector3f(0, 0, 1);
		Vector3f hud3Color = new Vector3f(1, 0, 0);
		Vector3f hud4Color = new Vector3f(1, 0, 0);
		Vector3f hud5Color = new Vector3f(1, 0, 0);
		int timeStrX = (int) (rs.getWidth() * mainVp.getRelativeWidth() /2 - 40);
		int timeStrY = (int) (rs.getHeight() * mainVp.getRelativeHeight() - 80);
		(engine.getHUDmanager()).setHUD1(dispStr1, hud1Color, timeStrX, timeStrY);

		int scoreStrX = (int) (rs.getWidth() * mainVp.getRelativeWidth() /2 - 40);
		int scoreStrY = (int) (rs.getHeight() * mainVp.getRelativeBottom() + 80);
		(engine.getHUDmanager()).setHUD2(dispStr2, hud2Color, scoreStrX, scoreStrY);

		int StrX = (int) (rs.getWidth() * playerVP.getRelativeWidth() + 10);
		int StrY = (int) (rs.getHeight() * playerVP.getRelativeBottom() + 180);
		(engine.getHUDmanager()).setHUD3(dispStr3, hud3Color, StrX, StrY);

		StrX = (int) (rs.getWidth() * playerVP.getRelativeWidth() + 10);
		StrY = (int) (rs.getHeight() * playerVP.getRelativeBottom() + 140);
		(engine.getHUDmanager()).setHUD4(dispStr4, hud4Color, StrX, StrY);

		StrX = (int) (rs.getWidth() * playerVP.getRelativeWidth() + 10);
		StrY = (int) (rs.getHeight() * playerVP.getRelativeBottom() + 100);
		(engine.getHUDmanager()).setHUD5(dispStr5, hud5Color, StrX, StrY);

		
	}

	private void inputSetup() {
		// ----------------- INPUTS SECTION -----------------------------
		im = engine.getInputManager();
		FwdAction fwdAction = new FwdAction(this);
		BwdAction bwdAction = new BwdAction(this);
		FwdBwdAction fwdbwdAction = new FwdBwdAction(this);
		TeleportAction teleportAction = new TeleportAction(this);

		TurnRightAction turnRightAction = new TurnRightAction(this);
		TurnLeftAction turnLeftAction = new TurnLeftAction(this);
		TurnAction turnAction = new TurnAction(this);

		SpeedUpAction speedUpAction = new SpeedUpAction(this);

		// Second Camera Control
		SecCamPanUpAction secCamPanUpAction = new SecCamPanUpAction(this);
		SecCamPanDownAction secCamPanDownAction = new SecCamPanDownAction(this);
		SecCamPanLeftAction secCamPanLeftAction = new SecCamPanLeftAction(this);
		SecCamPanRightAction secCamPanRightAction = new SecCamPanRightAction(this);
		SecCamPanZoomInAction secCamPanZoomInAction = new SecCamPanZoomInAction(this);
		SecCamPanZoomOutAction secCamPanZoomOutAction = new SecCamPanZoomOutAction(this);

		ToggleXYZ toggleXYZ = new ToggleXYZ(this);

		// Gamepad
		im.associateActionWithAllGamepads(
				net.java.games.input.Component.Identifier.Axis.Y, fwdbwdAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(
				net.java.games.input.Component.Identifier.Axis.X, turnAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		// Keyboard
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.W, fwdAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.S, bwdAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.A, turnLeftAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.D, turnRightAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.LSHIFT, teleportAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		// im.associateActionWithAllKeyboards(
		// 		net.java.games.input.Component.Identifier.Key.LSHIFT, speedUpAction,
		// 		InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);

		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.UP, secCamPanUpAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.DOWN, secCamPanDownAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.LEFT, secCamPanLeftAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.RIGHT, secCamPanRightAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.PERIOD, secCamPanZoomInAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.COMMA, secCamPanZoomOutAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.X, toggleXYZ,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	}

	public GameObject getAvatar() {
		return avatar;
	}

	public GameObject getXLine() {
		return x;
	}

	public GameObject getYLine() {
		return y;
	}

	public GameObject getZLine() {
		return z;
	}
	public static boolean getXYZ() {
		return showXYZ;
	}

	public static void setXYZ(boolean showXYZ) {
		MyGame.showXYZ = showXYZ;
	}

	public static boolean getBooster() {
		return booster;
	}

	public static void setBooster(boolean booster) {
		MyGame.booster = booster;
	}

	// Keyboard can use Esc and = key
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode())
		{ case KeyEvent.VK_G:
		{ mageAS.stopAnimation();
			mageAS.playAnimation("MOVE", 0.5f,
		AnimatedShape.EndType.LOOP, 0);
		break; }
		case KeyEvent.VK_H:
		{ mageAS.stopAnimation();
			mageAS.playAnimation("ATTACK", 0.5f,
		AnimatedShape.EndType.LOOP, 0);
		break;
		}
		case KeyEvent.VK_B:
		{ mageAS.stopAnimation();
			mageAS.playAnimation("MOVEATTACK", 0.5f,
		AnimatedShape.EndType.LOOP, 0);
		break;
		}
		}
		super.keyPressed(e);
	}

	public ScriptController getScriptController(){
		return this.scriptController;
	}

	// private Matrix4f randLoc() {
	// 	return (new Matrix4f()).translation(randNum(), 0, randNum());
	// }

	// return a number between -7 to 7 but not -2 to 2 (to close to origin)
	private int randNum() {
		int max = 7;
		int min = -7;
		int num = (rand.nextInt(max - min) + min);
		if (num <= 2 && num >= -2)
			randNum();
		return num;
	}

	// // return a matrix that the scaling at least 1
	// private Matrix4f randSize() {
	// 	return (new Matrix4f()).scaling(1 + rand.nextInt(1));
	// }

	// private Matrix4f randRotate() {
	// 	return (new Matrix4f()).rotate(rand.nextInt(360), 0, 0, 0);
	// }
}