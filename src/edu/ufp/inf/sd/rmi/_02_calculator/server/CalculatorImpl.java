package edu.ufp.inf.sd.rmi._02_calculator.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CalculatorImpl extends UnicastRemoteObject implements CalculatorRI {
    protected CalculatorImpl() throws RemoteException {
        super();
    }

    @Override
    public double add(double a, double b) throws RemoteException {
        double soma = a + b;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Soma = " + soma);
        return soma;
    }

    @Override
    public double add(ArrayList<Double> list) throws RemoteException {
        double soma = 0;
        for (Double k : list) {
            soma += k;
        }
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Soma = " + soma);
        return soma;
    }

    @Override
    public double sub(double a, double b) throws RemoteException {
        double sub = a - b;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Subtracao = " + sub);
        return sub;
    }

    @Override
    public double div(double a, double b) throws RemoteException {
        double div = a / b;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Divisao = " + div);
        return div;
    }

    @Override
    public double mult(double a, double b) throws RemoteException {
        double mult = a * b;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Multiplicacao = " + mult);
        return mult;
    }

    @Override
    public double fact(double a) throws RemoteException {
        double fact = a;
        a--;
        if (a == 1 || a == 0) {
            return a;
        } else {
            while (a > 1) {
                fact *= a;
                a--;
            }
        }
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Fatorial = " + fact);
        return fact;
    }

    @Override
    public double fibonacci(double a) throws RemoteException {
        double fact = a;
        a--;
        if (a == 1 || a == 0) {
            return a;
        } else {
            while (a > 1) {
                fact *= a;
                a--;
            }
        }
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Fatorial = " + fact);
        return fact;
    }
}
