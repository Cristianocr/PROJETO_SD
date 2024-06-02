package edu.ufp.inf.sd.rmi.client;

import java.awt.event.KeyEvent;
import java.rmi.RemoteException;

//thread que dispara as coordenadas seguintes aos clientes enquanto W/A/S/D não é solto
public class CoordinatesThrower extends Thread {
    boolean up, right, left, down;
    int id;

    private Client client;
    private ClientManager cm;

    private PlayerData player[];
    private Coordinate map[][];

    public CoordinatesThrower(ClientManager cm, int id) {
        this.id = id;
        up = down = right = left = false;

        this.cm = cm;
        this.client = cm.getClient();

        player = client.getPlayer();
        map = client.getMap();
    }

    public void run() {
        int newX = player[id].x;
        int newY = player[id].y;

        while (true) {
            if (up || down || right || left) {
                if (up) newY = player[id].y - Const.RESIZE;
                else if (down) newY = player[id].y + Const.RESIZE;
                else if (right) newX = player[id].x + Const.RESIZE;
                else if (left) newX = player[id].x - Const.RESIZE;

                try {
                    if (coordinateIsValid(newX, newY)) {
                        System.out.println("Moving to new coordinates: " + newX + ", " + newY);
                        cm.sendToAllClients(id, "newCoordinate " + newX + " " + newY);
                        player[id].x = newX;
                        player[id].y = newY;
                    } else {
                        newX = player[id].x;
                        newY = player[id].y;
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

                try {
                    sleep(Const.RATE_COORDINATES_UPDATE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    int getColumnOfMap(int x) {
        return x / Const.SIZE_SPRITE_MAP;
    }

    int getLineOfMap(int y) {
        return y / Const.SIZE_SPRITE_MAP;
    }

    // encontra sobre quais sprites do mapa o jogador está e verifica se são válidos
    boolean coordinateIsValid(int newX, int newY) throws RemoteException {
        if (!player[id].alive) {
            return false;
        }

        //verifica se o jogador foi para o fogo (coordenada do centro do corpo)
        int xBody = newX + Const.WIDTH_SPRITE_PLAYER / 2;
        int yBody = newY + 2 * Const.HEIGHT_SPRITE_PLAYER / 3;

        if (map[getLineOfMap(yBody)][getColumnOfMap(xBody)].img.contains("explosion")) {
            player[id].alive = false;
            cm.sendToAllClients(id, "newStatus dead");
            return true;
        }

        int x[] = new int[4], y[] = new int[4];
        int c[] = new int[4], l[] = new int[4];


        // EM RELAÇÃO À NOVA COORDENADA

        // 0: ponto do canto superior esquerdo
        x[0] = Const.VAR_X_SPRITES + newX + Const.RESIZE;
        y[0] = Const.VAR_Y_SPRITES + newY + Const.RESIZE;
        // 1: ponto do canto superior direito
        x[1] = Const.VAR_X_SPRITES + newX + Const.SIZE_SPRITE_MAP - 2 * Const.RESIZE;
        y[1] = Const.VAR_Y_SPRITES + newY + Const.RESIZE;
        // 2: ponto do canto inferior esquerdo
        x[2] = Const.VAR_X_SPRITES + newX + Const.RESIZE;
        y[2] = Const.VAR_Y_SPRITES + newY + Const.SIZE_SPRITE_MAP - 2 * Const.RESIZE;
        // 3: ponto do canto inferior direito
        x[3] = Const.VAR_X_SPRITES + newX + Const.SIZE_SPRITE_MAP - 2 * Const.RESIZE;
        y[3] = Const.VAR_Y_SPRITES + newY + Const.SIZE_SPRITE_MAP - 2 * Const.RESIZE;

        for (int i = 0; i < 4; i++) {
            c[i] = getColumnOfMap(x[i]);
            l[i] = getLineOfMap(y[i]);
        }

        if (
                (map[l[0]][c[0]].img.equals("floor-1") || map[l[0]][c[0]].img.contains("explosion")) &&
                        (map[l[1]][c[1]].img.equals("floor-1") || map[l[1]][c[1]].img.contains("explosion")) &&
                        (map[l[2]][c[2]].img.equals("floor-1") || map[l[2]][c[2]].img.contains("explosion")) &&
                        (map[l[3]][c[3]].img.equals("floor-1") || map[l[3]][c[3]].img.contains("explosion"))
        ) {
            return true; //estará em uma coordenada válida
        }
        if (
                (map[l[0]][c[0]].img.contains("block") || map[l[0]][c[0]].img.contains("wall")) ||
                        (map[l[1]][c[1]].img.contains("block") || map[l[1]][c[1]].img.contains("wall")) ||
                        (map[l[2]][c[2]].img.contains("block") || map[l[2]][c[2]].img.contains("wall")) ||
                        (map[l[3]][c[3]].img.contains("block") || map[l[3]][c[3]].img.contains("wall"))
        ) {
            return false; //estará sobre uma parede
        }


        // EM RELAÇÃO À COORDENADA ANTERIOR

        // 0: ponto do canto superior esquerdo
        x[0] = Const.VAR_X_SPRITES + player[id].x + Const.RESIZE;
        y[0] = Const.VAR_Y_SPRITES + player[id].y + Const.RESIZE;
        // 1: ponto do canto superior direito
        x[1] = Const.VAR_X_SPRITES + player[id].x + Const.SIZE_SPRITE_MAP - 2 * Const.RESIZE;
        y[1] = Const.VAR_Y_SPRITES + player[id].y + Const.RESIZE;
        // 2: ponto do canto inferior esquerdo
        x[2] = Const.VAR_X_SPRITES + player[id].x + Const.RESIZE;
        y[2] = Const.VAR_Y_SPRITES + player[id].y + Const.SIZE_SPRITE_MAP - 2 * Const.RESIZE;
        // 3: ponto do canto inferior direito
        x[3] = Const.VAR_X_SPRITES + player[id].x + Const.SIZE_SPRITE_MAP - 2 * Const.RESIZE;
        y[3] = Const.VAR_Y_SPRITES + player[id].y + Const.SIZE_SPRITE_MAP - 2 * Const.RESIZE;

        for (int i = 0; i < 4; i++) {
            c[i] = getColumnOfMap(x[i]);
            l[i] = getLineOfMap(y[i]);
        }

        if (
                map[l[0]][c[0]].img.contains("bomb-planted") ||
                        map[l[1]][c[1]].img.contains("bomb-planted") ||
                        map[l[2]][c[2]].img.contains("bomb-planted") ||
                        map[l[3]][c[3]].img.contains("bomb-planted")
        ) {
            System.out.println("Player " + id + " moving over planted bomb.");
            return true; //estava sobre uma bomba que acabou de platar, precisa sair
        }
        return false;
    }

    void keyCodePressed(int keyCode) throws RemoteException {
        switch (keyCode) {
            case KeyEvent.VK_W:
                up = true;
                down = right = left = false;
                cm.sendToAllClients(this.id, "newStatus up");
                break;
            case KeyEvent.VK_S:
                down = true;
                up = right = left = false;
                cm.sendToAllClients(this.id, "newStatus down");
                break;
            case KeyEvent.VK_D:
                right = true;
                up = down = left = false;
                cm.sendToAllClients(this.id, "newStatus right");
                break;
            case KeyEvent.VK_A:
                left = true;
                up = down = right = false;
                cm.sendToAllClients(this.id, "newStatus left");
                break;
        }
    }

    void keyCodeReleased(int keyCode) throws RemoteException {
        if (keyCode != KeyEvent.VK_W && keyCode != KeyEvent.VK_S && keyCode != KeyEvent.VK_D && keyCode != KeyEvent.VK_A)
            return;

        cm.sendToAllClients(this.id, "stopStatusUpdate");
        switch (keyCode) {
            case KeyEvent.VK_W:
                up = false;
                break;
            case KeyEvent.VK_S:
                down = false;
                break;
            case KeyEvent.VK_D:
                right = false;
                break;
            case KeyEvent.VK_A:
                left = false;
                break;
        }
    }
}