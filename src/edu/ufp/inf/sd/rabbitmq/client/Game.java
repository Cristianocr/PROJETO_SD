package edu.ufp.inf.sd.rabbitmq.client;

import javax.swing.*;
import java.awt.*;

public class Game extends JPanel {
    private static final long serialVersionUID = 1L;
    private Player you, enemy1, enemy2, enemy3;

    private Client client;

    private PlayerData player[];
    private Coordinate map[][];

    Game(Client client, int width, int height) {
        this.client = client;
        player = client.getPlayer();
        map = client.getMap();
        setPreferredSize(new Dimension(width, height));
        try {
            System.out.print("Inicializando jogadores...");
            you = new Player(client, client.id, this);
            enemy1 = new Player(client, (client.id + 1) % Const.QTY_PLAYERS, this);
            enemy2 = new Player(client, (client.id + 2) % Const.QTY_PLAYERS, this);
            enemy3 = new Player(client, (client.id + 3) % Const.QTY_PLAYERS, this);
        } catch (InterruptedException e) {
            System.out.println(" erro: " + e + "\n");
            System.exit(1);
        }
        System.out.print(" ok\n");

        System.out.println("Meu jogador: " + Sprite.personColors[client.id]);
    }

    //desenha os componentes, chamada por paint() e repaint()
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMap(g);
        enemy1.draw(g);
        enemy2.draw(g);
        enemy3.draw(g);
        you.draw(g);

        // System.out.format("%s: %s [%04d, %04d]\n", Game.you.color, Game.you.status, Game.you.x, Game.you.y);;
        Toolkit.getDefaultToolkit().sync();
    }

    public void drawMap(Graphics g) {
        for (int i = 0; i < Const.LIN; i++)
            for (int j = 0; j < Const.COL; j++)
                g.drawImage(
                        Sprite.ht.get(map[i][j].img),
                        map[i][j].x, map[i][j].y,
                        Const.SIZE_SPRITE_MAP, Const.SIZE_SPRITE_MAP, null
                );
    }

    public void setSpriteMap(String keyWord, int l, int c) {
        map[l][c].img = keyWord;
    }

    public Player getYou() {
        return you;
    }

    public void setYou(Player you) {
        this.you = you;
    }

    public Player getEnemy1() {
        return enemy1;
    }

    public void setEnemy1(Player enemy1) {
        this.enemy1 = enemy1;
    }

    public Player getEnemy2() {
        return enemy2;
    }

    public void setEnemy2(Player enemy2) {
        this.enemy2 = enemy2;
    }

    public Player getEnemy3() {
        return enemy3;
    }

    public void setEnemy3(Player enemy3) {
        this.enemy3 = enemy3;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}