package cst420.assign3.udserver;

import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.ByteBuffer;
import java.nio.file.*;
import javax.sound.sampled.*;

/**
 * This abstracts the handling of sockets to send and recieve data
 * @author Christian Murphy
 * @verison November 2013
 */
public class SocketFunctions {
	InputStream  inputSocket;
    OutputStream outputSocket;

    /**
     * Establishes a link to the hosts sockets
     * @param inputSocket
     * @param outputSocket
     */
	public SocketFunctions (InputStream inputSocket, OutputStream outputSocket) {
		this.inputSocket  = inputSocket;
		this.outputSocket = outputSocket;
	}

	/**
	 * sends an integer
	 * @param integer number to send
	 */
	public void sendInteger(int integer) {
		byte integerBytes[] = ByteBuffer.allocate(4).putInt(integer).array();

		try {
			outputSocket.write(integerBytes);
			outputSocket.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * recieves a string
	 * @return integer recieved
	 */
	public int recieveInteger() {
		byte array[] = new byte[4];

		try {
	    	inputSocket.read(array, 0, 4);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ByteBuffer.wrap(array).getInt();
	}

	/**
	 * sends a string
	 * @param string to be sent
	 */
	public void sendString(String string) {
		byte stringBytes[] = string.getBytes();
		sendInteger(stringBytes.length);

		try {
			outputSocket.write(stringBytes, 0, stringBytes.length);
			outputSocket.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * recieves a string
	 * @return string value recieved
	 */
	public String recieveString() {
		int length = recieveInteger();
		byte stringBytes[]    = new byte[length];

		try {
			inputSocket.read(stringBytes, 0, length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new String(stringBytes);
	}

	public void uploadFile () {

	}

	public void downloadFile (String path) {
		
	}

	public void listFiles () {

	}
}