/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.comparison.localAreas;

import java.awt.Color;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zanri
 */
public class PointsValues {
    public List<Integer> indexes;
    public List<Float> values;
    public List<Integer> clusterMark;

    public List<List<Integer>> distribution;
    public List<String> distributionBoundaries;
    public List<Color> distributionColor;
    public float clusterIndex;
    public int maxClusteredPoints;

    public float min;
    public float max;

    public PointsValues(){
        indexes = new ArrayList<>();
        values = new ArrayList<>();
        distribution = new ArrayList<>();
        distributionBoundaries = new ArrayList<>();
        clusterMark = new ArrayList<>();
        distributionColor = new ArrayList<>();
    }

    public void setValues(List<Float> values){
        this.values = values;
        indexes = new ArrayList<>();
        for (int i = 0; i < values.size(); i++ ){
            indexes.add(i);
        }
    }

    public void selectionSort() {
        for (int i = 0; i < values.size() - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < values.size(); j++) {
                if (values.get(j) > values.get(maxIndex)){
                    maxIndex = j;
                }
            }
            float tmp = values.get(i);
            values.set(i, values.get(maxIndex));
            values.set(maxIndex, tmp);

            int temp = indexes.get(i);
            indexes.set(i, indexes.get(maxIndex));
            indexes.set(maxIndex, temp);
        }
        max = values.get(0);
        min = values.get(values.size()-1);    
    }

    public void setColors(){
        distributionColor = new ArrayList<>();
        int r = 0;
        int g = 0;
        int b = 255;
        
        int len1 = distribution.size()/2;
        int len2 = distribution.size() - len1;
        int step1 = 255 / len1;
        int step2 = 255 / len2;

        r = (len1)*step1;
        g = 255;
        b = (len1)*step1;
        
        for (int i = 0; i < len1; i++){
            r = r - step1;
            b = b - step1;
            Color color = new Color(r, g, b);
            distributionColor.add(color);
        }
        
        r = 0;
        g = 0;
        b = 255;
        
        for (int i = 0; i < len2; i++){
            r = r + step2;
            g = g + step2;
            Color color = new Color(r, g, b);
            distributionColor.add(color);
        }
        
    }

    public void calculateDistribution(float range, int size){
        distribution = new ArrayList<>();
        distributionBoundaries = new ArrayList<>();
        clusterIndex = range / ((float)size/5f);

        int numberOfCollumns = (size/5);
        int index = 0;
        maxClusteredPoints = 0;
        
        for (int i = 0; i < numberOfCollumns; i++){
            float upValue = values.get(0)-(i)*clusterIndex;
            if (i>0){
                upValue = values.get(0)-(i-1)*clusterIndex;
            }
            float downValue = values.get(0)-i*clusterIndex;

            List<Integer> Items = new ArrayList<>();

            boolean condition = true;

            while (condition){

                if (index < values.size() - 1){
                    if (values.get(0)-i*clusterIndex <= values.get(index)){
                        index++;
                        Items.add(indexes.get(index));
                        
                    }
                    else {
                        condition = false;
                    }
                }
                else {
                    condition = false;
                }
            }
            if (i+1 == indexes.size()){
                for (; index < indexes.size(); index++){
                    Items.add(indexes.get(index));
                }
            }

            if (maxClusteredPoints<=Items.size()){
                maxClusteredPoints = Items.size();
            }
            distribution.add(Items);
            DecimalFormat df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.CEILING);

            distributionBoundaries.add("<"+df.format(downValue)+";"+df.format(upValue)+")");
        }
        
        this.setColors();
    }
}
