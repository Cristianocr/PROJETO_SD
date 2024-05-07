package edu.inf.ufp.sd.rmi._06_visitor.server;

public interface VisitorFolderOperationI {

    public Object visitConcreteElementStateBooks(ElementFolderRI element);

    public Object visitConcreteElementStateMagazines(ElementFolderRI element);
}
