package edu.ufp.inf.sd.rmi.client;

import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;

//tanto para you quanto para enemy
public class Player {
    int x, y;
    String status, color;
    JPanel panel;
    boolean alive;

    StatusChanger sc;

    private Game game;

    private Coordinate spawn[];
    private boolean alives[];

    Player(Game game,int id, JPanel panel) throws InterruptedException {
        this.game = game;
        spawn = game.getClient().getSpawn();
        alives = game.getClient().getAlive();

        this.x = spawn[id].x;
        this.y = spawn[id].y;
        this.color = Sprite.personColors[id];
        this.panel = panel;
        this.alive = alives[id];

        (sc = new StatusChanger(this, "wait")).start();
    }

    public void draw(Graphics g) {
        if (alive)
            g.drawImage(Sprite.ht.get(color + "/" + status), x, y, Const.WIDTH_SPRITE_PLAYER, Const.HEIGHT_SPRITE_PLAYER, null);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}

class StatusChanger extends Thread {
    Player p;
    String status;
    int index;
    boolean playerInMotion;

    StatusChanger(Player p, String initialStatus) {
        this.p = p;
        this.status = initialStatus;
        index = 0;
        playerInMotion = true;
    }

    public void run() {
        while (true) {
            p.status = status + "-" + index;
            if (playerInMotion) {
                index = (++index) % Sprite.maxLoopStatus.get(status);
                p.panel.repaint();
            }

            try {
                Thread.sleep(Const.RATE_PLAYER_STATUS_UPDATE);
            } catch (InterruptedException e) {
            }

            if (p.status.equals("dead-4")) {
                p.alive = false;
                if (p.getGame().getYou() == p)
                    System.exit(1);
            }
        }
    }

    void setLoopStatus(String status) {
        this.status = status;
        index = 1;
        playerInMotion = true;
    }

    void stopLoopStatus() {
        playerInMotion = false;
        index = 0;
    }

}



