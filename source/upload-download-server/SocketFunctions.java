package cst420.assign4.udserver;

import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.*;
import java.nio.ByteBuffer;
import java.nio.file.*;
import javax.sound.sampled.*;

/**
 * This abstracts the handling of sockets to send and receive data
 * @author Christian Murphy
 * @verison December 2013
 */
public class SocketFunctions {
	private 		InputStream  inputSocket;
	private 		OutputStream outputSocket;
	final private	int byteWordSize = 1024;

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
		integer = Integer.reverseBytes(integer);
		byte integerBytes[] = ByteBuffer.allocate(4).putInt(integer).array();

		try {
			outputSocket.write(integerBytes);
			outputSocket.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * receives a string
	 * @return integer received
	 */
	public int receiveInteger() {
		byte array[] = new byte[4];

		try {
			inputSocket.read(array, 0, 4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Integer.reverseBytes(ByteBuffer.wrap(array).getInt());
	}

	/**
	 * sends a string
	 * @param string to be sent
	 */
	public void sendString(String string) {
		byte stringBytes[] = string.getBytes();
		sendInteger(stringBytes.length);
		ByteBuffer stringByteBuffer = ByteBuffer.wrap(stringBytes);
		stringByteBuffer.order(ByteOrder.LITTLE_ENDIAN);

		try {
			outputSocket.write(stringByteBuffer.array());
			outputSocket.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * receives a string
	 * @return string value received
	 */
	public String receiveString() {
		int length = receiveInteger();
		byte stringBytes[]    = new byte[length];

		try {
			inputSocket.read(stringBytes, 0, length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ByteBuffer stringByteBuffer = ByteBuffer.wrap(stringBytes);
		stringByteBuffer.order(ByteOrder.BIG_ENDIAN);
		return new String(stringByteBuffer.array());
	}

	/**
	 * Takes a file and sends it
	 * @param path of file to sent
	 */
	public void sendFile (String filepath) {
		File file   = new File(filepath);
		byte[] fileBuffer = new byte[byteWordSize];

		try {
			FileInputStream fileInput = new FileInputStream(file);

			int fileState;
			do {
				fileState = fileInput.read(fileBuffer);
				outputSocket.write(fileBuffer);
				outputSocket.flush();
			} while (fileState > 0);

			fileInput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Recieves a file and stores it to a filepath
	 * @param filepath location to store file
	 */
	public void receiveFile (String filepath) {
		byte fileBuffer[] = new byte[byteWordSize];

		try {
			FileOutputStream fileOutput = new FileOutputStream(filepath);
			int size;
			while ( (size = inputSocket.read(fileBuffer, 0, byteWordSize)) > 0) {
				fileOutput.write(fileBuffer, 0, size);
			}

			fileOutput.flush();
			fileOutput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
