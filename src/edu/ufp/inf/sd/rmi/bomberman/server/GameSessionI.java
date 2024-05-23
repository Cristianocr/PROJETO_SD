package edu.ufp.inf.sd.rmi.bomberman.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameSessionI extends Remote {
    void logout() throws RemoteException;

    String getToken() throws RemoteException;

    void setToken(String token) throws RemoteException;
}
