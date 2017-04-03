/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.gui.comparison_batch;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zanri
 */
public class HistogramJPanel extends javax.swing.JPanel {

    private List<Float> values;
    private int width, height;
    private Color color;
    private int selectedIndex;
    
    /**
     * Creates new form HistogramJPanel
     */
    public HistogramJPanel() {
        initComponents();
        this.setBackground(Color.WHITE);
        this.values = new ArrayList<>();
        this.color = Color.WHITE;
        this.selectedIndex = -1;
    }
    
    public void setSize(int width, int height){
        this.width = width;
        this.height = height+10;
        this.setPreferredSize(new Dimension(width, height));
    }
    
    public void setColor(Color color){
        this.color = color;
        this.repaint();
    }
            
    
    public void setValues(List<Float> values){
        this.values = selectionSort(values);
        this.repaint();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);  
        
        if (values.size() > 0)  {
            int deltaX = (int)Math.ceil((this.width-40) / values.size());
            if (deltaX <= 3){
                this.values = clusterData(values, (this.width-40));
            }
            deltaX = (int)Math.ceil((this.width-40) / values.size());

            float maximal = (float) Math.ceil(values.get(0));
            float minimal = Math.round(values.get(values.size()-1))-1f; 

            int deltaY = (int)Math.round(Math.ceil((this.height-70) / (maximal-minimal)));

            int textPosX = 7;
            int textPosY1 = 35;
            int textPosY2 = height - 35;



            if(g instanceof Graphics2D)
            {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

                g2.drawString(""+maximal,textPosX,textPosY1); 
                g2.drawString(""+minimal,textPosX,textPosY2); 

                if (this.selectedIndex != -1){
                    g2.drawString(""+values.get(values.size() - this.selectedIndex - 1), 30 + deltaX * this.selectedIndex, textPosY1); 
                }

                for (int i = 0; i < values.size(); i++){
                    float value = values.get(values.size() - i -1);
                    int x = 30 + deltaX*i;
                    int y = 35+(int)Math.round(Math.ceil((values.get(0) - value)*deltaY));

                    int heightRec = height - 35 - y;
                    int widthRec = deltaX;

                    g.setColor(color);
                    g.fillRect(x,y,widthRec,heightRec);

                    if (this.selectedIndex == i){
                        float thickness = 2;
                        Stroke oldStroke = g2.getStroke();
                        g2.setStroke(new BasicStroke(thickness));
                        g.setColor(Color.black);
                        g.drawRect(x,y,widthRec,heightRec);
                        g2.setStroke(oldStroke);
                    } else {
                        g.setColor(Color.black);
                        g.drawRect(x,y,widthRec,heightRec);
                    }
                }
            }
        }
        
        
    }
    
    
    public static List<Float> selectionSort(List<Float> array) {
        for (int i = 0; i < array.size() - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < array.size(); j++) {
                if (array.get(j) > array.get(maxIndex)) maxIndex = j;
            }
            float tmp = array.get(i);
            array.set(i, array.get(maxIndex));
            array.set(maxIndex, tmp);
        } 
        return array;
    }
    
    private int getChoosenIndex(int mouseX, int mouseY){
        int result = -1;

        int deltaX = (int)Math.ceil((this.width-40) / values.size());

        for (int i = 0; i < values.size(); i++){
            int x = 30 + deltaX*i;
            int y = 35;

            int heightRec = height - 35 - y;
            int widthRec = deltaX;
            
            if (mouseX >= x && mouseX <= x+widthRec && mouseY >= y && mouseY <= y+heightRec){
                result = i;
            }
        }

        return result;
    }
    
    private static List<Float> clusterData(List<Float> values, int widthForHistogram){
        List<Float> result = new ArrayList<>();
        
        int clusterSize = (int)Math.ceil(values.size()/100);

        int length = (int)Math.ceil(values.size() / clusterSize); 
        
        for (int i = 0; i < length; i++){
            float value = 0f;
            for (int j = 0; j < clusterSize; j++){
                if ((i*clusterSize) + j < values.size()){
                    value += values.get((i*clusterSize) + j);
                }
            }
            value = value / clusterSize;
            result.add(value);
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

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        if (values != null){
            this.selectedIndex = getChoosenIndex(evt.getX(), evt.getY());
            this.repaint();
        }
    }//GEN-LAST:event_formMouseMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
