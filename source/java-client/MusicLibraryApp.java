package cst420.assign4.client;

import cst420.assign4.client.factory.*;
import cst420.assign4.client.library.*;

import cst420.media.*;

import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.util.List;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.text.html.*;
import javax.swing.tree.*;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

/**
 * Purpose: demonstrate use of MusicLibraryGui class for students to use as a
 * basis for solving cst420 Fall 2013 Assign1.
 * This problem provides for browsing and managing information about
 * music files. It uses a Swing JTree, JTextField, JComboBox controls to 
 * realize a GUI with a split pane. The left pane contains an expandable
 * JTree of the music library.
 * The right pane contains components that allow viewing, modifying and adding
 * music files and their descriptions.
 * @author Tim Lindquist (Tim.Lindquist@asu.edu), ASU Polytechnic, Engineering
 * @author Christian Murphy
 * @version December 2013
 */
public class MusicLibraryApp extends MusicLibraryGui implements TreeWillExpandListener, ActionListener, TreeSelectionListener {

	private static final boolean debugOn = true;

	private LibraryFactory	libraryFactory;
	private LibraryService	libraryService;
	private Library 		library;

	private static MusicPlayer	musicPlayerThread;
	private static Timer		timer;

	private String	hostname;
	private int		port;

	public MusicLibraryApp(String base, String hostname, int port) {
		super(base);
		this.hostname = hostname;
		this.port = port;

		for(int i=0; i<userMenuItems.length; i++){
			for(int j=0; j<userMenuItems[i].length; j++){
				userMenuItems[i][j].addActionListener(this);
			}
		}
		pauseResJB.addActionListener(this);
		tree.addTreeSelectionListener(this);
		tree.addTreeWillExpandListener(this);

		//musicDesciptionService = new MusicDescriptionService();
		libraryFactory = new LibraryFactoryService().getLibraryFactoryPort();
        libraryService = new LibraryService();
        library = libraryService.getPort(libraryFactory.getALibrary(), Library.class);

		setVisible(true);
	}

	private void debug(String message) {
		if (debugOn)
			System.out.println("debug: " + message);
	}

	/**
	 * create and initialize nodes in the JTree of the left pane.
	 * buildInitialTree is called by MusicLibraryGui to initialize the JTree.
	 * Classes that extend MusicLibraryGui should override this method to 
	 * perform initialization actions specific to the extended class.
	 * The default functionality is to set base as the label of root.
	 * In your solution, you will probably want to initialize by deserializing
	 * your library and building the tree.
	 * @param root Is the root node of the tree to be initialized.
	 * @param base Is the string that is the root node of the tree.
	 */
	public void buildInitialTree(DefaultMutableTreeNode root, String base){
		try{
			System.out.println("buildInitialTree called by Gui constructor");
		}catch (Exception ex){
			JOptionPane.showMessageDialog(this,"exception initial tree:"+ex);
			ex.printStackTrace();
		}
	}

	/**
	 * Adds a song into correct album in the tree
	 * @param title song title being inserted
	 * @param album album where song will be stored
	 */
	public void addSong(String title, String album) {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		int numberOfAlbums = model.getChildCount(root);
		boolean albumExists = false;

		//check all albums
		for (int index = 0; index < numberOfAlbums; index++) {
			DefaultMutableTreeNode currentAlbum = (DefaultMutableTreeNode) model.getChild(root, index);
			//if album already exists
			if ( album.equals( currentAlbum.getUserObject() ) ) {
				//insert into album
				model.insertNodeInto(new DefaultMutableTreeNode(title), currentAlbum, model.getChildCount(currentAlbum));
				albumExists = true;
				break;
			}
		}


		//album does not exist
		if (!albumExists) {
			//create a new album
			model.insertNodeInto(new DefaultMutableTreeNode(album), root, numberOfAlbums);
			DefaultMutableTreeNode currentAlbum = (DefaultMutableTreeNode) model.getChild(root, numberOfAlbums);
			//inserts the song into the album
			model.insertNodeInto(new DefaultMutableTreeNode(title), currentAlbum, model.getChildCount(currentAlbum));
		}
	}

	/**
	 * Fills the tree from the Library XML
	 */
	public void initializeTree( ){
		//remove event listeners while editing
		
		tree.removeTreeWillExpandListener(this);

		try {
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			//set root to username
			clearTree(root, model);
			tree.removeTreeSelectionListener(this);
			
			model.setRoot(new DefaultMutableTreeNode(System.getProperty("user.name")));

			List <String> titles = library.getMusicList();
			for (int index = 0; index < titles.size(); index++) {
				addSong(titles.get(index), library.getMusicDescription(titles.get(index)).getAlbum());
			}

			// expand all the nodes in the JTree
			for(int r =0; r < tree.getRowCount(); r++){
				tree.expandRow(r);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//reattach event listeners now that it has been update
		tree.addTreeSelectionListener(this);
		tree.addTreeWillExpandListener(this);
	}

	private void clearTree(DefaultMutableTreeNode root, DefaultTreeModel model){
		tree.removeTreeSelectionListener(this);
		tree.removeTreeWillExpandListener(this);

		try{
			DefaultMutableTreeNode next = null;
			int subs = model.getChildCount(root);
			for(int k=subs-1; k>=0; k--){
				next = (DefaultMutableTreeNode) model.getChild(root,k);
				debug("removing node labelled:" + (String)next.getUserObject() );
				model.removeNodeFromParent(next);
			}
		}catch (Exception ex) {
			System.out.println("Exception while trying to clear tree:");
			ex.printStackTrace();
		}

		tree.addTreeSelectionListener(this);
		tree.addTreeWillExpandListener(this);
	}


	public void treeWillCollapse(TreeExpansionEvent tee) {
		tree.setSelectionPath(tee.getPath());
	}

	public void treeWillExpand(TreeExpansionEvent tee) {
		DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) tee.getPath().getLastPathComponent();
		System.out.println("will expand node: " + dmtn.getUserObject() + " whose path is: " + tee.getPath());
	}

	public void valueChanged(TreeSelectionEvent e) {
		try{
			tree.removeTreeSelectionListener(this);
			//gets the current node
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			String nodeLabel = (String) node.getUserObject();
			System.out.println("Selected node labelled: "+nodeLabel);
			
			//if it is a song         
			if(node.isLeaf()) {
				titleJTF.setText(nodeLabel);
				authorJTF.setText(library.getMusicDescription(nodeLabel).getArtist());
				albumJTF.setText(library.getMusicDescription(nodeLabel).getAlbum());
			}
			//if it is an album
			else {
				titleJTF.setText("");
				authorJTF.setText("");
				albumJTF.setText(nodeLabel);
			}
	
		}catch (Exception ex){
			ex.printStackTrace();
		}
		tree.addTreeSelectionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		String event = e.getActionCommand();
		if ( event.equals("Exit") ) {
			System.exit(0);
		}
		else if (event.equals("Save")) {
			System.out.println("Save Selected");
			library.saveMusicLibrary();
		}
		else if (event.equals("Restore")) {
			System.out.println("Restore selected, initializing tree");
			library.restoreMusicLibrary();
			initializeTree();
		}
		else if (event.equals("Tree Refresh")){
			System.out.println("Restore selected, initializing tree");
			initializeTree();
		}
		else if (event.equals("Add")) {
			System.out.println("Add Selected");
			library.putMusicDescription( titleJTF.getText(), authorJTF.getText(), albumJTF.getText() );
			addSong(titleJTF.getText(), albumJTF.getText());
			uploadFile(titleJTF.getText());
		}
		else if (event.equals("Remove")) {
			System.out.println("Remove Selected");
			try {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				library.removeMusicDescription( (String) node.getUserObject());
				DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
				tree.removeTreeSelectionListener(this);
				model.removeNodeFromParent(node);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			tree.addTreeSelectionListener(this);
		}
		else if (event.equals("Play")) {
			System.out.println("Play Selected");

			//TODO stuff
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			downloadFile( node.getUserObject().toString() );
				
			//start music
			musicPlayerThread = new MusicPlayer();
			musicPlayerThread.start();
			try {
				musicPlayerThread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			//set length of music in progress bar
			progressJS.setMaximum((int)Math.round(musicPlayerThread.getDuration()));

			//set start time
			timeJLBL.setText(String.valueOf(0));

			//start timer
			timer = new Timer(1000, this);
			timer.setActionCommand("Timer");
			timer.start();
		}
		else if (event.equals("Pause")) {
			pauseResJB.removeActionListener(this);
			pauseResJB.setText("Resume");
			pauseResJB.addActionListener(this);

			musicPlayerThread.toggleState();
			timer.start();
		}
		else if (event.equals("Resume")) {
			pauseResJB.removeActionListener(this);
			pauseResJB.setText("Pause");
			pauseResJB.addActionListener(this);

			//stop timer
			timer.stop();

			//pause music
			musicPlayerThread.toggleState();
		}
		else if (event.equals("Timer")) {
			progressJS.setValue(progressJS.getValue() + 1);
			timeJLBL.setText( ( (Integer) (progressJS.getValue() + 1) ).toString() );
		}
	}

	/**
	 * Download file from server
	 * @param filename name of file to download
	 * @param hostname name of host to connect to
	 * @param port port number which server is hosting on
	 */
	public void downloadFile (String filename) {
		try {
			//connect to server
			Socket 			sock	= new Socket(hostname, port);
			InputStream		is		= sock.getInputStream();
			OutputStream	os		= sock.getOutputStream();

			SocketFunctions socketFunctions = new SocketFunctions(is, os);
			socketFunctions.sendInteger(1);
			socketFunctions.sendString(filename);
			socketFunctions.receiveFile(System.getProperty("user.dir") + "/client-music/" + filename + ".wav");

			os.close();
			is.close();
			sock.close();
		} catch(Exception x) {
			x.printStackTrace();
		}
	}

	/**
	 * Upload file to server
	 * @param filename name of file to upload
	 * @param hostname name of host to connect to
	 * @param port port number which server is hosting on
	 */
	public void uploadFile (String filename) {
		try {
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(System.getProperty("user.dir")));

			FileNameExtensionFilter filter = new FileNameExtensionFilter("wav files", "wav");
			fc.setFileFilter(filter);

			File file = new File("test");

			int returnVal = fc.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
			}

			//connect to server
			Socket sock		= new Socket(hostname, port);
			OutputStream os	= sock.getOutputStream();
			InputStream is	= sock.getInputStream();

			//send file
			SocketFunctions socketFunctions = new SocketFunctions(is, os);
			socketFunctions.sendInteger(2);
			socketFunctions.sendString(filename);
			socketFunctions.sendFile( file.getAbsolutePath() );

			os.close();
			is.close();
		    sock.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		try{
			String name = "Music Library";
			if (args.length >= 1) {
				name = args[0];
			}

			System.out.println("Wait a few seconds before clicking stuff or you will get errors");
			MusicLibraryApp ltree = new MusicLibraryApp(name, args[0], Integer.parseInt(args[1]));
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
(