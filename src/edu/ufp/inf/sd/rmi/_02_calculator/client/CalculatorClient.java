package edu.ufp.inf.sd.rmi._02_calculator.client;

import edu.ufp.inf.sd.rmi._02_calculator.server.CalculatorRI;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.util.ArrayList;
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
public class CalculatorClient {

    /**
     * Context for connecting a RMI client MAIL_TO_ADDR a RMI Servant
     */
    private SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold the Servant proxy
     */
    private CalculatorRI calculatorRI;

    public static void main(String[] args) {
        if (args != null && args.length < 2) {
            System.err.println("usage: java [options] edu.ufp.sd.inf.rmi._02_calculator.server.CalculatorServer <rmi_registry_ip> <rmi_registry_port> <service_name>");
            System.exit(-1);
        } else {
            //1. ============ Setup client RMI context ============
            CalculatorClient hwc=new CalculatorClient(args);
            //2. ============ Lookup service ============
            hwc.lookupService();
            //3. ============ Play with service ============
            hwc.playService();
        }
    }

    public CalculatorClient(String args[]) {
        try {
            //List ans set args
            SetupContextRMI.printArgs(this.getClass().getName(), args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            //Create a context for RMI setup
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(CalculatorClient.class.getName()).log(Level.SEVERE, null, e);
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
                
                //============ Get proxy MAIL_TO_ADDR Calculator service ============
                calculatorRI = (CalculatorRI) registry.lookup(serviceUrl);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return calculatorRI;
    }
    
    private void playService() {
        try {
            //============ Call Calculator remote service ============
            double soma=0, sum=0, sub=0, div=0, mult=0, fact =0 ;
            int dez =10;

            for(int i =0; i<dez; i++){
                soma = this.calculatorRI.add(i, i*i);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Soma = " + soma + "\n");
            }

            ArrayList<Double> list = new ArrayList<Double>();
            for(int i=1; i<=dez; i++){
                list.add((double) i);
            }
            sum = this.calculatorRI.add(list);
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Soma Lista = " + sum + "\n");


            for(int i =0; i<dez; i++){
                sub = this.calculatorRI.sub(i*i, i*2);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Substracao = " + sub + "\n");
            }

            for(int i =0 ; i<dez; i++){
                div = this.calculatorRI.div(i,3.0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Divisao = " + div + "\n");
            }

            for(int i =0 ; i<=dez; i++){
                mult = this.calculatorRI.mult(3.0, i);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Multiplicacao = " + mult + "\n");
            }

            fact = this.calculatorRI.fact(4);
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Fatorial = " + fact + "\n");



        } catch (RemoteException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
