package edu.ufp.inf.sd.rmi.bomberman.server;

import java.rmi.RemoteException;

public interface DigLibSessionRI {

    public Book[] search(String title, String Author);

    public void logout() throws RemoteException;
}
