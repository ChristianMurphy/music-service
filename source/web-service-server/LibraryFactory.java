package cst420.assign3.wsserver.factory;

import cst420.assign3.wsserver.library.*;

import com.sun.xml.ws.developer.StatefulWebServiceManager;
import javax.jws.WebService;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

/**
 * Web Service to demonstrate statelful using a banking application.
 * In this application, accounts can be created by the Banking class.
 * The accounts created are remote in that when an account is returned
 * to a client, a reference to the remote account is returned, not a
 * serialized replication of the account. All subsequent interactions with
 * the account are remote calls which modify the remote account object.
 * @author Tim Lindquist Dept of Engineering and Computing Systems; ASU Poly
 * @author Christian Murphy
 * @version November, 2013
 **/
@WebService
public class LibraryFactory {
	private Library library = new Library();
    /**
     * Web method to create a library instance and return it to the client
     */
    public W3CEndpointReference getALibrary() {
       return Library.manager.export(library);
    }
}
