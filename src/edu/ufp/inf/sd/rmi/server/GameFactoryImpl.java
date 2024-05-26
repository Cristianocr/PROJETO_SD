package edu.ufp.inf.sd.rmi.server;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;

import io.jsonwebtoken.security.Keys;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameFactoryImpl extends UnicastRemoteObject implements GameFactoryI {

    public DBMockup dbMockup;
    private GameSessionImpl sessionI;
    HashMap<String, GameSessionImpl> activeSessions = new HashMap<>();

    private final String SECRET_KEY = "my_secret_key"; // Use a secure method to store and retrieve this key

    public GameFactoryImpl() throws RemoteException {
        super();
        this.dbMockup = new DBMockup();
    }

    protected GameFactoryImpl(int port) throws RemoteException {
        super(port);
    }

    protected GameFactoryImpl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    @Override
    public boolean register(String username, String password) throws RemoteException {
        if (!dbMockup.exists(username)) {
            dbMockup.register(username, password);
            return true;
        }
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Conta já existente!!\n");
        return false;
    }

    @Override
    public GameSessionI login(String username, String password) throws RemoteException {
        if (dbMockup.exists(username, password)) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Login efetuado com sucesso!");
            if (!activeSessions.containsKey(username)) {
                GameSessionImpl newSession = new GameSessionImpl(this);
                activeSessions.put(username, newSession);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Sessão criada");
            }

            // Gerar um token JWT
            System.out.println("CRIAR TOKEN :)");
            //Key key = Keys.secretKeyFor(SignatureAlgorithm.forName(SECRET_KEY));
            System.out.println("TOKEN CRIADO :)");
            //String jwt = Jwts.builder().setSubject(username).signWith(key).compact();

            // Adicionar o token JWT à sessão
            sessionI = activeSessions.get(username);
            sessionI.setToken(SECRET_KEY);
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Sessão enviada!");
            return sessionI;
        }else{
            return null;
        }
    }

    @Override
    public GameSessionI getSessionRI() throws RemoteException {
        return this.sessionI;
    }

    public void logoutByProxy(GameSessionI sessionToRemove) throws RemoteException {
        for (Map.Entry<String, GameSessionImpl> entry : activeSessions.entrySet()) {
            if (entry.getValue() == sessionToRemove) {
                String name = entry.getKey();
                activeSessions.remove(name);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Bye!");
            }
        }
    }

    public DBMockup getDbMockup() {
        return dbMockup;
    }

}
