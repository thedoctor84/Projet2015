package fr.univavignon.courbes.inter.stats;

import java.awt.Color;
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
import javax.swing.JComboBox;
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

public class GraphPanel extends JPanel implements ActionListener
{
	
	/** FenÃªtre contenant ce panel */
	private MainWindow mainWindow;
	/** Table affichÃ©e par ce panel */
	private JPanel chart;
	/** Bouton pour revenir au menu principal */
	private JButton backButton;
	
	private JComboBox choice;
	private String stat = "Elo";
	private String Player;
		
	public GraphPanel(MainWindow mainWindow, String player)
	{	
		
		super();
		this.mainWindow = mainWindow;
		this.backButton = new JButton("Retour");
		backButton.addActionListener(this);
		Player = player;
		chart = new Chart(Player,stat);
		choice = new JComboBox<String>();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		choice_init();
		chart.add(choice);
		chart.add(backButton);
		add(chart);
		choice.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	stat = (String)choice.getSelectedItem();
		    	((Chart) chart).trigger(stat);
		    	revalidate();
                repaint();
		    }
		});
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==backButton)
			mainWindow.displayPanel(PanelName.STATISTICS);
	}
	
	public void choice_init() {
		choice.setPreferredSize(new Dimension(70,30));
		choice.addItem("Elo");
		choice.addItem("Victoire");
		choice.addItem("Defaite");
	}
}