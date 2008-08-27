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

public class mifmidLayer {

	private String fichier;
	private DPoint[] bounds = {new DPoint(Double.MAX_VALUE, Double.MAX_VALUE), new DPoint(Double.MIN_VALUE, Double.MIN_VALUE)};
	private String sizeUnit;
	private int nbObjets;
	private Vector<Object> objets;
	
	public mifmidLayer(String fichier) {
		this.fichier = fichier;
		this.objets = new Vector<Object>();
	}

	public String getFichier() {
		return fichier;
	}

	public void setFichier(String fichier) {
		this.fichier = fichier;
	}

	public DPoint getLBound() {
		return this.bounds[0];
	}
	
	public DPoint getHBound() {
		return this.bounds[1];
	}

	public void setBoundX(int index, double x) {
		this.bounds[index].x = x;
	}

	public void setBoundY(int index, double y) {
		this.bounds[index].y = y;
	}
	
	public String getSizeUnit() {
		return sizeUnit;
	}

	public void setSizeUnit(String sizeUnit) {
		this.sizeUnit = sizeUnit;
	}
	
	public void addObject(Object o) {
		this.objets.add(o);
		this.nbObjets += 1;
	}
	
	public Object getObjet(int index) {
		return this.objets.elementAt(index);
	}
	
	public int getNumObjets() {
		return this.nbObjets;
	}

}
