/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.fidentis.comparison.icp;

import cz.fidentis.comparison.kdTree.KdTree;
import cz.fidentis.utils.CompareUtils;
import cz.fidentis.utils.MathUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.Vector3f;
/**
 * Class which will built KdTree, K = 3
 * Most of the methods in the class are private, hence only contructor, root getter and nearest neighbor search are available publicly
 * Class builds balanced KdTree from point cloud.
 * 
 * @author Zuzana Ferkova
 */
public class KdTreeIndexed implements KdTree {
    
    private final KdNode root;
    private final List<Vector3f> points;
    
    private final static int X_AXIS = 0;
    private final static int Y_AXIS = 1;
    private final static int Z_AXIS = 2;
    private final static int USED_THREADS = 3;      //fixed is good enough here
    private final static int NUMBER_OF_AXIS = 3;
    
    /**
     * Constructor of KdTree class, sorts points based on each axis (x,y,z) and builds balanced tree.
     * Tree is represented by its root.
     * 
     * @param points Point cloud to be represented as KdTree
     */
    public KdTreeIndexed(List<Vector3f> points){
        if(points == null || points.isEmpty()){
            this.root = null;
            this.points = null;
            return;
        }
        
        this.points = points;
        
        List<Integer> pointsCopy = new ArrayList<>(points.size());
        for(int i = 0; i < points.size(); i++){
            pointsCopy.add(i);
        }
        
       
        pointsCopy = deleteDuplicates(pointsCopy, points);
        
        ExecutorService executor = Executors.newFixedThreadPool(USED_THREADS);
        List<Future<List<Integer>>> sortedP = new LinkedList<>();
        
        for(int i = 0; i < NUMBER_OF_AXIS; i++){
            Future<List<Integer>> res = executor.submit(new SortPointsAxisCallable(points, i, pointsCopy));
            sortedP.add(res);
        }
        
        executor.shutdown();
        
        List<Integer> sortedByX = null;
        List<Integer> sortedByY = null;
        List<Integer> sortedByZ = null;
 
        try {
            sortedByX = sortedP.get(X_AXIS).get();
            sortedByY = sortedP.get(Y_AXIS).get();
            sortedByZ = sortedP.get(Z_AXIS).get();
            
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(KdTreeIndexed.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
        root = buildTree(null, sortedByX, sortedByY, sortedByZ, 0, points);
    }

    public KdNode getRoot() {
        return root;
    }
    
    /**
     * Recursively builds KdTree until there are no points left.
     * Builds balanced KdTree.
     * Returns KdNode which is set as left or right child of previous KdNode
     * 
     * @param parent - previous KdNode which will have its child set to returned KdNode
     * @param byX - list of points sorted by its X values
     * @param byY - list of points sorted by its Y values
     * @param byZ - list of points sorted by its Z values
     * @param level - level of the KdNode created
     * @return - KdNode created to be set as child of the parent
     */
    private KdNode buildTree(KdNode parent, List<Integer> byX, List<Integer> byY, List<Integer> byZ, int level, List<Vector3f> points){
        if(byX == null || byY == null || byZ == null)
            return null;
        
        KdNode node = null;
        int mid;
        int midIndex;
        
        if(byX.size() > 0 && byY.size() > 0 && byZ.size() > 0){
            mid = (byX.size() / 2); 
            
            List<Integer> leftX = new ArrayList<>(mid);
            List<Integer> leftY = new ArrayList<>(mid);
            List<Integer> leftZ = new ArrayList<>(mid);
            
            //split lists in half, set middle element as new KdNode to be returned
            //first list to be split based on level, rest so that they contain points in first list
            //but also keep the ordering by their axis
            if (level % 3 == 0){
                midIndex = byX.get(mid);
                node = new KdNode(level, points.get(byX.get(mid)), midIndex, parent);
               
                splitTree(mid, leftX, byX, leftY, byY, leftZ, byZ);
                   
            }else if(level % 3 == 1){
                midIndex = byY.get(mid);
                node = new KdNode(level, points.get(byY.get(mid)), midIndex, parent);

                splitTree(mid, leftY, byY, leftX, byX, leftZ, byZ); 
                
            }else{
                midIndex = byZ.get(mid);
                node = new KdNode(level, points.get(byZ.get(mid)), midIndex, parent);

                splitTree(mid, leftZ, byZ, leftY, byY, leftX, byX);
                
            }
            
                //removes current middle node from each all lists, in case there were duplicates
                byX.removeAll(Collections.singleton(midIndex));
                byY.removeAll(Collections.singleton(midIndex));
                byZ.removeAll(Collections.singleton(midIndex));
                leftX.removeAll(Collections.singleton(midIndex));
                leftY.removeAll(Collections.singleton(midIndex));
                leftZ.removeAll(Collections.singleton(midIndex));
            
             node.setLesser(buildTree(node, leftX, leftY, leftZ, level + 1, points));
             node.setGreater(buildTree(node, byX, byY, byZ, level + 1, points));
        }
       
        return node;
    }
    
    private void splitTree(int mid, List<Integer> leftMain, List<Integer> mainList, List<Integer> leftSecond,
            List<Integer> secondList, List<Integer> leftThird, List<Integer> thirdList){
        for (int i = 0; i < mid; i++) {

            leftMain.add(mainList.get(0));

            mainList.remove(0);
        }
        
        splitArray(leftMain, leftSecond, secondList);
        splitArray(leftMain, leftThird, thirdList);

        /*for(int i = secondList.size() - 1; i >= 0; i--){
         if(leftMain.contains(secondList.get(i))){
         leftSecond.add(0, secondList.get(i));
         secondList.remove(i);
         }
         } 
                
         for(int i = thirdList.size() - 1; i >= 0; i--){
         if(leftMain.contains(thirdList.get(i))){
         leftThird.add(0, thirdList.get(i));
         thirdList.remove(i);
         }
         }*/

    }
    
    private void splitArray(List<Integer> leftMain, List<Integer> leftSecond, List<Integer> secondList){
         for(int i = secondList.size() - 1; i >= 0; i--){
                    if(leftMain.contains(secondList.get(i))){
                        leftSecond.add(0, secondList.get(i));
                        secondList.remove(i);
                    }
                }
    }
    
   
    /**
     * Defines distance between currently searched node and point to which we want to find nearest neighbor in the tree,
     * based on the axis given by level.
     * 
     * @param searched - currently searched node
     * @param p2 - point to which we want to find nearest neighbor in the tree
     * @param level - axis based on which distance will be computed
     * @return float value of distance on the axis given by level
     */
     private float minDistanceIntersection(Vector3f searched, Vector3f p2, int level){
        if(level % 3 == 0){
            return Math.abs(searched.x - p2.x);
        }else if(level % 3 == 1){
             return Math.abs(searched.y - p2.y);
        }else{
             return Math.abs(searched.z - p2.z);
        }
    } 
    
     /**
      * Finds nearest neighbor of Point p in KdTree given by root.
      * 
      * @param p - point of which we want to find nearest neighbor in the tree.
      * @return  - point representing nearest neighbor of point p
      */
    @Override
    public Vector3f nearestNeighbour(Vector3f p){       
        KdNode nn = nearestNeighborNode(p);
        
        if(nn == null){
            return null;
        }
        
        return nn.getId();
    }
    
    /**
      * Finds nearest neighbor of Point p in KdTree given by root.
      * 
      * @param p - point of which we want to find nearest neighbor in the tree.
      * @return  - index representing nearest neighbor of point p in original point list from which kdtree was created
      */
    public Integer nearestIndex(Vector3f p){
        KdNode nn = nearestNeighborNode(p);
        
        if(nn == null){
            return null;
        }
        
        return nn.getIndex();
    }
    
    public KdNode nearestNeighborNode(Vector3f p){
           if(p == null || root == null){
            return null;
        }
        
       double minDistance = Double.MAX_VALUE;
       double distOnAxis;
       KdNode near = root;
       KdNode searched = root;
       Queue<KdNode> queue = new LinkedList<KdNode>();
       
       //first search from root to leaf
       while(searched != null){
           
           double dist = MathUtils.instance().distancePointsSqrt(p, searched.getId());
           if(dist < minDistance){
               near = searched;
               minDistance = dist;
           }
           
           if(CompareUtils.instance().comparePointsOnLevel(p, searched.getId(), searched.getDepth())){
               searched = searched.getLesser();
           }else{
              searched = searched.getGreater(); 
           }
       }
       
       
        queue.add(root);
       
        //second search to find vertex that could be potentially closer than 
        //nearest vertex already found
        while (!queue.isEmpty()) {
            
            //if min distance is 0, break cycle (nothing can be closer)
           if(minDistance == 0){
               break;
           }
            
            searched = queue.poll();
            double dist = MathUtils.instance().distancePointsSqrt(p, searched.getId());
            
            if (dist < minDistance) {
                near = searched;
                minDistance = dist;
            }

            distOnAxis = minDistanceIntersection(searched.getId(), p, searched.getDepth());

             if (distOnAxis * distOnAxis> minDistance) {
                if (CompareUtils.instance().comparePointsOnLevel(p, searched.getId(), searched.getDepth())) {
                    if (searched.getLesser() != null) {
                        queue.add(searched.getLesser());
                    }
                } else {
                    if (searched.getGreater() != null) {
                        queue.add(searched.getGreater());
                    }
                }
            } else {
                if (searched.getLesser() != null) {
                    queue.add(searched.getLesser());
                }
                if (searched.getGreater() != null) {
                    queue.add(searched.getGreater());
                }
            }
       }
       
       return near;
    }
    
    /**
     * Deletes duplicated from the given list to avoid errors while searching KdTree.
     * 
     * @param list - list to have duplicates removed from
     * @return list without duplicated vertices.
     */
    private List<Integer> deleteDuplicates(List<Integer> list, List<Vector3f> points){
        List<Vector3f> noDuplicates = new ArrayList<>();
        List<Integer> noDupIndex = new ArrayList<>();
        
        /*for(Vector3f p : list){
            if(!noDuplicates.contains(p)){
                noDuplicates.add(p);
            }
        }*/
        
        for(int i = 0; i < points.size(); i++){
            if(!noDuplicates.contains(points.get(i))){
                noDuplicates.add(points.get(i));
                noDupIndex.add(i);
            }
        }
        
        return noDupIndex;
    }
    
    /**
     * Check if tree contains given point
     * @param p - check point in kdtree
     * @return true if p is in kdtree, false otherwise
     */
    public boolean containPoint(Vector3f p){
        Vector3f found = nearestNeighbour(p);
        
        if(found == null){
            return false;
        }
        
        return found.equals(p);
    }

    @Override
    public double nearestDistance(Vector3f p, Vector3f pNormal, boolean useRelative) {
        Vector3f found = nearestNeighbour(p);
        
        if(found == null){
            //error
            return Double.MAX_VALUE;
        }
        
        float sign = 1f;
        
        if(useRelative){
           sign = getSign(p, found, pNormal);
        }
        
        return sign * MathUtils.instance().distancePoints(found, p);
    }
    
    /**
     * Returns the sign for given points, depending on whether the point is
     * 'ín front' (+) or 'behind' (-) the mesh.
     * 
     * @param point - vertex for which we want to get the sign
     * @param nearest - nearest neighbour for 'point' in reference mesh
     * @param pointNormal - normal of 'point'
     * @return sign, either plus (+) if vertex is 'in front' of reference mesh or minus (-) if 
     *          vertex is 'behind' reference mesh.
     */
    private float getSign(Vector3f point, Vector3f nearest, Vector3f pointNormal){
        Vector3f pointToNearest = new Vector3f(point.x - nearest.x, point.y - nearest.y, point.z - nearest.z);
        
        return Math.signum(pointToNearest.dot(pointNormal));
    }
}