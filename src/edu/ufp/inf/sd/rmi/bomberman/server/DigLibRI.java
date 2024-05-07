package edu.ufp.inf.sd.rmi.bomberman.server;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface DigLibRI extends Remote {
    public void print(String msg) throws RemoteException;
}
