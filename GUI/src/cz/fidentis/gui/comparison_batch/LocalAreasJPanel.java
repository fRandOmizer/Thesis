/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.gui.comparison_batch;

import cz.fidentis.comparison.icp.KdTreeIndexed;
import cz.fidentis.comparison.localAreas.Area;
import cz.fidentis.comparison.localAreas.BinTree;
import cz.fidentis.comparison.localAreas.LocalAreaLibrary;
import cz.fidentis.comparison.localAreas.LocalAreas;
import cz.fidentis.comparison.localAreas.VertexArea;

import cz.fidentis.model.Model;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.geometry.Point3D;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

/**
 *
 * @author zanri
 */
public class LocalAreasJPanel extends javax.swing.JPanel {

    // <editor-fold desc="Workers">
    private class FindAreasWorker extends SwingWorker<String, Object>{
        @Override
        protected String doInBackground() {
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true);

            init();

            SetList(area.getAreas());

            return "Done.";
        }

        @Override
        protected void done() {
            progressBar.setVisible(false);
        }
    }
    
    private class SelectPointWorker extends SwingWorker<String, Object>{
        @Override
        protected String doInBackground() {

            while (isMouseOnCanvas){
                int difference = differenceInMiliseconds(timeOfMouseMovement, Calendar.getInstance());
                
                if (difference >= 100){
                    //pointerViewerPanel_Batch.setToolTip(mousePosition.x, mousePosition.y, "Hello!");
                    try {
                        drawHooveredPoint();
                    }
                    catch(Exception e){
                        System.out.println(e.toString());
                    }
                    
                    
                }
                
            }
            
            return "Done.";
        }

        @Override
        protected void done() {
        
        }
    }
    // </editor-fold>
    
    private BatchComparisonResults pointerBatchComparisonResult;
    private Double SizeOfArea;
    private Double BottomTresh;
    private Double TopTresh;
    private Double min;
    private Double max;
    private List<Area> AreasList;
    private List<Area> OriginalAreasList;
    private int SelectedAreas[];
    private Boolean RelativeValues;
    private ArrayList<Float> LocalAreas;
    private VertexArea area;
    private Model model;
    private Model initialModel;
    private boolean isInicialized;
    private boolean isAnyAreaDrawn;
    private boolean isAreaSelected;
    private JFrame LocalAreaFrame;
    private LocalAreasSelectedAreaJPanel LocalAreaJPanel;
    private boolean isMouseOnCanvas;
    private Calendar timeOfMouseMovement;
    private Vector2d mousePosition; 
    private boolean isWorkerRunning;
    private boolean isPointSelected;
    private boolean isAreasSet;
    private Vector4f choosenPoint;
    private boolean isVisible;
    
    
    /**
     * Creates new form LocalAreasJPanel
     */
    public LocalAreasJPanel() {
        initComponents();
        
        SizeOfArea = 0.0;
        BottomTresh = 0.0;
        TopTresh = 0.0;
        AreasList = new ArrayList<>();
        OriginalAreasList = new ArrayList<>();
        SelectedAreas = new int[10];
        RelativeValues = true;
        LocalAreas = null;
        model = null;
        isInicialized = false;
        isAnyAreaDrawn = false;
        isWorkerRunning = false;
        isAreaSelected = false;
        isPointSelected = false;
        
        AreasJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        progressBar.setVisible(false);
        
        SetEnableComponents(false);
        
    }
    
    public boolean isInitialized(){
        return this.isInicialized;
    }
    
    private void init(){
        model = pointerBatchComparisonResult.GetAverageModel(); 
        initialModel = pointerBatchComparisonResult.GetAverageModel(); 
        
        BinTree thres = new BinTree(LocalAreas);
        area = new VertexArea(model, thres);
        area.createAreas(SizeOfArea.intValue(), BottomTresh.floatValue(), TopTresh.floatValue());
        
        LocalAreaFrame = new JFrame("Area");
        LocalAreaFrame.setVisible(false);
        LocalAreaFrame.setMinimumSize(new Dimension(630, 730));

        LocalAreaJPanel = new LocalAreasSelectedAreaJPanel();
        LocalAreaJPanel.setPointerLocalAreasJPanel(this);
        
        LocalAreaFrame.add(LocalAreaJPanel);
        
        LocalAreaFrame.pack();
        isAreasSet = false;
        
    }
    
    public void updateModel(Model model){
        if (!this.isVisible()){
            return;
        }
        
        KdTreeIndexed indexer = new KdTreeIndexed(model.getVerts());
        List<Area> tempAreaList = new ArrayList<>();
        for (int i = 0; i < OriginalAreasList.size(); i++){
            
            Area tempArea = OriginalAreasList.get(i);
            List<Integer> tempVertices = new ArrayList<>();
            for (int j = 0; j < tempArea.vertices.size(); j++){
                int areaVertexIndex = tempArea.vertices.get(j);
                int index = indexer.nearestIndex(this.initialModel.getVerts().get(areaVertexIndex));
                tempVertices.add(index);
            }
            Area deepCopyArea = deepCopy(tempArea);
            deepCopyArea.vertices = tempVertices;
            tempAreaList.add(deepCopyArea);
        }  
        this.model = model;
        this.AreasList = tempAreaList;
        
        RenderSelectedAreas();
    }
    
    public void LoadValues(float min, float max) {
        LocalAreas = new ArrayList(pointerBatchComparisonResult.GetAuxiliaryAverageResults());
        BottomTresh = (double)min;
        TopTresh = (double)max;
        this.max = (double)max;
        this.min = (double)min;
        TopTextField.setText(TopTresh.toString());
        BottomTextField.setText(BottomTresh.toString());
        isInicialized = true;
        
    }
    
    public void isVisible(boolean value){
        this.isVisible = value;
        if (!value){
            LocalAreaFrame.setVisible(false);
        }
    }
    
    public boolean isAreasSet(){
        return isAreasSet;
    }

    
    
    public void SetPointerBatchComparisonResults(BatchComparisonResults pointer){
        this.pointerBatchComparisonResult = pointer;
        this.jLabelInitialModel.setText(pointerBatchComparisonResult.GetAverageModel().getName());
    }
    
    public void SetList(List<Area> areasList){
        AreasList = areasList;
        OriginalAreasList = areasList;
        DefaultListModel listModel = new DefaultListModel();
        if (areasList.size()>0){
            for (Area item : areasList){
                listModel.addElement(item.index+" Area");
            }
            SetEnableComponents(true);
        } else {
            SetEnableComponents(false);
            listModel.addElement("No Area was found!");
        }
        AreasJList.setModel(listModel);
        AreasJListRenderer renderer = new AreasJListRenderer();
        renderer.setAreas(areasList);
        AreasJList.setCellRenderer(renderer);
        this.isAreasSet = true;
    }

    public void setMouseClickPosition(double x, double y){
        if (LocalAreaFrame == null){
            return;
        }
        
        if (!isInicialized){
            return;
        }
        
        if (!isAnyAreaDrawn){
            return;
        }
        
        if (!isVisible){
            return;
        }
       
        
        
        
        
        
        LocalAreas localAreas = pointerBatchComparisonResult.getRenderer().getLocalAreas();
        double[] modelViewMatrix = pointerBatchComparisonResult.getRenderer().getModelViewMatrix();
        double[] projectionMatrix = pointerBatchComparisonResult.getRenderer().getProjectionMatrix();
        int[] viewPort = pointerBatchComparisonResult.getRenderer().getViewPort();
        int i = 0;
        for (List<Point3D> points : localAreas.getBoundariesAreasPoints()){
            Vector3f point = LocalAreaLibrary.intersectionWithArea(x, y, viewPort, modelViewMatrix, projectionMatrix, points);

            if (point != null){
                if (!LocalAreaFrame.isVisible()){
                    LocalAreaFrame.setVisible(true);
                    LocalAreaFrame.setAlwaysOnTop(true);
                }
                
                LocalAreaJPanel.SetArea(AreasList.get(localAreas.getIndexes()[i]), 
                        RelativeValues, 
                        pointerBatchComparisonResult.getContext().getHdVisualResults(), 
                        pointerBatchComparisonResult.getContext().getModels(),
                        pointerBatchComparisonResult.getContext().getMetricTypeIndex());
                
                SetSelectedArea(localAreas.getIndexes()[i]);
                isAreaSelected = true;
                setMouseOnCanvas(true);
                return;
            }
            i++;
        }
        
        
    }
    
    public void setMouseOnCanvas(boolean value){
        this.isMouseOnCanvas = value;
        if (value && (!isWorkerRunning) && isAreaSelected){
            new SelectPointWorker().execute();
            this.isWorkerRunning = true;
        } else {
            this.isWorkerRunning = false;
        }
    }
    
    public void setMousePosition(double x, double y, Calendar time){
        this.mousePosition = new Vector2d(x, y);
        this.timeOfMouseMovement = time;
    }
    
    public void setPointInfo(){
        if (isPointSelected){
            LocalAreaJPanel.SetChoosenPoint(this.choosenPoint);
        }
        
    }
    
    public static int differenceInMiliseconds(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        
        return (int)TimeUnit.MILLISECONDS.toMillis(Math.abs(end - start));
    }

    private void SetEnableComponents(Boolean value){
        AreasJList.setEnabled(value);
        SelectButton.setEnabled(value);
        AllButton.setEnabled(value);
        RelativeComboBox.setEnabled(value);
    }
    
    public void drawHooveredPoint(){
        List<Vector4f> points = pointerBatchComparisonResult.getRenderer().getLocalAreas().getAllPointsFromOneArea();
        double[] modelViewMatrix = pointerBatchComparisonResult.getRenderer().getModelViewMatrix();
        double[] projectionMatrix = pointerBatchComparisonResult.getRenderer().getProjectionMatrix();
        int[] viewPort = pointerBatchComparisonResult.getRenderer().getViewPort();
        Vector4f point = LocalAreaLibrary.intersectionWithPoint(mousePosition.x, mousePosition.y, viewPort, modelViewMatrix, projectionMatrix, points);
        
        if (point != null){
            choosenPoint = point;
            pointerBatchComparisonResult.getRenderer().setPointToDraw(point);
            isPointSelected = true;
            
        }
    }
    
    public void setColorForArea(Color color){

        List<Float> colorArray = new ArrayList<>();
        
        colorArray.add(color.getRed()/255f);
        colorArray.add(color.getGreen()/255f);
        colorArray.add(color.getBlue()/255f);

        Area tempArea = AreasList.get(SelectedAreas[0]);
        tempArea.color = colorArray;
        AreasList.set(SelectedAreas[0], tempArea);

        RenderSelectedAreas();
    }
    
    private void RenderSelectedAreas(){
        List<Area> tempList = new ArrayList<>();
        for (int i = 0; i < SelectedAreas.length; i++){
            Area selectedArea = AreasList.get(SelectedAreas[i]);
            tempList.add(selectedArea);
        }
        
        pointerBatchComparisonResult.SetLocalAreaRender(SelectedAreas, tempList, model);

    }
    
    private void SetSelectedArea(int index){
        SelectedAreas = new int[1];
        
        SelectedAreas[0]=index;
       
        AreasJList.clearSelection();
        AreasJList.setSelectedIndices(new int[] {index});
        RenderSelectedAreas();
        
        if (AreasJList.getSelectedIndices().length>0){
             isAnyAreaDrawn = true;
        }
    }
    
    private static Area deepCopy(Area area){
        Area result = new Area();
        
        result.ariMean = area.ariMean;
        result.color = area.color;
        result.csvValues = area.csvValues;
        result.geoMean = area.geoMean;
        result.index = area.index;
        result.max = area.max;
        result.min = area.min;
        result.percentileSevFiv = area.percentileSevFiv;
        result.rootMean = area.rootMean;
        result.variance = area.variance;
        result.vertices = area.vertices;

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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        BottomTextField = new javax.swing.JTextField();
        TopTextField = new javax.swing.JTextField();
        AreaTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        AreasJList = new javax.swing.JList<>();
        ApplyButton = new javax.swing.JButton();
        SelectButton = new javax.swing.JButton();
        AllButton = new javax.swing.JButton();
        RelativeComboBox = new javax.swing.JComboBox<>();
        ExportButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jLabel5 = new javax.swing.JLabel();
        jLabelInitialModel = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(313, 700));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(LocalAreasJPanel.class, "LocalAreasJPanel.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(LocalAreasJPanel.class, "LocalAreasJPanel.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(LocalAreasJPanel.class, "LocalAreasJPanel.jLabel3.text")); // NOI18N

        BottomTextField.setText(org.openide.util.NbBundle.getMessage(LocalAreasJPanel.class, "LocalAreasJPanel.BottomTextField.text")); // NOI18N
        BottomTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                BottomTextFieldFocusLost(evt);
            }
        });

        TopTextField.setText(org.openide.util.NbBundle.getMessage(LocalAreasJPanel.class, "LocalAreasJPanel.TopTextField.text")); // NOI18N
        TopTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TopTextFieldFocusLost(evt);
            }
        });

        AreaTextField.setText(org.openide.util.NbBundle.getMessage(LocalAreasJPanel.class, "LocalAreasJPanel.AreaTextField.text")); // NOI18N
        AreaTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                AreaTextFieldFocusLost(evt);
            }
        });

        jScrollPane1.setViewportView(AreasJList);

        org.openide.awt.Mnemonics.setLocalizedText(ApplyButton, org.openide.util.NbBundle.getMessage(LocalAreasJPanel.class, "LocalAreasJPanel.ApplyButton.text")); // NOI18N
        ApplyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ApplyButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(SelectButton, org.openide.util.NbBundle.getMessage(LocalAreasJPanel.class, "LocalAreasJPanel.SelectButton.text")); // NOI18N
        SelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(AllButton, org.openide.util.NbBundle.getMessage(LocalAreasJPanel.class, "LocalAreasJPanel.AllButton.text")); // NOI18N
        AllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AllButtonActionPerformed(evt);
            }
        });

        RelativeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "True", "False" }));
        RelativeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RelativeComboBoxActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(ExportButton, org.openide.util.NbBundle.getMessage(LocalAreasJPanel.class, "LocalAreasJPanel.ExportButton.text")); // NOI18N
        ExportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(LocalAreasJPanel.class, "LocalAreasJPanel.jLabel4.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(LocalAreasJPanel.class, "LocalAreasJPanel.jLabel5.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabelInitialModel, org.openide.util.NbBundle.getMessage(LocalAreasJPanel.class, "LocalAreasJPanel.jLabelInitialModel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(0, 43, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(AllButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(SelectButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(RelativeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(ExportButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ApplyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                            .addComponent(jLabel5))
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelInitialModel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(AreaTextField)
                            .addComponent(TopTextField)
                            .addComponent(BottomTextField))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabelInitialModel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(AreaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TopTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(BottomTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(ApplyButton)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(SelectButton)
                        .addGap(18, 18, 18)
                        .addComponent(AllButton)))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RelativeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addComponent(ExportButton)
                .addGap(18, 18, 18)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold desc="Event handlers">
    private void ApplyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ApplyButtonActionPerformed
        
        new FindAreasWorker().execute();
        
    }//GEN-LAST:event_ApplyButtonActionPerformed

    private void SelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectButtonActionPerformed
        SelectedAreas = new int[AreasJList.getSelectedIndices().length];
        SelectedAreas = AreasJList.getSelectedIndices();
        
        RenderSelectedAreas();
        
        if (AreasJList.getSelectedIndices().length>0){
             isAnyAreaDrawn = true;
        }
        
        if (!pointerBatchComparisonResult.getCurrentModel().getName().equals(this.initialModel.getName()))
        {
            this.updateModel(pointerBatchComparisonResult.getCurrentModel());
        }
    }//GEN-LAST:event_SelectButtonActionPerformed

    private void AllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AllButtonActionPerformed
        SelectedAreas = new int[AreasList.size()];
        for(int i=0; i<AreasList.size(); i++){
            SelectedAreas[i]=i;
        }
        
        AreasJList.setSelectedIndices(SelectedAreas);
        RenderSelectedAreas();
        
        if (AreasJList.getSelectedIndices().length>0){
             isAnyAreaDrawn = true;
        }
        
        if (!pointerBatchComparisonResult.getCurrentModel().getName().equals(this.initialModel.getName()))
        {
            this.updateModel(pointerBatchComparisonResult.getCurrentModel());
        }
        
    }//GEN-LAST:event_AllButtonActionPerformed

    private void RelativeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RelativeComboBoxActionPerformed
        if (RelativeComboBox.getSelectedIndex()==0){
            RelativeValues = true;
        } else {
            RelativeValues = false;
        }
    }//GEN-LAST:event_RelativeComboBoxActionPerformed

    private void ExportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportButtonActionPerformed
        if(RelativeComboBox.getSelectedIndex() == 0) {
            area.makeMatrics(true);
        } else{
            area.makeMatrics(false);
        }
        
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
                writer.write(area.getAreas().toString());
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
    }//GEN-LAST:event_ExportButtonActionPerformed

    private void AreaTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_AreaTextFieldFocusLost
        try{
           SizeOfArea = Double.valueOf(this.AreaTextField.getText());
        }catch(Exception e){
           AreaTextField.setText(SizeOfArea.toString());
        }
    }//GEN-LAST:event_AreaTextFieldFocusLost

    private void TopTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TopTextFieldFocusLost
        try{
           Double TopTreshTemp = Double.valueOf(this.TopTextField.getText());
           if (TopTreshTemp> max || TopTreshTemp <= BottomTresh){
               TopTextField.setText(max.toString());
           } else {
               TopTresh = TopTreshTemp;
           }
        }catch(Exception e){
           TopTextField.setText(TopTresh.toString());
        }
    }//GEN-LAST:event_TopTextFieldFocusLost

    private void BottomTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_BottomTextFieldFocusLost
        try{
           Double BottomTreshTemp = Double.valueOf(this.BottomTextField.getText());
           if (BottomTreshTemp < min || BottomTreshTemp >= TopTresh){
               BottomTextField.setText(min.toString());
           } else {
               BottomTresh = BottomTreshTemp;
           }
        }catch(Exception e){
           BottomTextField.setText(BottomTresh.toString());
        }
    }//GEN-LAST:event_BottomTextFieldFocusLost
    // </editor-fold>

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AllButton;
    private javax.swing.JButton ApplyButton;
    private javax.swing.JTextField AreaTextField;
    private javax.swing.JList<String> AreasJList;
    private javax.swing.JTextField BottomTextField;
    private javax.swing.JButton ExportButton;
    private javax.swing.JComboBox<String> RelativeComboBox;
    private javax.swing.JButton SelectButton;
    private javax.swing.JTextField TopTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelInitialModel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}
