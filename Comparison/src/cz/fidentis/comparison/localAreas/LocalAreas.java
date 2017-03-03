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
   
    private float[] vertexAreas;
    private float[] vertexColorAreas;
    private float[] vertexColorBlack;
    private float[] vertexes;
    private float[] vertexColors;
    private List<List<Point3D>> vertexAreasPoints3D;
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
        
        List<Float> vertexes = new ArrayList<>();
        List<Float> vertexColors = new ArrayList<>();
//        float[] vertexes = new float[model.getFaces().getFacesVertIdxs().size() * 6 * 3];
//        float[] vertexColors = new float[model.getFaces().getFacesVertIdxs().size() * 3 * 3];
//        int k = 0;
//        int l = 0;

        vertexAreasPoints3D = new ArrayList<>();

        int[] faceIndexes;
        
        for (int i = 0; i < model.getFaces().getFacesVertIdxs().size(); i++) {
            
            faceIndexes = model.getFaces().getFaceVertIdxs(i);
            
            float[] areasColor = getColor(faceIndexes, areas);
            if (areasColor != null){
                for (int j = 0; j < faceIndexes.length; j++){
                    int faceIndex = faceIndexes[j]-1;

                    vertexes.add(model.getVerts().get(faceIndex).x);
                    vertexes.add(model.getVerts().get(faceIndex).y);
                    vertexes.add(model.getVerts().get(faceIndex).z);

                    vertexes.add(model.getNormals().get(faceIndex).x);
                    vertexes.add(model.getNormals().get(faceIndex).y);
                    vertexes.add(model.getNormals().get(faceIndex).z);

                    vertexColors.add(areasColor[0]);
                    vertexColors.add(areasColor[1]);
                    vertexColors.add(areasColor[2]);
                    vertexColors.add(0.5f);

                }
            }
            
        }
        float[] vertexesArray = new float[vertexes.size()];
        int i = 0;
        for (Float f : vertexes) {
            vertexesArray[i++] = (f != null ? f : Float.NaN); 
        }
        
        float[] vertexColorsArray = new float[vertexColors.size()];
        i = 0;
        for (Float f : vertexColors) {
            vertexColorsArray[i++] = (f != null ? f : Float.NaN); 
        }
        
        
        vertexAreas = vertexesArray;
        vertexColorAreas = vertexColorsArray;
        
        List<Integer> tmp;
        vertexes = new ArrayList<>();
        vertexColors = new ArrayList<>();
        
        for (int j = 0; j < areas.size(); j++) {
            tmp = areas.get(j).vertices;

            if (tmp.size() > 0) {
                for (i = 0; i < tmp.size(); i++) {
                    vertexes.add(model.getVerts().get(tmp.get(i)).x);
                    vertexes.add(model.getVerts().get(tmp.get(i)).y);
                    vertexes.add(model.getVerts().get(tmp.get(i)).z);

                    vertexes.add(model.getNormals().get(tmp.get(i)).x);
                    vertexes.add(model.getNormals().get(tmp.get(i)).y);
                    vertexes.add(model.getNormals().get(tmp.get(i)).z);

                    vertexColors.add(areas.get(j).color.get(0));
                    vertexColors.add(areas.get(j).color.get(1));
                    vertexColors.add(areas.get(j).color.get(2));
                    vertexColors.add(1f);
                }
            }
        }

        this.vertexes = new float[vertexes.size()];
        i = 0;
        for (Float f : vertexes) {
            this.vertexes[i++] = (f != null ? f : Float.NaN); 
        }
        
        this.vertexColors = new float[vertexColors.size()];
        i = 0;
        for (Float f : vertexColors) {
            this.vertexColors[i++] = (f != null ? f : Float.NaN); 
        }
        
        for ( i = 0; i < areas.size(); i++){
            List<Integer> indexesOfAreasTemp = new ArrayList<>();
            
            for (int j = 0; j < model.getFaces().getFacesVertIdxs().size(); j++) {
            
                faceIndexes = model.getFaces().getFaceVertIdxs(j);
                indexesOfAreasTemp = getIndexes(faceIndexes, areas.get(i), indexesOfAreasTemp);
            }
            vertexAreasPoints3D.add(giftWrapping(indexesOfAreasTemp, model));
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
//            vertexAreasPoints3D.add(vertexPoints);
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
    

    public float[] getVertexAreas(){
        return vertexAreas;
    }
    
    public float[] getVertexAreasColors(){
        return vertexColorAreas;
    }
    
    public float[] getVertexes(){
        return vertexes;
    }
    
    public float[] getVertexColors(){
        return vertexColors;
    }
    
    public List<List<Point3D>> getBoundariesAreasPoints(){
        return vertexAreasPoints3D;
    }
    
    public int[] getIndexes(){
        return indexesOfAreas;
    }
    
    public List<Point3D> getAllPointsFromOneArea(){
        return allAreasPoints;
    }

    private static float[] getColor(int[] indexes, List<Area> areas) {
        
        for (int i = 0; i < areas.size(); i++){
            int k = 0;
            if (areas.get(i).vertices.contains(indexes[0]-1)) {
                k++;
            }
            if (areas.get(i).vertices.contains(indexes[1]-1)) {
                k++;
            }
            if (areas.get(i).vertices.contains(indexes[2]-1)) {
                k++;
            }
            
            if( k >= 1){
                return new float[] {areas.get(i).color.get(0), 
                                    areas.get(i).color.get(1), 
                                    areas.get(i).color.get(2)};
            }
        }
        return null;
//        return new float[]{0.8667f, 0.7176f, 0.6275f};
    }
    
    private static List<Integer> getIndexes(int[] indexes, Area area, List<Integer> alreadyAssignedIndexes) {
        
       
        int k = 0;
        if (area.vertices.contains(indexes[0]-1)) {
            k++;
        }
        if (area.vertices.contains(indexes[1]-1)) {
            k++;
        }
        if (area.vertices.contains(indexes[2]-1)) {
            k++;
        }

        if( k >= 1){
            if (!alreadyAssignedIndexes.contains(indexes[0]-1)){
                alreadyAssignedIndexes.add(indexes[0]-1);
            }
            if (!alreadyAssignedIndexes.contains(indexes[1]-1)){
                alreadyAssignedIndexes.add(indexes[1]-1);
            }
            if (!alreadyAssignedIndexes.contains(indexes[2]-1)){
                alreadyAssignedIndexes.add(indexes[2]-1);
            }
            
            return alreadyAssignedIndexes;
        }
        
        return alreadyAssignedIndexes;
//        return new float[]{0.8667f, 0.7176f, 0.6275f};
    }
    
    
}
