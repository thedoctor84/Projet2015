package fr.univavignon.courbes.inter.stats;

/*
 * Courbes
 * Copyright 2015-16 L3 Info UAPV 2015-16
 * 
 * This file is part of Courbes.
 * 
 * Courbes is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, 
 * either version 2 of the License, or (at your option) any later version.
 * 
 * Courbes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Courbes. If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;
import fr.univavignon.courbes.inter.simpleimpl.profiles.ProfileManager;
import fr.univavignon.courbes.inter.simpleimpl.profiles.ProfileTableModel;
import fr.univavignon.courbes.inter.simpleimpl.SettingsManager;

public class StatPanel extends JPanel implements ActionListener
{
	
	
	private static final long serialVersionUID = 1L;
	/** Nom par défaut pour le champ texte */
	private static final String DEFAULT_NAME = "Nom";
	/** Pays par défaut pour le champ texte */
	private static final String DEFAULT_COUNTRY = "Pays";
	/** Mot de passe par défaut pour le champ mot de passe */
	private static final String DEFAULT_MDP = "Mot de passe";
	/** Email par defaut pour le champ email */
	private static final String DEFAULT_MAIL = "Email";
	
	
	public StatPanel(MainWindow mainWindow)
	{	super();
		this.mainWindow = mainWindow;
		
		init();
	}
	
	/** Fenêtre contenant ce panel */
	private MainWindow mainWindow;
	/** Table affichée par ce panel */
	private JTable playerTable;
	/** Scrollpane contenu dans ce panel pour afficher la table */
	private JScrollPane scrollPane; 
	/** Bouton pour revenir au menu principal */
	private JButton backButton;
	/** Bouton pour ajouter les stars*/
	private JButton statButton;

	private void init()
	{	BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);
		initTablePanel();
		add(Box.createVerticalGlue());
		initButtonsPanel();
	}
	
	/**
	 * Initialisation de la table affichée par ce panel.
	 */
	private void initTablePanel()
	{	playerTable = new JTable();
		playerTable.setAutoCreateRowSorter(true);
		
		playerTable.setModel(new ProfileTableModel(1));
		
		scrollPane = new JScrollPane
		(	playerTable,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		Dimension frameDim = mainWindow.getPreferredSize();
		int boardHeight = SettingsManager.getBoardHeight();
		Dimension dim = new Dimension(frameDim.width,(int)(boardHeight*0.95));
		scrollPane.setPreferredSize(dim);
		scrollPane.setMaximumSize(dim);
		scrollPane.setMinimumSize(dim);
		add(scrollPane);
	}
	

	/**
	 * Initialisation des boutons contenus dans ce panel.
	 */
	private void initButtonsPanel()
	{	JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.LINE_AXIS);
		panel.setLayout(layout);
		
		
		panel.add(Box.createHorizontalGlue());
		backButton = new JButton("Retour");
		backButton.addActionListener(this);
		panel.add(backButton);
		
		panel.add(Box.createHorizontalGlue());
		statButton = new JButton("Afficher");
		statButton.addActionListener(this);
		panel.add(statButton);
		
		add(panel);
	}

	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==backButton)
			mainWindow.displayPanel(PanelName.MAIN_MENU);
	
		if(e.getSource()==statButton)
			mainWindow.displayPanel(PanelName.)
	}
	
	
}