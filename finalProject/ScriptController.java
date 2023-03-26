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
		startingHealth = ((int) (jsEngine.get("startingHealth")));
		startingLevel = ((int) (jsEngine.get("startingLevel")));
		startingExperience = ((int) (jsEngine.get("startingExperience")));
		startingSkillPoint = ((int) (jsEngine.get("startingSkillPoint")));
		atk = ((int) (jsEngine.get("atk")));
		
		fireballLv = ((int) jsEngine.get("fireballLv"));
		fireballTravelDistance = ((Double) (jsEngine.get("fireballTravelDistance"))).floatValue();
		avatarOrbiterLv = ((int) jsEngine.get("avatarOrbiterLv"));
		orbiterSpeed = ((Double) (jsEngine.get("orbiterSpeed"))).floatValue();
		circleLv = ((int) jsEngine.get("circleLv"));

		xpGainedPerOrb = ((int) (jsEngine.get("xpGainedPerOrb")));
		monsterAtk = ((int) (jsEngine.get("monsterAtk")));
		monsterSpeed = ((int) (jsEngine.get("monsterSpeed")));
		monsterHealth = ((int) (jsEngine.get("monsterHealth")));
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
}