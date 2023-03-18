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

	public ScriptController() {
		ScriptEngineManager factory = new ScriptEngineManager();
		jsEngine = factory.getEngineByName("js");

		scriptFile1 = new File("assets/scripts/InitParams.js");
		this.runScript(scriptFile1);
		initializeParameters();
	}

	private void initializeParameters() {
		baseSpeed = ((Double) (jsEngine.get("baseSpeed"))).floatValue();
		sprintSpeed = ((Double) (jsEngine.get("sprintSpeed"))).floatValue();
		teleportDistance = ((Double) (jsEngine.get("teleportDistance"))).floatValue();
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
}