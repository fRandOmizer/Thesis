/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LocalAreas;

import cz.fidentis.model.Model;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point3D;

/**
 *
 * @author zanri
 */
public class LocalAreas {
    private List<Point3D> Points;
    //private float[] vertexes;
    //private float[] vertexColors;
    private List<float[]> vertexAreas;
    private List<float[]> vertexColorAreas;
    
    public LocalAreas(){
        Points = new ArrayList();
    }
    
    public void SetAreas(List<Area> areas, Model model){
        
//        int s = 0;
//        for (int j = 0; j < areas.size(); j++) {
//            s += areas.get(j).vertices.size();
//        }
//
//        vertexes = new float[s * 3];
//        vertexColors = new float[s * 3];
//
//        int k = 0;
//        for (int j = 0; j < areas.size(); j++) {
//            tmp = areas.get(j).vertices;
//            if (tmp.size() != 0) {
//                for (int i = 0; i < tmp.size(); i++) {
//                    vertexes[k] = model.getVerts().get(tmp.get(i)).x;
//                    vertexColors[k] = areas.get(j).color.get(0);
//                    k++;
//                    vertexes[k] = model.getVerts().get(tmp.get(i)).y;
//                    vertexColors[k] = areas.get(j).color.get(1);
//                    k++;
//                    vertexes[k] = model.getVerts().get(tmp.get(i)).z;
//                    vertexColors[k] = areas.get(j).color.get(2);
//                    k++;
//                }
//            }
//        }


        float[] vertexes;
        float[] vertexColors;
        int k;
        List<Integer> tmp;
        vertexAreas = new ArrayList<>();
        vertexColorAreas = new ArrayList<>();

        for (int j = 0; j < areas.size(); j++) {
            k = 0;
            tmp = areas.get(j).vertices;
            vertexes = new float[areas.get(j).vertices.size() * 3];
            vertexColors = new float[areas.get(j).vertices.size() * 3];
            if (tmp.size() != 0) {
                for (int i = 0; i < tmp.size(); i++) {
                    if (model.getVerts().get(tmp.get(i)).x != 0 || 
                        model.getVerts().get(tmp.get(i)).y != 0 || 
                        model.getVerts().get(tmp.get(i)).z != 0){
                        
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
        }
    }

    public List<float[]> GetVertexAreas(){
        return vertexAreas;
    }
    
    public List<float[]> GetVertexColorsAreas(){
        return vertexColorAreas;
    }
}
