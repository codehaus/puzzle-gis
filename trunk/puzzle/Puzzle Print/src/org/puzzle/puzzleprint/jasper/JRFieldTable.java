/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007 Puzzle-GIS
 *  
 *  GPLv3 + Classpath exception
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.puzzle.puzzleprint.jasper;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperReport;

/**
 * @author johann sorel
 */
public class JRFieldTable extends JComponent {

    private JTable table = new JTable();
    private JasperReport report = null;
    private JRFieldTableModel model = null;

    public JRFieldTable() {
        JScrollPane pane = new JScrollPane(table);
        
        setLayout(new GridLayout(1,1));
        add(pane);
    }

    public void setReport(JasperReport report) {

        if (report == null) {
            throw new NullPointerException();
        }

        this.report = report;
        model = new JRFieldTableModel(report);
        table.setModel(model);
        revalidate();

    }

    public JasperReport getReport() {
        return report;
    }

    public JRDataSource getReportDataSource() {
        Map<String,Object> parameters = new HashMap<String,Object>();

        for (int i = 0,  n = model.getRowCount(); i < n; i++) {
            parameters.put((String)model.getValueAt(i, 0), model.getValueAt(i, 1));
        }
        
        JRDataSource source = new TableDataSource(parameters);
        return source;
    }

    private class TableDataSource implements JRDataSource {

        private int val = 0;
        private Map<String, Object> map = null;

        TableDataSource(Map<String, Object> map) {
            this.map = map;
        }

        public boolean next() throws JRException {
            if (val == 0) {
                val++;
                return true;
            }
            return false;
        }

        public Object getFieldValue(JRField field) throws JRException {            
            return map.get( field.getName() );            
        }
    }
}
