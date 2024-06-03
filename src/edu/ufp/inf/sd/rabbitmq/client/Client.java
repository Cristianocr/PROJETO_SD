package edu.ufp.inf.sd.rabbitmq.client;

import edu.ufp.inf.sd.rabbitmq.client.Player;
import edu.ufp.inf.sd.rabbitmq.chatgui.Observer;
import edu.ufp.inf.sd.rabbitmq.client.Game;

import javax.swing.*;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.rmi.RemoteException;


public class Client {
    public int id;

    public Observer observer;

    private Game game;
    private Client client;


    final static int rateStatusUpdate = 115;
    private PlayerData player[] = new PlayerData[Const.QTY_PLAYERS];
    private Coordinate map[][] = new Coordinate[Const.LIN][Const.COL];
    private Coordinate spawn[] = new Coordinate[Const.QTY_PLAYERS];
    private boolean alive[] = new boolean[Const.QTY_PLAYERS];

    public Client(Observer observer, int playerID) throws IOException {
        this.client = this;
        this.observer = observer;
        this.id = playerID;
        System.out.println("Observer id: " + id);

        setMap();
        setPlayerData();
        receiveInitialSettings();
        new Window();
        new ClientManager(this, this.observer).start();
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
        for (int i = 0; i < observer.getPlayerNumber(); i++) {
            alive[i] = true;
        }
        //coordenadas inicias de todos os jogadores
        for (int i = 0; i < Const.QTY_PLAYERS; i++) {
            spawn[i] = new Coordinate(player[i].x, player[i].y);
        }
    }

    /*void receiveInitialSettings() throws RemoteException {

        id = observer.getLastObserverState().getId();
        String str = observer.getLastObserverState().getInfo();

        String[] strMessage = str.trim().split(" ");

        int index = 0;


        //mapa
        for (int i = 0; i < Const.LIN; i++) {
            for (int j = 0; j < Const.COL; j++) {
                map[i][j] = new Coordinate(Const.SIZE_SPRITE_MAP * j, Const.SIZE_SPRITE_MAP * i, strMessage[index]);
                index++;
            }
        }

        //situação (vivo ou morto) inicial de todos os jogadores
        for (int i = 0; i < Const.QTY_PLAYERS; i++) {
            alive[i] = Boolean.parseBoolean(strMessage[index]);
            index++;
        }

        //coordenadas inicias de todos os jogadores
        for (int i = 0; i < Const.QTY_PLAYERS; i++) {
            int x = Integer.parseInt(strMessage[index]);
            index++;
            int y = Integer.parseInt(strMessage[index]);
            index++;
            spawn[i] = new Coordinate(x, y);
        }
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
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                try {
                    observer.sendMessage(id + " " + "pressedSpace " + game.getYou().x + " " + game.getYou().y);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else if (isNewKeyCode(e.getKeyCode())) {
                try {
                    observer.sendMessage(id + " " + "keyCodePressed " + e.getKeyCode());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        public void keyReleased(KeyEvent e) {
            try {
                observer.sendMessage(id + " " + "keyCodeReleased " + e.getKeyCode());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            lastKeyCodePressed = -1; //a próxima tecla sempre será nova
        }

        boolean isNewKeyCode(int keyCode) {
            boolean ok = (keyCode != lastKeyCodePressed) ? true : false;
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
            String str, msg;
            while (true) {
                System.out.println("ENTREI NO RUN CLIENT RECEIVER");

                if ((str = observer.getReceivedMessage()) == null) {
                    System.out.println("fsdsdsd");
                    continue;
                }

                System.out.println("ywywywy");
                System.out.println(str);
                String[] strMessage = str.split(" ");

                System.out.println(strMessage[0]);

                this.p = fromWhichPlayerIs(Integer.parseInt(strMessage[0])); //id do cliente
                msg = strMessage[1];

                if (msg.equals("mapUpdate")) { //p null
                    System.out.println(strMessage[2] + " " + strMessage[3] + " " + strMessage[4]);
                    game.setSpriteMap(strMessage[2], Integer.parseInt(strMessage[3]), Integer.parseInt(strMessage[4]));
                    game.getYou().panel.repaint();
                } else if (msg.equals("newCoordinate")) {
                    System.out.println(strMessage[2] + " " + strMessage[3]);

                    p.x = Integer.parseInt(strMessage[2]);
                    p.y = Integer.parseInt(strMessage[3]);
                    game.getYou().panel.repaint();
                } else if (msg.equals("newStatus")) {
                    System.out.println(strMessage[2]);
                    p.sc.setLoopStatus(strMessage[2]);
                } else if (msg.equals("stopStatusUpdate")) {
                    p.sc.stopLoopStatus();
                } else if (msg.equals("playerJoined")) {
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



