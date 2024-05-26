package edu.ufp.inf.sd.rmi.server;

import edu.ufp.inf.sd.rmi.client.ObserverRI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface GameSessionI extends Remote {
    void logout() throws RemoteException;

    BombermanGame createGame(Integer playerNumber, ObserverRI observerRI) throws RemoteException;

    BombermanGame joinGame(int id, ObserverRI observerRI) throws RemoteException;

    ArrayList<BombermanGame> printBombermanGameList() throws RemoteException;

    ArrayList<BombermanGame> getBombermanGameList() throws RemoteException;

    String getToken() throws RemoteException;

    void setToken(String token) throws RemoteException;

    ArrayList<BombermanGame> getAvailableGames() throws RemoteException;

    ArrayList<BombermanGame> getActiveGames() throws RemoteException;
}
