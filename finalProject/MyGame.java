package finalProject;

import finalProject.*;
import finalProject.camera.*;
import finalProject.control.*;
import finalProject.manualObj.*;

import tage.*;
import tage.shapes.*;
import tage.input.*;
import tage.input.action.AbstractInputAction;
import tage.networking.IGameConnection.ProtocolType;
import tage.nodeControllers.BounceController;
import tage.nodeControllers.RotationController;
import tage.physics.PhysicsEngine;
import tage.physics.PhysicsObject;
import tage.physics.PhysicsEngineFactory;
import tage.physics.JBullet.*;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.collision.dispatch.CollisionObject;

import org.joml.*;

import java.io.*;
import java.util.*;
import java.util.Random;
import java.lang.Math;
import java.util.UUID;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.awt.event.*;
import java.awt.Robot;
import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.script.ScriptEngine;

public class MyGame extends VariableFrameRateGame {
	// Engine
	private static Engine engine;

	// Input variables
	private InputManager im;
	private Robot robot;
	private float curMouseX, curMouseY, centerX, centerY;
	private float prevMouseX, prevMouseY; // loc of mouse prior to move
	private boolean isRecentering; // indicates the Robot is in action
	private boolean isRightClick = false;

	// Camera and Viewport variables
	private CameraOrbit3D orbitController;
	final private String MAINVP = "MAIN";
	final private String TOPLEFTVP = "TOPLEFT";
	final private String AVATARVP = "PLAYERVP";

	// Time variables
	private double lastFrameTime, currFrameTime, elapsTime, displayTime;
	private double timer = System.currentTimeMillis();
	private double fiveSec, tenSec;
	private boolean switchSkyBoxes = true;

	// Networking
	private GhostManager gm;
	private String serverAddress;
	private int serverPort;
	private ProtocolType serverProtocol;
	private ProtocolClient protClient;
	private boolean isClientConnected = false;
	private Vector3f orientationEuler = new Vector3f(0f, 0f, 0f);
	// Used for playing animation while moving
	private boolean currentlyMoving = false;
	private boolean currentlyPlayingWalkAnimation = false;
	private Vector3f lastFramePosition, currentFramePosition;

	private Random rand = new Random();
	private GameObject avatar, x, y, z, rocket;
	private GameObject soup;
	private GameObject gateN, gateE, gateS, gateW;
	private GameObject mage;
	private GameObject archer;
	private GameObject monsterNormal;
	private GameObject mageNPC, archerNPC;

	// Additional object
	private GameObject fence, lamp, house, town, fence2, fence3, fence4, fence5, fence6, fence7, fence8;
	private GameObject dog;
	private ArrayList<GameObject> trees = new ArrayList<GameObject>();

	private ArrayList<GameObject> xpOrbs = new ArrayList<GameObject>();
	public ArrayList<GameObject> monsterNormals = new ArrayList<GameObject>();

	private GameObject ranger;
	private ObjShape rangerS;
	private TextureImage rangerT;

	private GameObject arenaWall;
	private ObjShape arenaWallS;
	private TextureImage arenaWallT;

	// Skill Objects
	private GameObject fireball0, fireball1, fireball2;
	private boolean fire3Fireballs = false;
	private GameObject avatarOrbiter1, avatarOrbiter2, avatarOrbiter3;
	private GameObject circle;
	private GameObject angel;
	// Skill objects for ghost
	private GameObject gavatarOrbiter1, gavatarOrbiter2, gavatarOrbiter3;
	private GameObject gcircle;

	private boolean fireballCurrentlyMoving = false;
	private boolean leveledUp = false;
	private boolean skillMenu = false;
	private Vector3f initialFireballPosition;
	private float amtt = 0f;
	private float amtt2 = 180f;
	private float amtt3 = 360f;

	private ObjShape linxS, linyS, linzS, rocketS, xpOrbS;
	private ObjShape soupS;
	private ObjShape gateSh;
	private ObjShape mageS;
	private ObjShape archerS;
	private AnimatedShape mageAS;
	private AnimatedShape archerAS;
	private AnimatedShape monsterNormalAS;
	private ObjShape ghostS_Mage;
	private ObjShape ghostS_Archer;

	// Additional object Shape
	private ObjShape fenceS, lampS, houseS, townS, treeS;
	private ObjShape dogS;

	// Skill Shapes
	private ObjShape fireballS;
	private ObjShape avatarOrbiterS;
	private ObjShape circleS;
	private ObjShape angelS;

	private TextureImage rocketT;
	private TextureImage planeT;
	private TextureImage soupT;
	private TextureImage gateT;
	private TextureImage ghostT_Mage;
	private TextureImage ghostT_Archer;
	private TextureImage mageT;
	private TextureImage archerT;
	private TextureImage monsterNormalT;
	private TextureImage xpOrbT;
	private TextureImage treeT;
	// Additional object Texture
	private TextureImage fenceT, lampT, houseT, townT;
	private TextureImage dogT;

	// Skill Textures
	private TextureImage fireballT, avatarOrbiterT, circleT;

	private int darkSky, daySky;

	private Light light1, light2;
	private boolean lightsEnabled = true;

	private Plane planeS;

	private NodeController rc, mc;

	private File scriptFile1;
	private ScriptEngine jsEngine;

	// Physics Engine
	private PhysicsEngine physicsEngine;
	private PhysicsObject monster;
	private PhysicsObject fireball0P, fireball1P, fireball2P;
	private PhysicsObject avatarOrbiter1P, avatarOrbiter2P, avatarOrbiter3P;
	private PhysicsObject circleP;

	private boolean running = false;
	private float vals[] = new float[16];

	// ---------- Game Variables ----------
	private static boolean showXYZ;
	private static boolean booster;
	private boolean isBooster, isConsumed;
	private float orbiterSpeed;
	// private boolean winFlag;
	private int scoreCounter;
	private HashMap<String, Integer> playerStats;
	private HashMap<String, Integer> monsterStats;

	private ScriptController scriptController;
	private InputController inputController;

	// Image-Based Height Maps
	private GameObject terr;
	private ObjShape terrS;
	private TextureImage wall, terrT;
	private boolean inTeleportCooldown = false;
	private double timeToEndCooldown = 0.0;

	public MyGame(String serverAddress, int serverPort, String protocol) {
		super();
		gm = new GhostManager(this);
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		if (protocol.toUpperCase().compareTo("TCP") == 0)
			this.serverProtocol = ProtocolType.TCP;
		else
			this.serverProtocol = ProtocolType.UDP;
	}

	public static void main(String[] args) {
		MyGame game = new MyGame(args[0], Integer.parseInt(args[1]), args[2]);
		engine = new Engine(game);
		game.initializeSystem();
		game.game_loop();
	}

	@Override
	public void loadShapes() {
		rocketS = new Rocket();
		planeS = new Plane();
		xpOrbS = new Sphere();
		terrS = new TerrainPlane(1000); // pixel per axis = 1000 x 1000

		linxS = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(3f, 0f, 0f));
		linyS = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 3f, 0f));
		linzS = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, 3f));

		soupS = new ImportedModel("icecream.obj");
		gateSh = new ImportedModel("gate.obj");
		rangerS = new ImportedModel("ranger.obj");
		mageS = new ImportedModel("mage.obj");
		archerS = new ImportedModel("archer_join.obj");

		fenceS = new ImportedModel("fence.obj");
		lampS = new ImportedModel("lamp.obj");
		houseS = new ImportedModel("house.obj");
		townS = new ImportedModel("town.obj");
		treeS = new ImportedModel("tree.obj");
		dogS = new ImportedModel("dog.obj");
		arenaWallS = new ImportedModel("arenaWall.obj");

		mageAS = new AnimatedShape("mage.rkm", "mage.rks");
		mageAS.loadAnimation("MOVE", "mage_move.rka");
		mageAS.loadAnimation("ATTACK", "mage_attack.rka");
		mageAS.loadAnimation("MOVEATTACK", "mage_moveattack.rka");

		archerAS = new AnimatedShape("archer.rkm", "archer.rks");
		archerAS.loadAnimation("MOVE", "archer_move.rka");
		archerAS.loadAnimation("ATTACK", "archer_attack.rka");
		archerAS.loadAnimation("MOVEATTACK", "archer_moveattack.rka");

		monsterNormalAS = new AnimatedShape("monster_normal.rkm", "monster_normal.rks");
		monsterNormalAS.loadAnimation("MOVEATTACK", "monster_normal_moveattack.rka");

		ghostS_Mage = new AnimatedShape("mage.rkm", "mage.rks");
		ghostS_Archer = new AnimatedShape("archer.rkm", "archer.rks");

		// Skills
		fireballS = new ImportedModel("fireball.obj");
		avatarOrbiterS = new Torus();
		circleS = new ImportedModel("circle.obj");
		angelS = new ImportedModel("angel.obj");
	}

	@Override
	public void loadTextures() {
		rocketT = new TextureImage("myTextures.png");
		planeT = new TextureImage("sea.png");
		soupT = new TextureImage("icecream.png");
		gateT = new TextureImage("gate.png");
		ghostT_Mage = new TextureImage("mage1.png");
		ghostT_Archer = new TextureImage("archer.png");
		mageT = new TextureImage("mage1.png");
		archerT = new TextureImage("archer.png");
		xpOrbT = new TextureImage("soup.jpg");
		monsterNormalT = new TextureImage("monster_normal.png");
		rangerT = new TextureImage("ranger.png");
		fenceT = new TextureImage("fence.png");
		lampT = new TextureImage("lamp.png");
		houseT = new TextureImage("house.png");
		townT = new TextureImage("town.png");
		treeT = new TextureImage("tree.png");
		wall = new TextureImage("wall.jpg");
		terrT = new TextureImage("wood.jpg");
		dogT = new TextureImage("dog.png");
		arenaWallT = new TextureImage("wood.jpg");

		// Skills
		fireballT = new TextureImage("mage_skill1.png");
		avatarOrbiterT = new TextureImage("soup.jpg");
		circleT = new TextureImage("circle.png");
	}

	@Override
	public void buildObjects() {
		Matrix4f initialTranslation, initialScale;

		// build terrain object
		terr = new GameObject(GameObject.root(), terrS, terrT);
		initialTranslation = (new Matrix4f()).translation(0f, -0.1f, 0f);
		terr.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(50f);
		terr.setLocalScale(initialScale);
		terr.setHeightMap(wall);

		GameObject plane = new GameObject(GameObject.root(), planeS, planeT);
		initialTranslation = (new Matrix4f()).translation(0f, 0f, 0f);
		plane.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(80f);
		plane.setLocalScale(initialScale);

		// build manual object - rocket
		rocket = new GameObject(GameObject.root(), rocketS, rocketT);
		initialTranslation = (new Matrix4f()).translation(50, 3, 50);
		rocket.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(0.5f);
		rocket.setLocalScale(initialScale);

		// build ranger
		ranger = new GameObject(GameObject.root(), rangerS, rangerT);
		initialTranslation = (new Matrix4f()).translation(0, 1, 2);
		ranger.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(0.5f);
		ranger.setLocalScale(initialScale);

		arenaWall = new GameObject(GameObject.root(), arenaWallS, arenaWallT);
		initialTranslation = (new Matrix4f()).translation(0, 0, 0);
		arenaWall.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(35f);
		arenaWall.setLocalScale(initialScale);
		arenaWall.getRenderStates().setRenderHiddenFaces(true);

		// add X,Y,-Z axes
		x = new GameObject(GameObject.root(), linxS);
		y = new GameObject(GameObject.root(), linyS);
		z = new GameObject(GameObject.root(), linzS);
		(x.getRenderStates()).setColor(new Vector3f(1f, 0f, 0f));
		(y.getRenderStates()).setColor(new Vector3f(0f, 1f, 0f));
		(z.getRenderStates()).setColor(new Vector3f(0f, 0f, 1f));

		// build soup
		soup = new GameObject(GameObject.root(), soupS, soupT);
		initialTranslation = (new Matrix4f()).translation(randNum(), 0.8f, randNum());
		soup.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(0.2f);
		soup.setLocalScale(initialScale);

		// Gates
		// gateN = new GameObject(GameObject.root(), gateSh, gateT);
		// initialTranslation = (new Matrix4f()).translation(-31, 0, 36);
		// gateN.getRenderStates().setModelOrientationCorrection(
		// 		(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(50f)));
		// gateN.setLocalTranslation(initialTranslation);
		// gateE = new GameObject(GameObject.root(), gateSh, gateT);
		// initialTranslation = (new Matrix4f()).translation(-31, 0, -36);
		// gateE.getRenderStates().setModelOrientationCorrection(
		// 		(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(-45f)));
		// gateE.setLocalTranslation(initialTranslation);
		// gateS = new GameObject(GameObject.root(), gateSh, gateT);
		// initialTranslation = (new Matrix4f()).translation(31, 0, -36);
		// gateS.getRenderStates().setModelOrientationCorrection(
		// 		(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(225f)));
		// gateS.setLocalTranslation(initialTranslation);
		// gateW = new GameObject(GameObject.root(), gateSh, gateT);
		// initialTranslation = (new Matrix4f()).translation(31, 0, 36);
		// gateW.getRenderStates().setModelOrientationCorrection(
		// 		(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(130.0f)));
		// gateW.setLocalTranslation(initialTranslation);

		// Town
		town = new GameObject(GameObject.root(), townS, townT);
		initialTranslation = (new Matrix4f()).translation(90, 0, 90);
		town.setLocalTranslation(initialTranslation);
		town.getRenderStates().setModelOrientationCorrection(
				(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(-90.0f)));
		initialScale = (new Matrix4f()).scaling(4f);
		town.setLocalScale(initialScale);

		// ------House Hierarchical Object----------------------------
		house = new GameObject(GameObject.root(), houseS, houseT);
		initialTranslation = (new Matrix4f()).translation(0, 0, -10);
		house.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(0.5f);
		house.setLocalScale(initialScale);
		house.setParent(town);
		house.propagateTranslation(true);
		house.propagateRotation(false);
		house.propagateScale(false);
		// Fences
		Matrix4f rotationMatrix = new Matrix4f().rotateY((float) Math.toRadians(90));
		fence = new GameObject(GameObject.root(), fenceS, terrT);
		initialTranslation = (new Matrix4f()).translation(3, 0, -5);
		fence.setLocalTranslation(initialTranslation);
		fence.setParent(house);
		fence.propagateTranslation(true);
		fence.propagateRotation(true);
		fence.propagateScale(true);
		fence.applyParentRotationToPosition(true);
		fence2 = new GameObject(GameObject.root(), fenceS, terrT);
		initialTranslation = (new Matrix4f()).translation(-3, 0, -5);
		fence2.setLocalTranslation(initialTranslation);
		fence2.setParent(house);
		fence2.propagateTranslation(true);
		fence2.propagateRotation(true);
		fence2.propagateScale(true);
		fence2.applyParentRotationToPosition(true);
		fence3 = new GameObject(GameObject.root(), fenceS, terrT);
		initialTranslation = (new Matrix4f()).translation(-3, 0, 3);
		fence3.setLocalTranslation(initialTranslation);
		fence3.setParent(house);
		fence3.propagateTranslation(true);
		fence3.propagateRotation(true);
		fence3.propagateScale(true);
		fence3.applyParentRotationToPosition(true);
		fence4 = new GameObject(GameObject.root(), fenceS, terrT);
		initialTranslation = (new Matrix4f()).translation(3, 0, 3);
		fence4.setLocalTranslation(initialTranslation);
		fence4.setParent(house);
		fence4.propagateTranslation(true);
		fence4.propagateRotation(true);
		fence4.propagateScale(true);
		fence4.applyParentRotationToPosition(true);
		fence5 = new GameObject(GameObject.root(), fenceS, terrT);
		initialTranslation = (new Matrix4f()).translation(-5, 0, -3);
		fence5.setLocalRotation(rotationMatrix);
		fence5.setLocalTranslation(initialTranslation);
		fence5.setParent(house);
		fence5.propagateTranslation(true);
		fence5.propagateRotation(true);
		fence5.propagateScale(true);
		fence5.applyParentRotationToPosition(true);
		fence6 = new GameObject(GameObject.root(), fenceS, terrT);
		initialTranslation = (new Matrix4f()).translation(-5, 0, 1);
		fence6.setLocalRotation(rotationMatrix);
		fence6.setLocalTranslation(initialTranslation);
		fence6.setParent(house);
		fence6.propagateTranslation(true);
		fence6.propagateRotation(true);
		fence6.propagateScale(true);
		fence6.applyParentRotationToPosition(true);
		fence7 = new GameObject(GameObject.root(), fenceS, terrT);
		initialTranslation = (new Matrix4f()).translation(5, 0, -3);
		fence7.setLocalRotation(rotationMatrix);
		fence7.setLocalTranslation(initialTranslation);
		fence7.setParent(house);
		fence7.propagateTranslation(true);
		fence7.propagateRotation(true);
		fence7.propagateScale(true);
		fence7.applyParentRotationToPosition(true);
		fence8 = new GameObject(GameObject.root(), fenceS, terrT);
		initialTranslation = (new Matrix4f()).translation(5, 0, 1);
		fence8.setLocalRotation(rotationMatrix);
		fence8.setLocalTranslation(initialTranslation);
		fence8.setParent(house);
		fence8.propagateTranslation(true);
		fence8.propagateRotation(true);
		fence8.propagateScale(true);
		fence8.applyParentRotationToPosition(true);

		lamp = new GameObject(GameObject.root(), lampS, lampT);
		initialTranslation = (new Matrix4f()).translation(0, 0, 5);
		lamp.setLocalTranslation(initialTranslation);
		lamp.setParent(town);
		lamp.propagateTranslation(true);
		lamp.propagateRotation(false);
		lamp.propagateScale(false);

		dog = new GameObject(GameObject.root(), dogS, dogT);
		initialTranslation = (new Matrix4f()).translation(-1, 0, 4);
		dog.setLocalTranslation(initialTranslation);
		dog.getRenderStates().setModelOrientationCorrection(
			(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(180.0f)));
		initialScale = (new Matrix4f()).scaling(0.4f);
		dog.setLocalScale(initialScale);
		dog.setParent(town);
		dog.propagateTranslation(true);
		dog.propagateRotation(false);
		dog.propagateScale(false);

		for (int i = 0; i < 2; i++) {
			GameObject tree = new GameObject(GameObject.root(), treeS, treeT);
			initialTranslation = (new Matrix4f()).translation(i * 8 - 4, 0, -4);
			tree.setLocalTranslation(initialTranslation);
			tree.getRenderStates().setModelOrientationCorrection(
					(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(180.0f)));
			initialScale = (new Matrix4f()).scaling(0.5f);
			tree.setLocalScale(initialScale);
			tree.setParent(town);
			tree.propagateTranslation(true);
			tree.propagateScale(false);
			trees.add(tree);
		}

		mageNPC = new GameObject(GameObject.root(), mageAS, mageT);
		initialTranslation = (new Matrix4f()).translation(-5, 0.9f, 10);
		//initialTranslation = (new Matrix4f()).translation(0f, 0f, 0f);
		mageNPC.setLocalTranslation(initialTranslation);
		mageNPC.getRenderStates().setModelOrientationCorrection(
				(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(90.0f)));
		initialScale = (new Matrix4f()).scaling(0.4f);
		mageNPC.setLocalScale(initialScale);
		mageNPC.setParent(town);
		mageNPC.propagateTranslation(true);
		mageNPC.propagateRotation(true);
		mageNPC.propagateScale(false);
		mageNPC.applyParentRotationToPosition(true);

		archerNPC = new GameObject(GameObject.root(), archerAS, archerT);
		initialTranslation = (new Matrix4f()).translation(5, 0.9f, 10);
		archerNPC.setLocalTranslation(initialTranslation);
		archerNPC.getRenderStates().setModelOrientationCorrection(
				(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(90.0f)));
		initialScale = (new Matrix4f()).scaling(0.4f);
		archerNPC.setLocalScale(initialScale);
		archerNPC.setParent(town);
		archerNPC.propagateTranslation(true);
		archerNPC.propagateRotation(true);
		archerNPC.propagateScale(false);
		archerNPC.applyParentRotationToPosition(true);
		// -----------------------------------------------

		// Sets the current playable character to mage
		// avatar = mage;
		avatar = new GameObject(GameObject.root(), xpOrbS, xpOrbT);
		initialTranslation = (new Matrix4f()).translation(90f, 0.60f, 86f);
		avatar.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(0.2f);
		avatar.setLocalScale(initialScale);

		mage = new GameObject(GameObject.root(), mageAS, mageT);
		// initialTranslation = (new Matrix4f()).translation(0f, 0.6f, 0f);
		// mage.setLocalTranslation(initialTranslation);
		mage.getRenderStates().setModelOrientationCorrection(
				(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(-90.0f)));
		initialScale = (new Matrix4f()).scaling(0.2f);
		mage.setLocalScale(initialScale);
		mage.setParent(avatar);
		mage.propagateTranslation(true);
		mage.propagateRotation(true);
		mage.propagateScale(false);
		mage.applyParentRotationToPosition(true);
		mage.getRenderStates().disableRendering();

		archer = new GameObject(GameObject.root(), archerAS, archerT);
		// initialTranslation = (new Matrix4f()).translation(0f, 0.6f, 0f);
		// archer.setLocalTranslation(initialTranslation);
		archer.getRenderStates().setModelOrientationCorrection(
				(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(-90.0f)));
		initialScale = (new Matrix4f()).scaling(0.2f);
		archer.setLocalScale(initialScale);
		archer.setParent(avatar);
		archer.propagateTranslation(true);
		archer.propagateRotation(true);
		archer.propagateScale(false);
		archer.applyParentRotationToPosition(true);
		archer.getRenderStates().disableRendering();

		fireball0 = new GameObject(GameObject.root(), fireballS, fireballT);
		initialTranslation = (new Matrix4f()).translation(0, 0.5f, 0);
		fireball0.setLocalTranslation(initialTranslation);
		fireball0.getRenderStates().setModelOrientationCorrection(
				(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(-180.0f)));
		initialScale = (new Matrix4f()).scaling(0.2f);
		fireball0.setLocalScale(initialScale);
		fireball0.getRenderStates().disableRendering();

		fireball1 = new GameObject(GameObject.root(), fireballS, fireballT);
		initialTranslation = (new Matrix4f()).translation(0, 0.5f, 0);
		fireball1.setLocalTranslation(initialTranslation);
		fireball1.getRenderStates().setModelOrientationCorrection(
				(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(-180.0f)));
		initialScale = (new Matrix4f()).scaling(0.2f);
		fireball1.setLocalScale(initialScale);
		fireball1.getRenderStates().disableRendering();

		fireball2 = new GameObject(GameObject.root(), fireballS, fireballT);
		initialTranslation = (new Matrix4f()).translation(0, 0.5f, 0);
		fireball2.setLocalTranslation(initialTranslation);
		fireball2.getRenderStates().setModelOrientationCorrection(
				(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(-180.0f)));
		initialScale = (new Matrix4f()).scaling(0.2f);
		fireball2.setLocalScale(initialScale);
		fireball2.getRenderStates().disableRendering();

		avatarOrbiter1 = new GameObject(GameObject.root(), avatarOrbiterS, avatarOrbiterT);
		initialTranslation = (new Matrix4f()).translation(0, 0.3f, 0);
		avatarOrbiter1.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scale(2.5f, 10f, 2.5f);
		avatarOrbiter1.setLocalScale(initialScale);
		avatarOrbiter1.setParent(avatar);
		avatarOrbiter1.propagateTranslation(true);
		avatarOrbiter1.propagateRotation(false);
		avatarOrbiter1.getRenderStates().disableRendering();

		avatarOrbiter2 = new GameObject(GameObject.root(), avatarOrbiterS, avatarOrbiterT);
		initialTranslation = (new Matrix4f()).translation(0, 0.3f, 0);
		avatarOrbiter2.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scale(2.5f, 10f, 2.5f);
		avatarOrbiter2.setLocalScale(initialScale);
		avatarOrbiter2.setParent(avatar);
		avatarOrbiter2.propagateTranslation(true);
		avatarOrbiter2.propagateRotation(false);
		avatarOrbiter2.getRenderStates().disableRendering();

		avatarOrbiter3 = new GameObject(GameObject.root(), avatarOrbiterS, avatarOrbiterT);
		initialTranslation = (new Matrix4f()).translation(0, 0.3f, 0);
		avatarOrbiter3.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scale(2.5f, 10f, 2.5f);
		avatarOrbiter3.setLocalScale(initialScale);
		avatarOrbiter3.setParent(avatar);
		avatarOrbiter3.propagateTranslation(true);
		avatarOrbiter3.propagateRotation(false);
		avatarOrbiter3.getRenderStates().disableRendering();

		circle = new GameObject(GameObject.root(), circleS, circleT);
		initialTranslation = (new Matrix4f()).translation(0, -.58f, 0);
		circle.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scale(10f);
		circle.setLocalScale(initialScale);
		circle.setParent(avatar);
		circle.propagateTranslation(true);
		circle.propagateRotation(false);
		circle.getRenderStates().disableRendering();

		angel = new GameObject(GameObject.root(), angelS);
		angel.getRenderStates().setHasSolidColor(false);
		angel.getRenderStates().setColor(new Vector3f(1f, 1f, 1f));
		initialTranslation = (new Matrix4f()).translation(-.3f, 1f, -.3f);
		//initialTranslation = (new Matrix4f()).translation(0f, 0f, -1f);
		angel.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scale(0.1f);
		angel.setLocalScale(initialScale);
		angel.setParent(avatar);
		angel.propagateTranslation(true);
		angel.propagateRotation(true);
		angel.propagateScale(false);
		angel.applyParentRotationToPosition(true);

		gavatarOrbiter1 = new GameObject(GameObject.root(), avatarOrbiterS, avatarOrbiterT);
		gavatarOrbiter1.getRenderStates().disableRendering();
		gavatarOrbiter2 = new GameObject(GameObject.root(), avatarOrbiterS, avatarOrbiterT);
		gavatarOrbiter2.getRenderStates().disableRendering();
		gavatarOrbiter3 = new GameObject(GameObject.root(), avatarOrbiterS, avatarOrbiterT);
		gavatarOrbiter3.getRenderStates().disableRendering();
		gcircle = new GameObject(GameObject.root(), circleS, circleT);
		gcircle.getRenderStates().disableRendering();
	}

	@Override
	public void initializeLights() {
		Light.setGlobalAmbient(0.2f, 0.2f, 0.2f);
		light1 = new Light();
		light1.setType(Light.LightType.SPOTLIGHT);
		light1.setDirection(new Vector3f(0, -1, 0));
		light1.setLocation(new Vector3f(0f,5f,0f));
		(engine.getSceneGraph()).addLight(light1);
		setUpToggleableLights();
	}
	private void setUpToggleableLights(){
		//IMPORTANT: for any additional light added, you must also change the toggleLight().
		light2 = new Light();
		light2.setType(Light.LightType.POSITIONAL);
		light2.setLocation(lamp.getWorldLocation());
		(engine.getSceneGraph()).addLight(light2);
	}

	@Override
	public void createViewports() {
		(engine.getRenderSystem()).addViewport(MAINVP, 0, 0, 1f, 1f);
		(engine.getRenderSystem()).addViewport(TOPLEFTVP, 0.8f, 0.8f, .2f, .2f);
		(engine.getRenderSystem()).addViewport("PLAYERVP", 0f, 0.8f, .1f, .2f);

		Viewport mainVp = (engine.getRenderSystem()).getViewport(MAINVP);
		Viewport topRightVp = (engine.getRenderSystem()).getViewport(TOPLEFTVP);
		Viewport playerVp = (engine.getRenderSystem()).getViewport(AVATARVP);

		Camera mainCamera = mainVp.getCamera();
		Camera topRightCamera = topRightVp.getCamera();
		Camera avatarCamera = playerVp.getCamera();

		topRightVp.setHasBorder(true);
		topRightVp.setBorderWidth(4);
		topRightVp.setBorderColor(0.431f, 0.149f, 0.054f);

		playerVp.setHasBorder(true);
		playerVp.setBorderWidth(4);
		playerVp.setBorderColor(0.431f, 0.149f, 0.054f);

		mainCamera.setLocation(new Vector3f(-2, 0, 2));
		mainCamera.setU(new Vector3f(1, 0, 0));
		mainCamera.setV(new Vector3f(0, 1, 0));
		mainCamera.setN(new Vector3f(0, 0, -1));

		topRightCamera.setLocation(new Vector3f(0, 10, 0));
		topRightCamera.setU(new Vector3f(1, 0, 0));
		topRightCamera.setV(new Vector3f(0, 0, -1));
		topRightCamera.setN(new Vector3f(0, -1, 0));

		avatarCamera.setLocation(new Vector3f(getAvatar().getWorldLocation().x,
				getAvatar().getWorldLocation().y + 0.4f,
				getAvatar().getWorldLocation().z + 1f));
		avatarCamera.setU(new Vector3f(1, 0, 0));
		avatarCamera.setV(new Vector3f(0, 1, 0));
		avatarCamera.setN(new Vector3f(0, 0, -1));
	}

	@Override
	public void loadSkyBoxes() {
		darkSky = engine.getSceneGraph().loadCubeMap("darkSky");
		daySky = engine.getSceneGraph().loadCubeMap("customSky");
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
		// initialize physics system
		initPhysics();

		initMouseMode();
		// ----------------- initialize camera ----------------
		initCamera();

		// keyboard and gamepad input manager setup
		// inputController = new InputController(this, engine);
		inputSetup();

		// Used for playing animations while avatar is moving
		lastFramePosition = avatar.getWorldLocation();
		currentFramePosition = lastFramePosition;

		setupNetworking();
	}

	private void initPhysics() {
		// --- initialize physics system ---
		String engine = "tage.physics.JBullet.JBulletPhysicsEngine";
		float[] gravity = { 0f, -5f, 0f };
		physicsEngine = PhysicsEngineFactory.createPhysicsEngine(engine);
		physicsEngine.initSystem();
		//physicsEngine.setGravity(gravity);
		// --- create physics world ---
		float mass = 1.0f;
		float up[] = { 0, 1, 0 };
		double[] tempTransform;

		Matrix4f translation = new Matrix4f(angel.getLocalTranslation());
		tempTransform = toDoubleArray(translation.get(vals));
		fireball0P = physicsEngine.addSphereObject(physicsEngine.nextUID(),
				mass, tempTransform, 0.5f);
		//fireball0P.setBounciness(1.0f);
		angel.setPhysicsObject(fireball0P);

		translation = new Matrix4f(mageNPC.getLocalTranslation());
		tempTransform = toDoubleArray(translation.get(vals));
		fireball1P = physicsEngine.addSphereObject(physicsEngine.nextUID(),
				mass, tempTransform, 0.5f);	
		//fireball1P.setBounciness(1.0f);
		mageNPC.setPhysicsObject(fireball1P);

		// translation = new Matrix4f(monsterNormal.getLocalTranslation());
		// tempTransform = toDoubleArray(translation.get(vals));
		// monster = physicsEngine.addStaticPlaneObject(
		// 		physicsEngine.nextUID(), tempTransform, up, 0.0f);
		// monster.setBounciness(1.0f);
		// monsterNormal.setPhysicsObject(monster);
	}

	// ------------------ UTILITY FUNCTIONS used by physics
	private float[] toFloatArray(double[] arr) {
		if (arr == null)
			return null;
		int n = arr.length;
		float[] ret = new float[n];
		for (int i = 0; i < n; i++) {
			ret[i] = (float) arr[i];
		}
		return ret;
	}

	private double[] toDoubleArray(float[] arr) {
		if (arr == null)
			return null;
		int n = arr.length;
		double[] ret = new double[n];
		for (int i = 0; i < n; i++) {
			ret[i] = (double) arr[i];
		}
		return ret;
	}

	private void setupNetworking() {
		isClientConnected = false;
		try {
			protClient = new ProtocolClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (protClient == null) {
			System.out.println("missing protocol host");
		} else {
			// ask client protocol to send initial join message
			// to server, with a unique identifier for this client
			System.out.println("sending join message to protocol host");
			protClient.sendJoinMessage();
		}
	}

	private class SendCloseConnectionPacketAction extends AbstractInputAction {
		@Override
		public void performAction(float time, net.java.games.input.Event evt) {
			if (protClient != null && isClientConnected == true) {
				protClient.sendByeMessage();
			}
		}
	}

	@Override
	public void update() {
		updateTime();

		updateGameLogic();
		// update inputs, avatar location
		im.update((float) elapsTime);

		updatePhysics();

		monsterNormals.forEach((n) -> n.lookAt(avatar));
		ranger.lookAt(avatar);

		// update camera
		orbitController.updateCameraPosition();
		updateMapCamPosition();
		updatePlayerCamPosition();

		updateHUD();

		processNetworking((float) elapsTime);
	}

	private void updateGameLogic() {
		if (currFrameTime - timer >= 1000) {
			timer = currFrameTime;
			callSendChangeSkyBoxesMessage();
			callSendSpawnMonsterMessage();
		}
		// updateSkyboxes();
		keepPlayerOnTerrain();
		checkAvatarSelect();
		checkTouchSoup();
		handleFireballMovement();
		checkTouchXPOrb();
		rotateOrbiters(orbiterSpeed);
		levelUp();
		skillUpdate();
		handleTeleportCooldown();
		updateAnimation();
		moveLightsWithAvatar();
	}

	private void updateAnimation() {
		playWalkAnimation();
		mageAS.updateAnimation();
		archerAS.updateAnimation();
		monsterNormalAS.updateAnimation();
	}

	private void updatePhysics() {
		Matrix4f mat = new Matrix4f();
		Matrix4f mat2 = new Matrix4f().identity();
		checkForCollisions();
		physicsEngine.update((float) elapsTime);
		for (GameObject go : engine.getSceneGraph().getGameObjects()) {
			if (go.getPhysicsObject() != null) {
				mat.set(toFloatArray(go.getPhysicsObject().getTransform()));
				mat2.set(3, 0, mat.m30());
				mat2.set(3, 1, mat.m31());
				mat2.set(3, 2, mat.m32());
				go.setLocalTranslation(mat2);
			}
		}
	}

	protected void processNetworking(float elapsTime) {
		// Process packets received by the client from the server
		if (protClient != null)
			protClient.processPackets();
	}

	private void updateMapCamPosition() {
		Camera c = (engine.getRenderSystem())
				.getViewport(TOPLEFTVP).getCamera();
		c.setLocation(new Vector3f(avatar.getWorldLocation().x(), c.getLocation().y(), avatar.getWorldLocation().z()));

		Camera a = (engine.getRenderSystem())
				.getViewport(AVATARVP).getCamera();
		a.positionCameraWObj(avatar, 0.4f, 1f, 0f);
	}

	private void updatePlayerCamPosition() {
		Camera c = (engine.getRenderSystem()).getViewport("PLAYERVP").getCamera();
		c.setLocation(new Vector3f(avatar.getWorldLocation().x() + 1, avatar.getWorldLocation().y() + 1,
				avatar.getWorldLocation().z() + 1));
		c.lookAt(avatar);
	}

	private void initCamera() {
		im = engine.getInputManager();
		String gpName = im.getFirstGamepadName();
		Camera c = (engine.getRenderSystem())
				.getViewport(MAINVP).getCamera();
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
		rc.addTarget(avatarOrbiter1);
		rc.addTarget(avatarOrbiter2);
		rc.addTarget(avatarOrbiter3);
		rc.addTarget(gavatarOrbiter1);
		rc.addTarget(gavatarOrbiter2);
		rc.addTarget(gavatarOrbiter3);
		(engine.getSceneGraph()).addNodeController(rc);
		rc.enable();

		mc = new BounceController(engine);
		mc.addTarget(soup);
		// mc.addTarget(avatar);
		(engine.getSceneGraph()).addNodeController(mc);
		mc.enable();
	}

	private void initGameVar() {
		playerStats = new HashMap<String, Integer>();
		playerStats.put("class", 0);
		playerStats.put("health", scriptController.getStartingHealth());
		playerStats.put("level", scriptController.getStartingLevel());
		playerStats.put("experience", scriptController.getStartingExperience());
		playerStats.put("atk", scriptController.getAtk());
		playerStats.put("spd", scriptController.getBaseSpeed());
		playerStats.put("skillPoint", scriptController.getStartingSkillPoint());
		playerStats.put("fireballLv", scriptController.getfireballLv());
		playerStats.put("avatarOrbiterLv", scriptController.getAvatarOrbiterLv());
		playerStats.put("circleLv", scriptController.getCircleLv());

		monsterStats = new HashMap<String, Integer>();
		monsterStats.put("monsterHealth", scriptController.getMonsterHealth());
		monsterStats.put("monsterAtk", scriptController.getMonsterAtk());
		monsterStats.put("monsterSpeed", scriptController.getMonsterSpeed());
		// Older Variables. May or may not be needed
		scoreCounter = 0;
		showXYZ = true;
		booster = false;
		isConsumed = false;
		isBooster = false;

		fiveSec = -1;
		orbiterSpeed = scriptController.getOrbiterSpeed();
	}

	private void initObj() {
		x.getRenderStates().enableRendering();
		y.getRenderStates().enableRendering();
		z.getRenderStates().enableRendering();
		rocket.getRenderStates().setRenderHiddenFaces(true);
	}

	private void initMouseMode() {
		RenderSystem rs = engine.getRenderSystem();
		Viewport vw = rs.getViewport(MAINVP);
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
		Viewport vw = rs.getViewport(MAINVP);
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

			// if (isRightClick) {
			avatar.yaw(mouseDeltaX / 50);
			orbitController.pitch(mouseDeltaY);
			// }
			callSendMoveMessage();
			prevMouseX = curMouseX;
			prevMouseY = curMouseY;
			// tell robot to put the cursor to the center (since user just moved it)
			recenterMouse();
			RenderSystem rs = engine.getRenderSystem();
			Viewport vw = rs.getViewport(MAINVP);
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
	public void mouseClicked(MouseEvent e) {

		if (!fireballCurrentlyMoving) {
			/** May need to add some offset to spawn fireball0 in front of avatar */
			fireball0.setLocalLocation(avatar.getWorldLocation());
			fireball0.setLocalRotation(avatar.getWorldRotation());
			fireball0.getRenderStates().enableRendering();

			// If the player's level is greater than 3, they get access to 3 fireballs
			if (playerStats.get("level") >= 3) {
				fire3Fireballs = true;
			} else {
				fire3Fireballs = false;
			}
			if (fire3Fireballs) {
				fireball1.setLocalLocation(avatar.getWorldLocation());
				fireball1.setLocalRotation(avatar.getWorldRotation());
				fireball1.turnRightAction(.1f);
				fireball1.getRenderStates().enableRendering();
				fireball2.setLocalLocation(avatar.getWorldLocation());
				fireball2.setLocalRotation(avatar.getWorldRotation());
				fireball2.turnLeftAction(.1f);
				fireball2.getRenderStates().enableRendering();
			}
			initialFireballPosition = fireball0.getWorldLocation();
			fireballCurrentlyMoving = true;
			if (currentlyMoving) {
				mageAS.playAnimation("MOVEATTACK", 2f, AnimatedShape.EndType.LOOP, 1);
				archerAS.playAnimation("MOVEATTACK", 2f, AnimatedShape.EndType.LOOP, 1);
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						currentlyPlayingWalkAnimation = false;
					}
				}, 2000); // Delay in milliseconds
			} else {
				mageAS.playAnimation("ATTACK", 2f, AnimatedShape.EndType.LOOP, 1);
				archerAS.playAnimation("ATTACK", 2f, AnimatedShape.EndType.LOOP, 1);
			}

		}
	}

	private void handleFireballMovement() {
		if (fireballCurrentlyMoving &&
				(fireball0.getWorldLocation().distance(initialFireballPosition) < scriptController
						.getFireballTravelDistance())) {
			fireball0.fwdAction(.01f * (float) elapsTime);
			if (fire3Fireballs) {
				fireball1.fwdAction(.01f * (float) elapsTime);
				fireball2.fwdAction(.01f * (float) elapsTime);
			}
		} else if (fireballCurrentlyMoving &&
				(fireball0.getWorldLocation().distance(initialFireballPosition) >= scriptController
						.getFireballTravelDistance())) {
			fireball0.getRenderStates().disableRendering();
			// Moves the fireball0 below the world so it still doesnt interact with objects
			// even though rendering was disabled
			fireball0.setLocalLocation(new Vector3f(0, -10, 0));
			if (fire3Fireballs) {
				fireball1.getRenderStates().disableRendering();
				fireball1.setLocalLocation(new Vector3f(0, -10, 0));
				fireball2.getRenderStates().disableRendering();
				fireball2.setLocalLocation(new Vector3f(0, -10, 0));
			}
			fireballCurrentlyMoving = false;

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


	private void keepPlayerOnTerrain() {
		Vector3f loc = avatar.getWorldLocation();
		float terrHeight = terr.getHeight(loc.x(), loc.z()) + .6f;
		avatar.setLocalLocation(new Vector3f(loc.x(), terrHeight, loc.z()));
		// if (terrHeight <= 10){
			
		// }
	}

	private void levelUp() {
		int currExp = playerStats.get("experience");
		int currLvl = playerStats.get("level");
		int currSkillPoint = playerStats.get("skillPoint");
		if (currExp >= 100) {
			currExp -= 100;
			currLvl++;
			currSkillPoint++;
			playerStats.replace("experience", currExp);
			playerStats.replace("level", currLvl);
			playerStats.replace("skillPoint", currSkillPoint);
			leveledUp = true;
		}
	}

	private void skillUpdate() {
		int currAvatarOrbiterLv = playerStats.get("avatarOrbiterLv");
		int currCircleLv = playerStats.get("circleLv");
		// AvatarOrbiter
		if (currAvatarOrbiterLv == 1) {
			avatarOrbiter1.setLocalScale(new Matrix4f().scale(2.5f, 0.3f, 2.5f));
			avatarOrbiter2.setLocalScale(new Matrix4f().scale(2.5f, 0.3f, 2.5f));
			avatarOrbiter1.getRenderStates().enableRendering();
			avatarOrbiter2.getRenderStates().enableRendering();
		} else if (currAvatarOrbiterLv == 2) {
			avatarOrbiter1.setLocalScale(new Matrix4f().scale(3.5f, 0.3f, 3.5f));
			avatarOrbiter2.setLocalScale(new Matrix4f().scale(3.5f, 0.3f, 3.5f));
		} else if (currAvatarOrbiterLv == 3) {
			avatarOrbiter3.setLocalScale(new Matrix4f().scale(3.5f, 0.3f, 3.5f));
			avatarOrbiter3.getRenderStates().enableRendering();
		} else if (currAvatarOrbiterLv == 4) {
			orbiterSpeed = 0.06f;
			avatarOrbiter1.setLocalScale(new Matrix4f().scale(4f, 0.3f, 4f));
			avatarOrbiter2.setLocalScale(new Matrix4f().scale(4f, 0.3f, 4f));
			avatarOrbiter3.setLocalScale(new Matrix4f().scale(4f, 0.3f, 4f));
		}
		// Circle
		if (currCircleLv == 1) {
			circle.getRenderStates().enableRendering();
		} else if (currCircleLv == 2) {
			circle.setLocalScale(new Matrix4f().scale(15f));
		} else if (currCircleLv == 3) {
			circle.setLocalScale(new Matrix4f().scale(20f));
		} else if (currCircleLv == 4) {
			rc.addTarget(circle);
		}
	}

	// May use for single person game
	// private void updateSkyboxes() {
	// if (switchSkyBoxes) {
	// (engine.getSceneGraph()).setActiveSkyBoxTexture(daySky);
	// engine.getSceneGraph().setSkyBoxEnabled(true);
	// } else {
	// (engine.getSceneGraph()).setActiveSkyBoxTexture(darkSky);
	// engine.getSceneGraph().setSkyBoxEnabled(true);
	// }
	// // Update the flag every 30 seconds
	// if (displayTime % 60 < 30) {
	// switchSkyBoxes = true;
	// } else {
	// switchSkyBoxes = false;
	// }
	// }

	private void checkForCollisions() {
		com.bulletphysics.dynamics.DynamicsWorld dynamicsWorld;
		com.bulletphysics.collision.broadphase.Dispatcher dispatcher;
		com.bulletphysics.collision.narrowphase.PersistentManifold manifold;
		com.bulletphysics.dynamics.RigidBody object1, object2;
		com.bulletphysics.collision.narrowphase.ManifoldPoint contactPoint;
		dynamicsWorld = ((JBulletPhysicsEngine) physicsEngine).getDynamicsWorld();
		dispatcher = dynamicsWorld.getDispatcher();
		int manifoldCount = dispatcher.getNumManifolds();
		for (int i = 0; i < manifoldCount; i++) {
			manifold = dispatcher.getManifoldByIndexInternal(i);
			object1 = (com.bulletphysics.dynamics.RigidBody) manifold.getBody0();
			object2 = (com.bulletphysics.dynamics.RigidBody) manifold.getBody1();
			JBulletPhysicsObject obj1 = JBulletPhysicsObject.getJBulletPhysicsObject(object1);
			JBulletPhysicsObject obj2 = JBulletPhysicsObject.getJBulletPhysicsObject(object2);
			for (int j = 0; j < manifold.getNumContacts(); j++) {
				contactPoint = manifold.getContactPoint(j);
				if (contactPoint.getDistance() < 0.0f) {
					System.out.println("---- hit between " + obj1 + " and " + obj2);
					break;
				}
			}
		}
	}

	private void checkAvatarSelect() {
		Vector3f avloc, mNPCloc, aNPCloc;
		float avmNPCDis, avaNPCDis;
		float avsize;
		float mNPCsize, aNPCsize;
		avloc = avatar.getWorldLocation();
		avsize = (avatar.getWorldScale()).m00();

		mNPCloc = mageNPC.getWorldLocation();
		mNPCsize = (mageNPC.getWorldScale()).m00();
		avmNPCDis = avloc.distance(mNPCloc);
		aNPCloc = archerNPC.getWorldLocation();
		aNPCsize = (archerNPC.getWorldScale()).m00();
		avaNPCDis = avloc.distance(aNPCloc);

		if (avmNPCDis - avsize - mNPCsize <= .5) {
			// Sets the current playable character to mage
			mage.getRenderStates().enableRendering();
			archer.getRenderStates().disableRendering();
			avatar.getRenderStates().disableRendering();
			avatar.setLocalTranslation((new Matrix4f()).translation(0, 0.6f, 0));
			playerStats.replace("class", 1);
			protClient.sendPlayerStatsMessage(playerStats);
		}

		if (avaNPCDis - avsize - aNPCsize <= .5) {
			// Sets the current playable character to archer
			archer.getRenderStates().enableRendering();
			mage.getRenderStates().disableRendering();
			avatar.getRenderStates().disableRendering();
			avatar.setLocalTranslation((new Matrix4f()).translation(0, 0.6f, 0));
			playerStats.replace("class", 2);
			protClient.sendPlayerStatsMessage(playerStats);
		}
	}

	private void checkTouchSoup() {
		Vector3f avloc, souploc;
		float avrocDis;
		float avsize;
		float soupsize;
		int elapsTimeSec = Math.round((float) displayTime);

		avloc = avatar.getWorldLocation();
		avsize = (avatar.getWorldScale()).m00();

		souploc = soup.getWorldLocation();
		soupsize = (soup.getWorldScale()).m00();
		avrocDis = avloc.distance(souploc);

		if (avrocDis - avsize - soupsize <= .5 && !isConsumed) {
			isBooster = true;
			soup.getRenderStates().disableRendering();
			soup.setLocalTranslation((new Matrix4f()).translation(randNum(), 1f, randNum()));
			isConsumed = true;
		}

		if (isBooster) {
			tenSec = elapsTimeSec + 10;
		}

		if (elapsTimeSec <= tenSec) {
			booster = true;
			isBooster = false;
		} else {
			booster = false;
			isConsumed = false;
			soup.getRenderStates().enableRendering();
		}
	}

	private void updateHUD() {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(3);
		RenderSystem rs = engine.getRenderSystem();
		Viewport mainVp = rs.getViewport(MAINVP);
		Viewport playerVP = rs.getViewport(AVATARVP);

		// build and set HUD
		int elapsTimeSec = Math.round((float) displayTime);
		String elapsTimeStr = Integer.toString(elapsTimeSec);
		String counterStr = Integer.toString(scoreCounter);
		String dispStr1 = "Time: " + elapsTimeStr;
		String dispStr2 = "Score: " + counterStr;
		String dispStr3 = "HP: " + playerStats.get("health");
		String dispStr4 = "Level: " + playerStats.get("level");
		String dispStr5 = "XP: " + playerStats.get("experience");
		String dispStr6;
		String dispStr7;

		Vector3f hud1Color = new Vector3f(1, 0, 1);
		Vector3f hud2Color = new Vector3f(0, 0, 1);
		Vector3f hud3Color = new Vector3f(1, 0, 0);
		Vector3f hud4Color = new Vector3f(1, 0, 0);
		Vector3f hud5Color = new Vector3f(1, 0, 0);
		Vector3f hud6Color = new Vector3f(0, 1, 0);

		if (leveledUp) {
			dispStr6 = "Level Up! New Skill Point obtained";
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					leveledUp = false;
				}
			}, 5000); // Delay in milliseconds
		} else {
			dispStr6 = "";
		}

		if (skillMenu) {
			fiveSec = elapsTimeSec + 5;
		}

		if (elapsTimeSec <= fiveSec) {
			dispStr7 = "Skill point: " + playerStats.get("skillPoint");
			hud6Color = new Vector3f(0, 1, 0);
			(engine.getHUDmanager()).setHUD7(dispStr7, hud6Color, (rs.getWidth() / 2), (rs.getHeight() / 2));
			skillMenu = false;
		} else {
			dispStr7 = "";
			(engine.getHUDmanager()).setHUD7(dispStr7, hud6Color, (rs.getWidth() / 2), (rs.getHeight() / 2));
		}

		int timeStrX = (int) (rs.getWidth() * mainVp.getRelativeWidth() / 2 - 40);
		int timeStrY = (int) (rs.getHeight() * mainVp.getRelativeHeight() - 80);
		(engine.getHUDmanager()).setHUD1(dispStr1, hud1Color, timeStrX, timeStrY);

		int scoreStrX = (int) (rs.getWidth() * mainVp.getRelativeWidth() / 2 - 40);
		int scoreStrY = (int) (rs.getHeight() * mainVp.getRelativeBottom() + 80);
		(engine.getHUDmanager()).setHUD2(dispStr2, hud2Color, scoreStrX, scoreStrY);

		int StrX = (int) (rs.getWidth() * playerVP.getRelativeWidth() + 10);
		int StrY = (int) (rs.getHeight() * playerVP.getRelativeBottom() + 140);
		(engine.getHUDmanager()).setHUD3(dispStr3, hud3Color, StrX, StrY);

		StrX = (int) (rs.getWidth() * playerVP.getRelativeWidth() + 10);
		StrY = (int) (rs.getHeight() * playerVP.getRelativeBottom() + 100);
		(engine.getHUDmanager()).setHUD4(dispStr4, hud4Color, StrX, StrY);

		StrX = (int) (rs.getWidth() * playerVP.getRelativeWidth() + 10);
		StrY = (int) (rs.getHeight() * playerVP.getRelativeBottom() + 60);
		(engine.getHUDmanager()).setHUD5(dispStr5, hud5Color, StrX, StrY);

		StrX = (int) (rs.getWidth() / 2);
		StrY = (int) (rs.getHeight() * mainVp.getRelativeBottom() + 200);
		(engine.getHUDmanager()).setHUD6(dispStr6, hud6Color, timeStrX, StrY);
	}

	public GameObject getAvatar() {
		return avatar;
	}

	public HashMap getPlayerStats() {
		return playerStats;
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

	public ObjShape getXPOrbShape() {
		return xpOrbS;
	}

	public TextureImage getXPOrbTexture() {
		return xpOrbT;
	}

	public ObjShape getGhostMageShape() {
		return ghostS_Mage;
	}

	public TextureImage getGhostMageTexture() {
		return ghostT_Mage;
	}

	public ObjShape getGhostArcherShape() {
		return ghostS_Archer;
	}

	public TextureImage getGhostArcherTexture() {
		return ghostT_Archer;
	}

	public GameObject getgavatarOrbiter1() {
		return gavatarOrbiter1;
	}

	public GameObject getgavatarOrbiter2() {
		return gavatarOrbiter2;
	}

	public GameObject getgavatarOrbiter3() {
		return gavatarOrbiter3;
	}

	public GameObject getgcircle() {
		return gcircle;
	}

	public GhostManager getGhostManager() {
		return gm;
	}

	public Vector3f getPlayerPosition() {
		return avatar.getWorldLocation();
	}

	public static Engine getEngine() {
		return engine;
	}

	public void setIsConnected(boolean value) {
		this.isClientConnected = value;
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

	public int getDaySky() {
		return daySky;
	}

	public int getDarkSky() {
		return darkSky;
	}

	// Keyboard can use Esc and = key
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_H: {
				mageAS.stopAnimation();
				mageAS.playAnimation("ATTACK", 0.5f,
						AnimatedShape.EndType.LOOP, 0);
				archerAS.stopAnimation();
				archerAS.playAnimation("ATTACK", 0.5f,
						AnimatedShape.EndType.LOOP, 0);
				break;
			}
			case KeyEvent.VK_B: {
				mageAS.stopAnimation();
				mageAS.playAnimation("MOVEATTACK", 0.5f,
						AnimatedShape.EndType.LOOP, 0);
				archerAS.stopAnimation();
				archerAS.playAnimation("MOVEATTACK", 0.5f,
						AnimatedShape.EndType.LOOP, 0);
				break;
			}
			case KeyEvent.VK_C: {
				// Temporary key pressed action. Used to test xp orb creation
				float randX = randNum();
				float randZ = randNum();
				dropXP(randX, 1f, randZ);
				spawnMonsterNormal(randX, 0.6f, randZ);
				break;
			}
			case KeyEvent.VK_V: {
				monsterNormalAS.stopAnimation();
				monsterNormalAS.playAnimation("MOVEATTACK", 0.5f,
						AnimatedShape.EndType.LOOP, 0);
				break;
			}
			case KeyEvent.VK_F: {
				skillMenu = true;
				break;
			}
			case KeyEvent.VK_1: {
				int sp = playerStats.get("skillPoint");
				int currHP = playerStats.get("health");
				int currATK = playerStats.get("atk");
				int currSPD = playerStats.get("spd");
				if (sp >= 1) {
					sp--;
					playerStats.replace("skillPoint", sp);
					playerStats.replace("spd", ++currSPD);
				}
				break;
			}
			case KeyEvent.VK_2: {
				int sp = playerStats.get("skillPoint");
				int currAvatarOrbiterLv = playerStats.get("avatarOrbiterLv");
				if (sp >= 1) {
					sp--;
					playerStats.replace("skillPoint", sp);
					playerStats.replace("avatarOrbiterLv", ++currAvatarOrbiterLv);
				}
				callSendPlayerStatsMessage();
				break;
			}
			case KeyEvent.VK_3: {
				int sp = playerStats.get("skillPoint");
				int currCircleLv = playerStats.get("circleLv");
				if (sp >= 1) {
					sp--;
					playerStats.replace("skillPoint", sp);
					playerStats.replace("circleLv", ++currCircleLv);
				}
				callSendPlayerStatsMessage();
				break;
			}
			case KeyEvent.VK_6: {
				getAvatar().setLocalLocation(
						new Vector3f(getAvatar().getLocalLocation().x(), 30f, getAvatar().getLocalLocation().z()));
				break;
			}
			case KeyEvent.VK_7: {
				getAvatar().setLocalLocation(
						new Vector3f(getAvatar().getLocalLocation().x(), 0.6f, getAvatar().getLocalLocation().z()));
				break;
			}
			case KeyEvent.VK_8: {
				// Sets the current playable character to mage
				mage.getRenderStates().enableRendering();
				archer.getRenderStates().disableRendering();
				avatar.getRenderStates().disableRendering();
				playerStats.replace("class", 1);
				protClient.sendPlayerStatsMessage(playerStats);
				break;
			}
			case KeyEvent.VK_9: {
				// Sets the current playable character to archer
				archer.getRenderStates().enableRendering();
				mage.getRenderStates().disableRendering();
				avatar.getRenderStates().disableRendering();
				playerStats.replace("class", 2);
				protClient.sendPlayerStatsMessage(playerStats);
				break;
			}
		}
		super.keyPressed(e);
	}

	// If the avatar is currently moving, it plays the movement animations
	private void playWalkAnimation() {
		lastFramePosition = currentFramePosition;
		currentFramePosition = avatar.getWorldLocation();

		if ((lastFramePosition.x() == currentFramePosition.x())
				&& (lastFramePosition.z() == currentFramePosition.z())) {
			currentlyMoving = false;
		} else {
			currentlyMoving = true;
		}

		if (currentlyMoving && !currentlyPlayingWalkAnimation) {
			currentlyPlayingWalkAnimation = true;
			mageAS.playAnimation("MOVE", 2f, AnimatedShape.EndType.LOOP, 0);
			archerAS.playAnimation("MOVE", 2f, AnimatedShape.EndType.LOOP, 0);
		} else if (!currentlyMoving && currentlyPlayingWalkAnimation) {
			mageAS.stopAnimation();
			archerAS.stopAnimation();
			currentlyPlayingWalkAnimation = false;
		}

	}

	public ScriptController getScriptController() {
		return this.scriptController;
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

	public void callSendMoveMessage() {
		(getAvatar().getWorldRotation()).getEulerAnglesZYX(orientationEuler);
		protClient.sendMoveMessage(getAvatar().getWorldLocation(), orientationEuler);
	}

	public void callSendPlayerStatsMessage() {
		protClient.sendPlayerStatsMessage(playerStats);
	}

	public void callSendChangeSkyBoxesMessage() {
		protClient.sendChangeSkyBoxesMessage(switchSkyBoxes);
	}

	public void callSendSpawnMonsterMessage() {
		protClient.sendSpawnMonsterMessage();
	}

	public void dropXP(float x, float y, float z) {
		GameObject xpOrb = new GameObject(GameObject.root(), xpOrbS, xpOrbT);
		Matrix4f initialTranslation = (new Matrix4f()).translation(x, y, z);
		xpOrb.setLocalTranslation(initialTranslation);
		Matrix4f initialScale = (new Matrix4f()).scaling(0.1f);
		xpOrb.setLocalScale(initialScale);
		// mc.addTarget(xpOrb);
		xpOrbs.add(xpOrb);
	}

	public void spawnMonsterNormal(float x, float y, float z) {
		monsterNormal = new GameObject(GameObject.root(), monsterNormalAS, monsterNormalT);
		Matrix4f initialTranslation = (new Matrix4f()).translation(x, y, z);
		monsterNormal.setLocalTranslation(initialTranslation);
		monsterNormal.getRenderStates().setModelOrientationCorrection(
				(new Matrix4f()).rotationY((float) java.lang.Math.toRadians(-90.0f)));
		Matrix4f initialScale = (new Matrix4f()).scaling(0.2f);
		monsterNormal.setLocalScale(initialScale);
		monsterNormals.add(monsterNormal);
	}

	private void checkTouchXPOrb() {
		Vector3f avloc, orbLoc;
		float avrocDis;
		float avsize;
		float orbsize;
		for (int i = 0; i < xpOrbs.size(); i++) {

			avloc = avatar.getWorldLocation();
			avsize = (avatar.getWorldScale()).m00();

			orbLoc = (xpOrbs.get(i)).getWorldLocation();
			orbsize = (xpOrbs.get(i)).getWorldScale().m00();
			avrocDis = avloc.distance(orbLoc);

			if (avrocDis - avsize - orbsize <= .5) {
				(engine.getSceneGraph()).removeGameObject(xpOrbs.get(i));
				(xpOrbs.get(i)).setLocalTranslation((new Matrix4f()).translation(50, -20, 50));
				xpOrbs.remove(i);
				increaseXP(scriptController.getXpGainedPerOrb());
			}
		}
	}

	private void increaseXP(int amount) {
		int currXP = playerStats.get("experience");
		int newXP = currXP + amount;
		playerStats.replace("experience", newXP);
	}

	public void rotateOrbiters(float speed) {
		amtt += speed;
		Matrix4f currentTranslation = avatarOrbiter1.getLocalTranslation();
		currentTranslation.translation((float) Math.sin(amtt) * 2f, 0.3f, (float) Math.cos(amtt) * 2f);
		avatarOrbiter1.setLocalTranslation(currentTranslation);

		amtt2 += speed;
		currentTranslation = avatarOrbiter2.getLocalTranslation();
		currentTranslation.translation((float) Math.sin(amtt2) * 2f, 0.3f, (float) Math.cos(amtt2) * 2f);
		avatarOrbiter2.setLocalTranslation(currentTranslation);

		amtt3 += speed;
		currentTranslation = avatarOrbiter3.getLocalTranslation();
		currentTranslation.translation((float) Math.sin(amtt3) * 2f, 0.3f, (float) Math.cos(amtt3) * 2f);
		avatarOrbiter3.setLocalTranslation(currentTranslation);

		// For ghost
		currentTranslation = gavatarOrbiter1.getLocalTranslation();
		currentTranslation.translation((float) Math.sin(amtt) * 2f, 0.3f, (float) Math.cos(amtt) * 2f);
		gavatarOrbiter1.setLocalTranslation(currentTranslation);
		currentTranslation = gavatarOrbiter2.getLocalTranslation();
		currentTranslation.translation((float) Math.sin(amtt2) * 2f, 0.3f, (float) Math.cos(amtt2) * 2f);
		gavatarOrbiter2.setLocalTranslation(currentTranslation);
		currentTranslation = gavatarOrbiter3.getLocalTranslation();
		currentTranslation.translation((float) Math.sin(amtt3) * 2f, 0.3f, (float) Math.cos(amtt3) * 2f);
		gavatarOrbiter3.setLocalTranslation(currentTranslation);
	}

	private void inputSetup() {
		// ----------------- INPUTS SECTION -----------------------------
		// im = e.getInputManager();
		FwdAction fwdAction = new FwdAction(this);
		BwdAction bwdAction = new BwdAction(this);
		FwdBwdAction fwdbwdAction = new FwdBwdAction(this);
		TeleportAction teleportAction = new TeleportAction(this);
		ToggleLightAction toggleLightAction = new ToggleLightAction(this);

		TurnRightAction turnRightAction = new TurnRightAction(this);
		TurnLeftAction turnLeftAction = new TurnLeftAction(this);
		TurnAction turnAction = new TurnAction(this);

		// SpeedUpAction speedUpAction = new SpeedUpAction(this);

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
		im.associateActionWithAllKeyboards(
				net.java.games.input.Component.Identifier.Key.L, toggleLightAction,
				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		// im.associateActionWithAllKeyboards(
		// net.java.games.input.Component.Identifier.Key.LSHIFT, speedUpAction,
		// InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);

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

	public void startTeleportCooldown() {
		this.inTeleportCooldown = true;
		timeToEndCooldown = displayTime + scriptController.getTeleportCooldownTime();
	}

	public boolean getInTeleportCooldown() {
		return this.inTeleportCooldown;
	}

	private void handleTeleportCooldown() {
		if (displayTime >= timeToEndCooldown) {
			this.inTeleportCooldown = false;
		}
	}

	private void moveLightsWithAvatar(){
		light1.setLocation(new Vector3f(avatar.getWorldLocation().x(), avatar.getWorldLocation().y() + 10, avatar.getWorldLocation().z()));
	}

	public void toggleLight(){
		//Added a new removeLight method to TAGE. Changed SceneGraph and LightManager
		if(lightsEnabled){
			(engine.getSceneGraph()).removeLight(light2);
			lightsEnabled = false;
		}
		else {
			(engine.getSceneGraph()).addLight(light2);
			lightsEnabled = true;
		}
	}

}