/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.comparison.localAreas;

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

    public LocalAreas(){
        
    }
    
    public void SetAreas(List<Area> areas, Model model){
        
        float[] vertexes;
        float[] vertexColors;
        int k;
        List<Integer> tmp;
        vertexAreas = new ArrayList<>();
        vertexColorAreas = new ArrayList<>();

        for (int j = 0; j < areas.size(); j++) {
            k = 0;
            tmp = areas.get(j).vertices;
            if (tmp.size() > 0) {
                if (tmp.size() > 2){
                    vertexes = giftWrapping(areas.get(j), model);
                    vertexColors = new float[areas.get(j).vertices.size() * 3];
                    for (int i = 0; i < tmp.size(); i++) {
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
    
    private static float[] giftWrapping(Area area, Model model){
        
        List<Point3D> points = new ArrayList();
        List<Integer> tmp = area.vertices;
        for (int i = 0; i < area.vertices.size(); i++) {
           Point3D point = new Point3D( model.getVerts().get(tmp.get(i)).x, 
                                        model.getVerts().get(tmp.get(i)).y, 
                                        model.getVerts().get(tmp.get(i)).z);
           points.add(point);
        }
        
        List<Float> vertexList = new ArrayList<>();
        
        int pivot = smallestX(points);
        int currentIndex = pivot;
        int previousIndex = pivot;
        
        Point3D a = points.get(currentIndex);
        vertexList.add((float)a.getX());
        vertexList.add((float)a.getY());
        vertexList.add((float)a.getZ());
        
        List<Integer> indexes = new ArrayList<>();
        
        do {
            int nextIndex = getMinimalAnglePoint(points, currentIndex, previousIndex, indexes);
            indexes.add(nextIndex);
            if (currentIndex != previousIndex){
                a = points.get(currentIndex);
                vertexList.add((float)a.getX());
                vertexList.add((float)a.getY());
                vertexList.add((float)a.getZ());
            }
            previousIndex = currentIndex;
            currentIndex = nextIndex;
            
        } while (pivot != currentIndex);
        
        float[] vertexArray = new float[vertexList.size()];
        for(int i=0; i < vertexList.size(); i++){
            vertexArray[i] = vertexList.get(i);
        }
        return vertexArray;
    }
    
    private static int smallestX(List<Point3D> points){
        int index = -1;
        int pomIndex = -1;
        Double smallestX = 5000.0;
        
        for (Point3D point : points){
            pomIndex++;
            if (point.getX()<smallestX){
                smallestX = point.getX();
                index = pomIndex;
            }
        }
        return index;
    }
    
    private static int getMinimalAnglePoint(List<Point3D> points, int currentIndex, int previousIndex, List<Integer> usedIndexes){
        Point3D currentPoint = points.get(currentIndex) ; 
        Point3D previousPoint = points.get(previousIndex);
        
        //setting inicial line
        if (currentIndex == previousIndex){
            previousPoint = new Point3D(previousPoint.getX(), previousPoint.getY()+100, previousPoint.getZ());
        }
        
        double[] line1 =   {previousPoint.getX(), 
                            previousPoint.getY(),
                            previousPoint.getZ(),
                            currentPoint.getX(),
                            currentPoint.getY(),
                            currentPoint.getZ()};

        //inicializing loop
        int index = -1;
        int tempIndex = -1;
        Double minimalAngle = 5000.0;
        
        //looking for point that have minimal angle to the two previous points
        for (Point3D point : points){
            tempIndex++;
            
            if (tempIndex != currentIndex && tempIndex != previousIndex && !usedIndexes.contains(tempIndex)){
                
                double[] line2 =   {currentPoint.getX(),
                                    currentPoint.getY(),
                                    currentPoint.getZ(),
                                    point.getX(),
                                    point.getY(),
                                    point.getZ()};

                Double  pomMinimalAngle = angleBetween2Lines(line1, line2);

                if (pomMinimalAngle<minimalAngle){
                    minimalAngle = pomMinimalAngle;
                    index = tempIndex;
                }
            }
        }
        return index;
    }
    
    private static double angleBetween2Lines(double[] line1, double[] line2){
        
        Point3D a = new Point3D(line1[0] - line1[3], 
                                line1[1] - line1[4],
                                line1[2] - line1[5]);
        Double aNorm =  Math.sqrt(a.getX()*a.getX()+a.getY()*a.getY()+a.getZ()*a.getZ());
        a = new Point3D(a.getX()/aNorm, a.getY()/aNorm, a.getZ()/aNorm);
        
        Point3D b = new Point3D(line2[0] - line2[3], 
                                line2[1] - line2[4],
                                line2[2] - line2[5]);
        Double bNorm =  Math.sqrt(b.getX()*b.getX()+b.getY()*b.getY()+b.getZ()*b.getZ());
        b = new Point3D(b.getX()/bNorm, b.getY()/bNorm, b.getZ()/bNorm);
        
        Double dot = (a.getX()*b.getX()) + (a.getY()*b.getY()) + (a.getZ()*b.getZ());
        return Math.abs(Math.toDegrees(Math.acos(dot)));
    }
}
