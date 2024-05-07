package edu.inf.ufp.sd.rmi._03_pingpong.client;

import edu.ufp.inf.sd.rmi._03_pingpong.server.Ball;
import edu.ufp.inf.sd.rmi._03_pingpong.server.PingRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PongImpl extends UnicastRemoteObject implements PongRI {

    PingRI pingRI;
    public PongImpl(PingRI pingRI) throws RemoteException {
        super();
        pingRI.ping(new Ball(1),this);
        this.pingRI=pingRI;
    }

    @Override
    public void pong(Ball bola) throws RemoteException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Pong\n");
        pingRI.ping(bola,this);
    }
}