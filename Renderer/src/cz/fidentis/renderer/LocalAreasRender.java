package cz.fidentis.renderer;

import cz.fidentis.comparison.localAreas.Area;
import cz.fidentis.comparison.localAreas.LocalAreas;
import com.hackoeur.jglm.Mat4;
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
import javax.vecmath.Vector3f;

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
    private int vertMvpUniformLoc;
    private Mat4 matrix;
    private boolean isDrawPoint;
    private float[] pointToDraw;

    
    public LocalAreasRender(){
        this.isSetUp = false;
        this.localAreas = new LocalAreas();
        isDrawPoint = false;
    }
    
    public void SetUp(int[] areasIndexes, List<Area> areas, Model model){
        localAreas.SetAreas(areasIndexes, areas, model);
        this.isSetUp = true;
    }
    
    public void setPointToDraw(Vector3f pointToDraw){
        this.pointToDraw = new float[] {pointToDraw.x, pointToDraw.y, pointToDraw.z};
        isDrawPoint = true;
    }
    
    public Boolean IsSetUp(){
        return isSetUp;
    }

    public void HideLocalAreas(){
        this.isSetUp = false;
        isDrawPoint = false;
    }
    
    public Mat4 getMatrix(){
        return this.matrix;
    }
    
    public LocalAreas getLocalAreasBoundary(){
        return localAreas;
    }
    
    public GL2 drawLocalAreas(GL2 gl, int vertexShaderID, double[] a, double[] b){
        List<float[]> vertexesAreas = localAreas.getVertexes();
        List<float[]> colorAreas = localAreas.getVertexesColors();
        
        matrix = Mat4.MAT4_IDENTITY;

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

        matrix = matrix.multiply(projection);
        matrix = matrix.multiply(viewMat);

        gl.glClear(GL_DEPTH_BUFFER_BIT);
        gl.glEnable (GL_BLEND); 
        gl.glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        gl.glLineWidth(2);
        gl.glPointSize(5);

        for (int i = 0; i < vertexesAreas.size(); i++){
//            float[] colorForPoints = new float[vertexesAreas.size()];
//            for (int j = 0; j < vertexesAreas.size(); j++){
//                
//            }
            
            
            gl.glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
            gl.glBufferData(GL_ARRAY_BUFFER, vertexesAreas.get(i).length * Buffers.SIZEOF_FLOAT,
                    Buffers.newDirectFloatBuffer(vertexesAreas.get(i)), GL_DYNAMIC_DRAW);

            gl.glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
            gl.glBufferData(GL_ARRAY_BUFFER, colorAreas.get(i).length * Buffers.SIZEOF_FLOAT,
                    Buffers.newDirectFloatBuffer(colorAreas.get(i)), GL_DYNAMIC_DRAW);

            gl.glUseProgram(vertexShaderID);

            gl.glUniformMatrix4fv(vertMvpUniformLoc, 1, false, matrix.getBuffer());

            gl.glBindVertexArray(vertexArray);
            
            if (vertexesAreas.get(i).length > 6){
                gl.glDrawArrays(GL2.GL_POLYGON, 0, vertexesAreas.get(i).length/3);
            } else {
                if (vertexesAreas.get(i).length > 4){
                    gl.glDrawArrays(GL_LINES, 0, vertexesAreas.get(i).length/3);
                } else {
                    gl.glDrawArrays(GL_POINTS, 0, vertexesAreas.get(i).length/3);
                }
            }
            
//            gl.glBufferData(GL_ARRAY_BUFFER, colorAreas.get(i).length * Buffers.SIZEOF_FLOAT,
//                    Buffers.newDirectFloatBuffer(new float[]  {0.0f, 1.0f, 0.0f}), GL_DYNAMIC_DRAW);
//            gl.glDrawArrays(GL_POINTS, 0, vertexesAreas.get(i).length/3);
            
            gl.glBindVertexArray(joglArray);
        }
        
        
        if (isDrawPoint){
            
            gl.glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
            gl.glBufferData(GL_ARRAY_BUFFER, 3 * Buffers.SIZEOF_FLOAT,
                    Buffers.newDirectFloatBuffer(pointToDraw), GL_DYNAMIC_DRAW);

            gl.glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
            gl.glBufferData(GL_ARRAY_BUFFER, 3 * Buffers.SIZEOF_FLOAT,
                    Buffers.newDirectFloatBuffer(new float[]  {1.0f, 0.0f, 0.0f, 1.0f}), GL_DYNAMIC_DRAW);

            gl.glUseProgram(vertexShaderID);

            gl.glUniformMatrix4fv(vertMvpUniformLoc, 1, false, matrix.getBuffer());

            gl.glBindVertexArray(vertexArray);
            
            
            
            gl.glDrawArrays(GL_POINTS, 0, 1);
            
            
            gl.glBindVertexArray(joglArray);
        }
        
        return gl;
    }

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
        vertMvpUniformLoc = gl.glGetUniformLocation(vertexShaderID, "MVP");
        
        // set the attributes of the geometry
        int binding[] = new int[1];
        gl.glGetIntegerv(GL_VERTEX_ARRAY_BINDING, binding, 0);
        joglArray = binding[0];
        
        gl.glBindVertexArray(vertexArray);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        gl.glEnableVertexAttribArray(positionAttribLoc);
        gl.glVertexAttribPointer(positionAttribLoc, 3, GL_FLOAT, false, 0, 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
        gl.glEnableVertexAttribArray(colorAttribLoc);
        gl.glVertexAttribPointer(colorAttribLoc, 4, GL_FLOAT, false, 0, 0);
        
        gl.glBindVertexArray(joglArray);
        return gl;
    }
    

    
}
