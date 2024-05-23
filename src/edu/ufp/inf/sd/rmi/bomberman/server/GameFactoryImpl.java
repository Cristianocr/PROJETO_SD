package edu.ufp.inf.sd.rmi.bomberman.server;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;

import io.jsonwebtoken.security.Keys;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameFactoryImpl extends UnicastRemoteObject implements GameFactoryI {

    private final DataBase db = DataBase.getInstance();
    private GameSessionI sessionI;
    private final HashMap<String, GameSessionI> activeSessions = new HashMap<>();

    private final String SECRET_KEY = "my_secret_key"; // Use a secure method to store and retrieve this key

    protected GameFactoryImpl() throws RemoteException {
    }

    protected GameFactoryImpl(int port) throws RemoteException {
        super(port);
    }

    protected GameFactoryImpl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    @Override
    public boolean register(String username, String password) throws RemoteException {
        if (!db.exists(username, password)) {
            db.register(username, password);
            return true;
        }
        return false;
    }

    @Override
    public GameSessionI login(String username, String password) throws RemoteException {
        // Existing user
        if (db.exists(username, password)) {
            // Session not created
            if (!activeSessions.containsKey(username)) {
                GameSessionImpl newSession = new GameSessionImpl(this);
                activeSessions.put(username, newSession);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Server here. Session created");
            }

            // Gerar um token JWT
            Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            String jwt = Jwts.builder()
                    .setSubject(username)
                    .signWith(key)
                    .compact();

            // Adicionar o token JWT à sessão
            sessionI = activeSessions.get(username);
            sessionI.setToken(jwt);
            return sessionI;
        }

        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Server here. Session returned!");
        sessionI = activeSessions.get(username);
        return sessionI;
    }

    @Override
    public GameSessionI getSessionRI() throws RemoteException {
        return this.sessionI;
    }

    public void logoutByProxy(GameSessionI sessionToRemove) throws RemoteException {
        for (Map.Entry<String, GameSessionI> entry : activeSessions.entrySet()) {
            if (entry.getValue() == sessionToRemove) {
                String name = entry.getKey();
                activeSessions.remove(name);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Bye!");
            }
        }
    }
}
