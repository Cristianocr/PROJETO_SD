package edu.ufp.inf.sd.rmi.client;

import edu.ufp.inf.sd.rmi.client.ObserverRI;
import edu.ufp.inf.sd.rmi.server.BombermanGame;
import edu.ufp.inf.sd.rmi.server.State;
import edu.ufp.inf.sd.rmi.server.SubjectRI;
import edu.ufp.inf.sd.rmi.client.Game;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.Stack;


public class Client {
    public int id;
    public ObserverRI observer;
    public BombermanGame bg;
    public SubjectRI subjectRI;

    private Game game;
    private Client client;


    final static int rateStatusUpdate = 115;
    private PlayerData player[] = new PlayerData[Const.QTY_PLAYERS];
    private Coordinate map[][] = new Coordinate[Const.LIN][Const.COL];
    private Coordinate spawn[] = new Coordinate[Const.QTY_PLAYERS];
    private boolean alive[] = new boolean[Const.QTY_PLAYERS];

    public Client(BombermanGame bg, ObserverRI observer) throws RemoteException {
        this.client = this;
        this.bg = bg;
        this.observer = observer;
        this.subjectRI = bg.getSubjectRI();
        id = observer.getId();
        System.out.println("Observer id: " + id);

        setMap();
        setPlayerData();
        receiveInitialSettings();
        new Window();
        ClientManager clientManager = new ClientManager(this, this.bg, this.observer);
        clientManager.start();  // This will start the run() method in a new thread
        new Receiver().start();
    }

    void setMap() {
        for (int i = 0; i < Const.LIN; i++)
            for (int j = 0; j < Const.COL; j++)
                map[i][j] = new Coordinate(Const.SIZE_SPRITE_MAP * j, Const.SIZE_SPRITE_MAP * i, "block");

        // paredes fixas das bordas
        for (int j = 1; j < Const.COL - 1; j++) {
            map[0][j].img = "wall-center";
            map[Const.LIN - 1][j].img = "wall-center";
        }
        for (int i = 1; i < Const.LIN - 1; i++) {
            map[i][0].img = "wall-center";
            map[i][Const.COL - 1].img = "wall-center";
        }
        map[0][0].img = "wall-up-left";
        map[0][Const.COL - 1].img = "wall-up-right";
        map[Const.LIN - 1][0].img = "wall-down-left";
        map[Const.LIN - 1][Const.COL - 1].img = "wall-down-right";

        // paredes fixas centrais
        for (int i = 2; i < Const.LIN - 2; i++)
            for (int j = 2; j < Const.COL - 2; j++)
                if (i % 2 == 0 && j % 2 == 0)
                    map[i][j].img = "wall-center";

        // arredores do spawn
        map[1][1].img = "floor-1";
        map[1][2].img = "floor-1";
        map[2][1].img = "floor-1";
        map[Const.LIN - 2][Const.COL - 2].img = "floor-1";
        map[Const.LIN - 3][Const.COL - 2].img = "floor-1";
        map[Const.LIN - 2][Const.COL - 3].img = "floor-1";
        map[Const.LIN - 2][1].img = "floor-1";
        map[Const.LIN - 3][1].img = "floor-1";
        map[Const.LIN - 2][2].img = "floor-1";
        map[1][Const.COL - 2].img = "floor-1";
        map[2][Const.COL - 2].img = "floor-1";
        map[1][Const.COL - 3].img = "floor-1";
    }

    void setPlayerData() {
        player[0] = new PlayerData(
                map[1][1].x - Const.VAR_X_SPRITES,
                map[1][1].y - Const.VAR_Y_SPRITES
        );

        player[1] = new PlayerData(
                map[Const.LIN - 2][Const.COL - 2].x - Const.VAR_X_SPRITES,
                map[Const.LIN - 2][Const.COL - 2].y - Const.VAR_Y_SPRITES
        );
        player[2] = new PlayerData(
                map[Const.LIN - 2][1].x - Const.VAR_X_SPRITES,
                map[Const.LIN - 2][1].y - Const.VAR_Y_SPRITES
        );
        player[3] = new PlayerData(
                map[1][Const.COL - 2].x - Const.VAR_X_SPRITES,
                map[1][Const.COL - 2].y - Const.VAR_Y_SPRITES
        );
    }

    void receiveInitialSettings() throws RemoteException {

        //mapa
      /*for (int i = 0; i < Const.LIN; i++)
         for (int j = 0; j < Const.COL; j++)
            map[i][j] = new Coordinate(Const.SIZE_SPRITE_MAP * j, Const.SIZE_SPRITE_MAP * i, in.next());
      */
        //situação (vivo ou morto) inicial de todos os jogadores
        for (int i = 0; i < subjectRI.getObservers().size(); i++) {
            alive[i] = true;
        }
        //coordenadas inicias de todos os jogadores
        for (int i = 0; i < bg.getPlayerNumber(); i++) {
            spawn[i] = new Coordinate(player[i].x, player[i].y);
        }
    }

    /*void sendInitialSettings() {

        for (int i = 0; i < Const.LIN; i++)
            for (int j = 0; j < Const.COL; j++)
                out.print(" " + Client.map[i][j].img);

        for (int i = 0; i < Const.QTY_PLAYERS; i++)
            out.print(" " + Client.player[i].alive);

        for (int i = 0; i < Const.QTY_PLAYERS; i++)
            out.print(" " + Client.player[i].x + " " + Client.player[i].y);
        out.print("\n");
    }*/

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public PlayerData[] getPlayer() {
        return player;
    }

    public void setPlayer(PlayerData[] player) {
        this.player = player;
    }

    public Coordinate[][] getMap() {
        return map;
    }

    public void getMap(Coordinate[][] map) {
        this.map = map;
    }


    public Coordinate[] getSpawn() {
        return spawn;
    }

    public void setSpawn(Coordinate[] spawn) {
        this.spawn = spawn;
    }

    public boolean[] getAlive() {
        return alive;
    }

    public void setAlive(boolean[] alive) {
        this.alive = alive;
    }


    //escuta enquanto a janela (JFrame) estiver em foco
    public class Sender extends KeyAdapter {
        int lastKeyCodePressed;

        public void keyPressed(KeyEvent e) {
            String keyState = null;
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                keyState = "pressedSpace " + game.getYou().x + " " + game.getYou().y;
            } else if (isNewKeyCode(e.getKeyCode())) {
                keyState = "keyCodePressed " + e.getKeyCode();
            }
            if (keyState != null) {
                try {
                    System.out.println("teclas pressionadas no Sender: " + keyState);
                    State state = new State(observer.getId(), keyState);
                    subjectRI.setState(state);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        public void keyReleased(KeyEvent e) {
            String keyState = "keyCodeReleased " + e.getKeyCode();

            try {
                System.out.println("teclas released no Sender: " + keyState);

                State state = new State(observer.getId(), keyState);
                subjectRI.setState(state);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            lastKeyCodePressed = -1; //a próxima tecla sempre será nova
        }

        boolean isNewKeyCode(int keyCode) {
            boolean ok = (keyCode != lastKeyCodePressed);
            lastKeyCodePressed = keyCode;
            return ok;
        }
    }

    public class Receiver extends Thread {
        Player p;

        Player fromWhichPlayerIs(int id) {
            if (id == client.id)
                return game.getYou();
            else if (id == (client.id + 1) % Const.QTY_PLAYERS)
                return game.getEnemy1();
            else if (id == (client.id + 2) % Const.QTY_PLAYERS)
                return game.getEnemy2();
            else if (id == (client.id + 3) % Const.QTY_PLAYERS)
                return game.getEnemy3();
            return null;
        }

        public void run() {
            while (true) {
                String str;
                try {
                    str = subjectRI.getState().getInfo();
                    id = subjectRI.getState().getId();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

                this.p = fromWhichPlayerIs(id);
                String[] strMessage = str.split(" ");
                String message = strMessage[0];

                if (message.equals("mapUpdate")) { //p null
                    game.setSpriteMap(strMessage[1], Integer.parseInt(strMessage[2]), Integer.parseInt(strMessage[3]));
                    game.getYou().panel.repaint();
                } else if (message.equals("newCoordinate")) {
                    p.x = Integer.parseInt(strMessage[1]);
                    p.y = Integer.parseInt(strMessage[2]);
                    game.getYou().panel.repaint();
                } else if (message.equals("newStatus")) {
                    p.sc.setLoopStatus(strMessage[1]);
                } else if (message.equals("stopStatusUpdate")) {
                    p.sc.stopLoopStatus();
                } else if (message.equals("playerJoined")) {
                    p.alive = true;
                }
            }
        }
    }

    public class Window extends JFrame {
        private static final long serialVersionUID = 1L;

        public Window() {
            Sprite.loadImages();
            Sprite.setMaxLoopStatus();

            game = new Game(client, Const.COL * Const.SIZE_SPRITE_MAP, Const.LIN * Const.SIZE_SPRITE_MAP);
            add(game);
            setTitle("bomberman");
            pack();
            setVisible(true);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            addKeyListener(new Sender());
        }
    }

}


