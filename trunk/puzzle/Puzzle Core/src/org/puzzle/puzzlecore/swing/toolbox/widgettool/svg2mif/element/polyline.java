/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007-2008 Puzzle-GIS
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
package org.puzzle.puzzlecore.swing.toolbox.widgettool.svg2mif.element;

import java.awt.Point;
import java.util.Vector;

public class polyline {
	
	private int numSections;
	private Point center;
	private Vector<polylineSection> sections;

	public int getNumSections() {
		return numSections;
	}
	public void setNumSections(int numSections) {
		this.numSections = numSections;
	}
	public Vector<polylineSection> getSections() {
		return sections;
	}
	public void setSections(Vector<polylineSection> sections) {
		this.sections = sections;
	}
	
	public polylineSection getSection(int index) {
		return (polylineSection)this.sections.elementAt(index);
	}
	
	public void addSection(polylineSection section) {
		this.sections.add(section);
		this.numSections += 1;
	}
	
	public polyline() {
		this.numSections = 0;
		this.sections = new Vector<polylineSection>();
		this.center = new Point(0, 0);
	}
}
