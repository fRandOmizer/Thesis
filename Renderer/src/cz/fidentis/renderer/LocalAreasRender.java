package cz.fidentis.renderer;

import LocalAreas.Area;
import LocalAreas.Points;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Vec3;
import com.jogamp.common.nio.Buffers;
import cz.fidentis.merging.scene.Camera;
import cz.fidentis.model.Model;
import java.util.ArrayList;
import java.util.List;
import static javax.media.opengl.GL.GL_ARRAY_BUFFER;
import static javax.media.opengl.GL.GL_DYNAMIC_DRAW;
import static javax.media.opengl.GL.GL_FLOAT;
import static javax.media.opengl.GL.GL_STATIC_DRAW;
import javax.media.opengl.GL2;
import static javax.media.opengl.GL2GL3.GL_PIXEL_UNPACK_BUFFER;






/**
 *
 * @author zanri
 */
public class LocalAreasRender{
    private Boolean isSetUp;
    private List<Area> area;
    private Model model;
    private Points points;

    // our OpenGL resources
    private int vertexBuffer;
    private int vertexArray;
    
    private int colorBuffer;
    //private int indicesBuffer;
    
    // our GLSL resources
    private int positionAttribLoc;
    private int colorAttribLoc;
    private int vertMvpUniformLoc;

    
    public LocalAreasRender(){
        this.isSetUp = false;
        area = new ArrayList<>();
        this.points = new Points();
    }
    
    public void SetUp(List<Area> area, Model model){
        this.area = area;
        this.model = model;
        
        List<Integer> tmp;
        int s = 0;
        for (int j = 0; j < area.size(); j++) {
            s += area.get(j).vertices.size();
        }

        float vert[] = new float[s * 3];
        float color[] = new float[s * 3];

        int k = 0;
        for (int t = 0; t < area.size(); t++) {
            tmp = area.get(t).vertices;
            if (tmp.size() != 0) {
                for (int i = 0; i < tmp.size(); i++) {
                    vert[k] = model.getVerts().get(tmp.get(i)).x;
                    color[k] = area.get(t).color.get(0);
                    k++;
                    vert[k] = model.getVerts().get(tmp.get(i)).y;
                    color[k] = area.get(t).color.get(1);
                    k++;
                    vert[k] = model.getVerts().get(tmp.get(i)).z;
                    color[k] = area.get(t).color.get(2);
                    k++;
                }
            }
        }
        
        points.AddArray(vert, color);
        this.isSetUp = true;
    }
    
    public Boolean IsSetUp(){
        return isSetUp;
    }

    public void HideLocalAreas(){
        this.isSetUp = false;
    }
    
    public GL2 DrawLocalAreas(GL2 gl, int vertexShaderID, Mat4 vp, Vec3 position, float width, float height){
        return makeAreas(gl, vertexShaderID, vp, position, width, height);
    }
    
    
    public GL2 makeAreas(GL2 gl, int vertexShaderID, Mat4 vp, Vec3 position, float width, float height) {
        float[] vert = points.GetPoints();
        float[] color = points.GetVertexColors();
        
//        Mat4 perspective = Matrices.perspective(60.0f, (float) width / (float) height, 1.0f, 500.0f);
//        Vec3 yAxis = new Vec3(0.0f, 1.0f, 0.0f);
//        Mat4 view = Matrices.lookAt(position, Vec3.VEC3_ZERO, yAxis);
//        
//        Mat4 vp = Mat4.MAT4_IDENTITY;
//
//        vp = vp.multiply(perspective);
//        vp = vp.multiply(view);
        
        gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
        gl.glDisable(GL2.GL_LIGHTING);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, vert.length * Buffers.SIZEOF_FLOAT,
                Buffers.newDirectFloatBuffer(vert), GL_DYNAMIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, color.length * Buffers.SIZEOF_FLOAT,
                Buffers.newDirectFloatBuffer(color), GL_DYNAMIC_DRAW);

        gl.glUseProgram(vertexShaderID);
        
        gl.glUniformMatrix4fv(vertMvpUniformLoc, 1, false, vp.getBuffer());
        
        gl.glBindVertexArray(vertexArray);

        gl.glDrawArrays(GL2.GL_POINTS, 0, vert.length);

        gl.glEnable(GL2.GL_LIGHTING);
        
        

       // gl.glDrawArrays(GL2.GL_POINTS, 0, vert.length);
        
        return gl;
    }

    public GL2 init(GL2 gl, int vertexShaderID) {
       
        //gl.glUnmapBuffer(GL_PIXEL_UNPACK_BUFFER);

        float[] vert = new float[30000];//points.GetPoints();
        float[] color = new float[30000];//points.GetVertexColors();

        

        //gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        //gl.glEnableClientState(GL2.GL_VERTEX_ARRAY_BINDING);
        
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
        gl.glBindVertexArray(vertexArray);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        gl.glEnableVertexAttribArray(positionAttribLoc);
        gl.glVertexAttribPointer(positionAttribLoc, 3, GL_FLOAT, false, 0, 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
        gl.glEnableVertexAttribArray(colorAttribLoc);
        gl.glVertexAttribPointer(colorAttribLoc, 3, GL_FLOAT, false, 0, 0);
        //
        
        return gl;
    }
    

    
}
