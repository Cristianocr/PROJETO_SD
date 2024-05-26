package edu.ufp.inf.sd.rmi.server;

import edu.ufp.inf.sd.rmi.client.ObserverRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class SubjectImpl extends UnicastRemoteObject implements SubjectRI {

    private State subjectState;

    private ArrayList<ObserverRI> observers = new ArrayList<ObserverRI>();

    public SubjectImpl() throws RemoteException {
        super();
        subjectState = new State(0, "");

    }

    protected SubjectImpl(State subjectState) throws RemoteException {
        this.subjectState = subjectState;
        this.observers = new ArrayList<ObserverRI>();
    }


    @Override
    public void attach(ObserverRI obsRI) throws RemoteException {
        this.observers.add(obsRI);
    }

    @Override
    public void detach(ObserverRI obsRI) throws RemoteException {
        this.observers.remove(obsRI);
    }

    @Override
    public State getState() throws RemoteException {
        return this.subjectState;
    }

    @Override
    public void setState(State state) throws RemoteException {
        this.subjectState = state;
        notifyAllObservers(state);
    }

    public void notifyAllObservers(State state) throws RemoteException {
        for (ObserverRI observer : observers) {
            observer.update(state);
        }
    }

    public State getSubjectState() {
        return subjectState;
    }

    public void setSubjectState(State subjectState) {
        this.subjectState = subjectState;
    }

    public ArrayList<ObserverRI> getObservers() {
        return observers;
    }
}
