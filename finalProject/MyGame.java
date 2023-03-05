package finalProject;

import finalProject.camera.*;
import finalProject.control.*;
import finalProject.manualObj.*;

import tage.*;
import tage.shapes.*;
import tage.input.*;
import tage.nodeControllers.BounceController;
import tage.nodeControllers.RotationController;

import java.lang.Math;
import java.text.DecimalFormat;
import java.util.Random;
import java.awt.event.*;
import java.awt.Robot;
import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import org.joml.*;
import java.awt.Color;

import javax.script.ScriptEngine; 
import javax.script.ScriptEngineFactory; 
import javax.script.ScriptEngineManager; 
import javax.script.ScriptException; 
import java.io.*; 
import java.util.*; 
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

	private Random rand = new Random();
	private GameObject avatar, x, y, z, rocket;
	//private GameObject prize1, prize2, prize3;
	//private GameObject p1Line, p2Line, p3Line;
	//private GameObject carryp1, carryp2, carryp3;
	private GameObject soup, myRobot;
	private GameObject mage;

	private ObjShape dolS, prizeS, linxS, linyS, linzS, rocketS;
	//private ObjShape p1LineS, p2LineS, p3LineS;
	private ObjShape soupS;
	private AnimatedShape myRobAS;
	private ObjShape mageS;
	private AnimatedShape mageAS;

	private TextureImage doltx, prizeT, rocketT;
	private TextureImage planeT;
	private TextureImage soupT, myRoboT;
	private TextureImage mageT;

	private Light light1;

	private Plane planeS;

	private NodeController rc, mc;

	private File scriptFile1;
	private ScriptEngine jsEngine;
	
	// public static boolean ride;
	private static boolean showXYZ;
	private static boolean cheat;
	private static boolean booster;
	private boolean isCarryPShown, isBooster, isConsumed;
	//private boolean p1collect, p2collect, p3collect;
	private boolean winFlag;
	private int scoreCounter;

	//Game Parameters
	private float baseSpeed;
	private float sprintSpeed;

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

		// p1LineS = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, 1f));
		// p2LineS = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, 1f));
		// p3LineS = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, 1f));

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
		myRoboT = new TextureImage("myRobot.jpg");
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

		// build 3 prizes in random places
		//buildPrizes();
		// build 3 carry prizes behind the avatar
		//buildCarryPrizes();

		// add X,Y,-Z axes
		x = new GameObject(GameObject.root(), linxS);
		y = new GameObject(GameObject.root(), linyS);
		z = new GameObject(GameObject.root(), linzS);
		(x.getRenderStates()).setColor(new Vector3f(1f, 0f, 0f));
		(y.getRenderStates()).setColor(new Vector3f(0f, 1f, 0f));
		(z.getRenderStates()).setColor(new Vector3f(0f, 0f, 1f));

		// cheat lines
		/*
		p1Line = new GameObject(GameObject.root(), p1LineS);
		p2Line = new GameObject(GameObject.root(), p2LineS);
		p3Line = new GameObject(GameObject.root(), p3LineS);
		(p1Line.getRenderStates()).setColor(new Vector3f(1f, 1f, 0f));
		(p2Line.getRenderStates()).setColor(new Vector3f(1f, 1f, 0f));
		(p3Line.getRenderStates()).setColor(new Vector3f(1f, 1f, 0f));
		*/

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

		//build myRobot
		/*
		myRobot = new GameObject(GameObject.root(), myRobAS, myRoboT);
		initialTranslation = (new Matrix4f()).translation(0, 0.07f, 0.28f);
		myRobot.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(0.1f);
		myRobot.setLocalScale(initialScale);
		myRobot.getRenderStates().setModelOrientationCorrection(
			(new Matrix4f()).rotationY((float)java.lang.Math.toRadians(90.0f)));

		myRobot.setParent(avatar);
		myRobot.propagateTranslation(true);
		myRobot.propagateRotation(true);
		myRobot.applyParentRotationToPosition(true);
		*/
	}
	/*
	private void buildCarryPrizes() {
		carryp1 = new GameObject(GameObject.root(), prizeS, prizeT);
		carryp1.setLocalTranslation((new Matrix4f()).translation(0f, 0f, -1f));
		carryp1.setLocalScale((new Matrix4f()).scaling(0.1f));
		carryp1.setParent(avatar);
		carryp1.propagateTranslation(true);
		carryp1.propagateRotation(true);
		carryp1.applyParentRotationToPosition(true);
		carryp1.getRenderStates().disableRendering();

		carryp2 = new GameObject(GameObject.root(), prizeS, prizeT);
		carryp2.setLocalTranslation((new Matrix4f()).translation(0f, 0f, -1.5f));
		carryp2.setLocalScale((new Matrix4f()).scaling(0.1f));
		carryp2.setParent(avatar);
		carryp2.propagateTranslation(true);
		carryp2.propagateRotation(true);
		carryp2.applyParentRotationToPosition(true);
		carryp2.getRenderStates().disableRendering();

		carryp3 = new GameObject(GameObject.root(), prizeS, prizeT);
		carryp3.setLocalTranslation((new Matrix4f()).translation(0f, 0f, -2f));
		carryp3.setLocalScale((new Matrix4f()).scaling(0.1f));
		carryp3.setParent(avatar);
		carryp3.propagateTranslation(true);
		carryp3.propagateRotation(true);
		carryp3.applyParentRotationToPosition(true);
		carryp3.getRenderStates().disableRendering();
	}
	*/
	/*
	private void buildPrizes() {
		prize1 = new GameObject(GameObject.root(), prizeS, prizeT);
		prize1.setLocalTranslation(randLoc());
		prize1.setLocalScale(randSize());
		prize1.setLocalRotation(randRotate());

		prize2 = new GameObject(GameObject.root(), prizeS, prizeT);
		prize2.setLocalTranslation(randLoc());
		prize2.setLocalScale(randSize());
		prize2.setLocalRotation(randRotate());

		prize3 = new GameObject(GameObject.root(), prizeS, prizeT);
		prize3.setLocalTranslation(randLoc());
		prize3.setLocalScale(randSize());
		prize3.setLocalRotation(randRotate());
	}
	*/

	private Matrix4f randLoc() {
		return (new Matrix4f()).translation(randNum(), 0, randNum());
	}

	// return a number between -7 to 7 but not -2 to 2 (to close to origin)
	private int randNum() {
		int max = 7;
		int min = -7;
		int num = (rand.nextInt(max - min) + min);
		if (num <= 2 && num >= -2)
			randNum();
		return num;
	}

	// return a matrix that the scaling at least 1
	private Matrix4f randSize() {
		return (new Matrix4f()).scaling(1 + rand.nextInt(1));
	}

	private Matrix4f randRotate() {
		return (new Matrix4f()).rotate(rand.nextInt(360), 0, 0, 0);
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
		(engine.getRenderSystem()).addViewport(subVpName, 0.75f, 0f, .25f, .25f);

		Viewport mainVp = (engine.getRenderSystem()).getViewport(mainVpName);
		Viewport topleftVp = (engine.getRenderSystem()).getViewport(subVpName);

		Camera mainCamera = mainVp.getCamera();
		Camera topleftCamera = topleftVp.getCamera();

		topleftVp.setHasBorder(true);
		topleftVp.setBorderWidth(4);
		topleftVp.setBorderColor(0.431f, 0.149f, 0.054f);

		mainCamera.setLocation(new Vector3f(-2, 0, 2));
		mainCamera.setU(new Vector3f(1, 0, 0));
		mainCamera.setV(new Vector3f(0, 1, 0));
		mainCamera.setN(new Vector3f(0, 0, -1));

		topleftCamera.setLocation(new Vector3f(0, 2, 0));
		topleftCamera.setU(new Vector3f(1, 0, 0));
		topleftCamera.setV(new Vector3f(0, 0, -1));
		topleftCamera.setN(new Vector3f(0, -1, 0));
	}

	@Override
	public void loadSkyBoxes() { 
		int lake = engine.getSceneGraph().loadCubeMap("darkSky");
		(engine.getSceneGraph()).setActiveSkyBoxTexture(lake);
		engine.getSceneGraph().setSkyBoxEnabled(true);
	}

	@Override
	public void initializeGame() {
		(engine.getRenderSystem()).setWindowDimensions(1920, 1080);
		// initialize time
		initTime();

		initNodeControl();
		// initialize game variables
		initGameVar();
		// initialize objects
		initObj();

		initMouseMode();
		// ----------------- initialize camera ----------------
		initCamera();

		// keyboard and gamepad input manager setup
		inputSetup();

		//Parameter Setup
		scriptingSetup();
		baseSpeed = ((Double)(jsEngine.get("baseSpeed"))).floatValue();
		sprintSpeed = ((Double)(jsEngine.get("sprintSpeed"))).floatValue();
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
		rc.disable();

		mc = new BounceController(engine);
		mc.addTarget(rocket);
		mc.addTarget(soup);
		(engine.getSceneGraph()).addNodeController(mc);
		mc.enable();
	}

	private void initGameVar() {
		scoreCounter = 0;
		cheat = false;
		showXYZ = true;
		booster = false;
		// p1collect = false;
		// p2collect = false;
		// p3collect = false;
		winFlag = false;
		isCarryPShown = false;
		isConsumed = false;
		isBooster = false;
	}

	private void initObj() {
		x.getRenderStates().enableRendering();
		y.getRenderStates().enableRendering();
		z.getRenderStates().enableRendering();

		// p1Line.getRenderStates().disableRendering();
		// p2Line.getRenderStates().disableRendering();
		// p3Line.getRenderStates().disableRendering();

		rocket.getRenderStates().setRenderHiddenFaces(true);

		// myRobAS.stopAnimation();
		// myRobAS.playAnimation("WAVE", 0.5f,
		// 	AnimatedShape.EndType.LOOP, 0);
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

	// @Override
	// public void mousePressed(MouseEvent e) {
	// 	if (e.getButton() == MouseEvent.BUTTON3)
	// 		isRightClick = true;
	// }

	// @Override
	// public void mouseReleased(MouseEvent e) {
	// 	if (e.getButton() == MouseEvent.BUTTON3)
	// 		isRightClick = false;
	// }

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		orbitController.zoom(e.getWheelRotation());
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

		updateHUD();
	}

	private void updateTime() {
		lastFrameTime = currFrameTime;
		currFrameTime = System.currentTimeMillis();
		elapsTime = currFrameTime - lastFrameTime;
		displayTime += (currFrameTime - lastFrameTime) / 1000;
	}

	private void updateGameLogic() {
		//checkCollectPrize();
		//checkCarryPrize();
		checkTouchSoup();
		//checkTouchRocket();

		// update when cheat is on, reduce computation
		// if (cheat)
		// 	updateCheatLine();
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
			rc.enable(); //If this is enabled, it crashed the game for some reason if the avatar collides with it.
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
			rc.disable();
			soup.getRenderStates().enableRendering();
		}
	}

	/*
	private void checkCarryPrize() {
		if (isCarryPShown) {
			if (carryp1.getRenderStates().renderingEnabled() == false)
				carryp1.getRenderStates().enableRendering();
			else if (carryp2.getRenderStates().renderingEnabled() == false)
				carryp2.getRenderStates().enableRendering();
			else if (carryp3.getRenderStates().renderingEnabled() == false)
				carryp3.getRenderStates().enableRendering();
			isCarryPShown = false;
		}
	}

	private void updateCheatLine() {
		float p1avDis, p2avDis, p3avDis;

		p1avDis = (p1Line.getWorldLocation()).distance(avatar.getWorldLocation());
		p1Line.setLocalLocation(prize1.getWorldLocation());
		p1Line.setLocalScale((new Matrix4f()).scaling(p1avDis));
		p1Line.lookAt(avatar);

		p2avDis = (p2Line.getWorldLocation()).distance(avatar.getWorldLocation());
		p2Line.setLocalLocation(prize2.getWorldLocation());
		p2Line.setLocalScale((new Matrix4f()).scaling(p2avDis));
		p2Line.lookAt(avatar);

		p3avDis = (p3Line.getWorldLocation()).distance(avatar.getWorldLocation());
		p3Line.setLocalLocation(prize3.getWorldLocation());
		p3Line.setLocalScale((new Matrix4f()).scaling(p3avDis));
		p3Line.lookAt(avatar);
	}

	private void checkCollectPrize() {
		// avatar location
		Vector3f avloc;
		avloc = avatar.getWorldLocation();
		// Prize 1 var
		Vector3f p1loc;
		Matrix4f p1scale;
		float p1size;
		float p1camDis;
		// Prize 2 var
		Vector3f p2loc;
		Matrix4f p2scale;
		float p2size;
		float p2camDis;
		// Prize 3 var
		Vector3f p3loc;
		Matrix4f p3scale;
		float p3size;
		float p3camDis;

		// Prize 1
		p1loc = prize1.getWorldLocation();
		p1scale = prize1.getLocalScale();
		p1size = p1scale.m00();
		p1camDis = p1loc.distance(avloc);

		if (p1camDis - p1size <= 0 && !p1collect) {
			if (p1collect)
				return;
			scoreCounter++;
			p1collect = true;
			isCarryPShown = true;
			(engine.getSceneGraph()).removeGameObject(prize1);
			(engine.getSceneGraph()).removeGameObject(p1Line);
		}

		// Prize 2
		p2loc = prize2.getWorldLocation();
		p2scale = prize2.getLocalScale();
		p2size = p2scale.m00();
		p2camDis = p2loc.distance(avloc);

		if (p2camDis - p2size <= 0) {
			if (p2collect)
				return;
			scoreCounter++;
			p2collect = true;
			isCarryPShown = true;
			(engine.getSceneGraph()).removeGameObject(prize2);
			(engine.getSceneGraph()).removeGameObject(p2Line);
		}

		// Prize 3
		p3loc = prize3.getWorldLocation();
		p3scale = prize3.getLocalScale();
		p3size = p3scale.m00();
		p3camDis = p3loc.distance(avloc);

		if (p3camDis - p3size <= 0) {
			if (p3collect)
				return;
			scoreCounter++;
			p3collect = true;
			isCarryPShown = true;
			(engine.getSceneGraph()).removeGameObject(prize3);
			(engine.getSceneGraph()).removeGameObject(p3Line);
		}
	}

	private void checkTouchRocket() {
		Vector3f avloc, rocketloc;
		float avrocDis;
		float avsize;
		float rocsize;

		avloc = avatar.getWorldLocation();
		avsize = (avatar.getWorldScale()).m00();

		rocketloc = rocket.getWorldLocation();
		rocsize = (rocket.getWorldScale()).m00();
		avrocDis = avloc.distance(rocketloc);

		if (avrocDis - avsize - rocsize <= 0 && scoreCounter >= 3) {
			winFlag = true;
		}
	}
	*/

	private void updateHUD() {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(3);
		RenderSystem rs = engine.getRenderSystem();
		Viewport smallVp = rs.getViewport(subVpName);

		Vector3f avLoc = getAvatar().getWorldLocation();
		float avLocX = avLoc.x();
		float avLocY = avLoc.y();
		float avLocZ = avLoc.z();

		// build and set HUD
		int elapsTimeSec = Math.round((float) displayTime);
		String elapsTimeStr = Integer.toString(60 - elapsTimeSec);
		String counterStr = Integer.toString(scoreCounter);
		String dispStr1 = "Fuel left = " + elapsTimeStr;
		String dispStr2 = "Score = " + counterStr;
		String dispStr3 = "Avatar's world location X: " + df.format(avLocX) + " Y: " + df.format(avLocY) + " Z: "
				+ df.format(avLocZ);
		String win = "You Win!";
		String lose = "You Lose!";
		Vector3f hud1Color = new Vector3f(1, 0, 0);
		Vector3f hud2Color = new Vector3f(0, 0, 1);
		Vector3f hud3Color = new Vector3f(0, 1, 0);
		(engine.getHUDmanager()).setHUD1(dispStr1, hud1Color, 15, 15);
		(engine.getHUDmanager()).setHUD2(dispStr2, hud2Color, 500, 15);
		(engine.getHUDmanager()).setHUD3(dispStr3, hud3Color, (int) (rs.getWidth() * smallVp.getRelativeLeft()),
				(int) (rs.getHeight() * smallVp.getRelativeHeight() + 20));

		// Display text 3 seconds on screen
		// if (lostDolFlag || tooCloseFlag) {
		// threeSec = elapsTimeSec + 3;
		// }
		// if (elapsTimeSec <= threeSec) {
		// if (lostDolFlag) {
		// (engine.getHUDmanager()).setHUD3(lostDolStr, hud3Color,
		// (engine.getRenderSystem().getWidth()) / 2,
		// (engine.getRenderSystem().getHeight()) / 2);
		// lostDolFlag = false;
		// } else if (tooCloseFlag) {
		// (engine.getHUDmanager()).setHUD3(tooCloseStr, hud3Color,
		// (engine.getRenderSystem().getWidth()) / 2,
		// (engine.getRenderSystem().getHeight()) / 2);
		// tooCloseFlag = false;
		// }
		// } else {
		// (engine.getHUDmanager()).setHUD3("", hud3Color,
		// (engine.getRenderSystem().getWidth()) / 2,
		// (engine.getRenderSystem().getHeight()) / 2);
		// }

		// Lose condition
		if (elapsTimeSec >= 60) {
			// (engine.getHUDmanager()).setHUD1(lose, hud3Color,
			// 		(engine.getRenderSystem().getWidth()) / 2,
			// 		(engine.getRenderSystem().getHeight()) / 2);
			return;
		}

		// Win condition
		if (winFlag) {
			// (engine.getHUDmanager()).setHUD1(win, hud3Color,
			// 		(engine.getRenderSystem().getWidth()) / 2,
			// 		(engine.getRenderSystem().getHeight()) / 2);
		}
	}

	private void inputSetup() {
		// ----------------- INPUTS SECTION -----------------------------
		im = engine.getInputManager();
		FwdAction fwdAction = new FwdAction(this);
		BwdAction bwdAction = new BwdAction(this);
		FwdBwdAction fwdbwdAction = new FwdBwdAction(this);

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
		ToggleCheat toggleCheat = new ToggleCheat(this);

		// Gamepad
		im.associateActionWithAllGamepads(
				net.java.games.input.Component.Identifier.Axis.Y, fwdbwdAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(
				net.java.games.input.Component.Identifier.Axis.X, turnAction,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(
				net.java.games.input.Component.Identifier.Button._1, toggleCheat,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

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
		// im.associateActionWithAllKeyboards(
		// 		net.java.games.input.Component.Identifier.Key.C, toggleCheat,
		// 		InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
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
	/*
	public GameObject getp1Line() {
		return p1Line;
	}

	public GameObject getp2Line() {
		return p2Line;
	}

	public GameObject getp3Line() {
		return p3Line;
	}
	*/
	public static boolean getXYZ() {
		return showXYZ;
	}

	public static void setXYZ(boolean showXYZ) {
		MyGame.showXYZ = showXYZ;
	}

	// public static boolean getCheat() {
	// 	return cheat;
	// }

	// public static void setCheat(boolean cheat) {
	// 	MyGame.cheat = cheat;
	// }

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

	private void scriptingSetup(){
		ScriptEngineManager factory = new ScriptEngineManager();
		jsEngine = factory.getEngineByName("js");

		scriptFile1 = new File("assets/scripts/InitParams.js");
		this.runScript(scriptFile1);
	}

	private void runScript(File scriptFile) 
	{ 
		try 
		{ 
			FileReader fileReader = new FileReader(scriptFile); 
			jsEngine.eval(fileReader); 
			fileReader.close(); 
		} 
		catch (FileNotFoundException e1) 
		{ System.out.println(scriptFile + " not found " + e1); } 
		catch (IOException e2) 
		{ System.out.println("IO problem with " + scriptFile + e2); } 
		catch (ScriptException e3)  
		{ System.out.println("ScriptException in " + scriptFile + e3); } 
		catch (NullPointerException e4) 
		{ System.out.println ("Null ptr exception reading " + scriptFile + e4); 
		} 
	} 

	public float getBaseSpeed(){
		return this.baseSpeed;
	}
	public float getSprintSpeed(){
		return this.sprintSpeed;
	}
}