package edu.inf.ufp.sd.rmi._05_observer.client;

import edu.ufp.inf.sd.rmi._05_observer.server.State;
import edu.ufp.inf.sd.rmi._05_observer.server.SubjectRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ObserverImpl extends UnicastRemoteObject implements ObserverRI {

    private String id;
    private State lastObserverState;

    protected SubjectRI subjectRI;

    protected ObserverGuiClient chatFrame;

    public ObserverImpl(String id, ObserverGuiClient f, SubjectRI subjectRI) throws RemoteException {
        super();
        this.id = id;
        this.chatFrame = f;
        this.subjectRI = subjectRI;
        this.subjectRI.attach(this);

    }

    @Override
    public void update() throws RemoteException {
        this.lastObserverState =this.subjectRI.getState();
        this.chatFrame.updateTextArea();
    }

    public State getLastObserverState() {
        return lastObserverState;
    }
}
