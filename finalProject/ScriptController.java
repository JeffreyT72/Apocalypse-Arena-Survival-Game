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
	private float baseSpeed;
	private float sprintSpeed;
	private float teleportDistance;
	private int startingHealth;
	private int startingLevel;
	private int startingExperience;
	private int atk;
	private float fireballTravelDistance;
	private int xpGainedPerOrb;

	public ScriptController() {
		ScriptEngineManager factory = new ScriptEngineManager();
		jsEngine = factory.getEngineByName("js");

		scriptFile1 = new File("assets/scripts/InitParams.js");
		this.runScript(scriptFile1);
		initializeParameters();
	}

    private void initializeParameters(){
        baseSpeed = ((Double)(jsEngine.get("baseSpeed"))).floatValue();
		sprintSpeed = ((Double)(jsEngine.get("sprintSpeed"))).floatValue();
		teleportDistance = ((Double)(jsEngine.get("teleportDistance"))).floatValue();
		startingHealth = ((int)(jsEngine.get("startingHealth")));
		startingLevel = ((int)(jsEngine.get("startingLevel")));
		startingExperience = ((int)(jsEngine.get("startingExperience")));
		atk = ((int)(jsEngine.get("atk")));
		fireballTravelDistance = ((Double)(jsEngine.get("fireballTravelDistance"))).floatValue();
		xpGainedPerOrb = ((int)(jsEngine.get("xpGainedPerOrb")));
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
	public float getBaseSpeed() {
		return this.baseSpeed;
	}

	public float getSprintSpeed() {
		return this.sprintSpeed;
	}

	public float getTeleportDistance() {
		return this.teleportDistance;
	}
	public int getStartingHealth(){
		return this.startingHealth;
	}
	public int getStartingLevel(){
		return this.startingLevel;
	}
	public int getStartingExperience(){
		return this.startingExperience;
	}
	public int getAtk(){
		return this.atk;
	}
	public float getFireballTravelDistance(){
		return this.fireballTravelDistance;
	}
	public int getXpGainedPerOrb(){
		return this.xpGainedPerOrb;
	}
}