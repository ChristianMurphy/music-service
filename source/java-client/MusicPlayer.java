package cst420.assign4.client;

import java.io.*;


import javax.sound.sampled.*;


/**
 * This is the Threaded music decoder
 * @author Christian Murphy
 * @author Tim Lindquist (Tim.Lindquist@asu.edu), ASU Polytechnic, Engineering
 * @version December 2013
 */
public class MusicPlayer extends Thread {
	private String filename;
	private boolean play;
	private SourceDataLine sourceLine;
	private AudioInputStream audioStream;
	private double duration;

	/**
	 * Plays the music file
	 */
	public synchronized void run() {
		play = true;
		int BUFFER_SIZE = 4096;
		AudioFormat audioFormat;
		SourceDataLine sourceLine;

		try {
			filename = "client-music/NarniaTheBattleExcerpt.wav";
			File file = new File(filename);
			audioStream = AudioSystem.getAudioInputStream(file);
			audioFormat = audioStream.getFormat();

			//this gets the total time of the song in seconds
			long frames = audioStream.getFrameLength();
			duration = (frames+0.0) / audioFormat.getFrameRate();

			//start reading stuff
			DataLine.Info i = new DataLine.Info(SourceDataLine.class, audioFormat);
			sourceLine = (SourceDataLine) AudioSystem.getLine(i);
			sourceLine.open(audioFormat);
			sourceLine.start();

			int nBytesRead = 0;
			byte[] abData = new byte[BUFFER_SIZE];

			while(nBytesRead != -1) {
				//when paused put thread to sleep
				while(!play) {
					sleep(10);
				}

				//play music
				try{
					nBytesRead = audioStream.read(abData, 0, abData.length);
					if (nBytesRead >= 0) {
						@SuppressWarnings("unused")
						int nBytesWritten = sourceLine.write(abData,0,nBytesRead);
					}
				} catch (Exception e){
					e.printStackTrace();
				}
			}

			sourceLine.drain();
			sourceLine.close();
			audioStream.close();
			file.delete();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Toggles between play and pause state
	 */
	public void toggleState() {
		try {
			play = !play;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the filename of the song to be played
	 * @deprecated as of September 2013 it was requested that filename was hard coded
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * gets the length of the song (in seconds)
	 */
	public double getDuration() {
		return duration;
	}
}
