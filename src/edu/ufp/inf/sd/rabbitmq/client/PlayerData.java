package edu.ufp.inf.sd.rabbitmq.client;

public class PlayerData {
    boolean logged, alive;
    int x, y; //coordenada atual
    int numberOfBombs;

    PlayerData(int x, int y) {
        this.x = x;
        this.y = y;
        this.logged = false;
        this.alive = false;
        this.numberOfBombs = 1; // para 2 bombas, é preciso tratar cada bomba em uma thread diferente
    }
}
