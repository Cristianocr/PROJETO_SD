package edu.ufp.inf.sd.rmi.server;

import java.io.Serializable;

public class BombermanGame implements Serializable {
    private static int nextId = 1; // Campo estático para armazenar o próximo ID disponível
    public int id;
    public int playerNumber;
    private SubjectRI SubjectRI;
    boolean running = false;

    public BombermanGame(int playerNumber, SubjectRI subjectRI) {
        this.id = getNextId();
        this.playerNumber = playerNumber;
        SubjectRI = subjectRI;
    }

    public BombermanGame() {
    }

    public BombermanGame(SubjectRI subjectRI) {
        this.id = getNextId();
        SubjectRI = subjectRI;
    }

    private static synchronized int getNextId() {
        return nextId++; // Incrementa o ID e retorna o valor anterior
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerNumber() {
        return this.playerNumber;
    }

    public int setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
        return playerNumber;
    }

    public SubjectRI getSubjectRI() {
        return SubjectRI;
    }

    public void setSubjectRI(SubjectRI subjectRI) {
        SubjectRI = subjectRI;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
