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

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author johann sorel
 */
public class JReportList extends JList {

    private ReportModel model = new ReportModel();

    public JReportList() {
        setModel(model);
        setCellRenderer(new reportRenderer());
    }

    public void addReport(JasperReport report) {
        model.addReport(report);
    }

    private class ReportModel extends AbstractListModel {

        private List<JasperReport> reports = new ArrayList<JasperReport>();

        public void addReport(JasperReport report) {
            reports.add(report);
            fireContentsChanged(this, reports.size(), reports.size());
        }

        public int getSize() {
            return reports.size();
        }

        public JasperReport getElementAt(int index) {
            return reports.get(index);
        }
    }

    private class reportRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            value = ((JasperReport) value).getName();

            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}
