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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.puzzle.puzzlecore.swing.toolbox.widgettool.svg2mif.element.xml.Element;
import org.puzzle.puzzlecore.swing.toolbox.widgettool.svg2mif.element.xml.Parser;



/**
 * @author Laurent J�gou - jegou@univ-tlse2.fr
 * 
 */

public class Dialogue extends JFrame implements ActionListener {

	private String fichierSVG;
	private String fichierMIF;
	private mifmidLayer MM;

	JLabel jlFichierSVG;
	JTextField jtfFichierSVG;
	JButton jbOpenSVG;
        JLabel jlFichierMIF;
	JTextField jtfFichierMIF;
	JTextArea jtaOperations;
	JScrollPane jspOperations;
	JCheckBox jcbTCPoly;	
	JCheckBox jcbInversion;
	JCheckBox jcbSPoly;	
	JButton jbGo;
	JButton jbAnnule;	
	JButton jbHelp;
	JPanel p1;
	JPanel p2;
	JPanel p3;
	JPanel p4;	
	JPanel p5;
        
        
	public Dialogue() {
		initComponents();
	}

	private void initComponents() {

		System.setErr(new MessageErreur(System.err));
		setTitle(Messages.getString("Dialogue.title"));
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		this.setPreferredSize(new Dimension(530, 240));
		p1 = new JPanel();
		jlFichierSVG = new JLabel(Messages.getString("Dialogue.source"));
		p1.add(jlFichierSVG);
		jtfFichierSVG = new JTextField();
		jtfFichierSVG.setPreferredSize(new Dimension(260, 20));
		p1.add(jtfFichierSVG);
		jbOpenSVG = new JButton(Messages.getString("Dialogue.openSVGbutton"));
		jbOpenSVG.setActionCommand(jbOpenSVG.getText());
		jbOpenSVG.addActionListener(this);
		p1.add(jbOpenSVG);
		this.getContentPane().add(p1);

		p2 = new JPanel();
		jlFichierMIF = new JLabel(Messages.getString("Dialogue.destination"));
		p2.add(jlFichierMIF);
		jtfFichierMIF = new JTextField();
		jtfFichierMIF.setPreferredSize(new Dimension(354, 20));
		p2.add(jtfFichierMIF);
		this.getContentPane().add(p2);

		jtaOperations = new JTextArea("", 4, 20);
		jtaOperations.setWrapStyleWord(true);
		this.getContentPane().add(jtaOperations);
		jspOperations = new JScrollPane(jtaOperations,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.getContentPane().add(jspOperations);
		p4 = new JPanel();
		jcbTCPoly = new JCheckBox();
		jcbTCPoly = new JCheckBox(Messages.getString("Dialogue.allaspolygon"), false);
		p4.add(jcbTCPoly);
		jcbSPoly = new JCheckBox();
		jcbSPoly = new JCheckBox(Messages.getString("Dialogue.seulpolygon"), false);
		p4.add(jcbSPoly);
		jcbInversion = new JCheckBox();
		jcbInversion = new JCheckBox(Messages.getString("Dialogue.inversion"), false);
		p4.add(jcbInversion);
		this.getContentPane().add(p4);
		
		p5 = new JPanel();
		jbHelp = new JButton(Messages.getString("Dialogue.help"));
		jbHelp.addActionListener(this);
		p5.add(jbHelp);
		jbAnnule = new JButton(Messages.getString("Dialogue.exit"));
		jbAnnule.addActionListener(this);
		p5.add(jbAnnule);
		jbGo = new JButton(Messages.getString("Dialogue.go"));
		jbGo.addActionListener(this);
		p5.add(jbGo);
		this.getContentPane().add(p5);
		this.pack();

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});
	}

	public void ouvrirAction(java.awt.event.ActionEvent evt) {
		JFileChooser ouv = new JFileChooser();
		ExampleFileFilter scapCarteFilter = new ExampleFileFilter("svg"); //$NON-NLS-1$
		ouv.setFileFilter(scapCarteFilter);
		int returnVal = ouv.showOpenDialog(this);
		if (returnVal == JFileChooser.CANCEL_OPTION) { //$NON-NLS-1$
			return;
		}
		fichierSVG = ouv.getSelectedFile().getPath();

		if (fichierSVG.compareTo("null") != 0) { //$NON-NLS-1$
			jtfFichierSVG.setText(fichierSVG);
			if (jtfFichierMIF.getText() != "") {
				String fmif = fichierSVG.substring(0, fichierSVG.length() - 4)
						+ ".mif";
				jtfFichierMIF.setText(fmif);
				fichierMIF = fmif;
			}
			jtaOperations.setText("");
		}
	}

	public void conversionAction(java.awt.event.ActionEvent evt) {
		ouverture(fichierSVG);
		ecriture(fichierMIF);
	}

	public void actionPerformed(ActionEvent e) {
		// r�action aux actions sur le dialogue.
		if (e.getSource().getClass() == JButton.class) {
			JButton source = (JButton) e.getSource();
			if (source.getText() == Messages
					.getString("Dialogue.openSVGbutton")) {
				ouvrirAction(e);
			} else if (source.getText() == Messages.getString("Dialogue.go")) {
				conversionAction(e);
			} else if (source.getText() == Messages.getString("Dialogue.help")) {
				appelAide();
			} else if (source.getText() == Messages.getString("Dialogue.exit")) {
				System.exit(0);
			}
		}
	}

    protected void appelAide() {
    	// Appel de la page d'aide
    	FenetreAide jfAide = new FenetreAide();
    	jfAide.setVisible(true);
    }
	
	public void ouverture(String fichierSVG) {
		// Lecture SVG

		String ligne;

		try {
			MM = new mifmidLayer(fichierMIF);
			jtaOperations.setText("Ouverture SVG\n");
			Parser SVGParser = new Parser(fichierSVG);
			Enumeration SVGe = SVGParser.getElements();
			Element eec = new Element();
			while (SVGe.hasMoreElements()) {
				eec = (Element) SVGe.nextElement();
				jtaOperations.setText(jtaOperations.getText() + eec.getTagName() + "\n");
				if (eec.getTagName().equals("svg")) {
					if (eec.hasAttribute("width")) {
						// MM.setSize(new
						// DDimension(getValueFromSize(eec.getAttribute("width")),
						// getValueFromSize(eec.getAttribute("height"))));
						// MM.setSizeUnit(getUnitFromSize(eec.getAttribute("width")));
					}
					// jtaOperations.setText(jtaOperations.getText() +
					// MM.getSize().toString() + "\n");
				}
				String tn = eec.getTagName();
				if (!tn.equals("polygon") && !this.jcbSPoly.isSelected()) {
					if (tn.equals("path") || tn.equals("PATH")) {
						processPath(eec);
					}
					if (tn.equals("rect") || tn.equals("RECT")) {
						processRect(eec);
					}
					if (tn.equals("line") || tn.equals("LINE")) {
						processLine(eec);
					}
					if (tn.equals("polyline") || tn.equals("POLYLINE")) {
						processPolyline(eec);
					}
					if (tn.equals("circle") || tn.equals("CIRCLE")) {
						processCircle(eec);
					}
					if (tn.equals("ellipse") || tn.equals("ELLIPSE")) {
						processEllipse(eec);
					}					
				}

				if (tn.equals("polygon") || tn.equals("POLYGON")) {
					processPolygon(eec);
				}
			}
			// tampon.close();
		}/*
			 * catch (FileNotFoundException e) { // Exception d�clench�e si le
			 * fichier n'existe pas
			 * System.out.println(Messages.getString("Dialogue.fileNotExist"));
			 * //$NON-NLS-1$ } catch (EOFException e) { // Exception d�clench�e
			 * si la fin du fichier est atteinte
			 * System.out.println(Messages.getString("Dialogue.eof"));
			 * //$NON-NLS-1$ } catch (IOException e) { // Exception d�clench�e
			 * si un autre probl�me survient // pendant l'acc�s au fichier
			 * System.out.println(Messages.getString("Dialogue.IOerror"));
			 * //$NON-NLS-1$ }
			 */
		finally {
			// Pas d'erreur rencontr�e
			jtaOperations.setText(jtaOperations.getText() + "Objets convertis : " + MM.getNumObjets() + "\n** Conversion termin�e **");
		}
	}

private void processPath(Element eec) {
    	if (eec.hasAttribute("d") == false) {
    		// Erreur irr�cup�rable
    		return;
    	}
    	if (!this.jcbTCPoly.isSelected()) {
	    	DPoint premierPoint = new DPoint(0, 0);
	    	DPoint pt = new DPoint(0, 0);
	    	polyline pl = new polyline();
	    	String d = eec.getAttribute("d");
	    	Pattern p = Pattern.compile("\\s+|,");
	       	Scanner s = new Scanner(d).useDelimiter(p);
	       	while (s.hasNext()) {
	       		String e = s.next();
	       		String last = "";
	       		if (e.equals("M")) {
	       			polylineSection ps = new polylineSection();
	       			pt.x = Double.parseDouble(s.next());
	       			pt.y = Double.parseDouble(s.next());
	       			ps.addPoint((DPoint)pt.clone());
	       			premierPoint = (DPoint)pt.clone();
	       			e = s.next();
	       			while (e.toLowerCase().equals("z") != true) {
	       				if (e.equals("M")) {
	       					ps.addPoint((DPoint)premierPoint.clone());
	       					pl.addSection((polylineSection)ps.clone());
	       	       			ps = new polylineSection();
	       	       			pt.x = Double.parseDouble(s.next());
	       	       			pt.y = Double.parseDouble(s.next());
	       	       			premierPoint = (DPoint)pt.clone();
	       	       			verifBounds(pt);
	       	       			ps.addPoint((DPoint)pt.clone());	       	   
	       				}
	       				if (e.equals("m")) {
	       					ps.addPoint((DPoint)premierPoint.clone());
	       					pl.addSection((polylineSection)ps.clone());
	       	       			ps = new polylineSection();
	       	       			pt.x += Double.parseDouble(s.next());
	       	       			pt.y += Double.parseDouble(s.next());
	       	       			premierPoint = (DPoint)pt.clone();
	       	       			verifBounds(pt);
	       	       			ps.addPoint((DPoint)pt.clone());
	       				}
						if (e.equals("L")) {
			       			pt.x = Double.parseDouble(s.next());
			       			pt.y = Double.parseDouble(s.next());
	       	       			verifBounds(pt);
			       			ps.addPoint((DPoint)pt.clone());
			       			last = "L";
						} else if (e.equals("l")) {
			       			pt.x += Double.parseDouble(s.next());
			       			pt.y += Double.parseDouble(s.next());
	       	       			verifBounds(pt);
			       			ps.addPoint((DPoint)pt.clone());
			       			last = "l";
						} else {
			       			pt.x = Double.parseDouble(e);
			       			pt.y = Double.parseDouble(s.next());
	       	       			verifBounds(pt);
			       			ps.addPoint((DPoint)pt.clone());
						}
						if (s.hasNext()) {
							e = s.next();
						} else {
							e = "z";
						}
	       			}
	       			ps.addPoint((DPoint)premierPoint.clone());
	       			pl.addSection((polylineSection)ps.clone());
	       			MM.addObject(pl);
	       		}
	       	}
       	} else {
       		DPolygon po = new DPolygon();
	    	DPoint premierPoint = new DPoint(0, 0);
	    	DPoint pt = new DPoint(0, 0);
			DPoint[] bounds = { new DPoint(Double.MAX_VALUE, Double.MAX_VALUE),
					new DPoint(Double.MIN_VALUE, Double.MIN_VALUE) };
	    	String d = eec.getAttribute("d");
	    	Pattern p = Pattern.compile("\\s+|,");
	       	Scanner s = new Scanner(d).useDelimiter(p);
	       	while (s.hasNext()) {
	       		String e = s.next();
	       		if (e.equals("M")) {
	       			pt.x = Double.parseDouble(s.next());
	       			pt.y = Double.parseDouble(s.next());
	       			po.addPoint((DPoint)pt.clone());
	       			premierPoint = (DPoint)pt.clone();
	       			e = s.next();
	       			while (e.toLowerCase().equals("z") != true) {
	       				if (e.equals("M")) {
	       					po.addPoint((DPoint)premierPoint.clone());
	       					DPoint center = new DPoint(0, 0);
	       					center.x = (double) ((bounds[0].x + bounds[1].x) / 2);
	       					center.y = (double) ((bounds[0].y + bounds[1].y) / 2);
	       					po.setCenter((DPoint)center.clone());
	       					MM.addObject(po.clone());
	       					bounds[0] = new DPoint(Double.MAX_VALUE, Double.MAX_VALUE);
	       					bounds[1] = new DPoint(Double.MIN_VALUE, Double.MIN_VALUE);
	       	       			po = new DPolygon();
	       	       			pt.x = Double.parseDouble(s.next());
	       	       			pt.y = Double.parseDouble(s.next());
	       	       			premierPoint = (DPoint)pt.clone();
	       	       			verifBoundsP(pt, bounds);
	       	       			verifBounds(pt);
	       	       			po.addPoint((DPoint)pt.clone());
	       				}
	       				if (e.equals("m")) {
	       					po.addPoint((DPoint)premierPoint.clone());
	       					DPoint center = new DPoint(0, 0);
	       					center.x = (double) ((bounds[0].x + bounds[1].x) / 2);
	       					center.y = (double) ((bounds[0].y + bounds[1].y) / 2);
	       					po.setCenter((DPoint)center.clone());
	       					MM.addObject(po.clone());
	       					bounds[0] = new DPoint(Double.MAX_VALUE, Double.MAX_VALUE);
	       					bounds[1] = new DPoint(Double.MIN_VALUE, Double.MIN_VALUE);
	       	       			po = new DPolygon();
	       	       			pt.x += Double.parseDouble(s.next());
	       	       			pt.y += Double.parseDouble(s.next());
	       	       			premierPoint = (DPoint)pt.clone();
	       	       			verifBoundsP(pt, bounds);
	       	       			verifBounds(pt);
	       	       			po.addPoint((DPoint)pt.clone());
	       				}
						if (e.equals("L")) {
			       			pt.x = Double.parseDouble(s.next());
			       			pt.y = Double.parseDouble(s.next());
			       			verifBoundsP(pt, bounds);
			       			verifBounds(pt);
			       			po.addPoint((DPoint)pt.clone());
						} else if (e.equals("l")) {
			       			pt.x += Double.parseDouble(s.next());
			       			pt.y += Double.parseDouble(s.next());
			       			verifBoundsP(pt, bounds);
			       			verifBounds(pt);
			       			po.addPoint((DPoint)pt.clone());
						} else {
			       			pt.x += Double.parseDouble(e);
			       			pt.y += Double.parseDouble(s.next());
			       			verifBoundsP(pt, bounds);
			       			verifBounds(pt);
			       			po.addPoint((DPoint)pt.clone());
						}
						if (s.hasNext()) {
							e = s.next();
						} else {
							e = "z";
						}
	       			}
   					DPoint center = new DPoint(0, 0);
   					center.x = (double) ((bounds[0].x + bounds[1].x) / 2);
   					center.y = (double) ((bounds[0].y + bounds[1].y) / 2);
   					po.setCenter((DPoint)center.clone());
   					po.addPoint((DPoint)premierPoint.clone());	       			
	       			MM.addObject(po.clone());
	       		}
	       	}
	    }
    }
    	
    
    private void processRect(Element eec) {
		if (eec.hasAttribute("x") == false) {
			// Erreur irr�cup�rable
			return;
		}
		if (!this.jcbTCPoly.isSelected()) {
			double x1, y1, x2, y2;
			x1 = Double.parseDouble(eec.getAttribute("x"));
			y1 = Double.parseDouble(eec.getAttribute("y"));
			verifBounds(new DPoint(x1, y1));
			x2 = x1 + getValueFromSize(eec.getAttribute("width"));
			y2 = y1 + getValueFromSize(eec.getAttribute("height"));
			verifBounds(new DPoint(x2, y2));
			DRect r = new DRect(x1, y1, x2, y2);
			MM.addObject(r);			
		} else {
			double x1, y1, x2, y2;
       		DPolygon po = new DPolygon();
			DPoint[] bounds = { new DPoint(Double.MAX_VALUE, Double.MAX_VALUE),
					new DPoint(Double.MIN_VALUE, Double.MIN_VALUE) };
			x1 = Double.parseDouble(eec.getAttribute("x"));
			y1 = Double.parseDouble(eec.getAttribute("y"));
			verifBoundsP(new DPoint(x1, y1), bounds);
			verifBounds(new DPoint(x1, y1));
			x2 = x1 + getValueFromSize(eec.getAttribute("width"));
			y2 = y1 + getValueFromSize(eec.getAttribute("height"));
			verifBoundsP(new DPoint(x2, y2), bounds);
			verifBounds(new DPoint(x2, y2));
			po.addPoint(new DPoint(x1, y1));
			po.addPoint(new DPoint(x2, y1));
			po.addPoint(new DPoint(x2, y2));
			po.addPoint(new DPoint(x1, y2));
			po.addPoint(new DPoint(x1, y1));
			DPoint center = new DPoint(0, 0);
			center.x = (double) ((bounds[0].x + bounds[1].x) / 2);
			center.y = (double) ((bounds[0].y + bounds[1].y) / 2);
			po.setCenter((DPoint)center.clone());
			MM.addObject(po);
		}
	}

	private void processLine(Element eec) {
		if (eec.hasAttribute("x1") == false) {
			// Erreur irr�cup�rable
			return;
		}
		if (!this.jcbTCPoly.isSelected()) {
			double x1, y1, x2, y2;
			x1 = Double.parseDouble(eec.getAttribute("x1"));
			y1 = Double.parseDouble(eec.getAttribute("y1"));
			verifBounds(new DPoint(x1, y1));
			x2 = getValueFromSize(eec.getAttribute("x2"));
			y2 = getValueFromSize(eec.getAttribute("y2"));
			verifBounds(new DPoint(x2, y2));
			DLine l = new DLine(x1, y1, x2, y2);
			MM.addObject(l);
		} else {
			double x1, y1, x2, y2;
       		DPolygon po = new DPolygon();
			DPoint[] bounds = { new DPoint(Double.MAX_VALUE, Double.MAX_VALUE),
					new DPoint(Double.MIN_VALUE, Double.MIN_VALUE) };
			x1 = Double.parseDouble(eec.getAttribute("x1"));
			y1 = Double.parseDouble(eec.getAttribute("y1"));
			verifBoundsP(new DPoint(x1, y1), bounds);
			verifBounds(new DPoint(x1, y1));
			x2 = getValueFromSize(eec.getAttribute("x2"));
			y2 = getValueFromSize(eec.getAttribute("y2"));
			verifBoundsP(new DPoint(x2, y2), bounds);
			verifBounds(new DPoint(x2, y2));
			po.addPoint(new DPoint(x1, y1));
			po.addPoint(new DPoint(x2, y1));
			po.addPoint(new DPoint(x2, y2));
			po.addPoint(new DPoint(x1, y2));
			po.addPoint(new DPoint(x1, y1));
			DPoint center = new DPoint(0, 0);
			center.x = (double) ((bounds[0].x + bounds[1].x) / 2);
			center.y = (double) ((bounds[0].y + bounds[1].y) / 2);
			po.setCenter((DPoint)center.clone());
			MM.addObject(po);
		}
	}

	private void processPolyline(Element eec) {
		if (eec.hasAttribute("points") == false) {
			// Erreur irr�cup�rable
			return;
		}
		if (!this.jcbTCPoly.isSelected()) {
			DPoint premierPoint = new DPoint(0, 0);
			DPoint pt = new DPoint(0, 0);
			polyline pl = new polyline();
			polylineSection ps = new polylineSection();
			String points = eec.getAttribute("points");
			Pattern p = Pattern.compile("\\s+");
			Scanner s = new Scanner(points).useDelimiter(p);
			while (s.hasNext()) {
				String e = s.next();
				pt.x = Double.parseDouble(e.substring(0, e.indexOf(",")));
				pt.y = Double.parseDouble(e.substring(e.indexOf(",") + 1));
				verifBounds(pt);
				ps.addPoint((DPoint) pt.clone());
			}
			pl.addSection((polylineSection) ps.clone());
			MM.addObject(pl);			
		} else {
       		DPolygon po = new DPolygon();
	    	DPoint premierPoint = new DPoint(Double.MIN_VALUE, Double.MIN_VALUE);
	    	DPoint pt = new DPoint(0, 0);
			DPoint[] bounds = { new DPoint(Double.MAX_VALUE, Double.MAX_VALUE),
					new DPoint(Double.MIN_VALUE, Double.MIN_VALUE) };
	    	String d = eec.getAttribute("d");
			String points = eec.getAttribute("points");
			Pattern p = Pattern.compile("\\s+");
			Scanner s = new Scanner(points).useDelimiter(p);
			while (s.hasNext()) {
				String e = s.next();
				pt.x = Double.parseDouble(e.substring(0, e.indexOf(",")));
				pt.y = Double.parseDouble(e.substring(e.indexOf(",") + 1));
				if (premierPoint.x == Double.MIN_VALUE) {
					premierPoint.x = pt.x;
					premierPoint.y = pt.y;
				}
				verifBoundsP(pt, bounds);
				verifBounds(pt);
				po.addPoint((DPoint) pt.clone());
			}
			DPoint center = new DPoint(0, 0);
	        center.x = (double) ((bounds[0].x + bounds[1].x) / 2);
			center.y = (double) ((bounds[0].y + bounds[1].y) / 2);
			po.setCenter((DPoint)center.clone());
			po.addPoint((DPoint)premierPoint.clone());	  
			MM.addObject(po);
		}
	}

	private void processPolygon(Element eec) {
		if (eec.hasAttribute("points") == false) {
			// Erreur irr�cup�rable
			return;
		}
		DPoint[] bounds = { new DPoint(Double.MAX_VALUE, Double.MAX_VALUE),
				new DPoint(Double.MIN_VALUE, Double.MIN_VALUE) };
		DPoint pt = new DPoint(0, 0);
		DPolygon po = new DPolygon();
		String points = eec.getAttribute("points");
		Pattern p = Pattern.compile("\\s+");
		Scanner s = new Scanner(points).useDelimiter(p);
		while (s.hasNext()) {
			String e = s.next();
			pt.x = Double.parseDouble(e.substring(0, e.indexOf(",")));
			pt.y = Double.parseDouble(e.substring(e.indexOf(",") + 1));
			verifBoundsP(pt, bounds);
			verifBounds(pt);
			po.addPoint((DPoint) pt.clone());
		}
		DPoint center = new DPoint(0, 0);
		center.x = (double) ((bounds[0].x + bounds[1].x) / 2);
		center.y = (double) ((bounds[0].y + bounds[1].y) / 2);
		po.setCenter(center);
		MM.addObject(po);
	}
	
	private void processCircle(Element eec) {
		if (eec.hasAttribute("cx") == false) {
			// Erreur irr�cup�rable
			return;
		}
		if (!this.jcbTCPoly.isSelected()) {
			double cx, cy, r, x1, x2, y1, y2;
			cx = Double.parseDouble(eec.getAttribute("cx"));
			cy = Double.parseDouble(eec.getAttribute("cy"));
			r = Double.parseDouble(eec.getAttribute("r"));
			x1 = cx - r;
			y1 = cy - r;
			x2 = cx + r;
			y2 = cy + r;
			verifBounds(new DPoint(x1, y1));
			verifBounds(new DPoint(x2, y2));
			DCircle ci = new DCircle(x1, y1, x2, y2);
			MM.addObject(ci);
		} else {
			double cx, cy, r, x1, x2, y1, y2;
       		DPolygon po = new DPolygon();
			DPoint[] bounds = { new DPoint(Double.MAX_VALUE, Double.MAX_VALUE),
					new DPoint(Double.MIN_VALUE, Double.MIN_VALUE) };
			cx = Double.parseDouble(eec.getAttribute("cx"));
			cy = Double.parseDouble(eec.getAttribute("cy"));
			r = Double.parseDouble(eec.getAttribute("r"));
			x1 = cx - r;
			y1 = cy - r;
			x2 = cx + r;
			y2 = cy + r;
			verifBoundsP(new DPoint(x1, y1), bounds);
			verifBounds(new DPoint(x1, y1));
			verifBoundsP(new DPoint(x2, y2), bounds);
			verifBounds(new DPoint(x2, y2));
			po.addPoint(new DPoint(x1, y1));
			po.addPoint(new DPoint(x2, y1));
			po.addPoint(new DPoint(x2, y2));
			po.addPoint(new DPoint(x1, y2));
			po.addPoint(new DPoint(x1, y1));
			DPoint center = new DPoint(0, 0);
			center.x = (double) ((bounds[0].x + bounds[1].x) / 2);
			center.y = (double) ((bounds[0].y + bounds[1].y) / 2);
			po.setCenter((DPoint)center.clone());
			MM.addObject(po);
		}
	}

	private void processEllipse(Element eec) {
		if (eec.hasAttribute("cx") == false) {
			// Erreur irr�cup�rable
			return;
		}
		if (!this.jcbTCPoly.isSelected()) {
			double cx, cy, rx, ry, x1, x2, y1, y2;
			cx = Double.parseDouble(eec.getAttribute("cx"));
			cy = Double.parseDouble(eec.getAttribute("cy"));
			rx = Double.parseDouble(eec.getAttribute("rx"));
			ry = Double.parseDouble(eec.getAttribute("ry"));
			x1 = cx - rx;
			y1 = cy - ry;
			x2 = cx + rx;
			y2 = cy + ry;
			verifBounds(new DPoint(x1, y1));
			verifBounds(new DPoint(x2, y2));
			DCircle ci = new DCircle(x1, y1, x2, y2);
			MM.addObject(ci);
		} else {
			double cx, cy, rx, ry, x1, x2, y1, y2;
       		DPolygon po = new DPolygon();
			DPoint[] bounds = { new DPoint(Double.MAX_VALUE, Double.MAX_VALUE),
					new DPoint(Double.MIN_VALUE, Double.MIN_VALUE) };
			cx = Double.parseDouble(eec.getAttribute("cx"));
			cy = Double.parseDouble(eec.getAttribute("cy"));
			rx = Double.parseDouble(eec.getAttribute("rx"));
			ry = Double.parseDouble(eec.getAttribute("ry"));
			x1 = cx - rx;
			y1 = cy - ry;
			x2 = cx + rx;
			y2 = cy + ry;
			verifBoundsP(new DPoint(x1, y1), bounds);
			verifBounds(new DPoint(x1, y1));
			verifBoundsP(new DPoint(x2, y2), bounds);
			verifBounds(new DPoint(x2, y2));
			po.addPoint(new DPoint(x1, y1));
			po.addPoint(new DPoint(x2, y1));
			po.addPoint(new DPoint(x2, y2));
			po.addPoint(new DPoint(x1, y2));
			po.addPoint(new DPoint(x1, y1));
			DPoint center = new DPoint(0, 0);
			center.x = (double) ((bounds[0].x + bounds[1].x) / 2);
			center.y = (double) ((bounds[0].y + bounds[1].y) / 2);
			po.setCenter((DPoint)center.clone());
			MM.addObject(po);
		}
	}

	private void ecriture(String fichierMIF) {
		int i = 0, j = 0, k = 0;
		int coef = 1;
		String qm = new String();
		String fichierMID = fichierMIF.substring(0, fichierMIF.length() - 4) + ".MID";
		qm = "\"";

		if (jcbInversion.isSelected())
			coef = -1;
		try {
			// Ecriture MIF
			FileWriter sortie = new FileWriter(fichierMIF);
			BufferedWriter tampon = new BufferedWriter(sortie);
			tampon.write("VERSION 300");
			tampon.newLine();
			tampon.write("Charset " + qm + "WindowsLatin1" + qm);
			tampon.newLine();
			tampon.write("Delimiter " + qm + "," + qm);
			tampon.newLine();
			tampon.write("Unique 1");
			tampon.newLine();
			tampon.write("Index 1");
			tampon.newLine();
			tampon.write("CoordSys Nonearth Units " + qm + "cm" + qm);
			tampon.write(" Bounds (" + MM.getLBound().x + ", " + MM.getLBound().y * coef + ") (" + MM.getHBound().x + ", " + MM.getHBound().y * coef + ")");
			tampon.newLine();
			tampon.write("Columns 2");
			tampon.newLine();
			tampon.write("  code Char(10)");
			tampon.newLine();
			tampon.write("  nom Char(100)");
			tampon.newLine();
			tampon.write("Data");
			tampon.newLine();			
			for (i = 0; i < MM.getNumObjets(); i++) {
				if (MM.getObjet(i).getClass() == polyline.class) {
					polyline pl = (polyline) MM.getObjet(i);
					if (pl.getNumSections() > 1) {
						tampon.write("PLINE MULTIPLE " + pl.getNumSections());
						tampon.newLine();
						for (j = 0; j < pl.getNumSections(); j++) {
							polylineSection ps = new polylineSection();
							ps = pl.getSection(j);
							tampon.write(" " + ps.getNumPoints());
							tampon.newLine();
							for (k = 0; k < ps.getNumPoints(); k++) {
								tampon.write(ps.getPoint(k).x + " "
										+ ps.getPoint(k).y * coef);
								tampon.newLine();
							}
						}
					} else {
						tampon.write("PLINE");
						polylineSection ps = new polylineSection();
						ps = pl.getSection(0);
						tampon.write(" " + ps.getNumPoints());
						tampon.newLine();
						for (k = 0; k < ps.getNumPoints(); k++) {
							tampon.write(ps.getPoint(k).x + " "
									+ ps.getPoint(k).y * coef);
							tampon.newLine();
						}
					}
				}
				if (MM.getObjet(i).getClass() == DRect.class) {
					DRect r = new DRect();
					r = (DRect) MM.getObjet(i);
					tampon.write("RECT " + r.getX1() + " " + r.getY1() * coef + " "
							+ r.getX2() + " " + r.getY2() * coef);
					tampon.newLine();
				}
				if (MM.getObjet(i).getClass() == DLine.class) {
					DLine l = new DLine();
					l = (DLine) MM.getObjet(i);
					tampon.write("LINE " + l.getX1() + " " + l.getY1() * coef + " "
							+ l.getX2() + " " + l.getY2() * coef);
					tampon.newLine();
				}
				if (MM.getObjet(i).getClass() == DPolygon.class) {
					tampon.write("REGION 1");
					tampon.newLine();
					DPolygon po = new DPolygon();
					po = (DPolygon) MM.getObjet(i);
					tampon.write(" " + po.getNumPoints());
					tampon.newLine();
					for (k = 0; k < po.getNumPoints(); k++) {
						tampon.write(po.getPoint(k).x + " " + po.getPoint(k).y * coef);
						tampon.newLine();
					}
					tampon.write("Center " + po.getCenter().x + " "
							+ po.getCenter().y * coef);
					tampon.newLine();
				}
				if (MM.getObjet(i).getClass() == DCircle.class) {
					DCircle ci = new DCircle();
					ci = (DCircle) MM.getObjet(i);
					tampon.write("ELLIPSE " + ci.getX1() + " " + ci.getY1() * coef + " "
							+ ci.getX2() + " " + ci.getY2() * coef);
					tampon.newLine();
				}
			}
			tampon.close();
			sortie.close();

			// Ecriture MID
			sortie = new FileWriter(fichierMID);
			tampon = new BufferedWriter(sortie);
			for (i = 0; i < MM.getNumObjets(); i++) {
				tampon.write("" + qm + i + qm + "," + qm + MM.getObjet(i).getClass().toString() + i + qm);
				tampon.newLine();
			}
			tampon.close();
			sortie.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void verifBounds(DPoint pt) {
		if (pt.x < MM.getLBound().x)
			MM.setBoundX(0, pt.x);
		if (pt.y < MM.getLBound().y)
			MM.setBoundY(0, pt.y);
		if (pt.x > MM.getHBound().x)
			MM.setBoundX(1, pt.x);
		if (pt.y > MM.getHBound().y)
			MM.setBoundY(1, pt.y);
	}

	private void verifBoundsP(DPoint pt, DPoint[] bounds) {
		if (pt.x < bounds[0].x)
			bounds[0].x = pt.x;
		if (pt.y < bounds[0].y)
			bounds[0].y = pt.y;
		if (pt.x > bounds[1].x)
			bounds[1].x = pt.x;
		if (pt.y > bounds[1].y)
			bounds[1].y = pt.y;
	}

	private double getValueFromSize(String taille) {
		int l = taille.length();
		if (taille.substring(l - 1).equals("%")) {
			return Integer.parseInt(taille.substring(0, l - 1));
		} else if (taille.substring(l - 1).matches("[0-9]")) {
			return Double.parseDouble(taille);
		} else
			return Integer.parseInt(taille.substring(0, l - 2));
	}

	private String getUnitFromSize(String taille) {
		int l = taille.length();
		if (taille.substring(l - 1).equals("%")) {
			return "%";
		} else
			return taille.substring(l - 2);
	}

	private void exitForm(java.awt.event.WindowEvent evt) {
		System.exit(0);
	}

	public static void main(String args[]) {
		Dialogue d = new Dialogue();
		d.setVisible(true);
	}

}
