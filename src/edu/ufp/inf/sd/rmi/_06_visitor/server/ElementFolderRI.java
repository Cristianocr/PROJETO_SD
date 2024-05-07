package edu.ufp.inf.sd.rmi._06_visitor.server;

import java.rmi.Remote;

public interface ElementFolderRI extends Remote {

    public Object acceptVisitor(VisitorFolderOperationI visitor) throws Exception;
}
