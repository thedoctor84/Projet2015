package fr.univavignon.courbes.inter.simpleimpl.remote.client;

import java.io.IOException;

import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import fr.univavignon.courbes.inter.ClientConnectionHandler;
import fr.univavignon.courbes.inter.simpleimpl.SettingsManager;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;
import fr.univavignon.courbes.inter.simpleimpl.SettingsManager.NetEngineImpl;
import fr.univavignon.courbes.inter.simpleimpl.remote.AbstractConnectionPanel;
import fr.univavignon.courbes.network.ClientCommunication;
import fr.univavignon.courbes.network.central.ProfilRes;
import fr.univavignon.courbes.network.kryonet.ClientCommunicationKryonetImpl;
import fr.univavignon.courbes.network.simpleimpl.client.ClientCommunicationImpl;


public class CentralPanel extends AbstractConnectionPanel implements ClientConnectionHandler
{
	/**
	 * serialUID
	 */
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "Connexion TEST";
	
	
	public CentralPanel(MainWindow mainWindow)
	{	super(mainWindow,TITLE);
	}
	
	
	private boolean connect()
	{	// on initialise le Moteur Réseau
		ClientCommunication clientCom = null;
		NetEngineImpl netEngineImpl = SettingsManager.getNetEngineImpl();
		switch(netEngineImpl)
		{	case KRYONET:
				clientCom = new ClientCommunicationKryonetImpl();
				break;
			case SOCKET:
				clientCom = new ClientCommunicationImpl();
				break;
		}
		
		mainWindow.clientCom = clientCom;
		clientCom.setErrorHandler(mainWindow);
		clientCom.setConnectionHandler(this);
		
	
		
		ProfilRes a = new ProfilRes("http://93.118.34.229/returnip.php");
		ProfilRes b = new ProfilRes("http://93.118.34.229/returnport.php");
		
		try
		{
			String p = a.get();
			String t = b.get();
			String ipStr = p;
			clientCom.setIp(ipStr);
			SettingsManager.setLastServerIp(ipStr);
			
			String portStr = t;
			int port = Integer.parseInt(portStr);
			clientCom.setPort(port);
			SettingsManager.setLastServerPort(port);
			
			// puis on se connecte
			boolean result = clientCom.launchClient();
			return result;
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		
		
	
		return false;
	}
	
	@Override
	public void gotRefused()
	{	SwingUtilities.invokeLater(new Runnable()
		{	@Override
			public void run()
			{	JOptionPane.showMessageDialog(mainWindow, 
					"<html>Le serveur a rejeté votre candidature, car il ne reste "
					+ "<br/>pas de place dans la partie en cours de configuration.</html>");
			}
	    });
	}
	
	@Override
	public void gotAccepted()
	{	SwingUtilities.invokeLater(new Runnable()
		{	@Override
			public void run()
			{	mainWindow.clientCom.setConnectionHandler(null);
				mainWindow.displayPanel(PanelName.CLIENT_GAME_WAIT);
			}
	    });
	}
	
	@Override
	protected void nextStep()
	{	// on se connecte
		boolean connected = connect();
		
		if(connected)
		{	// on désactive les boutons le temps de l'attente
			backButton.setEnabled(false);
			nextButton.setEnabled(false);
		
			// puis on se contente d'attendre la réponse : acceptation ou rejet
			// la méthode correspondante du handler sera automatiquement invoquée
		}
		
		else
		{	JOptionPane.showMessageDialog(mainWindow, 
				"<html>Il n'a pas été possible d'établir la moindre connexion avec le serveur.</html>");
		}
	}

	@Override
	protected void previousStep()
	{	mainWindow.clientCom = null;
		mainWindow.displayPanel(PanelName.CLIENT_GAME_PLAYER_SELECTION);
	}
	
	@Override
	public String getDefaultIp()
	{	String result = SettingsManager.getLastServerIp();
		return result;
	}

	@Override
	public int getDefaultPort()
	{	int result = SettingsManager.getLastServerPort();
		return result;
	}
	
}