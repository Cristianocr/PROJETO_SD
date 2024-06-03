package edu.ufp.inf.sd.rabbitmq.client;

import edu.ufp.inf.sd.rabbitmq.bomberman.Server;
import edu.ufp.inf.sd.rabbitmq.chatgui.Observer;
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
    static List<Observer> listObsClients = new ArrayList<Observer>();

    private Observer observer;
    public int id;

    private Client client;
    private CoordinatesThrower ct;
    private MapUpdatesThrower mt;

    private PlayerData player[];
    private Coordinate map[][];


    public void sendToAllClients(int id, String msg) throws IOException {
        client.observer.sendMessage(id + " " + msg);
    }

    public ClientManager(Client client, Observer observer) throws IOException {
        this.id = observer.getPlayerID();
        this.observer = observer;

        this.client = client;
        player = client.getPlayer();
        map = client.getMap();


        (ct = new CoordinatesThrower(this, id)).start();
        (mt = new MapUpdatesThrower(this, id)).start();


        listObsClients.add(observer);
        player[id].logged = true;
        player[id].alive = true;
        //sendInitialSettings();

        sendToAllClients(id, "playerJoined");
    }


    public void run() {
        while (true) {
            if ((observer.getReceivedMessage()) == null) {
                System.out.println("fsdsdsd");
                continue;
            } else {

                String str[] = observer.getReceivedMessage().split(" ");

                if (str[0].equals("keyCodePressed") && player[id].alive) {
                    try {
                        ct.keyCodePressed(Integer.parseInt(str[1]));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (str[0].equals("keyCodeReleased") && player[id].alive) {
                    try {
                        ct.keyCodeReleased(Integer.parseInt(str[1]));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (str[0].equals("pressedSpace") && player[id].numberOfBombs >= 1) {
                    player[id].numberOfBombs--;
                    mt.setBombPlanted(Integer.parseInt(str[1]), Integer.parseInt(str[2]));
                }
            }
        }
    }

    /*void sendInitialSettings() throws RemoteException {
        StringBuilder msg = new StringBuilder();
        edu.ufp.inf.sd.rmi.server.State s = new edu.ufp.inf.sd.rmi.server.State(id, msg.toString());
        for (int i = 0; i < Const.LIN; i++) {
            for (int j = 0; j < Const.COL; j++) {
                msg.append(map[i][j].img).append(" ");
            }
        }
        for (int i = 0; i < Const.QTY_PLAYERS; i++) {
            msg.append(String.valueOf(player[i].alive)).append(" ");
        }

        for (int i = 0; i < Const.QTY_PLAYERS; i++) {
            msg.append(player[i].x).append(" ").append(player[i].y).append(" ");
        }
        s.setInfo("\n");
        observer.update(s);
    }*/

    void clientDesconnected() throws RemoteException {
        listObsClients.remove(observer);
        player[id].logged = false;
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