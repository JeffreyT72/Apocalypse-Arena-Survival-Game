package tage.nodeControllers;
import tage.*;
import org.joml.*;

public class BounceController extends NodeController {
    private float cycleTime = 2.0f;
    private float totalTime = 0.0f;
    private float direction = 0.2f;

    public BounceController(Engine e) { 
        super();
    }

    public void apply(GameObject go) { 
        float elapsedTime = super.getElapsedTime();
        totalTime += elapsedTime/1000.0f;
        if (totalTime > cycleTime) { 
            direction = -direction;
            totalTime = 0.0f;
        }

        Vector3f oldPosition = go.getWorldLocation();
        Vector4f fwdDirection;
		fwdDirection = new Vector4f(0f, direction, 0f, 1f);
		fwdDirection.mul(0.01f);
		Vector3f newPosition = oldPosition.add(fwdDirection.x(),
				fwdDirection.y(), fwdDirection.z());
		go.setLocalLocation(newPosition);
    }
}
