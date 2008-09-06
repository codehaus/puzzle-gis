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
package org.puzzle.pending.swing.toolbox.widgettool.svg2mif.element;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class FenetreAide extends JFrame {

	JTextArea jtaAide;
	JScrollPane jspAide;
	
	public FenetreAide() {
		int i;
		initComponents();
		jtaAide.setText(jtaAide.getText() + Messages.getString("Aide.2"));
		for (i = 3; i < 23; i++) {
			jtaAide.setText(jtaAide.getText() + "\n" + Messages.getString("Aide." + i));
		}
		jtaAide.setCaretPosition(0);
	}

	private void initComponents() {
		setTitle(Messages.getString("Aide.1"));
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		this.setPreferredSize(new Dimension(550, 260));
		jtaAide = new JTextArea("", 10, 20);
		jtaAide.setWrapStyleWord(true);
		this.getContentPane().add(jtaAide);
		jspAide = new JScrollPane(jtaAide,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.getContentPane().add(jspAide);
		this.pack();
	}
}
