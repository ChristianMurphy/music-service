package cst420.assign4.udserver;

//proxy to jax ws server
import cst420.assign4.client.factory.*;
import cst420.assign4.client.library.*;

import java.util.*;

class WebServiceFunctions {
	private LibraryFactory	libraryFactory;
	private LibraryService	libraryService;
	private Library 		library;

	public WebServiceFunctions() {
		libraryFactory = new LibraryFactoryService().getLibraryFactoryPort();
		libraryService = new LibraryService();
        library = libraryService.getPort(libraryFactory.getALibrary(), Library.class);
	}

	public void addSong(String title, String artist, String album) {
		library.putMusicDescription(title, artist, album);
	}

	public String[] getMusicList() {
		List <String> list = library.getMusicList();
		return list.toArray(new String[list.size()]);
	}
}