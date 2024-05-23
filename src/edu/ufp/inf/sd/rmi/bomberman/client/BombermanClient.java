package edu.ufp.inf.sd.rmi.bomberman.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import edu.ufp.inf.sd.rmi.bomberman.server.GameFactoryI;
import edu.ufp.inf.sd.rmi.bomberman.server.GameSessionI;

import javax.swing.*;

public class BombermanClient {

    private static String token;

    public static void main(String[] args) {
        while (true) {
            String[] options = {"Login", "Register"};
            int choice = JOptionPane.showOptionDialog(null, "Escolha uma opção:", "Login/Register", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            String username = JOptionPane.showInputDialog(null, "Digite seu nome de usuário:");
            String password = JOptionPane.showInputDialog(null, "Digite sua senha:");

            try {
                Registry registry = LocateRegistry.getRegistry("localhost", 1098);
                GameFactoryI server = (GameFactoryI) registry.lookup("BombermanServer");

                if (choice == 1) {
                    boolean registerSuccess = server.register(username, password);
                    if (registerSuccess) {
                        JOptionPane.showMessageDialog(null, "Registro bem-sucedido! Por favor, faça login.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Registro falhou. Nome de usuário já existe.");
                        continue;
                    }
                }

                GameSessionI gameSession = server.login(username, password);

                if (gameSession != null) {
                    JOptionPane.showMessageDialog(null, "Login bem-sucedido!");
                    token = gameSession.getToken(); // Get the JWT token
                    System.out.println("Token: " + token);
                    startGameSession(gameSession);
                    break;
                } else {
                    int retryChoice = JOptionPane.showOptionDialog(null, "Login falhou. Deseja tentar novamente ou registrar-se?", "Falha no Login",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Tentar Novamente", "Registrar", "Cancelar"}, null);

                    if (retryChoice == JOptionPane.CANCEL_OPTION) {
                        break;
                    } else if (retryChoice == JOptionPane.NO_OPTION) {
                        choice = 1;
                    }
                }

            } catch (Exception e) {
                System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void startGameSession(GameSessionI gameSession) {
        try {
            System.out.println("Sessão de jogo iniciada.");

            // Utilize the token for authenticated requests here

            gameSession.logout();
            System.out.println("Logout bem-sucedido!");

        } catch (Exception e) {
            System.err.println("Erro durante a sessão de jogo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
