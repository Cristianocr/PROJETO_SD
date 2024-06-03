package edu.ufp.inf.sd.rabbitmq.client;

class Coordinate {
    public int x, y;
    String img;

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Coordinate(int x, int y, String img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }
}
