package edu.ufp.inf.sd.rmi.server;

import edu.ufp.inf.sd.rmi.client.ObserverRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class GameSessionImpl extends UnicastRemoteObject implements GameSessionI {

    private GameFactoryI factory;
    private GameFactoryImpl gameFactory;
    private String token; // Add this field
    private ArrayList<BombermanGame> createdGames;
    private ArrayList<BombermanGame> activeGames;

    public GameSessionImpl(GameFactoryI factoryRI) throws RemoteException {
        super();
        factory = factoryRI;
        createdGames = new ArrayList<>();
        activeGames = new ArrayList<>();
    }
    public GameSessionImpl(GameFactoryImpl gameFactoryImpl) throws RemoteException{
        super();
        this.gameFactory = gameFactoryImpl;
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

    /**
     * Override do metodo de criar game
     * criamos um SubjectRI, inserimos o jogo na dbmockup e damos attach do observer ao jogo
     * @param playerNumber
     * @param observerRI
     * @return
     * @throws RemoteException
     */
    @Override
    public BombermanGame createGame(Integer playerNumber, ObserverRI observerRI) throws RemoteException {
        System.out.println("A criar jogo)\n");
        SubjectRI subjectRI = new SubjectImpl();
        System.out.println("A criar subject)\n");
        BombermanGame bombermanGame = gameFactory.dbMockup.insert(playerNumber,subjectRI);
        System.out.println("A criar bombermanGame)\n");
        bombermanGame.getSubjectRI().attach(observerRI);
        System.out.println("A dar attach)\n");
        return bombermanGame;
    }

    /**
     * Quando o user escolher um jogo para se conectar, dá attach ao observer e retorna o jogo
     * @param id
     * @param observerRI
     * @return
     * @throws RemoteException
     */
    @Override
    public BombermanGame joinGame(int id, ObserverRI observerRI) throws RemoteException {
        BombermanGame bg = gameFactory.dbMockup.selectGame(id);
        bg.getSubjectRI().attach(observerRI);
        return bg;
    }

    @Override
    public ArrayList<BombermanGame> printBombermanGameList() throws RemoteException {
        return this.gameFactory.dbMockup.printJogo();
    }

    public ArrayList<BombermanGame> getBombermanGameList() throws RemoteException {
        return this.gameFactory.dbMockup.jogos;
    }

    public ArrayList<BombermanGame> getCreatedGames() {
        if(this.gameFactory.dbMockup.jogos.isEmpty()){
            return null;
        }
        return this.gameFactory.dbMockup.jogos;
    }

    public ArrayList<BombermanGame> getActiveGames() throws RemoteException {
        if(this.gameFactory.dbMockup.jogos.isEmpty()){
            return null;
        }
        ArrayList<BombermanGame> activeGames = new ArrayList<>();
        for(BombermanGame bg : this.gameFactory.dbMockup.jogos){
            if(bg.isRunning()){
                activeGames.add(bg);
            }
        }
        return activeGames;
    }

    public ArrayList<BombermanGame> getAvailableGames() throws RemoteException {
        if(this.gameFactory.dbMockup.jogos.isEmpty()){
            return null;
        }
        ArrayList<BombermanGame> availableGames = new ArrayList<>();
        for(BombermanGame bg : this.gameFactory.dbMockup.jogos){
            if(!bg.isRunning()){
                availableGames.add(bg);
            }
        }
        return availableGames;
    }

    public String getToken() throws RemoteException {
        return token;
    }

    public void setToken(String token) throws RemoteException {
        this.token = token;
    }
}
