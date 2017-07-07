/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.gui.comparison_batch;

import cz.fidentis.comparison.hausdorffDistance.ComparisonMetrics;
import cz.fidentis.comparison.localAreas.Area;
import static cz.fidentis.processing.comparison.surfaceComparison.SurfaceComparisonProcessing.computeSingleVariation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Math.abs;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.vecmath.Vector4f;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;

/**
 *
 * @author zanri
 */
public class LocalAreasSelectedAreaJPanel extends javax.swing.JPanel {

    private String text;
    private LocalAreasJPanel pointerLocalAreasJPanel;
    private Area area;
    private ComparisonMetrics metric;
    private ArrayList<ArrayList<Float>> HdVisualResults;
    private List<Integer> faceComparison;
    private List<String> modelsName;
    private int metricIndex;
    private List<String> metricName;
    private HistogramJPanel histogram;
    private Color areaColor;
    
    /**
     * Creates new form LocalAreasSelectedPointJPanel
     */
    public LocalAreasSelectedAreaJPanel() {
        initComponents();
        text = "Point(x,y,z): [-1, -1, -1]";
        metricName = new ArrayList<>();
        metricName.add("Root Mean Square");
        metricName.add("Arithmetic Mean");
        metricName.add("Geometric Mean");
        metricName.add("Minimal Distance");
        metricName.add("Maximal Distance");
        metricName.add("Variance");
        metricName.add("75 percentil");
        histogram = new HistogramJPanel();
        histogram.setPointer(this);
        histogram.setSize(this.histogramHolder.getPreferredSize().width, this.histogramHolder.getPreferredSize().height);
        this.histogramHolder.setLayout(new BorderLayout());

        this.histogramHolder.add(histogram, BorderLayout.CENTER);
        
    }
    
    public void SetArea(Area area, Boolean relative, ArrayList<ArrayList<Float>> HdVisualResults, List<File> models, int metricIndex){
        this.area = area;
        this.metric = ComparisonMetrics.instance();
        this.HdVisualResults = HdVisualResults;
        
        area.geoMean = metric.geometricMean(area.csvValues, relative);
        area.ariMean = metric.aritmeticMean(area.csvValues, relative);
        area.percentileSevFiv = metric.percentileSeventyFive(area.csvValues, relative);
        area.max = metric.findMaxDistance(area.csvValues, relative);
        area.min = metric.findMinDistance(area.csvValues, relative);
        area.rootMean = metric.rootMeanSqr(area.csvValues, relative);
        area.variance = metric.variance(area.csvValues, relative);
        
        faceComparison = calculateFaceComparison(HdVisualResults, area, metricIndex, relative);
        modelsName = filterModelName(models);
        this.metricIndex = metricIndex;
                
        this.labelAreaName.setText("Area "+ area.index+"");
        this.GeoMean.setText(""+ area.geoMean+"");
        this.AriMean.setText(""+ area.ariMean+"");
        this.RootMean.setText(""+ area.rootMean+"");
        this.Max.setText(""+ area.max+"");
        this.Min.setText(""+ area.min+"");
        this.SeventyFive.setText(""+ area.percentileSevFiv+"");
        this.Variance.setText(""+ area.variance+"");
        this.DifferentFace.setText(modelsName.get(faceComparison.get(0))+"");
        this.SimilarFace.setText(modelsName.get(faceComparison.get(faceComparison.size()-1))+"");
        this.MetricName.setText(metricName.get(metricIndex));
        
        this.areaColor = new Color(area.color.get(0), area.color.get(1), area.color.get(2));
        this.histogram.setSize(this.histogramHolder.getWidth(), this.histogramHolder.getHeight());
        this.histogram.setValues(area.csvValues);
        this.histogram.setColor(areaColor);
        
    }
    
    public void setPointerLocalAreasJPanel(LocalAreasJPanel pointerLocalAreasJPanel){
        this.pointerLocalAreasJPanel = pointerLocalAreasJPanel;
    }
    
    public void SetChoosenPoint(Vector4f point){
        try{
            DecimalFormat df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.CEILING);
            
            double x = (double) point.x;
            double y = (double) point.y;
            double z = (double) point.z;
            
            this.labelPoint.setText("Point Value ["+df.format(x)+", "+df.format(y)+", "+df.format(z)+"]:");
            this.point.setText(df.format(area.csvValues.get(area.vertices.indexOf((int)point.w)))+"");
        } catch(Exception e) {
            
        }
        
    }
    
    void updateSelectedPoints(List<Integer> get) {
        
    }

    void setColors(List<List<Integer>> distribution, List<Color> distributionColor) {
        
    }

    private static List<String> filterModelName(List<File> files){
        List<String> result = new ArrayList<>();
        
        for (int i = 0; i < files.size(); i++){
            int index = files.get(i).getPath().lastIndexOf("\\")+1;
            String text = files.get(i).getPath().substring(index, files.get(i).getPath().length());
            result.add(text);
        }
        
        return result;
    }
    
    
    private static List<Integer> calculateFaceComparison(ArrayList<ArrayList<Float>> HdVisualResults, Area area, int metricIndex, boolean relative) {
        ComparisonMetrics metric = ComparisonMetrics.instance();
        List<Integer> result = new ArrayList<>();
        List<Float> averageDistance = new ArrayList<>();
        
        List<List<Float>> filteredArays = filterArrays(HdVisualResults, area);
        
        for (int i = 0; i < filteredArays.size(); i++){
            float average = computeSingleVariation(filteredArays.get(i), metricIndex, relative);
  
            averageDistance.add(average);
        }
        
        for (int i = 0; i < filteredArays.size(); i++){
            result.add(i);
        }
        
        for (int i = 0; i < averageDistance.size() - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < averageDistance.size(); j++) {
                if (averageDistance.get(j) > averageDistance.get(maxIndex)){
                    maxIndex = j;
                } 
            }
            float tmp = averageDistance.get(i);
            averageDistance.set(i, averageDistance.get(maxIndex));
            averageDistance.set(maxIndex, tmp);
            
            int temp = result.get(i);
            result.set(i, result.get(maxIndex));
            result.set(maxIndex, temp);
        } 
        
        
        return result;
    }

    
    private static List<List<Float>> filterArrays(ArrayList<ArrayList<Float>> arrays, Area area){
        List<List<Float>> result = new ArrayList<>();
        for (int i = 0; i < arrays.size(); i++){
            ArrayList<Float> array = arrays.get(i);
            result.add(filterItems(array, area));
        }
        
        return result;
    }
    
    private static List<Float> filterItems(ArrayList<Float> x, Area area){
        List<Float> result = new ArrayList<>();
        for (int i = 0; i < area.vertices.size(); i++){
            result.add(x.get(area.vertices.get(i)));
            
        }
        return result;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelAreaName = new javax.swing.JLabel();
        labelPoint = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        labelAreaName1 = new javax.swing.JLabel();
        labelAreaName2 = new javax.swing.JLabel();
        labelAreaName3 = new javax.swing.JLabel();
        labelAreaName4 = new javax.swing.JLabel();
        labelAreaName5 = new javax.swing.JLabel();
        labelAreaName6 = new javax.swing.JLabel();
        labelAreaName7 = new javax.swing.JLabel();
        GeoMean = new javax.swing.JLabel();
        AriMean = new javax.swing.JLabel();
        RootMean = new javax.swing.JLabel();
        Max = new javax.swing.JLabel();
        Min = new javax.swing.JLabel();
        SeventyFive = new javax.swing.JLabel();
        Variance = new javax.swing.JLabel();
        point = new javax.swing.JLabel();
        jButtonExport = new javax.swing.JButton();
        labelAreaName8 = new javax.swing.JLabel();
        DifferentFace = new javax.swing.JLabel();
        labelAreaName9 = new javax.swing.JLabel();
        SimilarFace = new javax.swing.JLabel();
        labelAreaName10 = new javax.swing.JLabel();
        MetricName = new javax.swing.JLabel();
        histogramHolder = new javax.swing.JPanel();
        labelAreaName11 = new javax.swing.JLabel();
        jButtonChangeColor = new javax.swing.JButton();
        jButtonAreaColorChange = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(600, 700));
        setPreferredSize(new java.awt.Dimension(600, 700));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        labelAreaName.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(labelAreaName, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.labelAreaName.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelPoint, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.labelPoint.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        org.openide.awt.Mnemonics.setLocalizedText(labelAreaName1, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.labelAreaName1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelAreaName2, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.labelAreaName2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelAreaName3, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.labelAreaName3.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelAreaName4, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.labelAreaName4.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelAreaName5, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.labelAreaName5.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelAreaName6, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.labelAreaName6.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelAreaName7, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.labelAreaName7.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(GeoMean, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.GeoMean.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(AriMean, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.AriMean.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(RootMean, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.RootMean.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(Max, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.Max.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(Min, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.Min.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(SeventyFive, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.SeventyFive.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(Variance, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.Variance.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(point, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.point.text")); // NOI18N
        point.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonExport, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.jButtonExport.text")); // NOI18N
        jButtonExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(labelAreaName8, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.labelAreaName8.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(DifferentFace, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.DifferentFace.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelAreaName9, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.labelAreaName9.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(SimilarFace, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.SimilarFace.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelAreaName10, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.labelAreaName10.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(MetricName, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.MetricName.text")); // NOI18N

        histogramHolder.setPreferredSize(new java.awt.Dimension(600, 200));

        javax.swing.GroupLayout histogramHolderLayout = new javax.swing.GroupLayout(histogramHolder);
        histogramHolder.setLayout(histogramHolderLayout);
        histogramHolderLayout.setHorizontalGroup(
            histogramHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        histogramHolderLayout.setVerticalGroup(
            histogramHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 205, Short.MAX_VALUE)
        );

        org.openide.awt.Mnemonics.setLocalizedText(labelAreaName11, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.labelAreaName11.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonChangeColor, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.jButtonChangeColor.text")); // NOI18N
        jButtonChangeColor.setEnabled(false);
        jButtonChangeColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChangeColorActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonAreaColorChange, org.openide.util.NbBundle.getMessage(LocalAreasSelectedAreaJPanel.class, "LocalAreasSelectedAreaJPanel.jButtonAreaColorChange.text")); // NOI18N
        jButtonAreaColorChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAreaColorChangeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(labelAreaName, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(histogramHolder, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelAreaName10)
                            .addComponent(labelAreaName8)
                            .addComponent(labelAreaName9)
                            .addComponent(labelAreaName11, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(SimilarFace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(DifferentFace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(MetricName, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(labelAreaName6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(labelAreaName5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(labelAreaName4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(labelAreaName3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(labelAreaName1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labelAreaName2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labelAreaName7))
                                    .addComponent(labelPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(point, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(GeoMean, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(Variance, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                                            .addComponent(SeventyFive, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(Min, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(Max, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(RootMean, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(AriMean, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButtonAreaColorChange, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonChangeColor, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(23, 23, 23)))
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonExport, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labelAreaName)
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelAreaName2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelAreaName1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelAreaName3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelAreaName4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelAreaName5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelAreaName6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelAreaName7))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(GeoMean)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AriMean)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RootMean)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Max)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Min)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SeventyFive)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Variance))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jButtonChangeColor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelAreaName11)
                    .addComponent(jButtonAreaColorChange))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(histogramHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelAreaName10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelAreaName8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelAreaName9)
                        .addGap(68, 68, 68)
                        .addComponent(labelPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(MetricName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DifferentFace)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SimilarFace)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonExport)
                        .addGap(18, 18, 18)
                        .addComponent(point)))
                .addGap(21, 21, 21))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportActionPerformed

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        fileChooser.setFileFilter(filter);
        Component modalToComponent = null;
        if (fileChooser.showSaveDialog(modalToComponent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // save to file
            try {
                // creates the file
                file.createNewFile();
            } catch (IOException ex) {
            }
            // creates a FileWriter Object
            FileWriter writer = null;
            try {
                writer = new FileWriter(file);
            } catch (IOException ex) {
            }
            try {
                // Writes the content to the file
                writer.write(area.toString());
            } catch (IOException ex) {
            }
            try {
                writer.flush();
            } catch (IOException ex) {
            }
            try {
                writer.close();
            } catch (IOException ex) {
            }
        }
    }//GEN-LAST:event_jButtonExportActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if (this.histogram != null && area != null){
            histogram.setSize(this.histogramHolder.getWidth(), this.histogramHolder.getHeight());
            this.histogram.setValues(area.csvValues);
        }
        
    }//GEN-LAST:event_formComponentResized

    private void jButtonChangeColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChangeColorActionPerformed

        areaColor = JColorChooser.showDialog(null, "Change Area Color", areaColor);
        histogram.setColor(areaColor);
        pointerLocalAreasJPanel.setColorForArea(areaColor);
    }//GEN-LAST:event_jButtonChangeColorActionPerformed

    private void jButtonAreaColorChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAreaColorChangeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAreaColorChangeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AriMean;
    private javax.swing.JLabel DifferentFace;
    private javax.swing.JLabel GeoMean;
    private javax.swing.JLabel Max;
    private javax.swing.JLabel MetricName;
    private javax.swing.JLabel Min;
    private javax.swing.JLabel RootMean;
    private javax.swing.JLabel SeventyFive;
    private javax.swing.JLabel SimilarFace;
    private javax.swing.JLabel Variance;
    private javax.swing.JPanel histogramHolder;
    private javax.swing.JButton jButtonAreaColorChange;
    private javax.swing.JButton jButtonChangeColor;
    private javax.swing.JButton jButtonExport;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labelAreaName;
    private javax.swing.JLabel labelAreaName1;
    private javax.swing.JLabel labelAreaName10;
    private javax.swing.JLabel labelAreaName11;
    private javax.swing.JLabel labelAreaName2;
    private javax.swing.JLabel labelAreaName3;
    private javax.swing.JLabel labelAreaName4;
    private javax.swing.JLabel labelAreaName5;
    private javax.swing.JLabel labelAreaName6;
    private javax.swing.JLabel labelAreaName7;
    private javax.swing.JLabel labelAreaName8;
    private javax.swing.JLabel labelAreaName9;
    private javax.swing.JLabel labelPoint;
    private javax.swing.JLabel point;
    // End of variables declaration//GEN-END:variables

    



    
}
