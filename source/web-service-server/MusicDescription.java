package cst420.assign4.wsserver.music;

import com.sun.xml.ws.developer.StatefulWebServiceManager;
import com.sun.xml.ws.developer.Stateful;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.xml.ws.soap.Addressing;

/**
 * Music Description
 * This is essentially a structure that stores all of the information for an album
 * in java deserialized format
 * @author Christian Murphy
 * @version November 2013
 */
@Stateful @WebService @Addressing
public class MusicDescription {
	private String title;
	private String artist;
	private String album;

	public MusicDescription() {}

	public MusicDescription(String title, String artist, String album) {
		this.title	= title;
		this.artist	= artist;
		this.album	= album;
	}
	
	@WebMethod
	public String getTitle() {
		return title;
	}

	@WebMethod
	public String getArtist() {
		return artist;
	}

	@WebMethod
	public String getAlbum() {
		return album;
	}

	@WebMethod
	public void setTitle(String title) {
		this.title = title;
	}

	@WebMethod
	public void setArtist(String artist) {
		this.artist = artist;
	}

	@WebMethod
	public void setAlbum(String album) {
		this.album = album;
	}

	/**
	 * The equals implementation so that two {@link Account} with the same
	 * bankName won't be exported twice.
	 */
	public boolean equals(Object that) {
		if (that == null || this.getClass() != that.getClass()) return false;
		return this.title.equals(((MusicDescription)that).title);
	}

	/**
	 * Hashcode implementation consistent with {@link #equals(Object)}.
	 */
	public int hashCode() {
		return title.hashCode();
	}

	/**
	 * This object is injected by the JAX-WS RI, and exposes various
	 * operations to support stateful web services. Consult its javadoc
	 * for more information.
	 */
	public static StatefulWebServiceManager<MusicDescription> manager;
}
