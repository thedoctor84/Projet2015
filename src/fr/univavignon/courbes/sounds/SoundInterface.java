package fr.univavignon.courbes.sounds;

/**
 * @author aurelien
 *
 */
public interface SoundInterface
{
	/**
	 *  Lance le Thread
	 */
	public void run();
	/**
	 * @param a path to the sound file
	 */
	public void setName(String a);
	
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
	/**
	 * play sound when someone take the speed bonus
	 */
	public void playSpeed();
	/**
	 * play sound when someone take the unspeed bonus
	 */
	public void playUnspeed();
	/**
	 * play sound when someone take the erase bonus
	 */
	public void playErase();
}