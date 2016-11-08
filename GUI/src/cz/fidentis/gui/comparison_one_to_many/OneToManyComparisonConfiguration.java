/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.gui.comparison_one_to_many;

import cz.fidentis.comparison.ComparisonMethod;
import cz.fidentis.comparison.ICPmetric;
import cz.fidentis.comparison.RegistrationMethod;
import cz.fidentis.visualisation.surfaceComparison.HDpainting;
import cz.fidentis.comparison.icp.KdTree;
import cz.fidentis.comparison.icp.KdTreeFaces;
import cz.fidentis.comparison.icp.KdTreeIndexed;
import cz.fidentis.comparison.procrustes.Procrustes1ToMany;
import cz.fidentis.controller.OneToManyComparison;
import cz.fidentis.featurepoints.FacialPoint;
import cz.fidentis.featurepoints.curvature.CurvatureType;
import cz.fidentis.featurepoints.curvature.Curvature_jv;
import cz.fidentis.gui.GUIController;
import cz.fidentis.gui.ProjectTopComponent;
import cz.fidentis.gui.guisetup.OneToManyGUISetup;
import cz.fidentis.model.Model;
import cz.fidentis.model.ModelLoader;
import cz.fidentis.processing.comparison.surfaceComparison.SurfaceComparisonProcessing;
import cz.fidentis.processing.exportProcessing.FPImportExport;
import cz.fidentis.processing.exportProcessing.ResultExports;
import cz.fidentis.utils.SortUtils;
import cz.fidentis.visualisation.procrustes.PApainting;
import cz.fidentis.visualisation.procrustes.PApaintingInfo;
import cz.fidentis.visualisation.surfaceComparison.HDpaintingInfo;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Exceptions;

/**
 *
 * @author Katka
 */
@SuppressWarnings("rawtypes")
public class OneToManyComparisonConfiguration extends javax.swing.JPanel {

    JPanel activeColorPanel;
    

    /**
     * Creates new form ComparisonConfiguration
     */
    public OneToManyComparisonConfiguration() {
        initComponents();
        activeColorPanel = new JPanel();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jFileChooser1 = new javax.swing.JFileChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        compMethodComboBox = new javax.swing.JComboBox();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        jPanel5 = new javax.swing.JPanel();
        procrustesPanel = new javax.swing.JPanel();
        fpScalingCheckBox = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        fpThresholdSlider = new javax.swing.JSlider();
        jLabel12 = new javax.swing.JLabel();
        processComparisonButton = new javax.swing.JButton();
        createAvgLabel = new javax.swing.JLabel();
        createAvgCheckBox = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton10 = new javax.swing.JButton();
        exportLandmarksButton = new javax.swing.JButton();

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(OneToManyComparisonConfiguration.class, "OneToManyComparisonConfiguration.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(OneToManyComparisonConfiguration.class, "OneToManyComparisonConfiguration.jLabel3.text")); // NOI18N

        compMethodComboBox.setModel(new DefaultComboBoxModel(ComparisonMethod.values()));
        compMethodComboBox.setMinimumSize(new java.awt.Dimension(187, 20));
        compMethodComboBox.setPreferredSize(new java.awt.Dimension(187, 20));
        compMethodComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compMethodComboBoxActionPerformed(evt);
            }
        });

        procrustesPanel.setVisible(false);

        org.openide.awt.Mnemonics.setLocalizedText(fpScalingCheckBox, org.openide.util.NbBundle.getMessage(OneToManyComparisonConfiguration.class, "OneToManyComparisonConfiguration.fpScalingCheckBox.text")); // NOI18N
        fpScalingCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fpScalingCheckBoxStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(OneToManyComparisonConfiguration.class, "OneToManyComparisonConfiguration.jLabel8.text")); // NOI18N

        fpThresholdSlider.setMajorTickSpacing(20);
        fpThresholdSlider.setMinorTickSpacing(5);
        fpThresholdSlider.setPaintLabels(true);
        fpThresholdSlider.setPaintTicks(true);
        fpThresholdSlider.setValue(30);
        fpThresholdSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fpThresholdSliderStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel12, org.openide.util.NbBundle.getMessage(OneToManyComparisonConfiguration.class, "OneToManyComparisonConfiguration.jLabel12.text")); // NOI18N

        javax.swing.GroupLayout procrustesPanelLayout = new javax.swing.GroupLayout(procrustesPanel);
        procrustesPanel.setLayout(procrustesPanelLayout);
        procrustesPanelLayout.setHorizontalGroup(
            procrustesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(procrustesPanelLayout.createSequentialGroup()
                .addGroup(procrustesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(procrustesPanelLayout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(fpThresholdSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(procrustesPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(fpScalingCheckBox)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        procrustesPanelLayout.setVerticalGroup(
            procrustesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(procrustesPanelLayout.createSequentialGroup()
                .addGroup(procrustesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fpScalingCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(procrustesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fpThresholdSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        Hashtable tresholdTable = new Hashtable();
        tresholdTable.put(new Integer(0), new JLabel("0"));
        tresholdTable.put(new Integer(20), new JLabel("0,2"));
        tresholdTable.put(new Integer(40), new JLabel("0,4"));
        tresholdTable.put(new Integer(60), new JLabel("0,6"));
        tresholdTable.put(new Integer(80), new JLabel("0,8"));
        tresholdTable.put(new Integer(100), new JLabel("1"));
        fpThresholdSlider.setLabelTable(tresholdTable);

        org.openide.awt.Mnemonics.setLocalizedText(processComparisonButton, org.openide.util.NbBundle.getMessage(OneToManyComparisonConfiguration.class, "OneToManyComparisonConfiguration.processComparisonButton.text")); // NOI18N
        processComparisonButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                processComparisonButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(createAvgLabel, org.openide.util.NbBundle.getMessage(OneToManyComparisonConfiguration.class, "OneToManyComparisonConfiguration.createAvgLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(createAvgCheckBox, org.openide.util.NbBundle.getMessage(OneToManyComparisonConfiguration.class, "OneToManyComparisonConfiguration.createAvgCheckBox.text")); // NOI18N
        createAvgCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                createAvgCheckBoxStateChanged(evt);
            }
        });
        createAvgCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createAvgCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(processComparisonButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(procrustesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(createAvgLabel)
                .addGap(18, 18, 18)
                .addComponent(createAvgCheckBox)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(createAvgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(createAvgCheckBox))
                .addGap(5, 5, 5)
                .addComponent(procrustesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(processComparisonButton)
                .addContainerGap())
        );

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(OneToManyComparisonConfiguration.class, "OneToManyComparisonConfiguration.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton10, org.openide.util.NbBundle.getMessage(OneToManyComparisonConfiguration.class, "OneToManyComparisonConfiguration.jButton10.text")); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(exportLandmarksButton, org.openide.util.NbBundle.getMessage(OneToManyComparisonConfiguration.class, "OneToManyComparisonConfiguration.exportLandmarksButton.text")); // NOI18N
        exportLandmarksButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportLandmarksButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(exportLandmarksButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(compMethodComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(compMethodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exportLandmarksButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(14, 14, 14))
        );

        jScrollPane1.setViewportView(jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fpThresholdSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fpThresholdSliderStateChanged
        getContext().setFpTreshold(fpThresholdSlider.getValue());
    }//GEN-LAST:event_fpThresholdSliderStateChanged

    private void fpScalingCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fpScalingCheckBoxStateChanged
        getContext().setFpScaling(fpScalingCheckBox.isSelected());
    }//GEN-LAST:event_fpScalingCheckBoxStateChanged

    public void computeComparison(final ProjectTopComponent tc){
        final OneToManyComparison c = getContext();
        
        Runnable run = new Runnable() {

            @Override
            public void run() {
                
                ComparisonMethod usedCM = c.getComparisonMethod();                
                
                if (usedCM == ComparisonMethod.HAUSDORFF_DIST
                        || usedCM == ComparisonMethod.HAUSDORFF_CURV) {
                    //Computing Hausdorff Distance

                    ProgressHandle p;

                    p = ProgressHandleFactory.createHandle("Computing comparison...");
                    p.start();

                    try {
                        SurfaceComparisonProcessing.setP(p);

                        List<Float> results;
                        List<ArrayList<Float>> numResults;
                        List<File> models = c.getRegisteredModels();
                        List<File> origModels = c.getModels();

                        if (models == null) {
                            models = c.getModels();
                        }

                        Model mainF = tc.getOneToManyViewerPanel().getListener1().getModel();
                        ModelLoader ml = new ModelLoader();
                        if (c.getRegistrationMethod() == RegistrationMethod.NO_REGISTRATION) {
                            mainF = ml.loadModel(c.getPrimaryModel().getFile(), false, false);
                        }

                        boolean createAvg = c.isCreateAvgFace();
                        c.setCreateAvgFace(createAvg);                        
                        
                        //TODO: pick which model is template?
                        Model template = ml.loadModel(models.get(0), Boolean.FALSE, false);

                        tc.getOneToManyViewerPanel().getListener2().removeModel();
                        tc.getOneToManyViewerPanel().getListener2().setModels(template);

                        ICPmetric metric = c.getIcpMetric();
                        if (metric == null) {
                            metric = ICPmetric.VERTEX_TO_VERTEX;
                        }

                        if(createAvg){
                            SurfaceComparisonProcessing.instance().computeAverage(template, models, metric);
                        }
                        KdTree templateTree;
                        List<Float> var;
                        c.setAvgFace(template);

                        if (usedCM == ComparisonMethod.HAUSDORFF_DIST) {

                            if (metric == ICPmetric.VERTEX_TO_VERTEX) {
                                templateTree = new KdTreeIndexed(template.getVerts());
                            } else {
                                templateTree = new KdTreeFaces(template.getVerts(), template.getFaces());
                            }

                            results = SurfaceComparisonProcessing.instance().compareOneToMany(templateTree, mainF, true, null, usedCM);
                        } else {
                            templateTree = new KdTreeIndexed(template.getVerts());
                            c.setIcpMetric(ICPmetric.VERTEX_TO_VERTEX);

                            Curvature_jv mainCurv = new Curvature_jv(mainF);
                            results = SurfaceComparisonProcessing.instance().compareOneToMany(templateTree, mainF, true,
                                    mainCurv.getCurvature(CurvatureType.Gaussian), usedCM);
                        }
                        
                        numResults = SurfaceComparisonProcessing.instance().compareOneToManyNumeric(mainF, models, true, usedCM);

                        var = SurfaceComparisonProcessing.instance().compareOneToManyVariation(numResults, 1.f, 0.0f, 0, true);
                        List<Float> sortedResRes = SortUtils.instance().sortValues(results);
                        List<Float> sortedResAbs;
                        List<Float> absVal = new LinkedList<>();

                        for (Float f : results) {
                            absVal.add(Math.abs(f));
                        }

                        sortedResAbs = SortUtils.instance().sortValues(absVal);

                        c.setSortedHdAbs(sortedResAbs);
                        c.setSortedHdRel(sortedResRes);

                        String strRes = setValues(var, origModels, mainF.getName(), 0);

                        c.setNumResults(numResults);
                        c.setNumericalResults(strRes);

                        c.setHd(results);

                        HDpaintingInfo info = new HDpaintingInfo(results, tc.getOneToManyViewerPanel().getListener1().getModel(), true);
                        HDpainting hd = new HDpainting(info);

                        tc.getOneToManyViewerPanel().getListener1().drawHD(true);
                        tc.getOneToManyViewerPanel().getListener1().setHdPaint(hd);
                        tc.getOneToManyViewerPanel().getListener1().setHdInfo(info);
                        c.setHDP(hd);
                        c.setHdPaintingInfo(info);

                        p.finish();
                    } catch (Exception ex) {
                        Exceptions.printStackTrace(ex);
                        
                    }finally{
                        p.finish();
                    }

                } else {

                    //Starting Procrustes Comparison
                    List<List<FacialPoint>> list = new ArrayList();
                    int size = c.getModels().size();
                    for (int i = 0; i < size; i++) {
                        List<FacialPoint> facialPoints = c.getFacialPoints(
                                c.getModels().get(i).getName());
                        list.add(facialPoints);
                    }

                    //Created list of points
                    Procrustes1ToMany procrustes = new Procrustes1ToMany(tc.getOneToManyViewerPanel().getListener1().getFpUniverse().getFacialPoints(),
                            list, c.isFpScaling());

                    String result = procrustes.compare1toN(c.getFpTreshold() / 100f, tc.getOneToManyViewerPanel().getListener1().getModel().getName(),
                            c.getModels());
                    c.setNumericalResults(result);

                    tc.getOneToManyViewerPanel().getListener2().setProcrustes(true);

                    PApaintingInfo paInfo = new PApaintingInfo(procrustes.getGpa(), procrustes.getPa(), 1);
 

                    if (c.isFpScaling()) {
                        tc.getOneToManyViewerPanel().getListener2().setCameraPosition(0, 0, 700);
                        paInfo.setPointSize(30 * 3);
                    } else {
                        tc.getOneToManyViewerPanel().getListener2().setCameraPosition(0, 0, 700);
                        paInfo.setPointSize(30 * 3);
                    }

                    tc.getOneToManyViewerPanel().getListener2().setPaInfo(paInfo);
                    tc.getOneToManyViewerPanel().getListener2().setPaPainting(new PApainting(paInfo));

                    repaint();
                }
                
                //set up default comparison result values
                OneToManyGUISetup.defaultValuesComparisonResults(c);

                c.setState(3);
               
                if (GUIController.getSelectedProjectTopComponent() == tc) {
                    GUIController.getConfigurationTopComponent().addOneToManyComparisonResults();
                }
                
                GUIController.updateNavigator();
            }
        };

        Thread t = new Thread(run);
        t.start();

    }

    private void processComparisonButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_processComparisonButtonActionPerformed
        computeComparison(GUIController.getSelectedProjectTopComponent());
        
    }//GEN-LAST:event_processComparisonButtonActionPerformed

    private void compMethodComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compMethodComboBoxActionPerformed
        if (((ComparisonMethod)compMethodComboBox.getSelectedItem()) == ComparisonMethod.PROCRUSTES) {
            procrustesPanel.setVisible(true);
            
            createAvgCheckBox.setVisible(false);
            createAvgLabel.setVisible(false);
        }
        if (((ComparisonMethod)compMethodComboBox.getSelectedItem()) == ComparisonMethod.HAUSDORFF_DIST ||
              ((ComparisonMethod)compMethodComboBox.getSelectedItem()) == ComparisonMethod.HAUSDORFF_CURV ) {
            procrustesPanel.setVisible(false);
            
            createAvgCheckBox.setVisible(true);
            createAvgLabel.setVisible(true);
        }

        getContext().setComparisonMethod((ComparisonMethod)compMethodComboBox.getSelectedItem());
    }//GEN-LAST:event_compMethodComboBoxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
   
        ModelLoader ml = new ModelLoader();
        Model model = ml.loadModel(GUIController.getSelectedProjectTopComponent().getOneToManyViewerPanel().getListener2().getModel().getFile(), false, true);

        GUIController.getSelectedProjectTopComponent().getOneToManyViewerPanel().getListener2().setModels(model);

        model = ml.loadModel(GUIController.getSelectedProjectTopComponent().getOneToManyViewerPanel().getListener1().getModel().getFile(), false, true);
        GUIController.getSelectedProjectTopComponent().getOneToManyViewerPanel().getListener1().setModels(model);
        getContext().setState(1);
        GUIController.getConfigurationTopComponent().addOneToManyRegistrationComponent();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        final ProjectTopComponent tc = GUIController.getSelectedProjectTopComponent();
        ResultExports.instance().saveRegisteredModelsOneToMany(tc, getContext().getRegisteredModels(),
                getContext().getModels(), 
                getContext().getPrimaryModel(), "_1n");
    }//GEN-LAST:event_jButton10ActionPerformed

    private void createAvgCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_createAvgCheckBoxStateChanged
  
    }//GEN-LAST:event_createAvgCheckBoxStateChanged

    private void exportLandmarksButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportLandmarksButtonActionPerformed
     final ProjectTopComponent tc = GUIController.getSelectedProjectTopComponent();
        FPImportExport.instance().exportOneToMany(tc,
                getContext(),
                tc.getOneToManyViewerPanel().getListener1().getFacialPoints(), tc.getOneToManyViewerPanel().getListener1().getModel());
    }//GEN-LAST:event_exportLandmarksButtonActionPerformed

    private void createAvgCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createAvgCheckBoxActionPerformed
       getContext().setCreateAvgFace(createAvgCheckBox.isSelected());
    }//GEN-LAST:event_createAvgCheckBoxActionPerformed

    public void setProcessComparisonEnabled(boolean en) {
        processComparisonButton.setEnabled(en);
        getContext().setCompareButtonEnabled(en);
    }

    private String setValues(List<Float> hdDistance, List<File> models, String mainFace, int varianceMethod) {
        StringBuilder strResults = new StringBuilder(SurfaceComparisonProcessing.instance().getNameOfVarianceMethod(varianceMethod) + " Upper: 100% Lower: 0% treshold;");
        
        for(int i = 0; i < hdDistance.size(); i++){
            strResults.append(models.get(i).getName()).append(';');
        }
        
        strResults.append("\n" + mainFace + ";");
        
        for(Float f : hdDistance){
            strResults.append(f).append(';');    
        }
        
        return strResults.toString();
    }
    
    private OneToManyComparison getContext(){
        return GUIController.getSelectedProjectTopComponent().getProject().getSelectedOneToManyComparison();
    }

    public void setConfiguration() {
        OneToManyComparison c = getContext();
        RegistrationMethod reg = c.getRegistrationMethod();
        
          
        //because some items might need to be removed based on the mode later
        compMethodComboBox.setModel(new DefaultComboBoxModel(ComparisonMethod.values()));
                
        compMethodComboBox.setSelectedItem(c.getComparisonMethod());
        createAvgCheckBox.setSelected(c.isCreateAvgFace());
        fpScalingCheckBox.setSelected(c.isFpScaling());
        fpThresholdSlider.setValue(c.getFpTreshold());
        exportLandmarksButton.setVisible(reg == RegistrationMethod.PROCRUSTES);
        procrustesPanel.setVisible(c.getComparisonMethod() == ComparisonMethod.PROCRUSTES);

        if((c.getRegistrationMethod() != RegistrationMethod.PROCRUSTES)){
            compMethodComboBox.removeItemAt(1);
        }
         
        processComparisonButton.setEnabled(c.isCompareButtonEnabled());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox compMethodComboBox;
    private javax.swing.JCheckBox createAvgCheckBox;
    private javax.swing.JLabel createAvgLabel;
    private javax.swing.JButton exportLandmarksButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JCheckBox fpScalingCheckBox;
    private javax.swing.JSlider fpThresholdSlider;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton processComparisonButton;
    private javax.swing.JPanel procrustesPanel;
    // End of variables declaration//GEN-END:variables
}