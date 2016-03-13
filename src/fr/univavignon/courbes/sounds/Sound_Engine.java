package fr.univavignon.courbes.sounds;

import javax.sound.sampled.*;
import java.io.*;

/**
 * @author aurelien
 *
 */
public class Sound_Engine implements Sound_Interface, Runnable, Serializable
{
	/**
	 *  Path to the sound file
	 */
	private String name;
	
	/**
	 *  Constructeur sans argument
	 */
	public Sound_Engine()
	{
		this.name = " ";
	}
	
	@Override
	public void Set_Name(String a)
	{
		this.name = a;
	}

	
	
	@Override
	public void run()
	{	

		try{
			AudioInputStream myStream = AudioSystem.getAudioInputStream(new File(name));
			AudioFormat format = myStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip myClip = (Clip)AudioSystem.getLine(info);
			myClip.open(myStream);
			myClip.start();
			myClip.drain();
			myClip.stop();
			myClip.setMicrosecondPosition(0);
			myClip.close();
			myStream.close();
		}
		
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	
	@Override
	public void playPopItem()
	{
		this.Set_Name("res/sounds/blop.wav");
		Thread t;
		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void playDeath()
	{
		this.Set_Name("res/sounds/death.wav");
		Thread t;
		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void playWin()
	{
		this.Set_Name("res/sounds/win.wav");
		Thread t;
		t = new Thread(this);
		t.start();						
	}
	
	@Override
	public void playSpeed()
	{
		this.Set_Name("res/sounds/speed.wav");
		Thread t;
		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void playUnspeed()
	{
		this.Set_Name("res/sounds/unspeed.wav");
		Thread t;
		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void playErase()
	{
		this.Set_Name("res/sounds/erase.wav");
		Thread t;
		t = new Thread(this);
		t.start();
	}
	
	public void playBack()
	{
		this.Set_Name("res/sounds/back.wav");
		Thread t;
		t = new Thread(this);
		t.start();
	}
	
	
}


