/*

 * To change this template, choose Tools | Templates

 * and open the template in the editor.

 */
package cz.fidentis.gui;

import cz.fidentis.comparison.ComparisonMethod;
import cz.fidentis.controller.BatchComparison;
import cz.fidentis.controller.Controller;
import cz.fidentis.controller.OneToManyComparison;
import cz.fidentis.controller.Project;
import cz.fidentis.controller.ProjectTree;
import cz.fidentis.controller.ProjectTree.Node;
import cz.fidentis.gui.actions.ButtonHelper;
import cz.fidentis.model.Model;
import cz.fidentis.model.ModelLoader;
import cz.fidentis.renderer.ComparisonGLEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 *
 * Top component which displays something.
 *
 */
@ConvertAsProperties(
        dtd = "-//cz.fidentis.gui//Projects//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "NavigatorTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "navigator", openAtStartup = true)
@ActionID(category = "Window", id = "cz.fidentis.gui.NavigatorTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ProjectsAction",
        preferredID = "NavigatorTopComponent")
@Messages({
    "CTL_ProjectsAction=Projects",
    "CTL_ProjectsTopComponent=Projects",
    "HINT_ProjectsTopComponent=This is a Projects window"
})
public final class NavigatorTopComponent extends TopComponent {

    // private ArrayList<Object> hierarchy = new ArrayList<Object>();
    private TreeSelectionListener tsl;
    private ProjectTree tree;
    private ResourceBundle strings = NbBundle.getBundle(Controller.class);

    public NavigatorTopComponent() {
        initComponents();

        tsl = new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();
                if(path != null) processSelection(path);
            }
        };
        tree = new ProjectTree("root");
        jTree1.addTreeSelectionListener(tsl);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(tree.getRoot().getName());
        jTree1.setModel(new DefaultTreeModel(processNodes(root, tree.getRoot())));
        jTree1.setRootVisible(false);
        jTree1.revalidate();

        setName(Bundle.CTL_ProjectsTopComponent());
        setToolTipText(Bundle.HINT_ProjectsTopComponent());
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_SLIDING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.TRUE);
    }

    /**
     *
     * This method is called from within the constructor to initialize the form.
     *
     * WARNING: Do NOT modify this code. The content of this method is always
     *
     * regenerated by the Form Editor.
     *
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menu_rename = new javax.swing.JMenuItem();
        menu_remove = new javax.swing.JMenuItem();
        menu_addModels = new javax.swing.JMenuItem();
        menu_removeModel = new javax.swing.JMenuItem();
        menu_loadModel = new javax.swing.JMenuItem();
        menu_loadSecondary = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();

        org.openide.awt.Mnemonics.setLocalizedText(menu_rename, org.openide.util.NbBundle.getMessage(NavigatorTopComponent.class, "NavigatorTopComponent.menu_rename.text")); // NOI18N
        menu_rename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_renameActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(menu_remove, org.openide.util.NbBundle.getMessage(NavigatorTopComponent.class, "NavigatorTopComponent.menu_remove.text")); // NOI18N
        menu_remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_removeActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(menu_addModels, org.openide.util.NbBundle.getMessage(NavigatorTopComponent.class, "NavigatorTopComponent.menu_addModels.text")); // NOI18N
        menu_addModels.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_addModelsActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(menu_removeModel, org.openide.util.NbBundle.getMessage(NavigatorTopComponent.class, "NavigatorTopComponent.menu_removeModel.text")); // NOI18N
        menu_removeModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_removeModelActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(menu_loadModel, org.openide.util.NbBundle.getMessage(NavigatorTopComponent.class, "NavigatorTopComponent.menu_loadModel.text")); // NOI18N
        menu_loadModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_loadModelActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(menu_loadSecondary, org.openide.util.NbBundle.getMessage(NavigatorTopComponent.class, "NavigatorTopComponent.menu_loadSecondary.text")); // NOI18N
        menu_loadSecondary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_loadSecondaryActionPerformed(evt);
            }
        });

        jTree1.setOpaque(false);
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        jTree1.setEditable(false);
        final TreePath selPath = jTree1.getPathForLocation(evt.getX(), evt.getY());
        
        if (selPath == null || selPath.getPathCount() < 2 || !SwingUtilities.isRightMouseButton(evt)) return;
        jTree1.setSelectionPath(selPath);
        
        JPopupMenu popup = new JPopupMenu();
        int selectedPart = GUIController.getSelectedProjectTopComponent().getProject().getSelectedPart();
        popup.add(menu_rename);
        popup.add(menu_remove);
        popup.addSeparator();
        
        if (selectedPart == 2 || selectedPart == 3 || selectedPart == 6) {
            // add item for loading primary model
            popup.add(menu_loadModel);
        }
        if (selectedPart == 2) {
            // add item for loading secondary model
            popup.add(menu_loadSecondary);
        }
        if (selectedPart == 3 || selectedPart == 4) {
            // add item for loading multiple models
            popup.add(menu_addModels);
            if(selPath.getParentPath().getLastPathComponent().toString().equals("Compared models")) {
                popup.addSeparator();
                popup.add(menu_removeModel);
            }
        }
        
        popup.show(jTree1, evt.getX(), evt.getY());
    }//GEN-LAST:event_jTree1MouseClicked

    private void menu_renameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_renameActionPerformed
        jTree1.setEditable(true);
        jTree1.startEditingAtPath(jTree1.getSelectionPath());
        jTree1.getCellEditor().addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                System.out.println(jTree1.getCellEditor().getCellEditorValue());
                String name = jTree1.getCellEditor().getCellEditorValue().toString();
                GUIController.getSelectedProjectTopComponent().getProject().setName(name);
                GUIController.getSelectedProjectTopComponent().setDisplayName(name);
                GUIController.getSelectedProjectTopComponent().setToolTipText("This is a " + name + " window");
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
                jTree1.setEditable(false);
            }
        });
    }//GEN-LAST:event_menu_renameActionPerformed

    private void menu_removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_removeActionPerformed
        GUIController.getSelectedProjectTopComponent().close();
    }//GEN-LAST:event_menu_removeActionPerformed

    private void menu_addModelsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_addModelsActionPerformed
        GUIController.getSelectedProjectTopComponent().triggerAddModel(false);
    }//GEN-LAST:event_menu_addModelsActionPerformed

    private void menu_loadModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_loadModelActionPerformed
        GUIController.getSelectedProjectTopComponent().triggerAddModel(true);
    }//GEN-LAST:event_menu_loadModelActionPerformed

    private void menu_loadSecondaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_loadSecondaryActionPerformed
        GUIController.getSelectedProjectTopComponent().triggerAddModel(false);
    }//GEN-LAST:event_menu_loadSecondaryActionPerformed

    private void menu_removeModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_removeModelActionPerformed
        Project proj = GUIController.getSelectedProjectTopComponent().getProject();
        int part = proj.getSelectedPart();
        TreePath path = jTree1.getSelectionPath();
        jTree1.setSelectionPath(path.getParentPath());
        int index = ((DefaultMutableTreeNode)path.getPathComponent(path.getPathCount()-2)).getIndex((DefaultMutableTreeNode)path.getLastPathComponent());
        if(part == 3) {
            proj.getSelectedOneToManyComparison().removeModel(index);
        }else if(part == 4) {
            proj.getSelectedBatchComparison().removeModel(index);
        }
        GUIController.getNavigatorTopComponent().update();
    }//GEN-LAST:event_menu_removeModelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jTree1;
    private javax.swing.JMenuItem menu_addModels;
    private javax.swing.JMenuItem menu_loadModel;
    private javax.swing.JMenuItem menu_loadSecondary;
    private javax.swing.JMenuItem menu_remove;
    private javax.swing.JMenuItem menu_removeModel;
    private javax.swing.JMenuItem menu_rename;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {

        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");

        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {

        String version = p.getProperty("version");

        // TODO read your settings according to their version
    }

    public void update() {
        tree.getRoot().removeChildren();
        for (int i = 0; i < Controller.getProjects().size(); i++) {
            tree.getRoot().addChild(Controller.getProjects().get(i).getTree().getRoot());
        }

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(tree.getRoot().getName());
        TreeModel m = new DefaultTreeModel(processNodes(root, tree.getRoot()));
        jTree1.setModel(m);
        jTree1.setRootVisible(false);
        expandTree((DefaultMutableTreeNode) m.getRoot());
        jTree1.revalidate();
        jTree1.repaint();
    }

    public void expandTree(DefaultMutableTreeNode node) {
        DefaultMutableTreeNode root = node;
        while (node != null) {
            TreePath tp = new TreePath(node.getPath());
            jTree1.expandPath(tp);
            node = node.getNextNode();
        }
    }

    /* 

     public void addProjectToHierarchy(ArrayList<Object> hierarchy) {

     this.hierarchy.add(hierarchy);

     jTree1.setModel(new DefaultTreeModel(processHierarchy(this.hierarchy)));

     jTree1.setRootVisible(false);

     jTree1.revalidate();

     }

     */
    @SuppressWarnings("unchecked")
    private DefaultMutableTreeNode processNodes(DefaultMutableTreeNode node, Node projectNode) {

        if (projectNode.getChildren() != null) {
            for (int i = 0; i < projectNode.getChildren().size(); i++) {
                Object nodeSpecifier = projectNode.getChildren().get(i).getName();
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(nodeSpecifier);
                node.add(processNodes(child, projectNode.getChildren().get(i)));

            }
        }
        return (node);
    }
    
    public void processSelection(TreePath path) {
        int projectIndex = ((DefaultMutableTreeNode)(path.getPathComponent(0))).getIndex((DefaultMutableTreeNode) path.getPathComponent(1));
        
        ProjectTopComponent tc = GUIController.getProjects().get(projectIndex);
        if(tc != GUIController.getSelectedProjectTopComponent()) {
            tc.requestActive();
        }
        
        if(path.getPathCount() > 2 && tc.getProject() != null) {
            Project project = tc.getProject();
            DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) path.getPathComponent(2);
            String nodeText = projectNode.toString();
            DefaultMutableTreeNode previousNode = (DefaultMutableTreeNode) path.getParentPath().getLastPathComponent();
            String previousNodeText = previousNode.toString();
            int lastNodeIndex = previousNode.getIndex((DefaultMutableTreeNode)path.getLastPathComponent());
            
            switch(nodeText) {
                case "Batch comparison":
                    project.setSelectedPart(4);
                    BatchComparison batchComparison = project.getSelectedBatchComparison();
                    ComparisonGLEventListener listener = tc.getViewerPanel_Batch().getListener();
                    listener.drawHD(false);
                    listener.setProcrustes(false);
                    listener.setFacialPoints(null);
                    
                    if(previousNodeText.equals(strings.getString("tree.node.comparedModels"))) {
                        File file = batchComparison.getModel(lastNodeIndex);
                        ModelLoader loader = new ModelLoader();
                        Model model = loader.loadModel(file, true, true);
                        listener.setModels(model);
                        
                        if (GUIController.getConfigurationTopComponent().getBatchComparisonResults().getLocalAreasJPanel() != null){
                            GUIController.getConfigurationTopComponent().getBatchComparisonResults().setCurrentModel(model);
                            if (GUIController.getConfigurationTopComponent().getBatchComparisonResults().getLocalAreasJPanel().isAreasSet()
                                    && GUIController.getConfigurationTopComponent().getBatchComparisonResults().isVisibleLocalArea()){
                                GUIController.getConfigurationTopComponent().getBatchComparisonResults().getLocalAreasJPanel().updateModel(model);
                                
                            }
                            
                        }
                    }
                    if(previousNodeText.equals(strings.getString("tree.node.registeredModels"))) {
                        File file = batchComparison.getRegistrationResults().get(lastNodeIndex);
                        ModelLoader loader = new ModelLoader();
                        Model model = loader.loadModel(file, true, true);
                        listener.setModels(model);
                        
                        listener.setFacialPoints(batchComparison.getFacialPoints(model.getName()));
                        
                        if (GUIController.getConfigurationTopComponent().getBatchComparisonResults().getLocalAreasJPanel()!= null){
                            GUIController.getConfigurationTopComponent().getBatchComparisonResults().setCurrentModel(model);
                            if (GUIController.getConfigurationTopComponent().getBatchComparisonResults().getLocalAreasJPanel().isAreasSet()
                                    && GUIController.getConfigurationTopComponent().getBatchComparisonResults().isVisibleLocalArea()){
                                GUIController.getConfigurationTopComponent().getBatchComparisonResults().getLocalAreasJPanel().updateModel(model);
                                
                            }
                        }
                    }
                    if(previousNodeText.equals(strings.getString("tree.node.averageModel"))) {
                        if(batchComparison.getAverageFace() == null) listener.setModels(batchComparison.getAverageFace());
                        else listener.setModels(batchComparison.getAverageFace());
                        if (GUIController.getConfigurationTopComponent().getBatchComparisonResults().getLocalAreasJPanel()!= null){
                            GUIController.getConfigurationTopComponent().getBatchComparisonResults().setCurrentModel(batchComparison.getAverageFace());
                            if (GUIController.getConfigurationTopComponent().getBatchComparisonResults().getLocalAreasJPanel().isAreasSet() 
                                    && GUIController.getConfigurationTopComponent().getBatchComparisonResults().isVisibleLocalArea()){
                                GUIController.getConfigurationTopComponent().getBatchComparisonResults().getLocalAreasJPanel().updateModel(batchComparison.getAverageFace());
                                
                            }
                        }
                    }
                    if(path.getLastPathComponent().toString().equals(strings.getString("tree.node.results"))) {
                        if(batchComparison.getComparisonMethod() == ComparisonMethod.PROCRUSTES) {
                            listener.setFacialPoints(batchComparison.getFacialPoints(batchComparison.getModel(0).getName()));
                            listener.setProcrustes(true);
                        } else {
                            listener.drawHD(true);
                        }
                    }
                    
                    break;
                case "2 faces comparison":
                    project.setSelectedPart(2);
                    break;
                case "1 to N comparison":
                    project.setSelectedPart(3);
                    OneToManyComparison comparison = project.getSelectedOneToManyComparison();
                    ComparisonGLEventListener listenerPrimary = tc.getOneToManyViewerPanel().getListener1();
                    ComparisonGLEventListener listenerSecondary = tc.getOneToManyViewerPanel().getListener2();
                    
                    if(previousNodeText.equals(strings.getString("tree.node.comparedModels"))) {
                        if(comparison.getPreregiteredModels() != null) {
                            Model m = comparison.getPreregiteredModels().get(lastNodeIndex);
                            listenerSecondary.setModels(m);
                            listenerSecondary.setFacialPoints(comparison.getFacialPoints(m.getName()));
                        } else {
                            File file = comparison.getModel(lastNodeIndex);
                            ModelLoader loader = new ModelLoader();
                            Model m = loader.loadModel(file, true, true);
                            listenerSecondary.setModels(m);
                        }
                    }
                    if(previousNodeText.equals(strings.getString("tree.node.registeredModels"))) {
                        File file = comparison.getRegisteredModels().get(lastNodeIndex);
                        ModelLoader loader = new ModelLoader();
                        Model model = loader.loadModel(file, true, true);
                        listenerSecondary.setModels(model);
                        listenerSecondary.setFacialPoints(comparison.getFacialPoints(model.getName()));
                    }
                    if(path.getLastPathComponent().toString().equals(strings.getString("tree.node.results"))) {
                        if(comparison.getComparisonMethod() == ComparisonMethod.PROCRUSTES) {
                            listenerPrimary.setFacialPoints(comparison.getFacialPoints(comparison.getModel(0).getName()));
                            listenerPrimary.setProcrustes(true);
                        } else {
                            listenerPrimary.drawHD(true);
                        }
                    }
                    if(previousNodeText.equals(strings.getString("tree.node.primaryModel"))) {
                        Model m = comparison.getPrimaryModel();
                        listenerPrimary.setProcrustes(false);
                        listenerPrimary.drawHD(false);
                        listenerPrimary.setModels(m);
                    }
                    break;
                case "Composite":
                    project.setSelectedPart(1);
                    break;
                case "Ageing":
                    project.setSelectedPart(6);
                    break;
                default:
                    project.setSelectedPart(5);
                    break;
            }
        }
    }
    
    public void clearSelectionIfNeeded(ProjectTopComponent project) {
        TreePath path = jTree1.getSelectionPath();
        if(path != null && path.getPathCount() >= 2) {
            int selectedIndex = ((DefaultMutableTreeNode) (path.getPathComponent(0))).getIndex((DefaultMutableTreeNode) path.getPathComponent(1));
            if (GUIController.getProjects().get(selectedIndex) != project) {
                jTree1.clearSelection();
            }
        }
    }
}
