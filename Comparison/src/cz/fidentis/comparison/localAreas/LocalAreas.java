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
        
        float[] vertexes = new float[model.getFaces().getFacesVertIdxs().size() * 6 * 3];
        float[] vertexColors = new float[model.getFaces().getFacesVertIdxs().size() * 3 * 3];
        int k = 0;
        int l = 0;

        vertexAreas = new ArrayList<>();
        vertexColorAreas = new ArrayList<>();
        vertexAreasPoints = new ArrayList<>();

        int[] faceIndexes;
        
        for (int i = 0; i < model.getFaces().getFacesVertIdxs().size(); i++) {
            
            faceIndexes = model.getFaces().getFaceVertIdxs(i);
            
            for (int j = 0; j < faceIndexes.length; j++){
                int faceIndex = faceIndexes[j]-1;
                
                float[] areasColor = getColor(faceIndex, areas);
                
                vertexes[k] = model.getVerts().get(faceIndex).x;
                k++;
                vertexes[k] = model.getVerts().get(faceIndex).y;
                k++;
                vertexes[k] = model.getVerts().get(faceIndex).z;
                k++;

                vertexes[k] = model.getNormals().get(faceIndex).x;
                k++;
                vertexes[k] = model.getNormals().get(faceIndex).y;
                k++;
                vertexes[k] = model.getNormals().get(faceIndex).z;
                k++;

                vertexColors[l] = areasColor[0];
                l++;
                vertexColors[l] = areasColor[1];
                l++;
                vertexColors[l] = areasColor[2];
                l++;
            }
            
            
            
        }
        vertexAreas.add(vertexes);
        vertexColorAreas.add(vertexColors);
        
        List<Integer> tmp;
        
        for (int j = 0; j < areas.size(); j++) {
            List<Point3D> vertexPoints = new ArrayList<>();

            tmp = areas.get(j).vertices;

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
            }
            vertexAreasPoints.add(vertexPoints);
        }
        
//        for (int j = 0; j < areas.size(); j++) {
//            List<Point3D> vertexPoints = new ArrayList<>();
//            k = 0;
//            l = 0;
//            tmp = areas.get(j).vertices;
//            tmp = selectionSort(tmp);
//            if (tmp.size() > 0) {
//                if (tmp.size() > 2){
//                    vertexPoints = giftWrapping(areas.get(j), model);
//                    
//                } else {
//                    for (int i = 0; i < tmp.size(); i++) {
//                        vertexPoints.add(new Point3D(model.getVerts().get(tmp.get(i)).x, 
//                                                  model.getVerts().get(tmp.get(i)).y, 
//                                                  model.getVerts().get(tmp.get(i)).z));
//                    }
//                    
//                    
//                }
//                vertexes = new float[areas.get(j).vertices.size() * 6];
//                vertexColors = new float[areas.get(j).vertices.size() * 4];
//                for (int i = 0; i < tmp.size(); i++) {
//                     
//                    vertexes[k] = model.getVerts().get(tmp.get(i)).x;
//                    k++;
//                    vertexes[k] = model.getVerts().get(tmp.get(i)).y;
//                    k++;
//                    vertexes[k] = model.getVerts().get(tmp.get(i)).z;
//                    k++;
//
//                    vertexes[k] = model.getNormals().get(tmp.get(i)).x;
//                    k++;
//                    vertexes[k] = model.getNormals().get(tmp.get(i)).y;
//                    k++;
//                    vertexes[k] = model.getNormals().get(tmp.get(i)).z;
//                    k++;
//
//                    vertexColors[l] = areas.get(j).color.get(0);
//                    l++;
//                    vertexColors[l] = areas.get(j).color.get(1);
//                    l++;
//                    vertexColors[l] = areas.get(j).color.get(2);
//                    l++;
//
//                }
//                
//                
//                vertexAreas.add(vertexes);
//                vertexColorAreas.add(vertexColors);
//            }
//            vertexAreasPoints.add(vertexPoints);
//        }
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

    private static float[] getColor(int index, List<Area> areas) {
        
        for (int i = 0; i < areas.size(); i++){
            if(areas.get(i).vertices.contains(index)){
                return new float[] {areas.get(i).color.get(0), 
                                    areas.get(i).color.get(1), 
                                    areas.get(i).color.get(2)};
            }
        }
        
        return new float[]{0.8667f, 0.7176f, 0.6275f};
    }
}
