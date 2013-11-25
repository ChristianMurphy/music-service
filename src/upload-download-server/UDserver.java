package cst420.assign3.udserver;

import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.ByteBuffer;
import java.nio.file.*;
import javax.sound.sampled.*;

/**
 * A class for client-server connections with a threaded server.
 * The echo server creates a server socket. Once a client arrives, a new
 * thread is created to service all client requests for the connection.
 * The example includes a java client and a C# client. If C# weren't involved,
 * the server and client could use a bufferedreader, which allows readln to be
 * used, and printwriter, which allows println to be used. These avoid
 * playing with byte arrays and encodings. See the Java Tutorial for an
 * example using buffered reader and printwriter.
 * @author Tim Lindquist ASU Polytechnic Department of Engineering
 * @author Christian Murphy
 * @version November 2013
 */
public class UDserver extends Thread {
  private Socket conn;
  private int id;

  /**
   * This takes in a socket and a coneection id
   * @param sock socket to connect to
   * @param id number of client that is connecting
   */
  public UDserver (Socket sock, int id) {
    this.conn = sock;
    this.id = id;
  }

  /**
   * The run method of the thread that sends the file
   */
  public void run() {
    try {
      InputStream inSock    = conn.getInputStream();
      OutputStream outSock  = conn.getOutputStream();

      //get the type of operation
      byte array[]          = new byte[4];
      inSock.read(array, 0, 4);
      int value             = ByteBuffer.wrap(array).getInt();
      System.out.println(value);

      //get length of string
      inSock.read(array, 0 , 4);
      int x = ByteBuffer.wrap(array).getInt();
      byte clientInput[]    = new byte[x];

      //Get the string that is the title
      inSock.read(clientInput, 0, x);
      String clientString   = new String(clientInput);
      System.out.println(clientString);

     switch(value) {
        //Sent file from Client to Server
        case 2:
          System.out.println("Upload");

          //location to store the file
          FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + "/UDserver/" + clientString + ".wav");

          //stores  1024 bit section
          byte bytesReceived[] = new byte[1024];
          inSock.read(array);

          int totalBytes = ByteBuffer.wrap(array).getInt();
          System.out.println(totalBytes);

          //write everything
          for(int i = totalBytes; i > -1; i--) {
            inSock.read(bytesReceived);
            fos.write(bytesReceived);
          }

          fos.flush();
          fos.close();

          break;

        //Sent file from Server to Client
        case 1:

          System.out.println("Download");
          
          //gets the file to sent to client
          File song   = new File(System.getProperty("user.dir") + "/UDserver/" + clientString + ".wav");
          
          //gets the length of the song file
          int length  = (int) song.length();
          System.out.println(length);

          //gets the number of sections to send
          int buffnum = length / 1024;
          System.out.println(buffnum);
          byte buff[] = ByteBuffer.allocate(4).putInt(buffnum).array();
          System.out.println(buff.length);

          outSock.write(buff, 0, 4);
          
          buff                 = new byte[1024];
          FileInputStream fis  = new FileInputStream(song);

          int numr = fis.read(buff);

          //write everything
          while(numr > 0) {
            outSock.write(buff);
            numr = fis.read(buff);
          }

          fis.close();
          
          break;

        default:

          break;
     }

     //alert that client has disconnected
     System.out.println("Client " + id + " closed");
     
     outSock.flush();
     outSock.close();
     inSock.close();
     conn.close();
  }
  catch (IOException e) {
    System.out.println("Can't get I/O for the connection.");
    e.printStackTrace();
  }
}

  /**
   * Main method waits for clients to connect
   * then creates a new socket and id for the client
   */
  public static void main (String args[]) {
    Socket sock;
    int id = 0;

    try {
      if (args.length != 1) {
        System.out.println("Usage: java cst420.socket.UDserver" + " [portNum]");
        System.exit(0);
      }

      int portNo = Integer.parseInt(args[0]);

      //if port number is too small default to 8888
      if (portNo <= 1024) {
          portNo = 8888;
      }

      ServerSocket serv = new ServerSocket(portNo);

      //always listen for connections
      while (true) {
        System.out.println( "Echo server waiting for connects on port " + portNo );
        sock = serv.accept();
        System.out.println( "Echo server connected to client: " + id );
        UDserver myServerThread = new UDserver(sock, id++);
        myServerThread.start();
      }
    } 
    catch(Exception e) {
      e.printStackTrace();
    }
  }
}
