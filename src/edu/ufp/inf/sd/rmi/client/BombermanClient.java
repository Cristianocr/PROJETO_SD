package edu.ufp.inf.sd.rmi.client;

import edu.ufp.inf.sd.rmi.rmisetup.SetupContextRMI;
import edu.ufp.inf.sd.rmi.server.BombermanGame;
import edu.ufp.inf.sd.rmi.server.GameFactoryI;
import edu.ufp.inf.sd.rmi.server.GameSessionI;

import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BombermanClient {

    private SetupContextRMI contextRMI;
    private static GameFactoryI gameFactoryRI;

    private static String username;
    private static String password;

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: java [options] BombermanClient <rmi_registry_ip> <rmi_registry_port> <service_name>");
            System.exit(-1);
        } else {
            BombermanClient client = new BombermanClient(args);
            client.lookupService();
            client.playService();
        }
    }

    public BombermanClient(String[] args) {
        try {
            SetupContextRMI.printArgs(this.getClass().getName(), args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(BombermanClient.class.getName()).log(Level.SEVERE, "Error initializing RMI context", e);
        }
    }

    private void lookupService() {
        try {
            Registry registry = contextRMI.getRegistry();
            if (registry != null) {
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Looking up service @ {0}", serviceUrl);
                gameFactoryRI = (GameFactoryI) registry.lookup(serviceUrl);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Registry not bound (check IPs).");
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error looking up service", ex);
        }
    }

    private void playService() {
        try {
            GameSessionI gameSessionI = authenticationMenu();
            gameOptions(gameSessionI);
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Finished, bye.");
        } catch (RemoteException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error during playService", ex);
        }
    }

    private GameSessionI authenticationMenu() throws RemoteException {
        Scanner userInput = new Scanner(System.in);
        while (true) {
            System.out.println("\n1 - Register\n2 - Login");
            System.out.print("Input: ");
            String userChoice = userInput.next();
            switch (userChoice) {
                case "1":
                    registerUser(userInput);
                    break;
                case "2":
                    GameSessionI gs = loginUser(userInput);
                    if (gs == null) {
                        break;
                    }
                    return gs;
                default:
                    System.out.println("Invalid option, please choose again.");
            }
        }
    }

    private void registerUser(Scanner userInput) throws RemoteException {
        System.out.println("\n=====Register=====");
        System.out.print("Username: ");
        username = userInput.next();
        System.out.print("Password: ");
        password = userInput.next();
        gameFactoryRI.register(username, password);
    }

    private GameSessionI loginUser(Scanner userInput) throws RemoteException {
        System.out.println("\n=====Login=====");
        System.out.print("Username: ");
        username = userInput.next();
        System.out.print("Password: ");
        password = userInput.next();
        GameSessionI session = gameFactoryRI.login(username, password);
        if (session == null) {
            System.out.println("Error logging in, please try again.");
            return null;
        }
        return session;
    }

    private void gameOptions(GameSessionI gameSessionRI) throws RemoteException {
        Scanner userInput = new Scanner(System.in);
        while (true) {
            System.out.println("1 - Create Game\n2 - Join Game\n3 - List Games\n4 - Exit");
            System.out.print("Input: ");
            String userChoice = userInput.next();
            switch (userChoice) {
                case "1":
                    gameCreation(gameSessionRI);
                    break;
                case "2":
                    joinGame(gameSessionRI);
                    break;
                case "3":
                    listGames(gameSessionRI, userInput);
                    break;
                case "4":
                    if (confirmExit(userInput)) {
                        System.out.println("\nThank you for playing!!!!!");
                        System.exit(0);
                    }
                    break;
                default:
                    System.out.println("Invalid option, please choose again.\n");
            }
        }
    }

    private void gameCreation(GameSessionI gameSessionRI) throws RemoteException {
        Scanner numero = new Scanner(System.in);
        System.out.print("\nNumber of Players: ");
        int number = numero.nextInt();
        if (number > 4 || number < 2) {
            System.out.println("Invalid number, please choose again.\n");
            gameCreation(gameSessionRI);
        }

        ObserverRI observerRI = new ObserverImpl();

        BombermanGame bg = gameSessionRI.createGame(number, observerRI);


        while (!bg.getSubjectRI().getState().getInfo().equals("START")) {
        }

        new Client(bg, observerRI);

        System.out.println("Game created successfully.");
        return;
    }

    private void joinGame(GameSessionI gameSessionRI) throws RemoteException {
        ArrayList<BombermanGame> games = gameSessionRI.getAvailableGames();

        if (games.isEmpty()) {
            System.out.println("No games available to join.\n");
            return;
        }

        for (BombermanGame game : games) {
            System.out.println("Game ID: " + game.id);
        }

        Scanner userInput = new Scanner(System.in);
        System.out.print("\nChoose a game ID to join: ");
        int choice = userInput.nextInt();

        ObserverRI observerRI = new ObserverImpl();
        BombermanGame bg = gameSessionRI.joinGame(choice, observerRI);

        while (!bg.getSubjectRI().getState().getInfo().equals("START")) {
        }

        new Client(bg, observerRI);

        System.out.println("Joined game successfully.\n");
        return;
    }

    private void listGames(GameSessionI gameSessionRI, Scanner userInput) throws RemoteException {
        System.out.println("All Games) \nAvailable Games) \nActive Games) \nBack)");
        System.out.print("Input: ");
        String userChoice = userInput.next();
        ArrayList<BombermanGame> games;
        switch (userChoice) {
            case "All":
                games = gameSessionRI.getCreatedGames();
                if (games == null || games.isEmpty()) {
                    System.out.println("No games, create one or wait\n");
                    return;
                }
                break;
            case "Available":
                games = gameSessionRI.getAvailableGames();
                if (games == null || games.isEmpty()) {
                    System.out.println("No games available.\n");
                    return;
                }
                break;
            case "Active":
                games = gameSessionRI.getActiveGames();
                if (games == null || games.isEmpty()) {
                    System.out.println("No games active.\n");
                    return;
                }
                break;
            case "Back":
                return;
            default:
                System.out.println("Invalid option, please choose again.");
                return;
        }
        for (BombermanGame game : games) {
            System.out.println("Game ID: " + game.id);
        }
    }

    private boolean confirmExit(Scanner userInput) {
        System.out.print("Are you sure you want to exit (Y|N)? ");
        String confirmation = userInput.next().toUpperCase(Locale.ROOT);
        return confirmation.equals("Y");
    }
}
