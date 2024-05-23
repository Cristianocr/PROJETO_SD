package edu.ufp.inf.sd.rmi.bomberman.server;

import edu.ufp.inf.sd.rmi.bomberman.client.ObserverRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class SubjectImpl extends UnicastRemoteObject implements SubjectRI {

    private State subjectState;

    private final ArrayList<ObserverRI> observers = new ArrayList<ObserverRI>();

    public SubjectImpl() throws RemoteException {
        super();
        subjectState = new State("", "");

    }

    public void notifyAllObservers() throws RemoteException {
        for(ObserverRI obs : observers){
            obs.update();
        }
    }

    @Override
    public void attach(ObserverRI obsRI) throws RemoteException {
        observers.add(obsRI);
    }

    @Override
    public void detach(ObserverRI obsRI) throws RemoteException{
        observers.remove(obsRI);
    }

    @Override
    public State getState() throws RemoteException{
        return subjectState;
    }

    @Override
    public void setState(State state) throws RemoteException{
        this.subjectState = state;
        notifyAllObservers();
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
