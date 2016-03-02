package fr.univavignon.courbes.sounds;

/**
 * @author aurelien
 *
 */
public interface Sound_Interface
{
	/**
	 *  Lance le Thread
	 */
	public void run();
	/**
	 * @param a path to the sound file
	 */
	public void Set_Name(String a);
	
	/**
	 * play sound when an item spawn
	 */
	public void playPopItem();
	
	/**
	 * play sound when someone die
	 */
	public void playDeath();
	
	/**
	 * play sound when someone win
	 */
	public void playWin();
	
	public void playSpeed();
	
	public void playUnspeed();
	
	public void playErase();
}