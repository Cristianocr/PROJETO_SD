package edu.ufp.inf.sd.rmi.server;

import edu.ufp.inf.sd.rmi.rmisetup.SetupContextRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BombermanServer {
    /**
     * Context for running a RMI Servant on a SMTP_HOST_ADDR
     */
    private SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold reference MAIL_TO_ADDR the Servant impl
     */
    private GameFactoryI gameFactoryI;
    private SubjectRI subjectRI;


    public static void main(String[] args) {
        if (args != null && args.length < 4) {
            System.err.println("usage: java [options] edu.ufp.sd._01_helloworld.server.HelloWorldServer <rmi_registry_ip> <rmi_registry_port> <service_name> <subject_name>");
            System.exit(-1);
        } else {
            //1. ============ Create Servant ============
            BombermanServer hws = new BombermanServer(args);
            //2. ============ Rebind servant on rmiregistry ============
            hws.rebindService();
            System.out.println("Servidor RMI Bomberman iniciado.");
        }
        /*
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    public BombermanServer(String args[]){
        try {
            //============ List and Set args ============
            SetupContextRMI.printArgs(this.getClass().getName(), args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            String serviceName2 = args[3];
            //============ Create a context for RMI setup ============
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName, serviceName2 });
        } catch (RemoteException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void rebindService() {
        try {
            //Get proxy MAIL_TO_ADDR rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Bind service on rmiregistry and wait for calls
            if (registry != null) {
                //============ Create Servant ============
                gameFactoryI = new GameFactoryImpl();
                subjectRI = new SubjectImpl();

                //Get service url (including servicename)
                String gameFactoryUrl = contextRMI.getServicesUrl(0);
                String subjectUrl = contextRMI.getServicesUrl(1);
                //Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going MAIL_TO_ADDR rebind service @ {0}", serviceUrl);

                //============ Rebind servant ============
                //Naming.bind(serviceUrl, helloWorldRI);
                registry.rebind(gameFactoryUrl, gameFactoryI);
                registry.rebind(subjectUrl, subjectRI);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "service bound and running. :)");
            } else {
                //System.out.println("HelloWorldServer - Constructor(): create registry on port 1099");
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
