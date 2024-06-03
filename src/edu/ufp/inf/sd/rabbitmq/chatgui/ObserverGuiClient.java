/**
 * <p>
 * Title: Projecto SD</p>
 * <p>
 * Description: Projecto apoio aulas SD</p>
 * <p>
 * Copyright: Copyright (c) 2011</p>
 * <p>
 * Company: UFP </p>
 *
 * @author Rui Moreira
 * @version 2.0
 */
package edu.ufp.inf.sd.rabbitmq.chatgui;

import com.rabbitmq.client.BuiltinExchangeType;
import edu.ufp.inf.sd.rabbitmq.util.RabbitUtils;
import edu.ufp.inf.sd.rabbitmq.client.Client;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author rjm
 */
public class ObserverGuiClient extends JFrame {

    private Observer observer;
    Client c;

    /**
     * Creates new form ChatClientFrame
     *
     * @param args
     */
    public ObserverGuiClient(String args[]) {
        try {
            //1. Init the GUI components
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, " After initComponents()...");

            RabbitUtils.printArgs(args);

            //Read args passed via shell command
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            String exchangeName = args[2];
            //String room=args[3];
            //String user=args[3];
            int playerID = Integer.parseInt(args[3]);
            //String general=args[5];
            this.observer = new Observer(this);

            this.observer.setPlayerID(playerID);
            //this.f = new Main(this.observer, playerID);


            //2. Create the _05_observer object that manages send/receive of messages to/from rabbitmq
            this.observer = new Observer(this, host, port, "guest", "guest", exchangeName, BuiltinExchangeType.FANOUT, "UTF-8", playerID, c);
            this.observer.setClient(new Client(observer, playerID));
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, " After initObserver()...");
            boolean isWaitingPlayers = true;
            while (isWaitingPlayers) {
                TimeUnit.MILLISECONDS.sleep(1);
                //System.out.println("Number of players that are ready to play: " + this.observer.getCount());
                if (this.observer.getCount() == 2) {
                    isWaitingPlayers = false;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    //================================================ BEGIN TO CHANGE ================================================

    /**
     * Sends msg through the _05_observer to the exchange where all observers are binded
     *
     * @param msgToSend
     */
    private void sendMsg(String user, String msgToSend) {
        try {
            msgToSend = "[" + user + "]: " + msgToSend;
            this.observer.sendMessage(msgToSend);
        } catch (IOException ex) {
            Logger.getLogger(ObserverGuiClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //================================================ END TO CHANGE ================================================


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException, TimeoutException {
        new ObserverGuiClient(args);
    }
}
