package fr.univavignon.courbes.inter.stats;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class Chart extends JPanel {

	/**
	 * serialUID
	 */
	private static final long serialVersionUID = 1L;
	private String player;
	private String stat;
	
    public Chart(String player, String stat) {
        setBackground(Color.BLACK);
        this.player = player;
        this.stat = stat;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
    	//Créer les axes
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawLine(100, 700, 900, 700);
        g.drawLine(100, 700, 100, 100);
        g.drawLine(90, 110, 100, 100);
        g.drawLine(110, 110, 100, 100);
        g.drawLine(890, 690, 900, 700);
        g.drawLine(890, 710, 900, 700);
        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30)); 
        g.drawString("Temps",500,750);
        Graphics2D g2D = (Graphics2D)g;

        AffineTransform fontAT = new AffineTransform();
        // Pour écrire verticalement sur l'axe des ordonnées
        Font theFont = g2D.getFont();
        fontAT.rotate(1.6);
        Font theDerivedFont = theFont.deriveFont(fontAT);
        g2D.setFont(theDerivedFont);
        g2D.drawString(stat, 30, 330);
        g2D.setFont(theFont);
        g.setColor(Color.BLUE);
        g.drawString(player, 30,30);
    }
    public void trigger(String stat) {
    	this.stat = stat;
    	revalidate();
    	repaint();
    }
}