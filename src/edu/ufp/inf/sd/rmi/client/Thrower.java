package edu.ufp.inf.sd.rmi.client;

import java.rmi.RemoteException;

//thread auxiliar
public class Thrower extends Thread {
    String keyWord, index[];
    int l, c;
    int delay;

    private MapUpdatesThrower mut;

    Thrower(String keyWord, String index[], int delay, int l, int c) {
        this.keyWord = keyWord;
        this.index = index;
        this.delay = delay;
        this.l = l;
        this.c = c;
    }

    public void run() {
        for (String i : index) {
            try {
                mut.changeMap(keyWord + "-" + i, l, c);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            try {
                sleep(delay);
            } catch (InterruptedException e) {}
        }
        //situação pós-explosão
        try {
            mut.changeMap("floor-1", l, c);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
