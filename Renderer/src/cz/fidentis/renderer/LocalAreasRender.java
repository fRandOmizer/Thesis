package cz.fidentis.renderer;

import com.hackoeur.jglm.Mat3;
import cz.fidentis.comparison.localAreas.Area;
import cz.fidentis.comparison.localAreas.LocalAreas;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;
import com.jogamp.common.nio.Buffers;
import cz.fidentis.model.Model;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_ARRAY_BUFFER;
import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DYNAMIC_DRAW;
import static javax.media.opengl.GL.GL_FLOAT;
import static javax.media.opengl.GL.GL_FRONT_AND_BACK;
import static javax.media.opengl.GL.GL_LINES;
import static javax.media.opengl.GL.GL_LINE_LOOP;
import static javax.media.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static javax.media.opengl.GL.GL_POINTS;
import static javax.media.opengl.GL.GL_SRC_ALPHA;
import static javax.media.opengl.GL.GL_TRIANGLE_FAN;
import javax.media.opengl.GL2;
import static javax.media.opengl.GL2GL3.GL_FILL;
import static javax.media.opengl.GL2GL3.GL_LINE;
import static javax.media.opengl.GL2GL3.GL_VERTEX_ARRAY_BINDING;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

/**
 *
 * @author zanri
 */
public class LocalAreasRender{
    private Boolean isSetUp;
    private LocalAreas localAreas;

    // our OpenGL resources
    private int vertexBuffer;
    private int vertexArray;
    private int joglArray;
    
    private int colorBuffer;
    //private int indicesBuffer;
    
    // our GLSL resources
    private int positionAttribLoc;
    private int colorAttribLoc;
    private int normalAttribLoc;
    private int vertMvpUniformLoc;
    private int normMvpUniformLoc;
    private Mat4 pointTranfMatrix;
    private boolean isDrawPoint;
    private boolean isClearSelection;
    private float[] pointToDraw;

    
    public LocalAreasRender(){
        this.isSetUp = false;
        this.localAreas = new LocalAreas();
        isDrawPoint = false;
        isClearSelection = false;
    }
    
    public void SetUp(int[] areasIndexes, List<Area> areas, Model model){
        localAreas.SetAreas(areasIndexes, areas, model);
        this.isSetUp = true;
        this.isClearSelection = false;
    }
    
    public void setPointToDraw(Vector4f pointToDraw){
        this.pointToDraw = new float[] {pointToDraw.x, pointToDraw.y, pointToDraw.z};
        isDrawPoint = true;
    }
    
    public Boolean IsSetUp(){
        return isSetUp;
    }
    
    public void clearSelection(){
        isClearSelection = true;
    }

    public void HideLocalAreas(){
        this.isSetUp = false;
        isDrawPoint = false;
    }
    
    public Mat4 getMatrix(){
        return this.pointTranfMatrix;
    }
    
    public LocalAreas getLocalAreasBoundary(){
        return localAreas;
    }
    
    public GL2 drawLocalAreas(GL2 gl, int vertexShaderID, double[] a, double[] b){
        
        if (isClearSelection){
            return gl;
        }
        
        float[] vertexesAreas = localAreas.getVertexAreas();
        float[] colorAreas = localAreas.getVertexAreasColors();
        
        pointTranfMatrix = Mat4.MAT4_IDENTITY;

        float[] projectionArray = new float[16];
        for (int i = 0 ; i < 16; i++)
        {
            projectionArray [i] = (float) a[i];
        }
        Mat4 projection = new Mat4(projectionArray);
        float[] viewArray = new float[16];
        for (int i = 0 ; i < 16; i++)
        {
            viewArray[i] = (float) b[i];
        }
        Mat4 viewMat = new Mat4(viewArray);

        pointTranfMatrix = pointTranfMatrix.multiply(projection);
        pointTranfMatrix = pointTranfMatrix.multiply(viewMat);
        
        Mat3 n = invertMatrix(getMat3(pointTranfMatrix));
        n = n.transpose();

        gl.glClear(GL_DEPTH_BUFFER_BIT);
        gl.glEnable (GL_BLEND); 
        gl.glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        gl.glLineWidth(2);
        


            
            
        gl.glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, vertexesAreas.length * Buffers.SIZEOF_FLOAT,
                Buffers.newDirectFloatBuffer(vertexesAreas), GL_DYNAMIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, colorAreas.length * Buffers.SIZEOF_FLOAT,
                Buffers.newDirectFloatBuffer(colorAreas), GL_DYNAMIC_DRAW);

        gl.glUseProgram(vertexShaderID);

        gl.glUniformMatrix4fv(vertMvpUniformLoc, 1, false, pointTranfMatrix.getBuffer());
        gl.glUniformMatrix3fv(normMvpUniformLoc, 1, false, n.getBuffer());

        gl.glBindVertexArray(vertexArray);


        gl.glDrawArrays(GL2.GL_TRIANGLES, 0, vertexesAreas.length/6);




        gl.glBindVertexArray(joglArray);

        
        gl.glClear(GL_DEPTH_BUFFER_BIT);
        
        gl.glPointSize(3);
            
            
        gl.glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, localAreas.getVertexes().length * Buffers.SIZEOF_FLOAT,
                Buffers.newDirectFloatBuffer(localAreas.getVertexes()), GL_DYNAMIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, localAreas.getVertexColors().length * Buffers.SIZEOF_FLOAT,
                Buffers.newDirectFloatBuffer(localAreas.getVertexColors()), GL_DYNAMIC_DRAW);

        gl.glUseProgram(vertexShaderID);

        gl.glUniformMatrix4fv(vertMvpUniformLoc, 1, false, pointTranfMatrix.getBuffer());
        gl.glUniformMatrix3fv(normMvpUniformLoc, 1, false, n.getBuffer());

        gl.glBindVertexArray(vertexArray);


        gl.glDrawArrays(GL2.GL_POINTS, 0, localAreas.getVertexes().length/6);

        gl.glBindVertexArray(joglArray);

        gl.glClear(GL_DEPTH_BUFFER_BIT);
        
        gl.glLineWidth(1f);
            
            
        gl.glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, localAreas.getBorder().length * Buffers.SIZEOF_FLOAT,
                Buffers.newDirectFloatBuffer(localAreas.getBorder()), GL_DYNAMIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, localAreas.getBorderColor().length * Buffers.SIZEOF_FLOAT,
                Buffers.newDirectFloatBuffer(localAreas.getBorderColor()), GL_DYNAMIC_DRAW);

        gl.glUseProgram(vertexShaderID);

        gl.glUniformMatrix4fv(vertMvpUniformLoc, 1, false, pointTranfMatrix.getBuffer());
        gl.glUniformMatrix3fv(normMvpUniformLoc, 1, false, n.getBuffer());

        gl.glBindVertexArray(vertexArray);


        gl.glDrawArrays(GL2.GL_LINES, 0, localAreas.getBorder().length/6);

        gl.glBindVertexArray(joglArray);
        
        
        
        gl.glClear(GL_DEPTH_BUFFER_BIT);
        gl.glPointSize(5);
        
        if (isDrawPoint){
            
            gl.glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
            gl.glBufferData(GL_ARRAY_BUFFER, 6 * Buffers.SIZEOF_FLOAT,
                    Buffers.newDirectFloatBuffer(pointToDraw), GL_DYNAMIC_DRAW);

            gl.glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
            gl.glBufferData(GL_ARRAY_BUFFER, 4 * Buffers.SIZEOF_FLOAT,
                    Buffers.newDirectFloatBuffer(new float[]  {1.0f, 0.0f, 0.0f, 1.0f}), GL_DYNAMIC_DRAW);

            gl.glUseProgram(vertexShaderID);

            gl.glUniformMatrix4fv(vertMvpUniformLoc, 1, false, pointTranfMatrix.getBuffer());
            gl.glUniformMatrix3fv(normMvpUniformLoc, 1, false, n.getBuffer());

            gl.glBindVertexArray(vertexArray);
            
            
            
            gl.glDrawArrays(GL_POINTS, 0, 1);
            
            
            gl.glBindVertexArray(joglArray);
        }
        
        return gl;
    }
    
    public void renderFace(int i, boolean flipTexCoords, GL2 gl) /* Render the ith face by getting the vertex, normal, and tex
     coord indicies for face i. Use those indicies to access the
     actual vertex, normal, and tex coord data, and render the face.

     Each face uses 3 array of indicies; one for the vertex
     indicies, one for the normal indicies, and one for the tex
     coord indicies.

     If the model doesn't use normals or tex coords then the indicies
     arrays will contain 0's.

     If the tex coords need flipping then the t-values are changed.
     */ {
//        if (i >= facesVertIdxs.size()) {
//            return;
//        }
//
//        int[] vertIdxs = facesVertIdxs.get(i);
//        // get the vertex indicies for face i
//
//        int polytype;
//        if (vertIdxs.length == 3) {
//            polytype = GL2.GL_TRIANGLES;
//        } else if (vertIdxs.length == 4) {
//            polytype = GL2.GL_QUADS;
//        } else {
//            polytype = GL2.GL_POLYGON;
//        }
//
//        gl.glBegin(polytype);
//
//        // get the normal and tex coords indicies for face i
//        int[] normIdxs = facesNormIdxs.get(i);
//        int[] texIdxs = facesTexIdxs.get(i);
//
//        /* render the normals, tex coords, and vertices for face i
//         by accessing them using their indicies */
//        Vector3f vert, norm, texCoord;
//        double yTC;
//        for (int f = 0; f < vertIdxs.length; f++) {
//            if (normIdxs[f] != 0) {  // if there are normals, render them
//                norm = normals.get(normIdxs[f] - 1);
//                gl.glNormal3d(norm.getX(), norm.getY(), norm.getZ());
//            }
//
//            if (texIdxs[f] != 0) {
//                // if there are tex coords, render them
//                texCoord = texCoords.get(texIdxs[f] - 1);
//                yTC = texCoord.getY();
//                if (flipTexCoords) {
//                    yTC = 1.0f - yTC;
//                }
//
//                if (texCoord.getZ() == DUMMY_Z_TC) {
//                    gl.glTexCoord2d(texCoord.getX(), yTC);
//                } else {
//                    gl.glTexCoord3d(texCoord.getX(), yTC, texCoord.getZ());
//                }
//                /*
//                 System.out.print("Tex index: " + (texIdxs[f]) + ": ");
//                 System.out.println("Tex coord: " + df.format(texCoord.getX()) + ", " +
//                 df.format( yTC ) + ", " +
//                 df.format( texCoord.getZ() ));
//                 */
//            }
//
//            vert = verts.get(vertIdxs[f] - 1);  // render the vertices
//            //     System.out.println(vertIdxs[f] - 1 + " " + verts.get(vertIdxs[f] - 1).length());
//            gl.glVertex3d(vert.getX(), vert.getY(), vert.getZ());
//
//        }
//
//        gl.glEnd();
    } // end of renderFace()
    
    
    

    public GL2 init(GL2 gl, int vertexShaderID) {

        float[] vert = new float[3];
        float[] color = new float[3];

        
        // create buffers with geometry
        int[] buffers = new int[2];
        gl.glGenBuffers(2, buffers, 0);
        vertexBuffer = buffers[0];
        colorBuffer = buffers[1];
        
        // create a vertex array object for the geometry
        int[] arrays = new int[1];
        gl.glGenVertexArrays(1, arrays, 0);
        vertexArray = arrays[0];

        gl.glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, vert.length * Buffers.SIZEOF_FLOAT,
                null, GL_DYNAMIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, color.length * Buffers.SIZEOF_FLOAT,
                null, GL_DYNAMIC_DRAW);

        positionAttribLoc = gl.glGetAttribLocation(vertexShaderID, "position");
        colorAttribLoc = gl.glGetAttribLocation(vertexShaderID, "color");
        normalAttribLoc = gl.glGetAttribLocation(vertexShaderID, "normal");
        vertMvpUniformLoc = gl.glGetUniformLocation(vertexShaderID, "MVP");
        normMvpUniformLoc = gl.glGetUniformLocation(vertexShaderID, "N");
        
        // set the attributes of the geometry
        int binding[] = new int[1];
        gl.glGetIntegerv(GL_VERTEX_ARRAY_BINDING, binding, 0);
        joglArray = binding[0];
        
        gl.glBindVertexArray(vertexArray);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        gl.glEnableVertexAttribArray(positionAttribLoc);
        gl.glVertexAttribPointer(positionAttribLoc, 3, GL_FLOAT, false, 6 * Buffers.SIZEOF_FLOAT, 0);
         gl.glEnableVertexAttribArray(normalAttribLoc);
        gl.glVertexAttribPointer(normalAttribLoc, 3, GL_FLOAT, false, 6 * Buffers.SIZEOF_FLOAT, 3 * Buffers.SIZEOF_FLOAT);
        gl.glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
        gl.glEnableVertexAttribArray(colorAttribLoc);
        gl.glVertexAttribPointer(colorAttribLoc, 4, GL_FLOAT, false, 0, 0);

        gl.glBindVertexArray(joglArray);
        return gl;
    }
    
    private static Mat3 getMat3(Mat4 m) {
        Vec4 col0 = m.getColumn(0);
        Vec4 col1 = m.getColumn(1);
        Vec4 col2 = m.getColumn(2);
        Mat3 ma = new Mat3(
                col0.getX(), col0.getY(), col0.getZ(),
                col1.getX(), col1.getY(), col1.getZ(),
                col2.getX(), col2.getY(), col2.getZ());
        return ma;
    }
    
    private static Mat3 invertMatrix(Mat3 m) {
        Vec3 col0 = m.getColumn(0);
        Vec3 col1 = m.getColumn(1);
        Vec3 col2 = m.getColumn(2);

        Matrix3f mt = new Matrix3f(
                col0.getX(), col0.getY(), col0.getZ(),
                col1.getX(), col1.getY(), col1.getZ(),
                col2.getX(), col2.getY(), col2.getZ());

        mt.invert();

        Mat3 matrix = new Mat3(mt.m00, mt.m01, mt.m02, mt.m10, mt.m11, mt.m12, mt.m20, mt.m21, mt.m22);

        return matrix;
    }

    

    
}
