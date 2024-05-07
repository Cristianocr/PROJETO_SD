package edu.ufp.inf.sd.rmi.bomberman.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class DigLibFactoryImpl extends UnicastRemoteObject implements DigLibFactoryRI {

    private final Map<User, DigLibSessionRI> session;
    private final DBMockup db = new DBMockup();

    protected DigLibFactoryImpl(Map<User, DigLibSessionRI> session) throws RemoteException {
        super();
        this.session = session;
    }

    @Override
    public void register(String username, String pwd) throws RemoteException {
        if(!db.exists(username, pwd)){
            db.register(username, pwd);
        }
    }

    @Override
    public DigLibSessionRI login(String username, String pwd) throws RemoteException {
        if(db.exists(username, pwd)){
            User user = new User(username, pwd);
            DigLibSessionRI hasSession = session.get(user);
            if(hasSession == null){
                try{
                    hasSession = new DigLibSessionImpl(username,null);
                    session.put(user, hasSession);
                }catch(RemoteException e){
                    e.printStackTrace();
                }
            }
            return hasSession;
        }
        return null;
    }

    @Override
    public void removeUserSession(String user) {
        session.remove(user);
    }
}

