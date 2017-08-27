/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.gui;

import cz.fidentis.controller.Controller;
import cz.fidentis.utils.FileUtils;
import cz.fidentis.utils.LoadLibraries;
import cz.fidentis.utilsException.FileManipulationException;
import cz.fidentis.validator.OSValidator;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ConcurrentModificationException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import org.netbeans.core.startup.Splash;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        Controller controller = new Controller();
        //GUIController guiController = new GUIController();
        // povodne  
        
        

        try {                
            LoadLibraries.loadOpenCV();
            
            //set Nimbus Look And Feel
            if (OSValidator.isWindows()) {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            }
        } //set Nimbus LAF primary colors
        /*      
         try {
         //set Nimbus Look And Feel
         UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
         //to nod display tabs 
         
         UIManager.put("EditorTabDisplayerUI",
         "icare.ui.tweak.tab.NoTabsTabDisplayerUI");
         
         UIManager.put("control", new Color(170, 170, 170));
         UIManager.put("info", new Color(128, 128, 128));
         UIManager.put("nimbusBase", new Color(38, 50, 79));
         UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
         UIManager.put("nimbusDisabledText", new Color(220, 220, 220));
         UIManager.put("nimbusFocus", new Color(115, 164, 209));
         UIManager.put("nimbusGreen", new Color(176, 179, 50));
         UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
         //  UIManager.put("nimbusLightBackground", new Color(218, 226, 233));
         UIManager.put("nimbusOrange", new Color(191, 98, 4));
         UIManager.put("nimbusRed", new Color(169, 46, 34));
         UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
         UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
         UIManager.put("text", new Color(0, 0, 0));}*/ catch (URISyntaxException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | ConcurrentModificationException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        // delete the temp folder if it existed before the start of the program
        Splash s = Splash.getInstance();
        s.print("Removing the old temporary directory.");
        try {
            FileUtils.instance().createTMPfolder(true);
        } catch (FileManipulationException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    @Override
    public boolean closing() {
        int answer = JOptionPane.showConfirmDialog(null,
                "Do you really want to close the application?", "", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {

            if (GUIController.isDeleteConfigFiles()) {
                try {
                    FileUtils.instance().deleteTmpFolder();
                } catch (FileManipulationException ex) {
                    Exceptions.printStackTrace(ex);
                }
                
                String path = "";

                if (OSValidator.isWindows()) {
                    path = System.getProperty("user.home");
                    path += File.separator + "AppData" + File.separator + "Roaming" + File.separator + ".fidentis";
                }

                File f = new File(path);
                if (f.exists()) {
                    try {
                        //f.delete();
                        FileUtils.instance().deleteFolder(f);
                        
                    } catch (FileManipulationException ex) {
                        //couldn't delete folder
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
            return true;
        }

        return false;
    }
}
