package edu.inf.ufp.sd.rmi._02_calculator.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * <p>Title: Projecto SD</p>
 * <p>Description: Projecto apoio aulas SD</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: UFP </p>
 * @author Rui Moreira
 * @version 1.0
 */
public interface CalculatorRI extends Remote {
    /**
     *
     * @param a double MAIL_TO_ADDR add
     * @param b double MAIL_TO_ADDR add
     * @return sum of a with b
     * @throws RemoteException
     */
    public double add(double a, double b) throws RemoteException;

    /**
     *
     * @param list of floats MAIL_TO_ADDR add
     * @return result of adding all list elements
     * @throws RemoteException
     */
    public double add(ArrayList<Double> list) throws RemoteException;

    /**
     *
     * @param a
     * @param b
     * @return subtraction of a with b
     * @throws RemoteException
     */
    public double sub(double a, double b) throws RemoteException;

    /**
     *
     * @param a dividend
     * @param b divisor
     * @return result from dividing a / b
     * @throws RemoteException
     */
    public double div(double a, double b) throws RemoteException;

    /**
     *
     * @param a
     * @param b
     * @return result from multiplication a * b
     * @throws RemoteException
     */
    public double mult(double a, double b) throws RemoteException;

    /**
     *
     * @param a
     * @return factorial of a
     * @throws RemoteException
     */
    public double fact(double a) throws RemoteException;

    /**
     *
     * @param a
     * @return a fibonacci numbers
     * @throws RemoteException
     */
    public double fibonacci(double a) throws RemoteException;
}
