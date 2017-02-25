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
    private List<Point3D> allAreasPoints;
    private int[] indexesOfAreas;

    public LocalAreas(){
        
    }
    
    public void SetAreas(int[] indexesOfAreas, List<Area> areas, Model model){
        
        
        
        allAreasPoints = new ArrayList<>();
        
        if (areas.size()==1){
            List<Integer> temp = areas.get(0).vertices;
            
            for (int i = 0; i < temp.size(); i++) {
                allAreasPoints.add(new Point3D(model.getVerts().get(temp.get(i)).x, 
                                              model.getVerts().get(temp.get(i)).y, 
                                              model.getVerts().get(temp.get(i)).z));
            }
        }
        
        
        this.indexesOfAreas = indexesOfAreas;
        
        float[] vertexes;
        float[] vertexColors;
        int k;
        int l;
        List<Integer> tmp;
        vertexAreas = new ArrayList<>();
        vertexColorAreas = new ArrayList<>();
        vertexAreasPoints = new ArrayList<>();

        for (int j = 0; j < areas.size(); j++) {
            List<Point3D> vertexPoints = new ArrayList<>();
            k = 0;
            l = 0;
            tmp = areas.get(j).vertices;
            tmp = selectionSort(tmp);
            if (tmp.size() > 0) {
                if (tmp.size() > 2){
                    vertexPoints = giftWrapping(areas.get(j), model);
                    
                } else {
                    for (int i = 0; i < tmp.size(); i++) {
                        vertexPoints.add(new Point3D(model.getVerts().get(tmp.get(i)).x, 
                                                  model.getVerts().get(tmp.get(i)).y, 
                                                  model.getVerts().get(tmp.get(i)).z));
                    }
                    
                    
                }
                vertexes = new float[areas.get(j).vertices.size() * 6];
                vertexColors = new float[areas.get(j).vertices.size() * 4];
                for (int i = 0; i < tmp.size(); i++) {
                     
                    vertexes[k] = model.getVerts().get(tmp.get(i)).x;
                    k++;
                    vertexes[k] = model.getVerts().get(tmp.get(i)).y;
                    k++;
                    vertexes[k] = model.getVerts().get(tmp.get(i)).z;
                    k++;

                    vertexes[k] = model.getNormals().get(tmp.get(i)).x;
                    k++;
                    vertexes[k] = model.getNormals().get(tmp.get(i)).y;
                    k++;
                    vertexes[k] = model.getNormals().get(tmp.get(i)).z;
                    k++;

                    vertexColors[l] = areas.get(j).color.get(0);
                    l++;
                    vertexColors[l] = areas.get(j).color.get(1);
                    l++;
                    vertexColors[l] = areas.get(j).color.get(2);
                    l++;

                }
                
                
                vertexAreas.add(vertexes);
                vertexColorAreas.add(vertexColors);
            }
            vertexAreasPoints.add(vertexPoints);
        }
    }
    
  
    private static List<Integer> selectionSort(List<Integer> array) {
        for (int i = 0; i < array.size() - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < array.size(); j++) {
                if (array.get(j) > array.get(maxIndex)) maxIndex = j;
            }
            int tmp = array.get(i);
            array.set(i, array.get(maxIndex))  ;
            array.set(maxIndex, tmp);
        } 
        return array;
    }
    

    public List<float[]> getVertexes(){
        return vertexAreas;
    }
    
    public List<float[]> getVertexesColors(){
        return vertexColorAreas;
    }
    
    public List<List<Point3D>> getBoundariesAreasPoints(){
        return vertexAreasPoints;
    }
    
    public int[] getIndexes(){
        return indexesOfAreas;
    }
    
    public List<Point3D> getAllPointsFromOneArea(){
        return allAreasPoints;
    }
}
