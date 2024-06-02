package edu.ufp.inf.sd.rmi.server;

import edu.ufp.inf.sd.rmi.client.ObserverRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class SubjectImpl extends UnicastRemoteObject implements SubjectRI {

    private ArrayList<ObserverRI> observers = new ArrayList<ObserverRI>();
    private State subjectState;
    private boolean isFull = false;

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
        if (observers.isEmpty()) {
            observers.add(obsRI);
            obsRI.setId(0);
        } else {
            for (int i = 0; i < observers.size(); i++) {
                if (observers.get(i) == null) {
                    observers.add(obsRI);
                    obsRI.setId(i);
                    break;
                }
            }
            if (!observers.contains(obsRI)) {
                observers.add(obsRI);
                obsRI.setId(observers.size() - 1);
            }
        }

        if (obsRI.getSubjectRI().getObservers().size() >= 2) {
            State state = new State(0, "START");
            obsRI.getSubjectRI().setState(state);
        }
    }

    @Override
    public void detach(ObserverRI obsRI) throws RemoteException {
        for (int i = 0; i < observers.size(); i++) {
            if (observers.get(i).equals(obsRI)) {
                observers.set(i, null);
                break;
            }
        }
    }

    @Override
    public State getState() throws RemoteException {
        return this.subjectState;
    }

    @Override
    public void setState(State state) throws RemoteException {
        this.subjectState = state;
        System.out.println("\n" + state.getInfo() + "\n");
        notifyAllObservers(state);
    }

    public void notifyAllObservers(State state) throws RemoteException {
        for (ObserverRI observer : observers) {
            observer.update(state);
        }
    }

    public int findObserverArrayPosition(ObserverRI observer) throws RemoteException {
        for (int i = 0; i < observers.size(); i++) {
            if (observer.equals(observers.get(i))) {
                return i;
            }
        }
        return -1;
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

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        this.isFull = full;
    }

}
