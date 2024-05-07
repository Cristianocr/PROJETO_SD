package edu.inf.ufp.sd.rmi.bomberman.client;

import edu.ufp.inf.sd.rmi._04_diglib.server.DigLibFactoryImpl;
import edu.ufp.inf.sd.rmi._04_diglib.server.DigLibFactoryRI;
import edu.ufp.inf.sd.rmi._04_diglib.server.DigLibRI;
import edu.ufp.inf.sd.rmi._04_diglib.server.User;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Title: Projecto SD</p>
 * <p>
 * Description: Projecto apoio aulas SD</p>
 * <p>
 * Copyright: Copyright (c) 2017</p>
 * <p>
 * Company: UFP </p>
 *
 * @author Rui S. Moreira
 * @version 3.0
 */
public class DigLibClient {

    /**
     * Context for connecting a RMI client MAIL_TO_ADDR a RMI Servant
     */
    private SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold the Servant proxy
     */
    private DigLibFactoryRI digLibFactoryRI;

    public static void main(String[] args) {
        if (args != null && args.length < 2) {
            System.err.println("usage: java [options] edu.ufp.sd.inf.rmi._04_diglib.server.DigLibClient <rmi_registry_ip> <rmi_registry_port> <service_name>");
            System.exit(-1);
        } else {
            //1. ============ Setup client RMI context ============
            DigLibClient hwc = new DigLibClient(args);
            //2. ============ Lookup service ============
            hwc.lookupService();
            //3. ============ Play with service ============
            hwc.playService();
        }
    }

    public DigLibClient(String args[]) {
        try {
            //List ans set args
            SetupContextRMI.printArgs(this.getClass().getName(), args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            //Create a context for RMI setup
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(DigLibClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Remote lookupService() {
        try {
            //Get proxy MAIL_TO_ADDR rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Lookup service on rmiregistry and wait for calls
            if (registry != null) {
                //Get service url (including servicename)
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going MAIL_TO_ADDR lookup service @ {0}", serviceUrl);

                //============ Get proxy MAIL_TO_ADDR HelloWorld service ============
                digLibFactoryRI = (DigLibFactoryRI) registry.lookup(serviceUrl);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return digLibFactoryRI;
    }

    private void playService() {
        try {
            Scanner scanner = new Scanner(System.in);
            for (; ; ) {
                System.out.print("\nDigite o nome de usuário: ");
                String username = scanner.nextLine();

                System.out.print("\nDigite a senha: ");
                String password = scanner.nextLine();

                if (this.digLibFactoryRI.login(username, password) != null) {
                    System.out.print("\nUtilizador logado com sucesso!\n");
                    break;
                } else {
                    System.out.print("\nUtilizador inexistente\n");
                    System.out.print("Deseja registar este utilizador?(S|N)\n");

                    if (scanner.nextLine().equals("s") || scanner.nextLine().equals("S")) {
                        this.digLibFactoryRI.register(username, password);
                        this.digLibFactoryRI.login(username, password);
                        System.out.print("\nUtilizador criado e logado com sucesso!\n");
                        break;
                    }
                }

            }
            scanner.close();

            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going MAIL_TO_ADDR finish, bye. ;)");
        } catch (RemoteException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

}
