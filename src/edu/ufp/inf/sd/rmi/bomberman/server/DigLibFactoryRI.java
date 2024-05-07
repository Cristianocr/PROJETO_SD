package edu.ufp.inf.sd.rmi.bomberman.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DigLibFactoryRI extends Remote {
    void register(String username, String pwd) throws RemoteException;

    DigLibSessionRI login(String username, String pwd) throws RemoteException;

    void removeUserSession(String user) throws RemoteException;
}
