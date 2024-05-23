package edu.ufp.inf.sd.rmi.bomberman.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameSessionImpl extends UnicastRemoteObject implements GameSessionI {

    private GameFactoryI factory;
    private GameFactoryImpl gameFactory;
    private String token; // Add this field

    public GameSessionImpl(GameFactoryI factoryRI) throws RemoteException {
        super();
        factory = factoryRI;
    }

    @Override
    public void logout() throws RemoteException {
        System.out.println("Logout!");
        if (factory != null) {
            GameSessionI sessionRI = factory.getSessionRI();
            if (factory instanceof GameFactoryImpl)
                gameFactory = (GameFactoryImpl) factory;
            if (sessionRI != null) {
                if (gameFactory != null) {
                    gameFactory.logoutByProxy(sessionRI);
                } else {
                    System.err.println("Error: factoryMethodsProxy is null");
                }
            } else {
                System.err.println("Error: sessionRI is null");
            }
        } else {
            System.err.println("Error: factory is null");
        }
    }

    public String getToken() throws RemoteException {
        return token;
    }

    public void setToken(String token) throws RemoteException {
        this.token = token;
    }
}
