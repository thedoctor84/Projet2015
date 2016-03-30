package fr.univavignon.courbes.inter.stats;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;

public class GraphPanel extends JPanel implements ActionListener
{
	
	/**
	 * serialUID
	 */
	private static final long serialVersionUID = 1L;
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
		choiceInit();
		chart.add(choice);
		chart.add(backButton);
		add(chart);
		choice.addActionListener (new ActionListener () {
		    @Override
			public void actionPerformed(ActionEvent e) {
		    	stat = (String)choice.getSelectedItem();
		    	((Chart) chart).trigger(stat);
		    	revalidate();
                repaint();
		    }
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==backButton)
			mainWindow.displayPanel(PanelName.STATISTICS);
	}
	
	public void choiceInit() {
		choice.setPreferredSize(new Dimension(70,30));
		choice.addItem("Elo");
		choice.addItem("Victoire");
		choice.addItem("Defaite");
	}
}