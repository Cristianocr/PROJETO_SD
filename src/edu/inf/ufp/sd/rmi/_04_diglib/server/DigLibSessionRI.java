package edu.inf.ufp.sd.rmi._04_diglib.server;

import java.rmi.RemoteException;

public interface DigLibSessionRI {

    public Book[] search(String title, String Author);

    public void logout() throws RemoteException;
}
