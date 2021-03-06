package cz.fidentis.comparison.hausdorffDistance.localDif;

import java.util.ArrayList;
import java.util.List;
import cz.fidentis.model.Model;
import java.util.Collections;
import java.util.Random;
import cz.fidentis.comparison.hausdorffDistance.ComparisonMetrics;

/**
 *
 * @author Rasto
 */
public class VertexArea {

    private Model model;
    private ComparisonMetrics metric;
    private BinTree csvData;
    private List<Area> areas;

    public VertexArea(Model model, BinTree csvData) {
        this.model = model;
        this.csvData = csvData;
        this.areas = new ArrayList<Area>();
        this.metric = ComparisonMetrics.instance();
    }

    /**
     *
     * Create all areas, based on size and threshold
     *
     * @param n size of one area
     * @param threshold threshold
     */
    public void createAreas(int n, float minThreshold, float maxThreshold) {
        areas = new ArrayList<Area>();
        List<Integer> vertCount = new ArrayList<Integer>();
        int count = 0;

        for (int i = 0; i < model.getVerts().size(); i++) {
            if (!vertCount.contains(i)) {
                Area tmp = new Area();
                tmp.setIndex(count);
                tmp.setVertices(getArea(i, n, minThreshold, maxThreshold));
                tmp.setCsvValues(getCsvValues(i, n, minThreshold, maxThreshold));
                tmp.setColor(new ArrayList<Float>());
                for (int j = 0; j < 3; j++) {
                    Random rn = new Random();
                    tmp.getColor().add(j, (float) (rn.nextInt(255) + 0) / 255);
                }
                if (tmp.getVertices() != Collections.EMPTY_LIST) {
                    areas.add(tmp);
                    vertCount.addAll(tmp.getVertices());
                    count++;
                }
            }
        }

    }

    public void makeMatrics(Boolean relative) {
        for (int i = 0; i < areas.size(); i++) {
            areas.get(i).setGeoMean(metric.geometricMean(areas.get(i).getCsvValues(), relative));
            areas.get(i).setAriMean(metric.aritmeticMean(areas.get(i).getCsvValues(), relative));
            areas.get(i).setPercentileSevFiv(metric.percentileSeventyFive(areas.get(i).getCsvValues(), relative));
            areas.get(i).setMax(metric.findMaxDistance(areas.get(i).getCsvValues(), relative));
            areas.get(i).setMin(metric.findMinDistance(areas.get(i).getCsvValues(), relative));
            areas.get(i).setRootMean(metric.rootMeanSqr(areas.get(i).getCsvValues(), relative));
            areas.get(i).setVariance(metric.variance(areas.get(i).getCsvValues(), relative));
        }
    }

    public List<Area> getAreas() {
        return this.areas;
    }

    public int sizeAreas() {
        return this.areas.size();
    }

    /**
     *
     * Get csv values in area
     *
     * @param vertex primary vertex od area
     * @param n size of area
     * @param threshold threshold
     * @return (list)csv values in area
     */
    public List<Float> getCsvValues(int vertex, int n, float minThreshold, float maxThreshold) {
        List<Integer> area = getArea(vertex, n, minThreshold, maxThreshold);
        List<Float> result = new ArrayList<Float>();

        for (int i = 0; i < area.size(); i++) {
            result.add(csvData.findNode(area.get(i)));
        }

        return result;
    }

    /**
     *
     * Get area of vertices, based on threshold
     *
     * @param vertex index of primary vertex
     * @param n size of area
     * @param threshold threshold
     * @return (list)indexes of area, area with size 0 return empty list
     */
    public List<Integer> getArea(int vertex, int n, float minThreshold, float maxThreshold) {
        List<Integer> area;

        if (csvData.findNode(vertex) >= minThreshold && csvData.findNode(vertex) <= maxThreshold) {
            area = findArea(vertex, minThreshold, maxThreshold);
            if (area.size() >= n) {
                return area;
            }
        }
        return Collections.EMPTY_LIST;
    }

    /**
     *
     * Find area of vertices
     *
     * @param vertex index of primary vertex
     * @param threshold threshold
     * @return (list)indexes of area
     */
    public List<Integer> findArea(int vertex, float minThreshold, float maxThreshold) {

        // list of vertex neighbors
        List<Integer> tmp = arrayToList(model.getCornerTable().getIndexNeighbors(vertex, model));
        List<Integer> result = new ArrayList<Integer>();
        result.add(vertex);

        while (tmp.size() != 0) {
            // remove double indexes
            for (int i = 0; i < result.size(); i++) {
                tmp.remove(result.get(i));
            }

            if (csvData.findNode(tmp.get(tmp.size() - 1)) >= minThreshold && csvData.findNode(tmp.get(tmp.size() - 1)) <= maxThreshold) {
                result.add(tmp.get(tmp.size() - 1));
                int[] tmp2 = model.getCornerTable().getIndexNeighbors(tmp.get(tmp.size() - 1), model);
                tmp.remove(tmp.get(tmp.size() - 1));
                tmp.addAll(arrayToList(tmp2));
            } else {
                tmp.remove(tmp.get(tmp.size() - 1));
            }
        }

        return result;
    }

    // copy int array to list of integers
    public List<Integer> arrayToList(int[] tmp) {

        List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < tmp.length; i++) {
            result.add(tmp[i]);
        }

        return result;
    }

}
