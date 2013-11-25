package cst420.assign4.udserver;

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
 * @author Christian Murphy
 * @author Tim Lindquist ASU Polytechnic Department of Engineering
 * @version November 2013
 */
public class UploadDownloadServer extends Thread {
  private Socket conn;
  private int id;

  /**
   * This takes in a socket and a coneection id
   * @param sock socket to connect to
   * @param id number of client that is connecting
   */
  public UploadDownloadServer (Socket sock, int id) {
    this.conn = sock;
    this.id = id;
  }

  /**
   * The run method of the thread that sends the file
   */
  public void run() {
    try {
      InputStream  inSock   = conn.getInputStream();
      OutputStream outSock  = conn.getOutputStream();
      SocketFunctions socketFunctions = new SocketFunctions(inSock, outSock);

      //get the type of operation
      int operation = socketFunctions.receiveInteger();
      System.out.println(operation);

      String filepath = socketFunctions.receiveString();
      System.out.println(filepath);

     switch(operation) {
        //Sent file from Client to Server
        case 2:
          System.out.println("Upload");
          socketFunctions.receiveFile(  System.getProperty("user.dir") + "/server-music/" + filepath + ".wav");
          break;

        //Sent file from Server to Client
        case 1:
          System.out.println("Download");
          socketFunctions.sendFile(     System.getProperty("user.dir") + "/server-music/" + filepath + ".wav");
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
    Socket socket;
    int id = 0;

    try {
      if (args.length != 1) {
        System.out.println("Usage: java cst420.socket.UDserver [portNum]");
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
        socket = serv.accept();
        System.out.println( "Echo server connected to client: " + id );
        UploadDownloadServer myServerThread = new UploadDownloadServer(socket, id++);
        myServerThread.start();
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
