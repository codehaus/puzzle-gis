/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2007-2009, Johann Sorel
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 3 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.puzzle.print.action;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.geotoolkit.gui.swing.report.JRConfigTree;
import org.geotoolkit.map.MapContext;
import org.geotoolkit.report.JRMappedDataSource;
import org.geotoolkit.report.JRMapper;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.opengis.geometry.Envelope;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author eclesia
 */
public class JJasperDialog extends JDialog{

    private final JComboBox box = new JComboBox();
    private final JRConfigTree tree = new JRConfigTree();
    private final JPanel panel = new JPanel(new BorderLayout());
    private final List<FileObject> files = new ArrayList<FileObject>();
    private final MapContext context;
    private Component comp = null;

    public JJasperDialog(Collection<FileObject> templates, final MapContext context, final Envelope env) throws JRException {
        this.context = context;
        
        files.addAll(templates);
        box.setModel(new ListComboBoxModel(files));
        box.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3, boolean arg4) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);
                lbl.setText( ((FileObject)arg1).getName() );
                return lbl;
            }
        });

        box.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    final JasperDesign jasperDesign = JRXmlLoader.load(FileUtil.toFile((FileObject) box.getSelectedItem()));
                    tree.setDesign(jasperDesign);
                } catch (JRException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });

        // load the first report
        final JasperDesign jasperDesign = JRXmlLoader.load(FileUtil.toFile(files.get(0)));
        tree.setDesign(jasperDesign);

        tree.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                changeDetail(null);
                int index = tree.getSelectedRow();

                if(index != -1){
                    Object obj = tree.getValueAt(index, 1);

                    if(obj instanceof JRMapper){
                        JRMapper mapper = (JRMapper)obj;
                        changeDetail(mapper.getComponent());
                    }
                }

            }
        });

        tree.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                changeDetail(null);
                int index = tree.getSelectedRow();

                if(index != -1){
                    Object obj = tree.getValueAt(index, 1);

                    if(obj instanceof JRMapper){
                        JRMapper mapper = (JRMapper)obj;
                        changeDetail(mapper.getComponent());
                    }
                }
            }
        });

        JScrollPane pane = new JScrollPane(tree);


        panel.add(BorderLayout.NORTH, box);
        panel.add(BorderLayout.WEST, pane);
        panel.add(BorderLayout.SOUTH, new JButton(new AbstractAction("show report") {

            @Override
            public void actionPerformed(ActionEvent e) {

                JRMappedDataSource<MapContext> source = new JRMappedDataSource<MapContext>();
                context.setAreaOfInterest(env);

                final Collection<MapContext> contexts = new ArrayList<MapContext>();
                contexts.add(context);
                source.setIterator(contexts.iterator());

                Map<Object,Object> parameters = tree.getParameters();
                Map<String,JRMapper<?,? super MapContext>> mapping = tree.getMapping();

                source.mapping().putAll(mapping);

                try {
                    final JasperReport jasperReport = JasperCompileManager.compileReport(tree.getDesign(null));
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, source);

                    // - preview du rapport
                    JasperViewer viewer = new JasperViewer(jasperPrint);
                    viewer.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    viewer.setVisible(true);

                } catch (JRException ex) {
                    Exceptions.printStackTrace(ex);
                }

            }
        }));

        setContentPane(panel);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void changeDetail(Component candidate){
        if(comp != null){
            panel.remove(comp);
            comp = null;
        }

        comp = candidate;

        if(comp != null){
            panel.add(BorderLayout.CENTER,comp);
        }

        panel.revalidate();
        panel.repaint();
    }

}
