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
import javax.vecmath.Vector4f;

/**
 *
 * @author zanri
 */
public class LocalAreas {
   
    private float[] vertexAreas;
    private float[] vertexColorAreas;
    private float[] borderAreas;
    private float[] borderColorAreas;
    private float[] vertexes;
    private float[] vertexColors;
    private List<List<Point3D>> vertexAreasPoints3D;
    private List<Vector4f> allAreasPoints;
    private int[] indexesOfAreas;

    public LocalAreas(){
        
    }
    
    public void SetAreas(int[] indexesOfAreas, List<Area> areas, Model model){
        // <editor-fold desc="Point positions">

        allAreasPoints = new ArrayList<>();
        
        if (areas.size()==1){
            List<Integer> temp = areas.get(0).vertices;
            
            for (int i = 0; i < temp.size(); i++) {
                allAreasPoints.add(new Vector4f(model.getVerts().get(temp.get(i)).x, 
                                                model.getVerts().get(temp.get(i)).y, 
                                                model.getVerts().get(temp.get(i)).z,
                                                temp.get(i)));
            }
        }
        // </editor-fold>    
        
        // <editor-fold desc="Triangular mesh">
        this.indexesOfAreas = indexesOfAreas;
        
        List<Float> vertexes = new ArrayList<>();
        List<Float> vertexColors = new ArrayList<>();

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
        // </editor-fold>      
        
        // <editor-fold desc="Border">
        this.indexesOfAreas = indexesOfAreas;

        
        List<Float> border = new ArrayList<>();
        List<Float> borderColors = new ArrayList<>();

        vertexAreasPoints3D = new ArrayList<>();
        List<List<Integer>> alreadyAddedLines = new ArrayList<>();
        
        for (i = 0; i < model.getFaces().getFacesVertIdxs().size(); i++) {
            
            faceIndexes = model.getFaces().getFaceVertIdxs(i);
            
            List<Integer> borderIndexes = getLines(faceIndexes, areas);
            
            
            if (borderIndexes  != null){
                
                alreadyAddedLines = deleteDuplicates(alreadyAddedLines, borderIndexes);

            }
        }
        
        for (i = 0; i < alreadyAddedLines.size(); i++) {
            List<Integer> borderIndexes = alreadyAddedLines.get(i);

            for (int j = 0; j < borderIndexes.size()-1; j++){
                int borderIndex  = borderIndexes.get(j);

                border.add(model.getVerts().get(borderIndex).x);
                border.add(model.getVerts().get(borderIndex).y);
                border.add(model.getVerts().get(borderIndex).z);

                border.add(model.getNormals().get(borderIndex).x);
                border.add(model.getNormals().get(borderIndex).y);
                border.add(model.getNormals().get(borderIndex).z);

                borderColors.add(0f);
                borderColors.add(0f);
                borderColors.add(0f);
                borderColors.add(1f);

            }
        }

        vertexesArray = new float[border.size()];
        i = 0;
        for (Float f : border) {
            vertexesArray[i++] = (f != null ? f : Float.NaN); 
        }
        
        vertexColorsArray = new float[borderColors.size()];
        i = 0;
        for (Float f : borderColors) {
            vertexColorsArray[i++] = (f != null ? f : Float.NaN); 
        }
        
        borderAreas = vertexesArray;
        borderColorAreas = vertexColorsArray;
        // </editor-fold>      

        // <editor-fold desc="Single Points">
        List<Integer> tmp = null;
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
        // </editor-fold>

        // <editor-fold desc="Gift wrapping">
        for ( i = 0; i < areas.size(); i++){
            List<Integer> indexesOfAreasTemp = new ArrayList<>();
            
            for (int j = 0; j < model.getFaces().getFacesVertIdxs().size(); j++) {
            
                faceIndexes = model.getFaces().getFaceVertIdxs(j);
                indexesOfAreasTemp = getIndexes(faceIndexes, areas.get(i), indexesOfAreasTemp);
            }
            vertexAreasPoints3D.add(giftWrapping(indexesOfAreasTemp, model));
        }
        // </editor-fold> 
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
    
    public float[] getBorder(){
        return borderAreas;
    }
    
    public float[] getBorderColor(){
        return borderColorAreas;
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
    
    public List<Vector4f> getAllPointsFromOneArea(){
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
    }
    
    private static List<Integer> getLines(int[] indexes, List<Area> areas) {
        
        for (int i = 0; i < areas.size(); i++){
            List<Integer> result = new ArrayList<>();
            int k = 0;
            if (areas.get(i).vertices.contains(indexes[0]-1)) {
                k++;
            } else {
                result.add(indexes[0]-1);
            }
            if (areas.get(i).vertices.contains(indexes[1]-1)) {
                k++;
            } else {
                result.add(indexes[1]-1);
            }
            if (areas.get(i).vertices.contains(indexes[2]-1)) {
                k++;
            } else {
                result.add(indexes[2]-1);
            }

            if( k == 1){
                result.add(areas.get(i).index);
                
                return result;
            }
        }

        return null;
    }
    
    private static List<List<Integer>> deleteDuplicates(List<List<Integer>> alreadyAddedLines, List<Integer> borderIndexes){
        List<Integer> borderIndexesRotated = new ArrayList<>();
        borderIndexesRotated.add(borderIndexes.get(1));
        borderIndexesRotated.add(borderIndexes.get(0));
        borderIndexesRotated.add(borderIndexes.get(2));
        int index = -1;
        for (int i = 0; i < alreadyAddedLines.size(); i++){
            List<Integer> line = alreadyAddedLines.get(i);
            if ((line.get(0).equals(borderIndexes.get(0))&&line.get(1).equals(borderIndexes.get(1))&&line.get(2).equals(borderIndexes.get(2))) ||
                (line.get(1).equals(borderIndexes.get(0))&&line.get(0).equals(borderIndexes.get(1))&&line.get(2).equals(borderIndexes.get(2)))){
                index = i;
            }
        }
        if (index != -1){
            alreadyAddedLines.remove(index);
        } else {
            alreadyAddedLines.add(borderIndexes);
        }
        
        return alreadyAddedLines;
    }
    
    
}
