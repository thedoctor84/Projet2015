package fr.univavignon.courbes.inter.simpleimpl.profiles;

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

import java.io.FileInputStream;

import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Vector;

import fr.univavignon.courbes.network.central.*;

import fr.univavignon.courbes.common.Profile;

/**
 * Classe chargée de charger, gérer et enregistrer les profils.
 * On utilise pour cela un ficher texte.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ProfileManager
{	/** Fichier contenant les profils */
	private static final String PROFILE_FILE = "res/profiles/profiles.txt";
	/** Chaine de caractères de séparation */
	private static final String SEPARATOR = ",";
	/** Liste des profils */
	private static final TreeSet<Profile> PROFILES = new TreeSet<Profile>();
	/** Nombre de champs à lire par profil */
	private static final int PROFILE_FIELD_NBR = 6;
	
	/**
	 * Renvoie la liste de tous profils disponibles. La méthode charge
	 * cette liste si ce n'est pas déjà fait.
	 *  
	 * @return
	 * 		Liste des profils disponibles.
	 */
	public static TreeSet<Profile> getProfiles()
	{	
		try{
			
			Profil_Res co = new Profil_Res("http://93.118.34.229/");
			String temp="";
			
			temp = co.get();
			Vector<String> t;
			t = co.coupe_chaine(temp);
			
			FileWriter fichier = new FileWriter("res/profiles/profiles.txt",false);
			BufferedWriter output = new BufferedWriter(fichier);
			
			for(int i=0;i!=t.size();i++){
				
				output.write(co.getPseudo(t.elementAt(i)));
				output.write(",");
				output.write(co.getPays(t.elementAt(i)));
				output.write(",");
				output.write(co.getElo(t.elementAt(i)));
				output.write(",");
				output.write("fake@fake.fake");
				output.write(",");
				output.write(co.getMdp(t.elementAt(i)));
				output.write(",null\n");
				
				
			}
			output.flush();
			output.close();
		}
		
		catch(IOException e)
		{
			System.out.println(e);
		}
		
		if(PROFILES.isEmpty())
			loadProfiles();
		return PROFILES;
	}
	
	/**
	 * Ajoute un profil à la liste. Aucune vérification n'est effectuée
	 * sur les noms ou emails des utilisateurs. La liste modifiée est
	 * enregistrée.
	 * 
	 * @param profile
	 * 		Utilisateur à rajouter.
	 */
	public static void addProfile(Profile profile)
	{	
		try
		{
			String a = "http://93.118.34.229/add.php?name=";
			a+=profile.userName;
			a+="&mdp=";
			a+=profile.password;
			a+="&mail=";
			a+=profile.email;
			a+="&pays=";
			a+=profile.country;
			Profil_Res co = new Profil_Res(a);
			co.get();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}

		if(PROFILES.isEmpty())
			profile.profileId = 0;
		else
		{	Profile mx = Collections.max(PROFILES);
			profile.profileId = mx.profileId + 1;
		}
		
		PROFILES.add(profile);
		recordProfiles();
	}
	
	/**
	 * Supprime un profil de la liste, et enregistre.
	 * 
	 * @param profile
	 * 		Le profil à supprimer.
	 */
	public static void removeProfile(Profile profile)
	{	
		try
		{
			String a = "http://93.118.34.229/sup.php?name=";
			a+=profile.userName;
			Profil_Res co = new Profil_Res(a);
			co.get();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		
		PROFILES.remove(profile);
		recordProfiles();
	}
	
	/**
	 * Enregistre la liste des profils dans un fichier
	 * texte, dans lequel chaque ligne représente un profil,
	 * et les champs d'un profil sont séparés par {@link #SEPARATOR}.
	 */
	private static void recordProfiles()
	{	try
		{	FileOutputStream fos = new FileOutputStream(PROFILE_FILE);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			PrintWriter writer = new PrintWriter(osw);
			
			for(Profile profile: PROFILES)
			{	writer.write
				(	profile.userName + SEPARATOR + 
					profile.country + SEPARATOR +
					profile.eloRank + SEPARATOR +
					profile.email + SEPARATOR +
					profile.password + SEPARATOR +
					profile.agent + "\n"
				);
			}
			writer.flush();
			writer.close();
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}
	
	/**
	 * Charge la liste des profils à partir d'un fichier
	 * texte structuré comme expliqué pour {@link #recordProfiles()}.
	 */
	private static void loadProfiles()
	{	try
		{	// on ouvre le fichier en lecture
			FileInputStream fis = new FileInputStream(PROFILE_FILE);
			InputStreamReader isr = new InputStreamReader(fis);
			Scanner scanner = new Scanner(isr);
			
			int profileId = 0;
			// on en lit chaque ligne
			while(scanner.hasNext())
			{	String line = scanner.nextLine();
				String elem[] = line.split(SEPARATOR);
				if(elem.length == PROFILE_FIELD_NBR)
				{	// on crée le profil et on l'initialise
					Profile profile = new Profile();
					profile.profileId = profileId;
					profile.userName = elem[0].trim();
					profile.country = elem[1].trim();
					profile.eloRank = Integer.parseInt(elem[2].trim());
					profile.email = elem[3].trim();
					profile.password = elem[4].trim();
					profile.agent = elem[5].trim();
					if(profile.agent.equals("null"))
						profile.agent = null;
					PROFILES.add(profile);
				}
				else
					System.err.println("Erreur à la ligne "+(profileId+1)+" : elle contient " + elem.length + " éléments au lieu des "+PROFILE_FIELD_NBR+" attendus");
				
				profileId++;
			}
			
			// on ferme le fichier
			scanner.close();  
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}
	
	/**
	 * Cherche le noms spécifié parmi les utilisateurs de la liste
	 * et renvoie {@code true} s'il est trouvé.
	 * 
	 * @param userName
	 * 		Nom recherché.
	 * @return
	 * 		{@code true} ssi le nom est déjà utilisé.
	 */
	public static boolean containsUserName(String userName)
	{	boolean found = false;
		Iterator<Profile> it = PROFILES.iterator();
		while(!found && it.hasNext())
		{	Profile profile = it.next();
			found = userName.equalsIgnoreCase(profile.userName);
		}
		return false;
	}
	
	/**
	 * Renvoie le profil associé au numéro de profil spécifié,
	 * ou {@code null} si aucun profil ne correspond.
	 * 
	 * @param profileId
	 * 		Numéro du profil désiré.
	 * @return
	 * 		Le profil associé à ce numéro.
	 */
	public static Profile getProfile(int profileId)
	{	Profile result = null;
		Iterator<Profile> it = PROFILES.iterator();
		while(result==null && it.hasNext())
		{	Profile profile = it.next();
			if(profile.profileId==profileId)
				result = profile;
		}
		return result;
	}
}
