package cz.fidentis.renderer;

import LocalAreas.Area;
import LocalAreas.Points;
import com.jogamp.common.nio.Buffers;
import cz.fidentis.model.Model;
import cz.fidentis.model.ModelLoader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import javax.media.opengl.GL2;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import javax.vecmath.Vector3f;




/**
 *
 * @author zanri
 */
public class LocalAreasRender{
    private Boolean isSetUp;
    private List<Area> area;
    private Model model;
    private Points points;

    
    public LocalAreasRender(){
        this.isSetUp = false;
        area = new ArrayList<>();
        this.points = new Points();
    }
    
    public void SetUp(List<Area> area, Model model){
        this.area = area;
        this.isSetUp = true;
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
    }
    
    public Boolean IsSetUp(){
        return isSetUp;
    }

    public void HideLocalAreas(){
        this.isSetUp = false;
    }
    
    public GL2 DrawLocalAreas(GL2 gl){
        return makeAreas(gl);
    }
    
    
    public GL2 makeAreas(GL2 gl) {
        float[] vert = points.GetPoints();
        float[] color = points.GetVertexColors();
        
//        gl.glClear(GL_DEPTH_BUFFER_BIT);
        gl.glDisable(GL_LIGHTING);
        gl.glPointSize(5f);
        gl.glBegin(GL.GL_POINTS);

        
        for (int i = 0; i < points.getNumberOfVertexes(); i++) {
            int index = i*3;

            gl.glColor3f(color[index],color[index+1],color[index+2]);
            Vector3f v = new Vector3f(vert[index],vert[index+1],vert[index+2]);

            gl.glVertex3d(v.x, v.y, v.z);
           
        }
        
        gl.glEnable(GL_LIGHTING);

        gl.glEnd();
        return gl;
    }
    
}
