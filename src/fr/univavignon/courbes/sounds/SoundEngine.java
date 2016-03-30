package fr.univavignon.courbes.sounds;

import javax.sound.sampled.*;
import java.io.*;

/**
 * @author aurelien
 *
 */
public class SoundEngine implements SoundInterface, Runnable, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *  Path to the sound file
	 */
	private String name;
	
	/**
	 *  Constructeur sans argument
	 */
	public SoundEngine()
	{
		this.name = " ";
	}
	
	@Override
	public void setName(String a)
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
		this.setName("res/sounds/blop.wav");
		Thread t;
		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void playDeath()
	{
		this.setName("res/sounds/death.wav");
		Thread t;
		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void playWin()
	{
		this.setName("res/sounds/win.wav");
		Thread t;
		t = new Thread(this);
		t.start();						
	}
	
	@Override
	public void playSpeed()
	{
		this.setName("res/sounds/speed.wav");
		Thread t;
		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void playUnspeed()
	{
		this.setName("res/sounds/unspeed.wav");
		Thread t;
		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void playErase()
	{
		this.setName("res/sounds/erase.wav");
		Thread t;
		t = new Thread(this);
		t.start();
	}
	/**
	 * La musique en background
	 */
	public void playBack()
	{
		this.setName("res/sounds/back.wav");
		Thread t;
		t = new Thread(this);
		t.start();
	}
	
	
}


