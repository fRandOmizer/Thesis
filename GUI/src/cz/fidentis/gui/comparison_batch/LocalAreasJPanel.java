/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.gui.comparison_batch;

import cz.fidentis.comparison.localAreas.Area;
import cz.fidentis.comparison.localAreas.BinTree;
import cz.fidentis.comparison.localAreas.VertexArea;
import cz.fidentis.gui.GUIController;

import cz.fidentis.landmarkParser.CSVparser;
import cz.fidentis.model.Model;
import cz.fidentis.visualisation.surfaceComparison.HDpaintingInfo;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author zanri
 */
public class LocalAreasJPanel extends javax.swing.JPanel {

    

    private class MyWorker extends SwingWorker<String, Object>{
        protected String doInBackground() {
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        
        init();
        
        SetList(area.getAreas());
       
        return "Done.";
     }

     protected void done() {
        progressBar.setVisible(false);
     }
    }
    
    private BatchComparisonResults pointer;
    private Double SizeOfArea;
    private Double BottomTresh;
    private Double TopTresh;
    private Double min;
    private Double max;
    private List<Area> AreasList;
    private int SelectedAreas[];
    private Boolean RelativeValues;
    private ArrayList<Float> LocalAreas;
    private VertexArea area;
    private Model model;
    private boolean isInicialized;
    private boolean isAnyAreaDrawn;
    private JFrame LocalAreaFrame;
    private LocalAreasSelectedAreaJPanel LocalAreaJPanel;
    
    
    
    /**
     * Creates new form LocalAreasJPanel
     */
    public LocalAreasJPanel() {
        initComponents();
        
        SizeOfArea = 0.0;
        BottomTresh = 0.0;
        TopTresh = 0.0;
        AreasList = new ArrayList<>();
        SelectedAreas = new int[10];
        RelativeValues = true;
        LocalAreas = null;
        model = null;
        isInicialized = false;
        isAnyAreaDrawn = false;
        
        AreasJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        progressBar.setVisible(false);
        
        SetEnableComponents(false);
    }
    
    public boolean isInitialized(){
        return this.isInicialized;
    }
    
    private void init(){
        model = pointer.GetCurrentModel();
        BinTree thres = new BinTree(LocalAreas);
        area = new VertexArea(model, thres);
        area.createAreas(SizeOfArea.intValue(), BottomTresh.floatValue(), TopTresh.floatValue());
        
        LocalAreaFrame = new JFrame("LocalAreas");
        LocalAreaFrame.setVisible(false);
        LocalAreaFrame.setMinimumSize(new Dimension(100, 100));
        LocalAreaFrame.setMaximumSize(new Dimension(100, 100));

        LocalAreaJPanel = new LocalAreasSelectedAreaJPanel();
        
        LocalAreaFrame.add(LocalAreaJPanel);
        
        LocalAreaFrame.pack();
        
    }
    
    public void LoadValues(float min, float max) {
        LocalAreas = new ArrayList(pointer.GetAuxiliaryAverageResults());
        BottomTresh = (double)min;
        TopTresh = (double)max;
        this.max = (double)max;
        this.min = (double)min;
        TopTextField.setText(TopTresh.toString());
        BottomTextField.setText(BottomTresh.toString());
        isInicialized = true;
        
    }

    public void SetPointer(BatchComparisonResults pointer){
        this.pointer = pointer;
    }
    
    public void SetList(List<Area> areasList){
        AreasList = areasList;
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
       
        
        if (!LocalAreaFrame.isVisible()){
            LocalAreaFrame.setVisible(true);
        }
        LocalAreaJPanel.SetPosition(x, y, 0);
        
    }

    private void SetEnableComponents(Boolean value){
        AreasJList.setEnabled(value);
        SelectButton.setEnabled(value);
        AllButton.setEnabled(value);
        RelativeComboBox.setEnabled(value);
    }
    
    private void RenderSelectedAreas(){
        List<Area> tempList = new ArrayList<>();
        for (int i = 0; i < SelectedAreas.length; i++){
            Area selectedArea = AreasList.get(SelectedAreas[i]);
            tempList.add(selectedArea);
        }
        
        pointer.SetLocalAreaRender(tempList, model);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
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
                    .addComponent(ExportButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AreaTextField)
                            .addComponent(TopTextField)
                            .addComponent(BottomTextField)))
                    .addComponent(ApplyButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
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
                .addGap(18, 18, 18)
                .addComponent(ApplyButton)
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(SelectButton)
                        .addGap(18, 18, 18)
                        .addComponent(AllButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
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

    private void ApplyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ApplyButtonActionPerformed
        
        new MyWorker().execute();
        
    }//GEN-LAST:event_ApplyButtonActionPerformed

    private void SelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectButtonActionPerformed
        SelectedAreas = new int[AreasJList.getSelectedIndices().length];
        SelectedAreas = AreasJList.getSelectedIndices();
        
        RenderSelectedAreas();
        
        if (AreasJList.getSelectedIndices().length>0){
             isAnyAreaDrawn = true;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}
