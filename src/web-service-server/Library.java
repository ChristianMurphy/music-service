package cst420.assign3.wsserver.library;

import cst420.assign3.wsserver.music.*;

//java stateful service handler
import com.sun.xml.ws.developer.StatefulWebServiceManager;
import com.sun.xml.ws.developer.Stateful;

//java file stream handler
import java.io.FileOutputStream;
import java.io.FileInputStream;

//java xml serialize and deserialize
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;

//java data structures
import java.util.*;

//java web services
import javax.jws.WebService;
import javax.jws.WebMethod;

//java url handling
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

/**
 * This is the library that is a collection (treemap to be exact) of Music Descriptions
 * for simple java storage
 * @author Christian Murphy
 * @version November 2013
 */
@Stateful @WebService @Addressing
public class Library {
	/**
	 * Stores the songs in a hash map,
	 * album storage is implicit
	 */
	private HashMap <String, MusicDescription> library;

	/**
	 * intializes the hash map
	 */
	public Library () {
		library = new HashMap <String, MusicDescription>();
	}

	/**
	 * This retrieves a Music Description based on its title
	 * @param title title of the song being retrieved
	 */
	@WebMethod
	public MusicDescription getMusicDescription(String title) {
		return library.get(title);
	}

	/**
	 * inserts a new Song into the library based on title
	 * and stores the Music Description object
	 * @param title unique title of the song
	 * @param Music Description object with song info
	 */
	@WebMethod
	public void putMusicDescription(String title, String artist, String album) {
		library.put(title, new MusicDescription(title, artist, album));
	}

	/**
	 * Removes a song based on its title
	 * @param title title of the song being removed
	 */
	@WebMethod
	public void removeMusicDescription(String title) {
		library.remove(title);
	}

	/**
	 * Lists the title of all the songs
	 */
	@WebMethod
	public List <String> getMusicList() {
		return Arrays.asList( library.keySet().toArray( new String[ library.size() ] ) );
	}

	/**
	 * Serializes the library into an XML object and stores it to an XML file
	 */
	@WebMethod
	public void saveMusicLibrary() {
		try {
			FileOutputStream writeBuffer = new FileOutputStream(System.getProperty("catalina.base") + "/webapps/library.xml");
			XMLEncoder xmlEncoder = new XMLEncoder(writeBuffer);
			xmlEncoder.writeObject(library);
			xmlEncoder.close();
			writeBuffer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens and deserializes the Library object from the XML file
	 */
	@SuppressWarnings("unchecked")
	@WebMethod
	public void restoreMusicLibrary() {
		try {
			FileInputStream readBuffer = new FileInputStream(System.getProperty("catalina.base") + "/webapps/library.xml");
			XMLDecoder xmlDecoder = new XMLDecoder(readBuffer);
			library = ((HashMap<String, MusicDescription>) xmlDecoder.readObject());
			xmlDecoder.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The equals implementation so that two {@link Account} with the same
	 * bankName won't be exported twice.
	 */
	public boolean equals(Object that) {
		if (that == null || this.getClass() != that.getClass()) return false;
		return this.library.equals(((Library)that).library);
	}

	/**
	 * Hashcode implementation consistent with {@link #equals(Object)}.
	 */
	public int hashCode() {
		return library.hashCode();
	}

	/**
	 * This object is injected by the JAX-WS RI, and exposes various
	 * operations to support stateful web services. Consult its javadoc
	 * for more information.
	 */
	public static StatefulWebServiceManager<Library> manager;
} 
