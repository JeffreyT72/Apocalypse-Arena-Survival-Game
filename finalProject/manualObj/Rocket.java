package finalProject.manualObj;
import tage.*;
import tage.shapes.*;

public class Rocket extends ManualObject {
    private float[] vertices = new float[] {
            -1.0f, -1.0f,  1.0f,    1.0f, -1.0f,  1.0f,    0.0f,  1.0f,  0.0f,    //front
             1.0f, -1.0f,  1.0f,    1.0f, -1.0f, -1.0f,    0.0f,  1.0f,  0.0f,    //right
             1.0f, -1.0f, -1.0f,   -1.0f, -1.0f, -1.0f,    0.0f,  1.0f,  0.0f,    //back
            -1.0f, -1.0f, -1.0f,   -1.0f, -1.0f,  1.0f,    0.0f,  1.0f,  0.0f,    //left
            -1.0f, -4.0f, -1.0f,    1.0f, -4.0f,  1.0f,   -1.0f, -4.0f,  1.0f,    //LF
             1.0f, -4.0f,  1.0f,   -1.0f, -4.0f, -1.0f,    1.0f, -4.0f, -1.0f,     //RR
             // rocket body
            -1.0f, -1.0f,  1.0f,    1.0f, -1.0f,  1.0f,   -1.0f, -4.0f,  1.0f,    //front up
             1.0f, -4.0f,  1.0f,   -1.0f, -4.0f,  1.0f,    1.0f, -1.0f,  1.0f,     //front down

            // Right
            1.0f, -1.0f, 1.0f,   1.0f, -1.0f, -1.0f,   1.0f, -4.0f, 1.0f,
            1.0f, -4.0f, -1.0f,  1.0f, -4.0f, 1.0f,   1.0f, -1.0f, -1.0f,
// Back
            1.0f, -1.0f, -1.0f,   -1.0f, -1.0f, -1.0f,  1.0f, -4.0f, -1.0f,
            -1.0f, -4.0f, -1.0f,  1.0f, -4.0f, -1.0f,  -1.0f, -1.0f, -1.0f,
// Left
            -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -4.0f, -1.0f,
            -1.0f, -4.0f, 1.0f, -1.0f, -4.0f, -1.0f, -1.0f, -1.0f, 1.0f,

            -1.0f, -4.0f, 1.0f, -2.0f, -4.0f, 1.0f, -1.0f, -3.0f, 1.0f,
            -1.0f, -4.0f, -1.0f, -2.0f, -4.0f, -1.0f, -1.0f, -3.0f, -1.0f,

            -1.0f, -3.0f, -1.0f, -1.0f, -3.0f, 1.0f, -2.0f, -4.0f, -1.0f,
            -2.0f, -4.0f, 1.0f, -2.0f, -4.0f, -1.0f, -1.0f, -3.0f, 1.0f,

            1.0f, -4.0f, 1.0f, 2.0f, -4.0f, 1.0f, 1.0f, -3.0f, 1.0f,
            1.0f, -4.0f, -1.0f, 2.0f, -4.0f, -1.0f, 1.0f, -3.0f, -1.0f,

            1.0f, -3.0f, 1.0f, 1.0f, -3.0f, -1.0f, 2.0f, -4.0f, 1.0f,
            2.0f, -4.0f, -1.0f, 2.0f, -4.0f, 1.0f, 1.0f, -3.0f, -1.0f
    };

    private float[] texcoords = new float[] {
            0.0f, 0.5f,   0.5f, 0.5f,    0.25f, 1.0f,
            0.0f, 0.5f,   0.5f, 0.5f,    0.25f, 1.0f,
            0.0f, 0.5f,   0.5f, 0.5f,    0.25f, 1.0f,
            0.0f, 0.5f,   0.5f, 0.5f,    0.25f, 1.0f,
            0.0f, 0.5f,   0.5f, 0.5f,    0.25f, 1.0f,
            0.0f, 0.5f,   0.5f, 0.5f,    0.25f, 1.0f,

            0.0f, 0.5f,   0.5f, 0.5f,   0.0f, 0.0f,     //front up
            0.5f, 0.0f,   0.0f, 0.0f,   0.5f, 0.5f,      //front down
            0.0f, 0.5f,   0.5f, 0.5f,   0.0f, 0.0f,     //front up
            0.5f, 0.0f,   0.0f, 0.0f,   0.5f, 0.5f,      //front down
            0.0f, 0.5f,   0.5f, 0.5f,   0.0f, 0.0f,     //front up
            0.5f, 0.0f,   0.0f, 0.0f,   0.5f, 0.5f,      //front down
            0.0f, 0.5f,   0.5f, 0.5f,   0.0f, 0.0f,     //front up
            0.5f, 0.0f,   0.0f, 0.0f,   0.5f, 0.5f,      //front down

            0.5f, 0.0f,  1.0f, 0.0f,  1.0f, 0.5f,
            0.5f, 0.0f,  1.0f, 0.0f,  1.0f, 0.5f,

            0.5f, 0.5f,  1.0f, 0.5f,  1.0f, 1.0f,
            0.5f, 0.5f,  1.0f, 0.5f,  1.0f, 1.0f,

            0.5f, 0.0f,  1.0f, 0.0f,  1.0f, 0.5f,
            0.5f, 0.0f,  1.0f, 0.0f,  1.0f, 0.5f,

            0.5f, 0.5f,  1.0f, 0.5f,  1.0f, 1.0f,
            0.5f, 0.5f,  1.0f, 0.5f,  1.0f, 1.0f
    };

    private float[] normals = new float[] {
            0.0f, 1.0f, 1.0f,     0.0f, 1.0f, 1.0f,      0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f,     1.0f, 1.0f, 0.0f,      1.0f, 1.0f, 0.0f,
            0.0f, 1.0f, -1.0f,    0.0f, 1.0f, -1.0f,    0.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 0.0f,   -1.0f, 1.0f, 0.0f,     -1.0f, 1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,    0.0f, -1.0f, 0.0f,    0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,    0.0f, -1.0f, 0.0f,    0.0f, -1.0f, 0.0f,
            // rocket normal
            0.0f, 0.0f, 1.0f,     0.0f, 0.0f, 1.0f,      0.0f, 0.0f, 1.0f,   //front up
            0.0f, 0.0f, 1.0f,     0.0f, 0.0f, 1.0f,      0.0f, 0.0f, 1.0f,    //front down

            // Right
            1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
// Back
            0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f,
// Left
            -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,

            0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f,

            0.0f, 1.0f ,1.0f, 0.0f, 1.0f ,1.0f, 0.0f, 1.0f ,1.0f,
            0.0f, 1.0f ,1.0f, 0.0f, 1.0f ,1.0f, 0.0f, 1.0f ,1.0f,

            0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f,

            1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f
    };

    public Rocket() {
        super();
        setNumVertices(66);
        setVertices(vertices);
        setTexCoords(texcoords);
        setNormals(normals);

        setMatAmb(Utils.silverAmbient());
        setMatDif(Utils.silverDiffuse());
        setMatSpe(Utils.silverSpecular());
        setMatShi(Utils.silverShininess());
    }
}
