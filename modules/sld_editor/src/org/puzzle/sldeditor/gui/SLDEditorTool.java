/*
 * SLDEditorTool.java
 *
 * Created on 22 mars 2008, 19:36
 */
package org.puzzle.sldeditor.gui;

import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;
import javax.swing.filechooser.FileFilter;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.gui.swing.misc.filter.FileFilterFactory;
import org.geotools.sld.SLDConfiguration;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.openide.util.Exceptions;
import org.puzzle.puzzlecore.swing.toolbox.widgettool.WidgetTool;
import org.puzzle.puzzlecore.swing.toolbox.widgettool.WidgetToolListener;

/**
 *
 * @author  eclesia
 */
public class SLDEditorTool extends javax.swing.JPanel implements WidgetTool {

    private File openFile = null;
    private EventListenerList listeners = new EventListenerList();
    private JAdvancedSLDPanel guiEditor = new JAdvancedSLDPanel();

    /** Creates new form SLDEditorTool */
    public SLDEditorTool() {

        initComponents();
        pan_style.setLayout(new GridLayout(1, 1));
        pan_style.add(guiEditor);
        
    }

    private void open(File sldFile) {

        if (sldFile != null && sldFile.exists()) {
            InputStream xml = null;
            try {
                Configuration configuration = new SLDConfiguration();
                Parser parser = new Parser(configuration);

                xml = new FileInputStream(sldFile);
                StyledLayerDescriptor sld = (StyledLayerDescriptor) parser.parse(xml);

                guiEditor.setEdited(sld);
                openFile = sldFile;

            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                try {
                    xml.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    private File newSLD(File sldFile) {

        if (sldFile != null) {

            try {
                StyledLayerDescriptor sld = CommonFactoryFinder.getStyleFactory(GeoTools.getDefaultHints()).createStyledLayerDescriptor();

                StyleFactory stylefactory = CommonFactoryFinder.getStyleFactory(GeoTools.getDefaultHints());
                UserLayer layer = stylefactory.createUserLayer();
                layer.setLayerFeatureConstraints(new FeatureTypeConstraint[]{null});
                sld.addStyledLayer(layer);
                layer.addUserStyle(new StyleBuilder().createStyle());

                SLDTransformer styleTransform = new SLDTransformer();
                String xml = styleTransform.transform(sld);

                ArrayList<String> str = new ArrayList<String>();
                str.add(xml);
                write(sldFile.getPath(), str);
            } catch (Exception ex) {
                sldFile = null;
                ex.printStackTrace();
            }

        }

        return sldFile;

    }

    private void save() {
        if (openFile != null) {
            SLDTransformer st = new SLDTransformer();

            try {
                String xml = st.transform(guiEditor.getEdited());
                ArrayList<String> str = new ArrayList<String>();
                str.add(xml);
                write(openFile.getPath(), str);
                JOptionPane.showMessageDialog(this, "file saved");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void saveas() {
        if (openFile != null) {
            SLDTransformer st = new SLDTransformer();

            JFileChooser jfc = new JFileChooser();
            jfc.setMultiSelectionEnabled(false);
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

            FileFilter fsld = FileFilterFactory.createFileFilter(FileFilterFactory.FORMAT.STYLE_LAYER_DESCRIPTOR);
            jfc.addChoosableFileFilter(fsld);
            jfc.setFileFilter(fsld);

            int ret = jfc.showSaveDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                try {
                    String xml = st.transform(guiEditor.getEdited());
                    ArrayList<String> str = new ArrayList<String>();
                    str.add(xml);
                    write(jfc.getSelectedFile().getPath(), str);
                    JOptionPane.showMessageDialog(this, "file saved");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }



        }
    }

    private void newSLDProcedure() {
        JFileChooser jfc = new JFileChooser();
        jfc.setMultiSelectionEnabled(false);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        FileFilter fsld = FileFilterFactory.createFileFilter(FileFilterFactory.FORMAT.STYLE_LAYER_DESCRIPTOR);
        jfc.addChoosableFileFilter(fsld);
        jfc.setFileFilter(fsld);

        int ret = jfc.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File sldFile = newSLD(jfc.getSelectedFile());

            if (sldFile != null) {
                open(sldFile);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    pan_style = new javax.swing.JPanel();
    jToolBar1 = new javax.swing.JToolBar();
    guiNew = new javax.swing.JButton();
    guiOpen = new javax.swing.JButton();
    guiSave = new javax.swing.JButton();
    guiSaveAs = new javax.swing.JButton();

    setLayout(new java.awt.BorderLayout());

    org.jdesktop.layout.GroupLayout pan_styleLayout = new org.jdesktop.layout.GroupLayout(pan_style);
    pan_style.setLayout(pan_styleLayout);
    pan_styleLayout.setHorizontalGroup(
      pan_styleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(0, 641, Short.MAX_VALUE)
    );
    pan_styleLayout.setVerticalGroup(
      pan_styleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(0, 441, Short.MAX_VALUE)
    );

    add(pan_style, java.awt.BorderLayout.CENTER);

    jToolBar1.setFloatable(false);
    jToolBar1.setRollover(true);

    guiNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/puzzle/sldeditor/gui/filenew.png"))); // NOI18N
    guiNew.setText(org.openide.util.NbBundle.getMessage(SLDEditorTool.class, "new_file")); // NOI18N
    guiNew.setToolTipText(org.openide.util.NbBundle.getMessage(SLDEditorTool.class, "new")); // NOI18N
    guiNew.setBorderPainted(false);
    guiNew.setContentAreaFilled(false);
    guiNew.setFocusable(false);
    guiNew.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    guiNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    guiNew.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        guiNewActionPerformed(evt);
      }
    });
    jToolBar1.add(guiNew);

    guiOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/puzzle/sldeditor/gui/fileopen.png"))); // NOI18N
    guiOpen.setText(org.openide.util.NbBundle.getMessage(SLDEditorTool.class, "open_file")); // NOI18N
    guiOpen.setToolTipText(org.openide.util.NbBundle.getMessage(SLDEditorTool.class, "open")); // NOI18N
    guiOpen.setBorderPainted(false);
    guiOpen.setContentAreaFilled(false);
    guiOpen.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        guiOpenActionPerformed(evt);
      }
    });
    jToolBar1.add(guiOpen);

    guiSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/puzzle/sldeditor/gui/filesave.png"))); // NOI18N
    guiSave.setText(org.openide.util.NbBundle.getMessage(SLDEditorTool.class, "save_file")); // NOI18N
    guiSave.setToolTipText(org.openide.util.NbBundle.getMessage(SLDEditorTool.class, "save")); // NOI18N
    guiSave.setBorderPainted(false);
    guiSave.setContentAreaFilled(false);
    guiSave.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        guiSaveActionPerformed(evt);
      }
    });
    jToolBar1.add(guiSave);

    guiSaveAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/puzzle/sldeditor/gui/filesaveas.png"))); // NOI18N
    guiSaveAs.setText(org.openide.util.NbBundle.getMessage(SLDEditorTool.class, "saveas_file")); // NOI18N
    guiSaveAs.setToolTipText(org.openide.util.NbBundle.getMessage(SLDEditorTool.class, "saveas")); // NOI18N
    guiSaveAs.setBorderPainted(false);
    guiSaveAs.setContentAreaFilled(false);
    guiSaveAs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        guiSaveAsActionPerformed(evt);
      }
    });
    jToolBar1.add(guiSaveAs);

    add(jToolBar1, java.awt.BorderLayout.PAGE_START);
  }// </editor-fold>//GEN-END:initComponents
    private void guiOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guiOpenActionPerformed
        JFileChooser jfc = new JFileChooser();
        jfc.setMultiSelectionEnabled(false);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        FileFilter fsld = FileFilterFactory.createFileFilter(FileFilterFactory.FORMAT.STYLE_LAYER_DESCRIPTOR);
        jfc.addChoosableFileFilter(fsld);
        jfc.setFileFilter(fsld);

        int retour = jfc.showOpenDialog(this);

        if (retour == JFileChooser.APPROVE_OPTION) {
            open(jfc.getSelectedFile());
        }
        
        
}//GEN-LAST:event_guiOpenActionPerformed

    private void guiNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guiNewActionPerformed

        if (openFile == null) {
            newSLDProcedure();
        } else {
            int ret = JOptionPane.showConfirmDialog(this, "A style is open, do you want close it?", "", JOptionPane.OK_CANCEL_OPTION);
            if (ret == JOptionPane.OK_OPTION) {
                newSLDProcedure();
            }
        }
        
    }//GEN-LAST:event_guiNewActionPerformed

    private void guiSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guiSaveActionPerformed
        save();
    }//GEN-LAST:event_guiSaveActionPerformed

    private void guiSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guiSaveAsActionPerformed
        saveas();
    }//GEN-LAST:event_guiSaveAsActionPerformed

    public JComponent getComponent() {
        return this;
    }

    public void addWidgetToolListener(WidgetToolListener listener) {
        listeners.add(WidgetToolListener.class, listener);
    }

    public void removeWidgetToolListener(WidgetToolListener listener) {
        listeners.remove(WidgetToolListener.class, listener);
    }

    public WidgetToolListener[] getWidgetToolListeners() {
        return listeners.getListeners(WidgetToolListener.class);
    }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton guiNew;
  private javax.swing.JButton guiOpen;
  private javax.swing.JButton guiSave;
  private javax.swing.JButton guiSaveAs;
  private javax.swing.JToolBar jToolBar1;
  private javax.swing.JPanel pan_style;
  // End of variables declaration//GEN-END:variables
    /** 
     * Write a String List in a file
     * 
     * @param adress : path to the file
     * @param val : String List to write
     * @throws IOException if an error happen while writing
     */
    private void write(String adress, List<String> val) throws IOException {
        FileWriter fw = new FileWriter(adress, false);
        BufferedWriter output = new BufferedWriter(fw);

        int size = val.size();
        for (int i = 0; i < size; i++) {
            output.write(val.get(i));
            output.flush();
        }

        fw.close();
        output.close();
    }
}
