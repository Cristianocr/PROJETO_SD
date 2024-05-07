package edu.inf.ufp.sd.rmi.bomberman.server;

import java.rmi.RemoteException;

public interface DigLibSessionRI {

    public Book[] search(String title, String Author);

    public void logout() throws RemoteException;
}
