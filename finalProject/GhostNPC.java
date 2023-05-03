package finalProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import tage.*;
import tage.shapes.ImportedModel;
import tage.shapes.Torus;

import org.joml.*;

public class GhostNPC extends GameObject {
    private int id;

    public GhostNPC(int id, ObjShape s, TextureImage t, Vector3f p) {
        super(GameObject.root(), s, t);
        this.id = id;
        setPosition(p);
    }

    public void setSize(boolean big) {
        if (!big) {
            this.setLocalScale((new Matrix4f()).scaling(0.5f));
        } else {
            this.setLocalScale((new Matrix4f()).scaling(1.0f));
        }
    }

    public void setPosition(Vector3f p) {
        setLocalLocation(p);
    }

    public void attack() {
        //this.lookAt(ghostAvatar);
        this.setLocalScale((new Matrix4f()).scaling(1.0f));
    }

    public void petrol() {
        this.setLocalScale((new Matrix4f()).scaling(0.5f));
    }
}
