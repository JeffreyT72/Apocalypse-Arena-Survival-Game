package finalProject;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.*;
import javax.script.Invocable;

public class ScriptController {
	private File scriptFile1;
	private ScriptEngine jsEngine;

	// Game Parameters
	private int baseSpeed;
	private float sprintSpeed;
	private float teleportDistance;
	private int startingHealth;
	private int startingLevel;
	private int startingExperience;
	private int startingSkillPoint;
	private int atk;
	private int fireballLv;
	private float fireballTravelDistance;
	private int avatarOrbiterLv;
	private float orbiterSpeed;
	private int circleLv;
	private int xpGainedPerOrb;
	private int monsterSpeed;
	private int monsterHealth;
	private int monsterAtk;
	private double teleportCooldownTime;
	private double timeBetweenPassiveHealing;
	private int passiveHealingAmount;
	private int upgrade1;
	private int upgrade2;
	private int upgrade3;
	private int upgrade4;
	private int upgrade5;
	private int upgrade6;
	private int upgrade7;
	private int upgrade8;
	private int baseSoundVolume;
	private int maxLevelXP;
	private float arenaInvisibleWallDistance;
	private int icecreamHealthAmount;
	private float terrainY;
	private float terrainScale;
	private float planeY;
	private float planeScale;
	private float grenadeX;
	private float grenadeY;
	private float grenadeZ;
	private float townX;
	private float townZ;
	private float orbiterYHeight;
	private float angelX;
	private float angelY;
	private float angelZ;
	private float ambientLight;
	private float avatarX;
	private float avatarY;
	private float avatarZ;
	private float spotlightHeightAboveAvatar;
	private float gravity;
	private float grenadeBounciness;
	private float rcMin;
	private float rcMax;
	private float orbiterLvl1Scale;
	private float orbiterLvl2Scale;
	private float orbiterLvl4Scale;
	private float rangerAttackForce;


	public ScriptController() {
		ScriptEngineManager factory = new ScriptEngineManager();
		jsEngine = factory.getEngineByName("js");

		scriptFile1 = new File("assets/scripts/InitParams.js");
		this.runScript(scriptFile1);
		initializeParameters();
	}

	private void initializeParameters() {
		baseSpeed = (int) jsEngine.get("baseSpeed");
		sprintSpeed = ((Double) (jsEngine.get("sprintSpeed"))).floatValue();
		teleportDistance = ((Double) (jsEngine.get("teleportDistance"))).floatValue();
		arenaInvisibleWallDistance = ((Double) (jsEngine.get("arenaInvisibleWallDistance"))).floatValue();
		terrainScale = ((Double) (jsEngine.get("terrainScale"))).floatValue();
		terrainY = ((Double) (jsEngine.get("terrainY"))).floatValue();
		planeY = ((Double) (jsEngine.get("planeY"))).floatValue();
		planeScale = ((Double) (jsEngine.get("planeScale"))).floatValue();
		grenadeX = ((Double) (jsEngine.get("grenadeX"))).floatValue();
		grenadeY = ((Double) (jsEngine.get("grenadeY"))).floatValue();
		grenadeZ = ((Double) (jsEngine.get("grenadeZ"))).floatValue();
		townX = ((Double) (jsEngine.get("townX"))).floatValue();
		townZ = ((Double) (jsEngine.get("townZ"))).floatValue();
		orbiterYHeight = ((Double) (jsEngine.get("orbiterYHeight"))).floatValue();
		angelX = ((Double) (jsEngine.get("angelX"))).floatValue();
		angelY = ((Double) (jsEngine.get("angelY"))).floatValue();
		angelZ = ((Double) (jsEngine.get("angelZ"))).floatValue();
		ambientLight = ((Double) (jsEngine.get("ambientLight"))).floatValue();
		avatarX = ((Double) (jsEngine.get("avatarX"))).floatValue();
		avatarY = ((Double) (jsEngine.get("avatarY"))).floatValue();
		avatarZ = ((Double) (jsEngine.get("avatarZ"))).floatValue();
		spotlightHeightAboveAvatar = ((Double) (jsEngine.get("spotlightHeightAboveAvatar"))).floatValue();
		gravity = ((Double) (jsEngine.get("gravity"))).floatValue();
		grenadeBounciness = ((Double) (jsEngine.get("grenadeBounciness"))).floatValue();
		rcMin = ((Double) (jsEngine.get("rcMin"))).floatValue();
		rcMax = ((Double) (jsEngine.get("rcMax"))).floatValue();
		orbiterLvl1Scale = ((Double) (jsEngine.get("orbiterLvl1Scale"))).floatValue();
		orbiterLvl2Scale = ((Double) (jsEngine.get("orbiterLvl2Scale"))).floatValue();
		orbiterLvl4Scale = ((Double) (jsEngine.get("orbiterLvl4Scale"))).floatValue();
		rangerAttackForce = ((Double) (jsEngine.get("rangerAttackForce"))).floatValue();

		startingHealth = ((int) (jsEngine.get("startingHealth")));
		startingLevel = ((int) (jsEngine.get("startingLevel")));
		startingExperience = ((int) (jsEngine.get("startingExperience")));
		startingSkillPoint = ((int) (jsEngine.get("startingSkillPoint")));
		atk = ((int) (jsEngine.get("atk")));
		teleportCooldownTime = ((Double) (jsEngine.get("teleportCooldownTime")));
		timeBetweenPassiveHealing = ((Double) (jsEngine.get("timeBetweenPassiveHealing")));
		passiveHealingAmount = ((int) (jsEngine.get("passiveHealingAmount")));
		icecreamHealthAmount = ((int) (jsEngine.get("icecreamHealthAmount")));
		baseSoundVolume = ((int) (jsEngine.get("baseSoundVolume")));
		
		fireballLv = ((int) jsEngine.get("fireballLv"));
		fireballTravelDistance = ((Double) (jsEngine.get("fireballTravelDistance"))).floatValue();
		avatarOrbiterLv = ((int) jsEngine.get("avatarOrbiterLv"));
		orbiterSpeed = ((Double) (jsEngine.get("orbiterSpeed"))).floatValue();
		circleLv = ((int) jsEngine.get("circleLv"));

		xpGainedPerOrb = ((int) (jsEngine.get("xpGainedPerOrb")));
		monsterAtk = ((int) (jsEngine.get("monsterAtk")));
		monsterSpeed = ((int) (jsEngine.get("monsterSpeed")));
		monsterHealth = ((int) (jsEngine.get("monsterHealth")));
		maxLevelXP = ((int) (jsEngine.get("maxLevelXP")));

		upgrade1 = ((int) (jsEngine.get("upgrade1")));
		upgrade2 = ((int) (jsEngine.get("upgrade2")));
		upgrade3 = ((int) (jsEngine.get("upgrade3")));
		upgrade4 = ((int) (jsEngine.get("upgrade4")));
		upgrade5 = ((int) (jsEngine.get("upgrade5")));
		upgrade6 = ((int) (jsEngine.get("upgrade6")));
		upgrade7 = ((int) (jsEngine.get("upgrade7")));
		upgrade8 = ((int) (jsEngine.get("upgrade8")));
	}

	private void runScript(File scriptFile) {
		try {
			FileReader fileReader = new FileReader(scriptFile);
			jsEngine.eval(fileReader);
			fileReader.close();
		} catch (FileNotFoundException e1) {
			System.out.println(scriptFile + " not found " + e1);
		} catch (IOException e2) {
			System.out.println("IO problem with " + scriptFile + e2);
		} catch (ScriptException e3) {
			System.out.println("ScriptException in " + scriptFile + e3);
		} catch (NullPointerException e4) {
			System.out.println("Null ptr exception reading " + scriptFile + e4);
		}
	}

	// --------------Getters for Parameters--------------------
	public int getBaseSpeed() {
		return this.baseSpeed;
	}

	public float getSprintSpeed() {
		return this.sprintSpeed;
	}

	public float getTeleportDistance() {
		return this.teleportDistance;
	}

	public int getStartingHealth() {
		return this.startingHealth;
	}

	public int getStartingLevel() {
		return this.startingLevel;
	}

	public int getStartingExperience() {
		return this.startingExperience;
	}

	public int getStartingSkillPoint() {
		return this.startingSkillPoint;
	}

	public int getAtk() {
		return this.atk;
	}

	public int getfireballLv() {
		return this.fireballLv;
	}

	public float getFireballTravelDistance() {
		return this.fireballTravelDistance;
	}

	public int getAvatarOrbiterLv() {
		return this.avatarOrbiterLv;
	}

	public float getOrbiterSpeed() {
		return this.orbiterSpeed;
	}

	public int getCircleLv() {
		return this.circleLv;
	}

	public int getXpGainedPerOrb() {
		return this.xpGainedPerOrb;
	}

	public int getMonsterHealth() {
		return this.monsterHealth;
	}

	public int getMonsterSpeed() {
		return this.monsterSpeed;
	}

	public int getMonsterAtk() {
		return this.monsterAtk;
	}

	public double getTeleportCooldownTime(){
		return this.teleportCooldownTime;
	}

	public double getTimeBetweenPassiveHealing(){
		return this.timeBetweenPassiveHealing;
	}

	public int getPassiveHealingAmount(){
		return this.passiveHealingAmount;
	}

	public int getUpgrade1(){
		return this.upgrade1;
	}

	public int getUpgrade2(){
		return this.upgrade2;
	}

	public int getUpgrade3(){
		return this.upgrade3;
	}

	public int getUpgrade4(){
		return this.upgrade4;
	}

	public int getUpgrade5(){
		return this.upgrade5;
	}
	public int getUpgrade6(){
		return this.upgrade6;
	}
	public int getUpgrade7(){
		return this.upgrade7;
	}
	public int getUpgrade8(){
		return this.upgrade8;
	}

	public float getArenaInvisibleWallDistance(){
		return this.arenaInvisibleWallDistance;
	}

	public int getIcreamHealthAmount(){
		return this.icecreamHealthAmount;
	}
	public int getbaseSoundVolume(){
		return this.baseSoundVolume;
	}
	public int getmaxLevelXP(){
		return this.maxLevelXP;
	}

	public float getterrainY(){
		return this.terrainY;
	}
	public float getterrainScale(){
		return this.terrainScale;
	}
	public float getplaneY(){
		return this.planeY;
	}
	public float getplaneScale(){
		return this.planeScale;
	}
	public float getgrenadeX(){
		return this.grenadeX;
	}
	public float getgrenadeY(){
		return this.grenadeY;
	}
	public float getgrenadeZ(){
		return this.grenadeZ;
	}

	public float gettownX(){
		return this.townX;
	}
	public float gettownZ(){
		return this.townZ;
	}
	public float getorbiterYHeight(){
		return this.orbiterYHeight;
	}
	public float getangelX(){
		return this.angelX;
	}
	public float getangelY(){
		return this.angelY;
	}
	public float getangelZ(){
		return this.angelZ;
	}
	public float getambientLight(){
		return this.ambientLight;
	}
	public float getavatarX(){
		return this.avatarX;
	}
	public float getavatarY(){
		return this.avatarY;
	}
	public float getavatarZ(){
		return this.avatarZ;
	}
	public float getspotlightHeightAboveAvatar(){
		return this.spotlightHeightAboveAvatar;
	}
	public float getgravity(){
		return this.gravity;
	}
	public float getgrenadeBounciness(){
		return this.grenadeBounciness;
	}
	public float getrcMin(){
		return this.rcMin;
	}
	public float getrcMax(){
		return this.rcMax;
	}
	public float getorbiterLvl1Scale(){
		return this.orbiterLvl1Scale;
	}
	public float getorbiterLvl2Scale(){
		return this.orbiterLvl2Scale;
	}
	public float getorbiterLvl4Scale(){
		return this.orbiterLvl4Scale;
	}
	public float getrangerAttackForce(){
		return this.rangerAttackForce;
	}
}