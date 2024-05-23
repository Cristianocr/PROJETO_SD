package edu.ufp.inf.sd.rmi.bomberman.server;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class BombermanGame extends UnicastRemoteObject implements BombermanGameI {
    protected BombermanGame() throws RemoteException {
    }

    protected BombermanGame(int port) throws RemoteException {
        super(port);
    }

    protected BombermanGame(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }


}
