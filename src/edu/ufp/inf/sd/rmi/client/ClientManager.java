package edu.ufp.inf.sd.rmi.client;

import edu.ufp.inf.sd.rmi.client.ObserverRI;
import edu.ufp.inf.sd.rmi.server.BombermanGame;
import edu.ufp.inf.sd.rmi.server.State;
import edu.ufp.inf.sd.rmi.server.SubjectRI;
import edu.ufp.inf.sd.rmi.server.BombermanGame;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


//a cada cliente que entra no servidor, uma nova thread é instanciada para tratá-lo
class ClientManager extends Thread {
    static List<ObserverRI> listObsClients = new ArrayList<ObserverRI>();

    private BombermanGame bg;
    private ObserverRI observerRI;
    private Socket clientSocket = null;
    private Scanner in = null;
    private PrintStream out = null;
    public int id;

    private Client client;
    private CoordinatesThrower ct;
    private MapUpdatesThrower mt;

    private PlayerData player[];
    private Coordinate map[][];


    public void sendToAllClients(int id, String msg) throws RemoteException {
        client.observer.getSubjectRI().setState(new edu.ufp.inf.sd.rmi.server.State(id, msg));
    }

    public ClientManager(Client client, BombermanGame bg, ObserverRI observerRI) throws RemoteException {
        this.id = observerRI.getId();
        this.observerRI = observerRI;
        this.bg = bg;

        this.client = client;
        player = client.getPlayer();
        map = client.getMap();


        (ct = new CoordinatesThrower(this,id)).start();
        (mt = new MapUpdatesThrower(this,id)).start();


        listObsClients.add(observerRI);
        player[id].logged = true;
        player[id].alive = true;
        //sendInitialSettings(); // envia uma única string

        sendToAllClients(id, "playerJoined");
    }


    public void run() {
        String str;

        while (true) {
            try {
                str = observerRI.getSubjectRI().getState().getInfo();
                System.out.println("msg observer: " + str);
                id = observerRI.getSubjectRI().getState().getId();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

            String[] strMessage = str.split(" ");
            String message = strMessage[0];


            if (message.equals("keyCodePressed") && player[id].alive) {
                try {
                    ct.keyCodePressed(Integer.parseInt(strMessage[1]));
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else if (message.equals("keyCodeReleased") && player[id].alive) {
                try {
                    ct.keyCodeReleased(Integer.parseInt(strMessage[1]));
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else if (message.equals("pressedSpace") && player[id].numberOfBombs >= 1) {
                player[id].numberOfBombs--;
                mt.setBombPlanted(Integer.parseInt(strMessage[1]), Integer.parseInt(strMessage[2]));
            }
        }
    }

    void sendInitialSettings() {
        out.print(id);
        for (int i = 0; i < Const.LIN; i++)
            for (int j = 0; j < Const.COL; j++)
                out.print(" " + map[i][j].img);

        for (int i = 0; i < Const.QTY_PLAYERS; i++)
            out.print(" " + player[i].alive);

        for (int i = 0; i < Const.QTY_PLAYERS; i++)
            out.print(" " + player[i].x + " " + player[i].y);
        out.print("\n");
    }

    void clientDesconnected() throws RemoteException {
        listObsClients.remove(observerRI);
        player[id].logged = false;
    }

    public BombermanGame getBg() {
        return bg;
    }

    public void setBg(BombermanGame bg) {
        this.bg = bg;
    }

    public ObserverRI getObserverRI() {
        return observerRI;
    }

    public void setObserverRI(ObserverRI observerRI) {
        this.observerRI = observerRI;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public Scanner getIn() {
        return in;
    }

    public void setIn(Scanner in) {
        this.in = in;
    }

    public PrintStream getOut() {
        return out;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public CoordinatesThrower getCt() {
        return ct;
    }

    public void setCt(CoordinatesThrower ct) {
        this.ct = ct;
    }

    public MapUpdatesThrower getMt() {
        return mt;
    }

    public void setMt(MapUpdatesThrower mt) {
        this.mt = mt;
    }
}