package edu.inf.ufp.sd.rmi.bomberman.server;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class DigLibSessionImpl extends UnicastRemoteObject implements DigLibSessionRI {

    private final String user;
    private final DigLibFactoryRI factory;

    private final DBMockup db = new DBMockup();

    protected DigLibSessionImpl(String user, DigLibFactoryRI factory ) throws RemoteException {
        super();
        this.user = user;
        this.factory = factory;
    }

    @Override
    public Book[] search(String title, String Author) {
        return db.select(title, Author);
    }

    @Override
    public void logout() throws RemoteException {
            // Remove o usuário do HashMap do factory
            factory.removeUserSession(user);
        }
}
