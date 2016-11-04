/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LocalAreas;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point3D;

/**
 *
 * @author zanri
 */
public class Points {
    private List<Point3D> Points;
    private float[] vertexes;
    private float[] vertexColors;
    private int numberOfVertexes;
    
    public Points(){
        Points = new ArrayList();
    }
    
    public void AddPoint(Float x, Float y, Float z){
        Point3D point = new Point3D(x, y, z);
        Points.add(point);
    }
    
    public void AddArray(float vert[], float color[]){
        this.vertexes = vert;
        this.vertexColors = color;
        numberOfVertexes = vert.length/3;
    }

    public float[] GetPoints(){
        return vertexes;
    }
    
    public float[] GetVertexColors(){
        return vertexColors;
    }
    
    public int getNumberOfVertexes(){
        return numberOfVertexes;
    }
    
    
}
