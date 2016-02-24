package fr.univavignon.courbes.sounds;

import javax.sound.sampled.*;
import java.io.*;

/**
 * @author aurelien
 *
 */
public class Sound_Engine implements Sound_Interface
{
	@Override
	public void playSound(String name)
	{
		try{
			AudioInputStream myStream = AudioSystem.getAudioInputStream(new File(name));
			AudioFormat format = myStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip myClip = (Clip)AudioSystem.getLine(info);
			myClip.open(myStream);
			myClip.start();
			
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
}