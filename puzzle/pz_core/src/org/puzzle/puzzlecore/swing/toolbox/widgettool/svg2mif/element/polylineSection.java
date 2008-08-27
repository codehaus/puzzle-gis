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

import java.util.Vector;

public class polylineSection {

	private int numPoints;
	private Vector<DPoint> points;

	public polylineSection() {
		this.numPoints = 0;
		this.points = new Vector<DPoint>();
	}
	
	public int getNumPoints() {
		return numPoints;
	}
	public void setNumPoints(int numPoints) {
		this.numPoints = numPoints;
	}
	public Vector<DPoint> getPoints() {
		return points;
	}
	public void setPoints(Vector<DPoint> points) {
		this.points = points;
	}
	
	public DPoint getPoint(int index) {
		return this.points.elementAt(index);
	}
	
	public void addPoint(double x, double y) {
		this.points.add(new DPoint(x, y));
		this.numPoints += 1;
	}
	
	public void addPoint(DPoint p) {
		this.points.add(p);
		this.numPoints += 1;
	}
	
	public Object clone() {
		int i = 0;
		polylineSection ps2 = new polylineSection();
		for (i = 0; i < this.getNumPoints(); i++) {
			ps2.addPoint(this.points.elementAt(i));
		}
		return ps2;
	}
	
}
