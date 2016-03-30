package fr.univavignon.courbes.network.central;

import java.net.*;
import java.io.*;
import java.util.Vector;


/**
 * @author aurelien
 *
 */
public class ProfilRes
{
	private String url;

	public ProfilRes(String a)
	{
		this.url = a;
	}

	public String get() throws IOException
	{
		String source = "";
		URL oracle = new URL(this.url);
		URLConnection yc = oracle.openConnection();
		BufferedReader in = new BufferedReader(
		new InputStreamReader(
		yc.getInputStream()));
		String inputLine;
 
		while ((inputLine = in.readLine()) != null)
		source +=inputLine;
		in.close();
		return source;
	}

	public Vector<String> coupe_chaine(String s)
	{
		Vector<String> a = new Vector<String>();

		int i = 0;

		while(i != s.length())
		{

			String temp = "";

			while(s.charAt(i) != ':' && i != s.length())
			{
				temp += s.charAt(i);
				i++;
			}

			a.add(temp);

			i++;
		}

		return a;
	}
	

	public String getPseudo(String s)
	{
		String temp="";

		int i = 2;

		while(s.charAt(i) != ' ')
		{
			temp += s.charAt(i);
			i++;
		}

		return temp;	
	}

	public String getElo(String s)
	{
		String temp="";

		int i = 2;

		while(s.charAt(i) != ' ')
		{
			i++;
		}

		i++;

		while(s.charAt(i) != ' ')
		{
			temp += s.charAt(i);
			i++;
		}

		return temp;	
	}

	public String getWin(String s)
	{
		String temp = "";
		int i = 2;

		while(s.charAt(i) != ' ')
			i++;
		
		i++;

		while(s.charAt(i) != ' ')
			i++;

		i++;

		while(s.charAt(i) != ' ')
		{
			temp += s.charAt(i);
			i++;
		}

		return temp;
	}

	public String getLoose(String s)
	{

		String temp = "";
		int i = 2;

		while(s.charAt(i) != ' ')
			i++;
		
		i++;

		while(s.charAt(i) != ' ')
			i++;

		i++;

		while(s.charAt(i) != ' ')
			i++;

		i++;

		while(s.charAt(i) != ' ')
		{
			temp += s.charAt(i);
			i++;
		}

		return temp;

	}


	public String getMdp(String s)
	{
		String temp = "";
		int i = 2;

		while(s.charAt(i) != ' ')
			i++;
		
		i++;

		while(s.charAt(i) != ' ')
			i++;

		i++;

		while(s.charAt(i) != ' ')
			i++;

		i++;

		while(s.charAt(i) != ' ')
			i++;

		i++;

		while(s.charAt(i) != ' ')
		{
			temp += s.charAt(i);
			i++;
		}

		return temp;
	}


	public String getPays(String s)
	{
		String temp = "";
		int i = 2;

		while(s.charAt(i) != ' ')
			i++;
		
		i++;

		while(s.charAt(i) != ' ')
			i++;

		i++;

		while(s.charAt(i) != ' ')
			i++;

		i++;

		while(s.charAt(i) != ' ')
			i++;

		i++;

		while(s.charAt(i) != ' ')
			i++;

		i++;

		while(i != s.length())
		{
			temp += s.charAt(i);
			i++;
		}

		return temp;
	}

}