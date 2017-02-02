/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.comparison.localAreas;

import static cz.fidentis.comparison.localAreas.LocalAreaLibrary.giftWrapping;
import cz.fidentis.model.Model;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point3D;

/**
 *
 * @author zanri
 */
public class LocalAreas {
   
    private List<float[]> vertexAreas;
    private List<float[]> vertexColorAreas;
    private List<List<Point3D>> vertexAreasPoints;
    private int[] indexesOfAreas;

    public LocalAreas(){
        
    }
    
    public void SetAreas(int[] indexesOfAreas, List<Area> areas, Model model){
        
        this.indexesOfAreas = indexesOfAreas;
        
        float[] vertexes;
        float[] vertexColors;
        int k;
        List<Integer> tmp;
        vertexAreas = new ArrayList<>();
        vertexColorAreas = new ArrayList<>();
        vertexAreasPoints = new ArrayList<>();

        for (int j = 0; j < areas.size(); j++) {
            List<Point3D> vertexPoints = new ArrayList<>();
            k = 0;
            tmp = areas.get(j).vertices;
            if (tmp.size() > 0) {
                if (tmp.size() > 2){
                    vertexes = giftWrapping(areas.get(j), model);
                    vertexColors = new float[areas.get(j).vertices.size() * 3];
                    for (int i = 0; i < tmp.size(); i++) {
                        vertexPoints .add(new Point3D(model.getVerts().get(tmp.get(i)).x, 
                                                      model.getVerts().get(tmp.get(i)).y, 
                                                      model.getVerts().get(tmp.get(i)).z));
                        vertexColors[k] = areas.get(j).color.get(0);
                        k++;
                        vertexColors[k] = areas.get(j).color.get(1);
                        k++;
                        vertexColors[k] = areas.get(j).color.get(2);
                        k++;
                    }
                } else {
                    vertexes = new float[areas.get(j).vertices.size() * 3];
                    vertexColors = new float[areas.get(j).vertices.size() * 3];
                    for (int i = 0; i < tmp.size(); i++) {
                        vertexPoints .add(new Point3D(model.getVerts().get(tmp.get(i)).x, 
                                                      model.getVerts().get(tmp.get(i)).y, 
                                                      model.getVerts().get(tmp.get(i)).z));
                        vertexes[k] = model.getVerts().get(tmp.get(i)).x;
                        vertexColors[k] = areas.get(j).color.get(0);
                        k++;
                        vertexes[k] = model.getVerts().get(tmp.get(i)).y;
                        vertexColors[k] = areas.get(j).color.get(1);
                        k++;
                        vertexes[k] = model.getVerts().get(tmp.get(i)).z;
                        vertexColors[k] = areas.get(j).color.get(2);
                        k++;
                    }
                }
                
                vertexAreas.add(vertexes);
                vertexColorAreas.add(vertexColors);
            }
            vertexAreasPoints.add(vertexPoints);
        }
    }

    public List<float[]> GetVertexAreas(){
        return vertexAreas;
    }
    
    public List<float[]> GetVertexColorsAreas(){
        return vertexColorAreas;
    }
    
    public List<List<Point3D>> GetVertexAreasPoints(){
        return vertexAreasPoints;
    }
}
