package edu.ufp.inf.sd.rmi.bomberman.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameFactoryI extends Remote {
    boolean register(String username, String password) throws RemoteException;

    GameSessionI login(String username, String password) throws RemoteException;

    GameSessionI getSessionRI() throws RemoteException;
}
