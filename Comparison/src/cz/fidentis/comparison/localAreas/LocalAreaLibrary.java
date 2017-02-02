/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.comparison.localAreas;

import com.hackoeur.jglm.Mat3;
import cz.fidentis.model.Model;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point3D;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Vector3f;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import com.hackoeur.jglm.Vec3;
import java.nio.FloatBuffer;

/**
 *
 * @author zanri
 */
public class LocalAreaLibrary {
    public static Vector3f intersectionWithArea(double mouseReleasedX, 
                                               double mouseReleasedY, 
                                               int[] viewport, 
                                               double[] modelViewMatrix, 
                                               double[] projectionMatrix,
                                               List<Point3D> areaBoundary) {
        
        if (areaBoundary.size()<3){
            return null;
        }
    
        double v0[] = new double[4];
        double v1[] = new double[4];
        int realY = viewport[3] - (int) mouseReleasedY - 1;

        GLU glu = new GLU();

        glu.gluUnProject(mouseReleasedX, realY, 0.0, 
                modelViewMatrix, 0,
                projectionMatrix, 0,
                viewport, 0,
                v0, 0);

        glu.gluUnProject(mouseReleasedX, realY, 1.0, 
                modelViewMatrix, 0,
                projectionMatrix, 0,
                viewport, 0,
                v1, 0);

        Point3D a = areaBoundary.get(0);
        Point3D b = areaBoundary.get(1);

        for (int i = 2; i < areaBoundary.size(); i++){
            
            Point3D c = areaBoundary.get(i);

            Point3D[] triangle = {a, b, c};
            Vector3f point = calculateIntersectionWithTriangle(v0, v1, triangle);
            if (point != null){
                return point;
            }
        }

        return null;
    }
    

    
    private static Vector3f calculateIntersectionWithTriangle(double[] P0, double[] P1, Point3D[] triangle) {
        if (triangle.length == 3) {
            //ray - points  P0, P1
            //triangle - points T0,T1,T2
            Vector3f u = new Vector3f((float)(triangle[1].getX() - triangle[0].getX()), //T1-T0
                                      (float)(triangle[1].getY() - triangle[0].getY()),
                                      (float)(triangle[1].getZ() - triangle[0].getZ()));
            Vector3f v = new Vector3f((float)(triangle[2].getX() - triangle[0].getX()),//T2-T0
                                      (float)(triangle[2].getY() - triangle[0].getY()),
                                      (float)(triangle[2].getZ() - triangle[0].getZ()));
            Vector3f n = new Vector3f();
            n.cross(u, v);
            Vector3f tp = new Vector3f((float)(triangle[0].getX() - (float) P0[0]), //T0-P0
                                       (float)(triangle[0].getY() - (float) P0[1]),
                                       (float)(triangle[0].getZ() - (float) P0[2]));

            Vector3f pp = new Vector3f((float) P1[0] - (float) P0[0], //P1-P0
                    (float) P1[1] - (float) P0[1],
                    (float) P1[2] - (float) P0[2]);

            n.normalize();
            pp.normalize();

            Vector3f p0 = new Vector3f((float) P0[0], (float) P0[1], (float) P0[2]);

            float i;
            if (n.dot(pp) != 0) {
                //     i = tp.dot(n) / pp.dot(n);
            
                 float d = (-1) * (n.dot(new Vector3f ((float )triangle[0].getX(),
                                                       (float )triangle[0].getY(),
                                                       (float )triangle[0].getZ())));
                i = -(n.dot(p0) + d) / n.dot(pp);
            } else {
                return null;
            }
            
            if (i >= 0) {
                Vector3f intP = new Vector3f( p0.x + i * pp.x, // intersection with plane
                        p0.y + i * pp.y,
                        p0.z + i * pp.z);

                Vector3f w = new Vector3f(intP.getX() - (float )triangle[0].getX(), //PI-T0
                    intP.getY() - (float )triangle[0].getY(),
                    intP.getZ() - (float )triangle[0].getZ());

            float uv = u.dot(v);
            float uu = u.dot(u);
            float vv = v.dot(v);
            float wu = w.dot(u);
            float wv =  w.dot( v);
            float s = (uv * wv - vv * wu) / (uv * uv - uu * vv);
            float t = (uv * wu - uu * wv) / (uv * uv - uu * vv);
            
            if (s >= 0 && t >= 0 && s + t <= 1) {
                  return intP;//intersectionPoint;
                  //return true;
            }
            }
        }
        //return false;
        return null;
    }
    
    
    
    public static float[] giftWrapping(Area area, Model model){
        
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
    
    private static Vector3f crossProduct(Vector3f u, Vector3f v){

        float x = u.getY() * v.getZ() - v.getY() * u.getZ();
        float y = u.getZ() * v.getX() - v.getZ() * u.getX();
        float z = u.getX() * v.getY() - v.getX() * u.getY();
        
        return new Vector3f(x, y, z);
    }
    
     private static float dotProduct(Vector3f u, Vector3f v){

        float x = u.getX() * v.getX();
        float y = u.getY() * v.getY();
        float z = u.getZ() * v.getZ();
        
        return x+y+z;
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
