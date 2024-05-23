package edu.ufp.inf.sd.rmi.bomberman.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BombermanServer {
    public static void main(String[] args) {
        try {
            GameFactoryImpl server = new GameFactoryImpl();
            Registry registry = LocateRegistry.createRegistry(1098);
            registry.rebind("BombermanServer", server);
            System.out.println("Servidor RMI Bomberman iniciado.");
        } catch (Exception e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
