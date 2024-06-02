package edu.ufp.inf.sd.rmi.server;

import edu.ufp.inf.sd.rmi.client.ObserverRI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface SubjectRI extends Remote {

    public void attach(ObserverRI obsRI) throws RemoteException;

    public void detach(ObserverRI obsRI) throws RemoteException;

    public State getState() throws RemoteException;

    public void setState(State state) throws RemoteException;

    ArrayList<ObserverRI> getObservers() throws RemoteException;

    public void notifyAllObservers(State state) throws RemoteException;

    public boolean isFull() throws RemoteException;

    public void setFull(boolean full) throws RemoteException;
}
