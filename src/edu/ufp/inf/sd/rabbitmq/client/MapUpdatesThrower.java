package edu.ufp.inf.sd.rabbitmq.client;

import java.io.IOException;
import java.rmi.RemoteException;

//thread que lança mudanças graduais no mapa que ocorrem logo após a bomba ser plantada
public class MapUpdatesThrower extends Thread {
    public boolean bombPlanted;
    public int l, c;
    public int id;

    private ClientManager cm;
    private MapUpdatesThrower mut;

    private PlayerData player[];
    private Coordinate map[][];

    MapUpdatesThrower(ClientManager cm, int id) {
        this.id = id;
        this.bombPlanted = false;
        this.mut = this;

        this.cm = cm;
        player = cm.getClient().getPlayer();
        map = cm.getClient().getMap();
    }

    void setBombPlanted(int x, int y) {
        x += Const.WIDTH_SPRITE_PLAYER / 2;
        y += 2 * Const.HEIGHT_SPRITE_PLAYER / 3;

        this.c = x / Const.SIZE_SPRITE_MAP;
        this.l = y / Const.SIZE_SPRITE_MAP;

        this.bombPlanted = true;
    }

    //muda o mapa no servidor e no cliente
    public void changeMap(String keyWord, int l, int c) throws IOException {
        map[l][c].img = keyWord;
        cm.sendToAllClients(id, "mapUpdate " + keyWord + " " + l + " " + c);
    }

    int getColumnOfMap(int x) {
        return x / Const.SIZE_SPRITE_MAP;
    }

    int getLineOfMap(int y) {
        return y / Const.SIZE_SPRITE_MAP;
    }

    // verifica se o fogo atingiu algum jogador parado (coordenada do centro do corpo)
    public void checkIfExplosionKilledSomeone(int linSprite, int colSprite) throws IOException {
        int linPlayer, colPlayer, x, y;

        for (int id = 0; id < Const.QTY_PLAYERS; id++)
            if (player[id].alive) {
                x = player[id].x + Const.WIDTH_SPRITE_PLAYER / 2;
                y = player[id].y + 2 * Const.HEIGHT_SPRITE_PLAYER / 3;

                colPlayer = getColumnOfMap(x);
                linPlayer = getLineOfMap(y);

                if (linSprite == linPlayer && colSprite == colPlayer) {
                    player[id].alive = false;
                    cm.sendToAllClients(id, "newStatus dead");
                }
            }
    }

    public void run() {
        while (true) {
            if (bombPlanted) {
                bombPlanted = false;

                for (String index : Const.indexBombPlanted) {
                    try {
                        changeMap("bomb-planted-" + index, l, c);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        sleep(Const.RATE_BOMB_UPDATE);
                    } catch (InterruptedException e) {
                    }
                }

                //efeitos da explosão
                new Thrower(mut, "center-explosion", Const.indexExplosion, Const.RATE_FIRE_UPDATE, l, c).start();
                try {
                    checkIfExplosionKilledSomeone(l, c);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                //abaixo
                if (map[l + 1][c].img.equals("floor-1")) {
                    new Thrower(mut, "down-explosion", Const.indexExplosion, Const.RATE_FIRE_UPDATE, l + 1, c).start();
                    try {
                        checkIfExplosionKilledSomeone(l + 1, c);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (map[l + 1][c].img.contains("block"))
                    new Thrower(mut, "block-on-fire", Const.indexBlockOnFire, Const.RATE_BLOCK_UPDATE, l + 1, c).start();

                //a direita
                if (map[l][c + 1].img.equals("floor-1")) {
                    new Thrower(mut, "right-explosion", Const.indexExplosion, Const.RATE_FIRE_UPDATE, l, c + 1).start();
                    try {
                        checkIfExplosionKilledSomeone(l, c + 1);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (map[l][c + 1].img.contains("block"))
                    new Thrower(mut, "block-on-fire", Const.indexBlockOnFire, Const.RATE_BLOCK_UPDATE, l, c + 1).start();

                //acima
                if (map[l - 1][c].img.equals("floor-1")) {
                    new Thrower(mut, "up-explosion", Const.indexExplosion, Const.RATE_FIRE_UPDATE, l - 1, c).start();
                    try {
                        checkIfExplosionKilledSomeone(l - 1, c);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (map[l - 1][c].img.contains("block"))
                    new Thrower(mut, "block-on-fire", Const.indexBlockOnFire, Const.RATE_BLOCK_UPDATE, l - 1, c).start();

                //a esquerda
                if (map[l][c - 1].img.equals("floor-1")) {
                    new Thrower(mut, "left-explosion", Const.indexExplosion, Const.RATE_FIRE_UPDATE, l, c - 1).start();
                    try {
                        checkIfExplosionKilledSomeone(l, c - 1);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (map[l][c - 1].img.contains("block"))
                    new Thrower(mut, "block-on-fire", Const.indexBlockOnFire, Const.RATE_BLOCK_UPDATE, l, c - 1).start();

                player[id].numberOfBombs++; //libera bomba
            }
            try {
                sleep(0);
            } catch (InterruptedException e) {
            }
        }
    }
}